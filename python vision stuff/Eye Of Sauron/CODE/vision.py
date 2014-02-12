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
smallest_bumper_area_to_return = int(config.get('tracking', 'smallest_bumper_area_to_return'))
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
    for ball in balls[1:]:#we might be able to use opencv's contour hierarchy instead of recalculating
        if cv2.contourArea(ball)>cv2.contourArea(biggest_ball):#@TODO optimize, b/c we don't need to be recalculating contour_are all the time.
            biggest_ball = ball
    if cv2.contourArea(biggest_ball) < smallest_ball_area_to_return:
        return("tball,180,120,0")
    M = cv2.moments(biggest_ball)#an image moment is the weighted average of a blob
    cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
    return("tball,"+str(frame_width-cx)+","+str(cy)+"," + str(int(cv2.contourArea(biggest_ball))))
   
"""
    Returns info about the biggest bumper
    @TODO make it work
"""  
def trackbump(camera):
    _,capture = camera.read()
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    inrangepixels = cv2.inRange(hsvcapture,np.array((track_hue_lower,track_saturation_lower,track_value_lower)),np.array((track_hue_upper,track_saturation_upper,track_value_upper)))
#    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
    dilate = cv2.dilate(inrangepixels,None,iterations = 5)
    erode = cv2.erode(dilate,None,iterations = 10)
    dilatedagain = cv2.dilate(erode,None,iterations = 5)  
    if draw_gui=="True":
        cv2.imshow("capture",capture)
        cv2.imshow("inrangepixels",dilatedagain)
    contours,hierarchy = cv2.findContours(dilatedagain,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    bumpers = []
    for contour in contours:
        if not mathstuff.is_contour_a_ball(contour):
            bumpers.append(contour)   
    if bumpers == []:#no bumpers detected  
        return("tbump,180,120,0")
    biggest_bumper = bumpers[0]
    for bumper in bumpers[1:]:#to save cpu cyles we could just assume that everything we see is a bumper.  In this mode we are in possession of the ball anyways.
        if cv2.contourArea(bumper)>cv2.contourArea(biggest_bumper):
            biggest_bumper = bumper
    if cv2.contourArea(biggest_bumper) < smallest_bumper_area_to_return:
        return("tbump,180,120,0")        
    M = cv2.moments(biggest_bumper)
    cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
    return("tbump,"+str(frame_width-cx)+","+str(cy)+"," + str(int(cv2.contourArea(biggest_bumper))))    

shoot_hue_lower = int(config.get('shooting','hue_lower'))
shoot_hue_upper = int(config.get('shooting','hue_upper'))
shoot_saturation_lower = int(config.get('shooting','saturation_lower'))
shoot_saturation_upper = int(config.get('shooting','saturation_upper'))
shoot_value_lower = int(config.get('shooting','value_lower'))
shoot_value_upper = int(config.get('shooting','value_upper'))
lower_avg = int(config.get('shooting','lower_avg'))
upper_avg = int(config.get('shooting','upper_avg'))
min_pixel_weight = int(config.get('shooting','min_pixel_weight'))
frame_height=int(config.get('camera','height'))
frame_width = int(config.get('camera','width'))
"""
    If you were to shoot now, would it hit ? "shit" : "smiss"
"""
def shooting(camera):
    _,capture = camera.read()
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)        
    inrangepixels = cv2.inRange(hsvcapture,np.array((shoot_hue_lower,shoot_saturation_lower,shoot_value_lower)),np.array((shoot_hue_upper,shoot_saturation_upper,shoot_value_upper)))#in opencv, HSV is 0-180,0-255,0-255
    try:
        #This averaging math is explained in ../mshooting/math.png
        row_averages = np.mean(inrangepixels, axis=1)
        avg_height=int(np.average(range(0,frame_height), weights=row_averages, axis=0))
    except ZeroDivisionError:
        avg_height=0
        
    if(np.sum(row_averages, axis=0)<min_pixel_weight):#if we don't count enough pixels, we probably aren't looking at the target.
        avg_height=0 
    if(draw_gui=="True"):    
        cv2.line(capture,(0,avg_height),(frame_width,avg_height),(255,0,0),5)
        cv2.imshow("capture",capture)
        cv2.imshow("inrangepixels",inrangepixels)
    if(avg_height<upper_avg and avg_height>lower_avg):
        return("shit")
    else:    
        return("smiss")
