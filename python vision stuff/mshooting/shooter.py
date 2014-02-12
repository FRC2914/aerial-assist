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
min_pixel_weight = int(config.get('shooter','min_pixel_weight'))

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
        
        try:
            #This averaging math is explained in ./math.png
            row_averages = np.mean(inrangepixels, axis=1)
            avgheight=int(np.average(range(0,height), weights=row_averages, axis=0))
        except ZeroDivisionError:
            avgheight=0
        if(np.sum(row_averages, axis=0)<min_pixel_weight):
            avgheight=0            
        
        cv2.line(capture,(0,avgheight),(width,avgheight),(255,0,0),5)
        cv2.imshow("capture",capture)
        cv2.imshow("inrangepixels",inrangepixels)
        cv2.waitKey(1)
        #raise Exception("")
except KeyboardInterrupt as e: 
    print "done"
    print e
    cv2.destroyAllWindows()
    camera.release()
