import math
import ConfigParser

import cv2
import numpy


config = ConfigParser.RawConfigParser()
config.read("settings.conf")
area_difference_to_area_for_circle_detect = int(config.get('tracking', 'area_difference_to_area_for_circle_detect'))
min_max_radius_for_circle_detect = float(config.get('tracking', 'min_max_radius_for_circle_detect'))

"""
    Performs two checks.
    Check one:
        Calculate actual perimeter and actual area.  Then use the perimeter /2PI to get radius and calculate what the area should be if it's a circle.  Is that area close to the actual area?
    Check two:
        Measures distance from center to outside.  The largest distance can be no more than twice as long as the shortest distance.
"""
def is_contour_a_ball(contour):
    real_area = cv2.contourArea(contour)
    perimeter = cv2.arcLength(contour,True)
    M = cv2.moments(contour)#an image moment is the weighted average of a blob
    cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
    calculated_area=math.pow((perimeter/(2*math.pi)),2)*math.pi
    area_difference=abs(real_area-calculated_area)
    area_difference_to_area=int(area_difference/real_area*10)
    min_radius = 99999999
    max_radius = 0
    for coord in contour:
        dist_from_center =  math.sqrt((coord[0][0]-cx)**2+(coord[0][1]-cy)**2)
        if dist_from_center < min_radius:
            min_radius=dist_from_center
        if dist_from_center > max_radius:
            max_radius=dist_from_center
    if(min_radius<=0):
        min_radius=1        
    check_one = area_difference_to_area<area_difference_to_area_for_circle_detect
    check_two = max_radius/min_radius < min_max_radius_for_circle_detect 
    return check_one and check_two # True if circle
