import math
import numpy as np
import ConfigParser
import time

import cv2


config = ConfigParser.RawConfigParser()
config.read("settings.conf")
# get configs for autonomous
hue_lower = int(config.get('autonomous', 'hue_lower'))
hue_upper = int(config.get('autonomous', 'hue_upper'))
saturation_lower = int(config.get('autonomous', 'saturation_lower'))
saturation_upper = int(config.get('autonomous', 'saturation_upper'))
value_lower = int(config.get('autonomous', 'value_lower'))
value_upper = int(config.get('autonomous', 'value_upper'))
yellow_pixel_thresh = int(config.get('autonomous', 'yellow_pixel_thresh'))
auto_delay_every_cycle = float(config.get('autonomous', 'delay_every_cycle'))
#debug
draw_gui = config.get('autonomous', 'draw_gui')
"""
    counts the yellow pixels in the camera frame.  If above yellow_pixels_thresh, return hhot, else hnot.
"""
def autonomous(camera):
    time.sleep(auto_delay_every_cycle)
    _,capture = camera.read()
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture, cv2.COLOR_BGR2HSV)    
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture, np.array((hue_lower, saturation_lower, value_lower)), np.array((hue_upper, saturation_upper, value_upper)))  # in opencv, HSV is 0-180,0-255,0-255
    yellows = cv2.countNonZero(inrangepixels)
    if draw_gui=="True":
        cv2.imshow("capture",capture)
        cv2.imshow("yellows",yellows)
    if(yellows > yellow_pixel_thresh):
        return("hhot")
    else:
        return("hnot")
        
"""
    Returns info about the biggest ball.
    @TODO make it work
"""    
def trackball(camera):
    return("tball,180,120,1000")
   
"""
    Returns info about the biggest bumper
    @TODO make it work
"""  
def trackbump(camera):
    return("tbump,180,120,1000")

"""
    If you were to shoot now, would it hit ? "shit" : "smiss"
    @TODO make it work
"""
def shooting(camera):
    return("smiss")
