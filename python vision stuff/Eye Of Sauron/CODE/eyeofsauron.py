"""
    This is the actual vision program to be run on the pandaboard.
    Lots of code is copied directly from the other samples in "python vision stuff".
    
    There are 4 modes: 
        -Autonomous (based on hotornot)
        -Bumper Tracking (based on pyballfinder)
        -Ball tracking (based on pyballfinder)
        -Shooting
    
    Here's a flowchart(kinda):
        -Pandaboard Boots and runs this script.  It's on a usb drive.
        -Read settings.conf file from same directory.  Abort if not found.  Use blue USB drive with blue bumpers, red with red.
        -Connect to pandaboard
        -loop:
            -get image
            -run mode program on that image (one of the three modes)
            -write log files
            -send result to cRIO
            -New mode yet from cRio?
        
        Autonomous:
            -filter out non-yellow pixels, yellow specified in conf file
            -count the pixels
            -threshold that number, send "HOT" or "NOT"
            
        Target Tracking:
            -Filter out by color specified in conf file
            -Contour detect
            -ignore contours smaller than threshold
            -send <BALL/BUMP>,<x-coord>,<y-coord>,<area>; for each detected object, \n after last object.
            
        Shooting:
            -Filter out by color except green specified in conf file
            -Contour detect
            -???
            -Profit!
"""
import math
import ConfigParser
import socket
import sys
import time
import select
import string

import cv2
import numpy as np

import mathstuff
import vision
import logger

def shutdown(logmessage):
    try:
        sock.close()    
        cv2.destroyAllWindows()
        frontcamera.release()
        rearcamera.release()
        logger.log("Shutting down: " + logger.logmessage, 20)
    except Exception:
        logger.log("Had trouble shutting down", 50)
    sys.exit(logmessage)


def getcrio(sock):
    ready = select.select([sock], [], [], 0)
    if ready[0]:
        return sock.recv(64)
    else:
        return ""
    
def establishconnection(ip,port):
    sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    logger.log("Preparing to connect to server", 20)
    socket.setdefaulttimeout(5.0)
    for _ in range(25):
        try:
            sock.connect((crio_ip, crio_tcp_loc_coords_port))
            sock.setblocking(0)
            return sock
        except Exception as e:
            logger.log("Coudn't connect to cRIO. Details:" + str(e), 50)
            continue 
    return None

#get configuration stuff for camera
config = ConfigParser.RawConfigParser()
config.read("settings.conf")
log_fps=config.get('debug','log_fps')
exposure = int(config.get('camera','exposure'))
height = int(config.get('camera','height'))
width = int(config.get('camera','width'))
    
#set up cameras. If that fails, don't bother connecting to cRIO, just exit
frontcamera = cv2.VideoCapture(0)
frontcamera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 5 gives dark image. 100 gives bright image.
frontcamera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
frontcamera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
rearcamera = cv2.VideoCapture(1)
rearcamera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 5 gives dark image. 100 gives bright image.
rearcamera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
rearcamera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
if frontcamera.get(3)==0.0:
    shutdown("Could not connect to front webcam.  Exiting.")
    pass
if rearcamera.get(3)==0.0:
    shutdown("Could not connect to rear webcam.  Exiting.")
    pass
        
#Connect to cRio.
crio_ip = config.get('network_communication','crio_ip')
crio_tcp_loc_coords_port = int(config.get('network_communication','crio_tcp_loc_coords_port'))
send_over_network = config.get('network_communication','send_over_network')
crio_timeout_time = config.get('network_communication','crio_timeout_time')
crio_on_localhost = config.get('debug','crio_on_localhost')
if crio_on_localhost == "True":
    crio_ip="127.0.0.1"
if(send_over_network=="True"):
    #set up socket connection
    sock=establishconnection(crio_ip, crio_tcp_loc_coords_port)
    if sock == None:
        shutdown("Could Not Connect to cRIO.")
    logger.log("cRIO connected", 20)
    
mode = "none"
timeoflastping=time.time()#if it's been more than 500ms since we heard from the cRio, close socket and restart.

fps = 0
secs = int(round(time.clock())*1000)

try:
    while(1):
        
        oldsecs = secs
        secs = int(round(time.time())*1000)
        
        if secs == oldsecs:
            fps = fps + 1
        else:
            if log_fps=="True":
                logger.log("FPS: " + str(fps), 20)
            fps = 0;
    
        packetforcrio=""
        if time.time()-timeoflastping > crio_timeout_time:
            shutdown("Ping Timeout")
            #reconnect will happen because the linux machine will restart this script
        if mode == 'autonomous\n':
            packetforcrio = vision.autonomous(frontcamera)
        elif mode == 'trackbump\n':
            packetforcrio = vision.trackbump(frontcamera)
        elif mode == 'trackball\n':
            packetforcrio = vision.trackball(frontcamera)
        elif mode == 'shooting\n':
            packetforcrio = vision.shooting(frontcamera) 
        else:#especially mode=="none"
            pass 
        
        if packetforcrio != "" and send_over_network=="True":
            logger.log("Sending to cRio: " + packetforcrio, 10)
            try:
                sock.send(packetforcrio + "\n")
            except Exception as e:
                logger.log("Could not send packet. Details: " + str(e), 40)
                
        fromcrio = getcrio(sock)
        if(fromcrio!=""):
            split = string.split(fromcrio,"\n")
            for s in split:
                if(s[:1]=="m"):
                    mode = fromcrio[1:]
                    logger.log("Mode Changed to: " + mode, 20)
                elif(s[:1]=="p"):
                    sock.send("p\n")
                    timeoflastping=time.time()
        if cv2.waitKey(1) == 27:
            break
except KeyboardInterrupt:
    shutdown("KeyboardInterrupt")
        
shutdown("Reached EOF.  That wasn't supposed to happen.")