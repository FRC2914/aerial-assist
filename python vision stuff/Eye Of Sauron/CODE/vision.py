import math
import numpy as np
import ConfigParser
import time
import sys

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
"""
    counts the yellow pixels in the camera frame.  If above yellow_pixels_thresh, return hhot, else hnot.
"""
def autonomous(capture):
    time.sleep(auto_delay_every_cycle)
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture, cv2.COLOR_BGR2HSV)    
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture, np.array((auto_hue_lower, auto_saturation_lower, auto_value_lower)), np.array((auto_hue_upper, auto_saturation_upper, auto_value_upper)))  # in opencv, HSV is 0-180,0-255,0-255
    yellows = cv2.countNonZero(inrangepixels)
    
    cv2.rectangle(capture,(0,0),(frame_width,frame_height),(255,0,0),5)
    cv2.putText(capture,"Mode: autonomous",(10,25),cv2.FONT_HERSHEY_PLAIN,1.5,(255,0,0))  
    cv2.rectangle(capture,(15,200),(305,220),(255,0,0),5)
    cv2.line(capture,(20,210),(int(1.0*yellows/yellow_pixel_thresh*305+10),210),(255,0,0),15)
    cv2.putText(capture,str(yellows)+"/"+str(yellow_pixel_thresh),(15,180),cv2.FONT_HERSHEY_PLAIN,1,(255,0,0))
    
    if(yellows > yellow_pixel_thresh):
        return(capture,"hhot")
    else:
        return(capture,"hnot")
     
     
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
upper_bump_detect=int(config.get('tracking', 'upper_bump_detect')) #this should be the smaller number! 0,0 is upper left!
lower_bump_detect=int(config.get('tracking', 'lower_bump_detect')) 
"""
    Returns info about the biggest ball.
    @TODO make it work
"""    
def trackball(capture):
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV) #maybe sort by rg(b) or (r)gb instead of resource intensive hsv
    #    find pixels that are the right color
    inrangepixels = cv2.inRange(hsvcapture,np.array((track_hue_lower,track_saturation_lower,track_value_lower)),np.array((track_hue_upper,track_saturation_upper,track_value_upper)))#in opencv, HSV is 0-180,0-255,0-255
    #    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
    dilate = cv2.dilate(inrangepixels,None,iterations = 5)
    erode = cv2.erode(dilate,None,iterations = 10)
    dilatedagain = cv2.dilate(erode,None,iterations = 5)  
    contours,hierarchy = cv2.findContours(dilatedagain,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    
    #draw nice gui
    cv2.rectangle(capture,(0,0),(frame_width,frame_height),(255,0,0),5)
    cv2.putText(capture,"Mode: trackball",(10,25),cv2.FONT_HERSHEY_PLAIN,1.5,(255,0,0))
    
    #make a arrays of balls, ball sizes and ball centroids
    balls = []
    for contour in contours:
        if mathstuff.is_contour_a_ball(contour):
            balls.append(contour)
    if balls == []:#no balls detected    
        return(capture,"tball,180,120,0")
    ball_sizes=[]
    for ball in balls:
        ball_sizes.append(cv2.contourArea(ball))
    ball_centroids = []
    for ball in balls:
        ball_centroids.append(cv2.moments(ball))
    
    #find biggest ball (by size)    
    biggest_ball_index = 0
    for i in range(1,len(ball_sizes)):
        if ball_sizes[i]>ball_sizes[biggest_ball_index]:
            biggest_ball_index = i
    biggest_ball=balls[biggest_ball_index]
    
    #all ball_centroids get a pink dot
    for ball_centroid in ball_centroids:
        cx,cy = int(ball_centroid['m10']/ball_centroid['m00']), int(ball_centroid['m01']/ball_centroid['m00'])
        cv2.circle(capture,(cx,cy),4,(200,110,255),3)
        
    #if biggest ball is tiny, ignore it an return       
    if ball_sizes[biggest_ball_index] < smallest_ball_area_to_return:
        return(capture,"tball,180,120,0")
    
    #biggest ball gets a green dot
    biggest_ball_centroid=ball_centroids[biggest_ball_index]
    cx,cy = int(biggest_ball_centroid['m10']/biggest_ball_centroid['m00']), int(biggest_ball_centroid['m01']/biggest_ball_centroid['m00'])
    cv2.circle(capture,(cx,cy),4,(20,255,60),3)
    
    message_to_return = "tball,"+str(frame_width-cx)+","+str(cy)+"," + str(int(ball_sizes[biggest_ball_index]))
    return (capture,message_to_return)
   
"""
    Returns info about the biggest bumper
    @TODO make it work
"""  
def trackbump(capture):
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    #only search for bumper in certain region.  This works b/c camera is mounted at same height as bumpers
    hsvroi=hsvcapture[upper_bump_detect:lower_bump_detect,0:frame_width]    
    inrangepixels = cv2.inRange(hsvroi,np.array((track_hue_lower,track_saturation_lower,track_value_lower)),np.array((track_hue_upper,track_saturation_upper,track_value_upper)))
    dilate = cv2.dilate(inrangepixels,None,iterations = 5)
    erode = cv2.erode(dilate,None,iterations = 10)
    dilatedagain = cv2.dilate(erode,None,iterations = 5) 
    contours,hierarchy = cv2.findContours(dilatedagain,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)

    #draw nice gui
    cv2.line(capture,(0,lower_bump_detect),(frame_width,lower_bump_detect),(255,0,0),5)
    cv2.line(capture,(0,upper_bump_detect),(frame_width,upper_bump_detect),(255,0,0),5)
    cv2.rectangle(capture,(0,0),(frame_width,frame_height),(255,0,0),5)
    cv2.putText(capture,"Mode: trackbump",(10,25),cv2.FONT_HERSHEY_PLAIN,1.5,(255,0,0)) 
    
    #bumper info in three separate arrays
    bumpers = contours    
    if bumpers == []:#no bumpers detected
        return(capture,"tbump,180,120,0")
    bumper_sizes = []
    for bumper in bumpers:
        bumper_sizes.append(cv2.contourArea(bumper))
    bumper_centroids = []
    for bumper in bumpers:
        bumper_centroids.append(cv2.moments(bumper))
    
    #find biggest bumper (by size)    
    biggest_bumper_index = 0
    for i in range(1,len(bumper_sizes)):
        if bumper_sizes[i]>bumper_sizes[biggest_bumper_index]:
            biggest_bumper_index = i
            
    #all bumper_centroids get a pink dot
    for bumper_centroid in bumper_centroids:
        cx,cy = int(bumper_centroid['m10']/bumper_centroid['m00']), int(bumper_centroid['m01']/bumper_centroid['m00'])
        cv2.circle(capture,(cx,cy+upper_bump_detect),4,(200,110,255),3)
    #if biggest bumper is really small, ignore it
    if bumper_sizes[biggest_bumper_index]<smallest_bumper_area_to_return:
        return(capture,"tbump,180,120,0")
    #biggest bumper_centroid get a green dot
    biggest_bumper_centroid=bumper_centroids[biggest_bumper_index]
    cx,cy = int(biggest_bumper_centroid['m10']/biggest_bumper_centroid['m00']), int(biggest_bumper_centroid['m01']/biggest_bumper_centroid['m00'])
    cv2.circle(capture,(cx,cy+upper_bump_detect),4,(20,255,60),3)
    
    biggest_bumper=bumpers[biggest_bumper_index]
    message_to_return = "tbump,"+str(cx)+","+str(cy+upper_bump_detect)+"," + str(int(bumper_sizes[biggest_bumper_index]))
    return(capture,message_to_return)  

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
def shooting(capture):
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)        
    inrangepixels = cv2.inRange(hsvcapture,np.array((shoot_hue_lower,shoot_saturation_lower,shoot_value_lower)),np.array((shoot_hue_upper,shoot_saturation_upper,shoot_value_upper)))#in opencv, HSV is 0-180,0-255,0-255
    try:
        #This averaging math is explained in ../mshooting/math.png
        row_averages = np.mean(inrangepixels, axis=1)
        avg_height=int(np.average(range(0,frame_height), weights=row_averages, axis=0))
    except ZeroDivisionError:
        avg_height=-1
        
    if(np.sum(row_averages, axis=0)<min_pixel_weight):#if we don't count enough pixels, we probably aren't looking at the target.
        avg_height=-1
    is_hit = avg_height<upper_avg and avg_height>lower_avg
    cv2.rectangle(capture,(0,0),(frame_width,frame_height),(255,0,0),5)
    cv2.putText(capture,"Mode: shooting",(10,25),cv2.FONT_HERSHEY_PLAIN,1.5,(255,0,0))
    color=(0,0,255)
    if is_hit:
        color=(0,255,0)
    cv2.line(capture,(0,lower_avg),(frame_width,lower_avg),color,5)
    cv2.line(capture,(0,upper_avg),(frame_width,upper_avg),color,5)
    cv2.line(capture,(0,avg_height),(frame_width,avg_height),(255,0,0),5)
    hit = "smiss"
    if is_hit:
        hit="shit"
    return (capture,hit)
