import cv2
import numpy as np
import math
import ConfigParser
import socket
import sys

import ballFinder

def ballIdentifier():
    for ballFinder.contour in ballFinder.contours:  
        real_area = cv2.contourArea(contour)
        if ballFinder.real_area > ballFinder.min_contour_area:
            perimeter = cv2.arcLength(contour, True)
            M = cv2.moments(contour) #an image moment is the weighted average of a blob
            cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
            cv2.circle(capture,(cx,cy),5,(0,0,255),-1)
            type = ""          
            if ballFinder.is_contour_a_ball(contour,real_area,perimeter,(cx,cy)):
                if(not ballFinder.skip_gui):
                    return (cx,cy)

                