import cv2
import numpy as np
"""
    Convert image to HSV plane using cvtColor() function
    Extract red and blue colors from it using inRange() function
    Apply erosion and dilation to avoid noise using erode() and dilate() functions
    Find Contours using findContours() function
    Draw the contours using drawContours() function.
"""

c = cv2.VideoCapture(0)
width,height = c.get(3),c.get(4)

def getthresholdedimg(hsv):
    return cv2.inRange(hsv,np.array((22,20,200)),np.array((37,240,255)))#in opencv, HSV is 0-180,0-255,0-255

while(1):
    _,capture = c.read()
    capture = cv2.flip(capture,1)
    
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    
#    turn it into a binary image representing yellows
    inrangepixels = getthresholdedimg(hsvcapture)

#    Apply erosion and dilation to avoid noise using erode() and dilate() functions
    erode = cv2.erode(inrangepixels,None,iterations = 3)
    dilate = cv2.dilate(erode,None,iterations = 5)

###
#    count yellows

    yellows = cv2.countNonZero(dilate)

    
    if(yellows>2000):
        cv2.putText(capture,"HOT",(0,450),cv2.FONT_HERSHEY_SIMPLEX,10,(0,0,255))
    else:
        cv2.putText(capture,"NOT",(0,450),cv2.FONT_HERSHEY_SIMPLEX,10,(0,0,255))
    cv2.imshow('yellow',dilate)
    cv2.imshow('capture',capture)

    if cv2.waitKey(25) == 27:
        break
    
cv2.destroyAllWindows()
c.release()