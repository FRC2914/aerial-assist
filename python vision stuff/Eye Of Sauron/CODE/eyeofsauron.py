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
        logger.log(time.time()-start_time,"Shutting down: " + logmessage, 20)
        sock.close()    
        cv2.destroyAllWindows()
        frontcamera.release()
        rearcamera.release()
    except Exception as e:
        logger.log(time.time()-start_time,"Had trouble shutting down: "+ str(e), 50)
    sys.exit(logmessage)


def getcrio(sock):
    ready = select.select([sock], [], [], 0)
    if ready[0]:
        return sock.recv(64)
    else:
        return ""
    
def establishconnection(ip,port):
    sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    logger.log(time.time()-start_time,"Preparing to connect to server", 20)
    socket.setdefaulttimeout(5.0)
    for _ in range(25):
        try:
            sock.connect((crio_ip, crio_tcp_loc_coords_port))
            sock.setblocking(0)
            time.sleep(2.0)
            if select.select([sock], [], [], 0)[0]:
                return sock
            else:
                sock.close()
                sock=None
                sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
                raise Exception("cRIO not responding")
        except Exception as e:
            logger.log(time.time()-start_time,"Couldn't connect to cRIO. Details:" + str(e), 50)
            time.sleep(2)
            continue         
    return None

start_time=time.time()

#get configuration stuff for camera
config = ConfigParser.RawConfigParser()
config.read("settings.conf")
log_fps=config.get('debug','log_fps')
exposure = float(config.get('camera','exposure'))
height = int(config.get('camera','height'))
width = int(config.get('camera','width'))

#debug
skip_gui = len(sys.argv) >= 2 and sys.argv[1] == "--nogui"

logger.log(0,config.get('color','color'),30)
#set up cameras. If that fails, don't bother connecting to cRIO, just exit
frontcamera = cv2.VideoCapture(0)
frontcamera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 0.05 gives dark image. 0.10 gives bright image.
frontcamera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
frontcamera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
rearcamera = cv2.VideoCapture(1)
rearcamera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 0.05 gives dark image. 0.10 gives bright image.
rearcamera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
rearcamera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
if frontcamera.get(3)==0.0:
    shutdown("Could not connect to front webcam.  Exiting.")
if rearcamera.get(3)==0.0:
    shutdown("Could not connect to rear webcam.  Exiting.")
        
#Connect to cRio.
crio_ip = config.get('network_communication','crio_ip')
crio_tcp_loc_coords_port = int(config.get('network_communication','crio_tcp_loc_coords_port'))
send_over_network = config.get('network_communication','send_over_network')
crio_on_localhost = config.get('debug','crio_on_localhost')
if crio_on_localhost == "True":
    crio_ip="127.0.0.1"
if(send_over_network=="True"):
    #set up socket connection
    sock=establishconnection(crio_ip, crio_tcp_loc_coords_port)
    if sock == None:
        shutdown("Could Not Connect to cRIO.")
    logger.log(time.time()-start_time,"cRIO connected", 20)
    
mode = "none"

vision.set_draw_gui(not skip_gui)

time_of_last_fps_log=time.time()+3
cycles=0
try:
    while(1):
        cycles=cycles+1
        if time.time()-time_of_last_fps_log>3:
            if log_fps=="True":
                fps=cycles/(time.time()-time_of_last_fps_log)
                logger.log(time.time()-start_time,"FPS: " + str(int(fps)), 20)
            time_of_last_fps_log=time.time()
            cycles=0
        
        _,bow_frame = frontcamera.read()
        _,stern_frame = rearcamera.read() 
        packetforcrio=""
        
        if mode == 'autonomous\n':
            bow_frame,packetforcrio = vision.autonomous(bow_frame)
        elif mode == 'trackbump\n':
            bow_frame,packetforcrio = vision.trackbump(bow_frame)
        elif mode == 'trackball\n':
            stern_frame,packetforcrio = vision.trackball(stern_frame)
        elif mode == 'shooting\n':
            bow_frame,packetforcrio = vision.shooting(bow_frame) 
        else:#especially mode=="none"
            pass 
        
        if(not skip_gui):
            rendered_frame = np.hstack((bow_frame,stern_frame))
            cv2.imshow("frame",rendered_frame)
        
        #Send Packets
        if packetforcrio != "" and send_over_network=="True":
            logger.log(time.time()-start_time,"Sending to cRio: " + packetforcrio, 10)
            try:
                sock.send(packetforcrio + "\n")
		logger.log(time.time()-start_time,"sending: " + packetforcrio,10)
            except Exception as e:
                logger.log(time.time()-start_time,"Could not send packet. Details: " + str(e), 40)
                if str(e)[:10] == "[Errno 32]":
                    shutdown("Lost connection")
        #Receive and deal with packets        
        fromcrio = getcrio(sock)
        if(fromcrio!=""):
            split = string.split(fromcrio,"\n")
            for s in split:
                if(s[:1]=="m"):
                    mode = fromcrio[1:]
                    sock.send(fromcrio)
                    logger.log(time.time()-start_time,"Mode Changed to: " + mode, 20)
                    
        if cv2.waitKey(1) == 27:
            break
except KeyboardInterrupt as e:
    shutdown(str(e))
        
shutdown("Reached EOF.  That wasn't supposed to happen.")
