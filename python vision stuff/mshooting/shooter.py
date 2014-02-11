"""
    This file is a basic script that will determine if a ball would score if it were to be catapulted immediately.
    We are using the led strips around the target to find it.
    Flowchart:
        -Get and flip image
        -Count pixels within hsv range
        -Average their y-coordinates
        -print shit if that is in range, smiss if not
"""
import ConfigParser
import time

import numpy as np
import cv2

#parse config stuff
config = ConfigParser.RawConfigParser()
config.read("../vision.conf")
exposure = int(config.get('camera','exposure'))
height = int(config.get('camera','height'))
width = int(config.get('camera','width'))
hue_lower = int(config.get('shooter','hue_lower'))
hue_upper = int(config.get('shooter','hue_upper'))
saturation_lower = int(config.get('shooter','saturation_lower'))
saturation_upper = int(config.get('shooter','saturation_upper'))
value_lower = int(config.get('shooter','value_lower'))
value_upper = int(config.get('shooter','value_upper'))
lower_avg = int(config.get('shooter','lower_avg'))
upper_avg = int(config.get('shooter','upper_avg'))

camera = cv2.VideoCapture(0)
camera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 5 gives dark image. 100 gives bright image.
camera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
camera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
print camera.get(3),camera.get(4)

try:
    while True:
        _,capture = camera.read()
        capture = cv2.flip(capture,1)
        hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)        
        inrangepixels = cv2.inRange(hsvcapture,np.array((hue_lower,saturation_lower,value_lower)),np.array((hue_upper,saturation_upper,value_upper)))#in opencv, HSV is 0-180,0-255,0-255
        print inrangepixels
        avgheight = 0
        for y in range(0,width):
            for x in range(0,height):
                if inrangepixels[x][y]==1:
                    print x
                    avgheight+=y     
        print avgheight  
        avgheight = avgheight/(width*height)
        print avgheight
        cv2.line(inrangepixels,(0,avgheight),(width,avgheight),(255,0,0),20)
        cv2.imshow("capture",capture)
        cv2.waitKey(5000)
        raise Exception("")
except Exception as e: 
    print "done"
    print e
    cv2.destroyAllWindows()
    camera.release()
