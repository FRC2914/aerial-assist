import cv2
import numpy as np
import socket
import ConfigParser
import sys
"""
    Convert image to HSV plane using cvtColor() function
    Extract red and blue colors from it using inRange() function
    Apply erosion and dilation to avoid noise using erode() and dilate() functions
    Find Contours using findContours() function
    Draw the contours using drawContours() function.
"""

c = cv2.VideoCapture(1)
width,height = c.get(3),c.get(4)

config = ConfigParser.RawConfigParser()
config.read("../vision.conf")
height = int(config.get('camera','height'))
width = int(config.get('camera','width'))
c.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
c.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
hue_lower = int(config.get('hotornot','hue_lower'))
hue_upper = int(config.get('hotornot','hue_upper'))
saturation_lower = int(config.get('hotornot','saturation_lower'))
saturation_upper = int(config.get('hotornot','saturation_upper'))
value_lower = int(config.get('hotornot','value_lower'))
value_upper = int(config.get('hotornot','value_upper'))
send_over_network = (config.get('hotornot','send_over_network'))
crio_ip = config.get('network_communication','crio_ip')
crio_tcp_loc_coords_port = int(config.get('network_communication','crio_tcp_loc_coords_port'))
yellow_pixel_thresh = int(config.get('hotornot','yellow_pixel_thresh'))
skip_gui = len(sys.argv) >= 2 and sys.argv[1] == "--nogui"

if(send_over_network == "True"):
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.connect((crio_ip,crio_tcp_loc_coords_port))

def getthresholdedimg(hsv):
    return cv2.inRange(hsv,np.array((hue_lower,saturation_lower,value_lower)),np.array((hue_upper,saturation_upper,value_upper)))#in opencv, HSV is 0-180,0-255,0-255

while(1):
    message=""
    _,capture = c.read()
    capture = cv2.flip(capture,1)  
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)    
#    turn it into a binary image representing yellows
    inrangepixels = getthresholdedimg(hsvcapture)
#    Apply erosion and dilation to avoid noise using erode() and dilate() functions
    erode = cv2.erode(inrangepixels,None,iterations = 3)
    dilate = cv2.dilate(erode,None,iterations = 5)
#    count yellows
    yellows = cv2.countNonZero(dilate)
    if(yellows>yellow_pixel_thresh):
        message="HOT\n"
        if(not skip_gui):
            cv2.putText(capture,"HOT",(0,450),cv2.FONT_HERSHEY_SIMPLEX,10,(0,0,255))
    else:
        message="NOT\n"
        if(not skip_gui):
            cv2.putText(capture,"NOT",(0,450),cv2.FONT_HERSHEY_SIMPLEX,10,(0,0,255))
        
    if(not skip_gui):        
        cv2.imshow('yellow',dilate)
        cv2.imshow('capture',capture)

    if cv2.waitKey(25) == 27:
        break
    
    if(message and send_over_network == "True"):
        s.send(message)
    
cv2.destroyAllWindows()
c.release()