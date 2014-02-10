import math
import numpy as np
import ConfigParser
import time

import cv2

import mathstuff


config = ConfigParser.RawConfigParser()
config.read("settings.conf")
# get configs for autonomous
auto_hue_lower = int(config.get('autonomous', 'hue_lower'))
auto_hue_upper = int(config.get('autonomous', 'hue_upper'))
auto_saturation_lower = int(config.get('autonomous', 'saturation_lower'))
auto_saturation_upper = int(config.get('autonomous', 'saturation_upper'))
auto_value_lower = int(config.get('autonomous', 'value_lower'))
auto_value_upper = int(config.get('autonomous', 'value_upper'))
yellow_pixel_thresh = int(config.get('autonomous', 'yellow_pixel_thresh'))
auto_delay_every_cycle = float(config.get('autonomous', 'delay_every_cycle'))
#debug
draw_gui = config.get('debug', 'draw_gui')
"""
    counts the yellow pixels in the camera frame.  If above yellow_pixels_thresh, return hhot, else hnot.
"""
def autonomous(camera):
    time.sleep(auto_delay_every_cycle)
    _,capture = camera.read()
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture, cv2.COLOR_BGR2HSV)    
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture, np.array((auto_hue_lower, auto_saturation_lower, auto_value_lower)), np.array((auto_hue_upper, auto_saturation_upper, auto_value_upper)))  # in opencv, HSV is 0-180,0-255,0-255
    yellows = cv2.countNonZero(inrangepixels)
    if draw_gui=="True":
        cv2.imshow("capture",capture)
        cv2.imshow("inrangepixels",inrangepixels)
    if(yellows > yellow_pixel_thresh):
        return("hhot")
    else:
        return("hnot")
     
     
# get configs for tracking
track_hue_lower = int(config.get('tracking', 'hue_lower'))
track_hue_upper = int(config.get('tracking', 'hue_upper'))
track_saturation_lower = int(config.get('tracking', 'saturation_lower'))
track_saturation_upper = int(config.get('tracking', 'saturation_upper'))
track_value_lower = int(config.get('tracking', 'value_lower'))
track_value_upper = int(config.get('tracking', 'value_upper'))  
smallest_ball_area_to_return = int(config.get('tracking', 'smallest_ball_area_to_return'))   
frame_width = int(config.get('camera', 'width'))     
"""
    Returns info about the biggest ball.
    @TODO make it work
"""    
def trackball(camera):
    _,capture = camera.read()
    #capture = cv2.flip(capture,1) #Instead, we're just returning width-cx to save cpu cycles.
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV) #maybe sort by rg(b) or (r)gb instead of resource intensive hsv
    #    find pixels that are the right color
    inrangepixels = cv2.inRange(hsvcapture,np.array((track_hue_lower,track_saturation_lower,track_value_lower)),np.array((track_hue_upper,track_saturation_upper,track_value_upper)))#in opencv, HSV is 0-180,0-255,0-255
    #    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
    dilate = cv2.dilate(inrangepixels,None,iterations = 5)
    erode = cv2.erode(dilate,None,iterations = 10)
    dilatedagain = cv2.dilate(erode,None,iterations = 5)  
    if draw_gui=="True":
        cv2.imshow("capture",capture)
        cv2.imshow("inrangepixels",inrangepixels)
    contours,hierarchy = cv2.findContours(dilatedagain,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    #make a list of only balls
    balls = []
    for contour in contours:
        if mathstuff.is_contour_a_ball(contour):
            balls.append(contour)
    if balls == []:#no balls detected    
        return("tball,180,120,0")
    #find biggest ball
    biggest_ball = balls[0]
    for ball in balls[1:]:
        if cv2.contourArea(ball)>cv2.contourArea(biggest_ball):
            biggest_ball = ball
    if cv2.contourArea(biggest_ball) < smallest_ball_area_to_return:
        return("tball,180,120,0")
    M = cv2.moments(biggest_ball)#an image moment is the weighted average of a blob
    cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
    return("tball,"+str(frame_width-cx)+","+str(cy)+"," + str(cv2.contourArea(biggest_ball)))
   
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
