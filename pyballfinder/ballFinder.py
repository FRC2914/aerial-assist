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
#c.set(cv2.cv.CV_CAP_PROP_EXPOSURE,50)
#c.set(cv2.cv.CV_CAP_PROP_FPS,2)
while(1):
    _,capture = c.read()
    capture = cv2.flip(capture,1)
    
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture,np.array((98,50,50)),np.array((118,204,255)))#in opencv, HSV is 0-180,0-255,0-255

#    Apply erosion and dilation to avoid noise using erode() and dilate() functions
    dilate = cv2.dilate(inrangepixels,None,iterations = 9)
    erode = cv2.erode(dilate,None,iterations = 5)
     
    cv2.imshow('binary',erode)
    cv2.imshow('binary',inrangepixels)
    cv2.imshow('capture',capture)

    if cv2.waitKey(25) == 27:
        break
    
cv2.destroyAllWindows()
c.release()