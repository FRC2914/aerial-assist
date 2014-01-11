import cv2
import numpy as np
"""
    looks for blue blobs.  calculates the center of mass (centroid) of the biggest blob.  Still far from done.
"""

c = cv2.VideoCapture(0)
width,height = c.get(3),c.get(4)
c.set(cv2.cv.CV_CAP_PROP_EXPOSURE,80)#time in milliseconds. 5 gives dark image. 100 gives bright image.
while(1):
    _,capture = c.read()
    capture = cv2.flip(capture,1)
    
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture,np.array((98,90,0)),np.array((118,204,220)))#in opencv, HSV is 0-180,0-255,0-255

#    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
    erode = cv2.erode(inrangepixels,None,iterations = 5)
    dilate = cv2.dilate(erode,None,iterations = 10)
    erodedagain = cv2.erode(dilate,None,iterations = 5)
    
#    find the contours
    tobecontourdetected = erodedagain.copy()
    contours,hierarchy = cv2.findContours(tobecontourdetected,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    
#    find contour with maximum area and store it as best_cnt
#    @TODO program will crash if zero contours are detected in the first cycle. 
    max_area = 0
    for cnt in contours:
        area = cv2.contourArea(cnt)
        if area > max_area:
            max_area = area
            best_cnt = cnt
    
    
#    find centroids (fancy word for center of mass) of best_cnt and draw a red circle there
    M = cv2.moments(best_cnt)#an image moment is the weighted average of a blob
    cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
    cv2.circle(capture,(cx,cy),5,(0,0,255),-1)        
            
#    show our image during different stages of processing
    cv2.imshow('capture',capture) 
    cv2.imshow('binary',inrangepixels)
    cv2.imshow('erodedbinary',erodedagain)

    if cv2.waitKey(25) == 27:
        break
    
cv2.destroyAllWindows()
c.release()