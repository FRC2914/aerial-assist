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
    red = cv2.inRange(hsv,np.array((175,140,50)),np.array((180,180,200)))#in opencv, HSV is 0-180,0-255,0-255
    #redHigh = cv2.inRange(hsv,np.array((0,50,50)),np.array((30,200,200)))
    #red = cv2.add(redLow,redHigh)
    return red

while(1):
    _,capture = c.read()
    capture = cv2.flip(capture,1)
    
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    
#    Extract red and blue colors from it using inRange() function
    inrangepixels = getthresholdedimg(hsvcapture)

#    Apply erosion and dilation to avoid noise using erode() and dilate() functions
    erode = cv2.erode(inrangepixels,None,iterations = 3)
    dilate = cv2.dilate(erode,None,iterations = 10)

###

    cv2.imshow('binaryImg',dilate)
    cv2.imshow('capture',capture)

    if cv2.waitKey(25) == 27:
        break
    
cv2.destroyAllWindows()
c.release()