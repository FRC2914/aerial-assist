import cv2
import numpy as np
import math
"""
    looks for blue blobs.  calculates the center of mass (centroid) of the biggest blob.  Still far from done.
"""

camera = cv2.VideoCapture(0)
width,height = camera.get(3),camera.get(4)
camera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,50)#time in milliseconds. 5 gives dark image. 100 gives bright image.
best_contour=(width/2,height/2)#start best_contour at the center so that it doesn't crash if no blue in first frame
while(1):
    _,capture = camera.read()
    capture = cv2.flip(capture,1)
    
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)
    
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture,np.array((98,90,0)),np.array((118,204,220)))#in opencv, HSV is 0-180,0-255,0-255

#    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
#     erode = cv2.erode(inrangepixels,None,iterations = 5)
#     dilate = cv2.dilate(erode,None,iterations = 10)
#     erodedagain = cv2.erode(dilate,None,iterations = 5)
    dilate = cv2.dilate(inrangepixels,None,iterations = 5)
    erode = cv2.erode(dilate,None,iterations = 10)
    dilatedagain = cv2.dilate(erode,None,iterations = 5)
    
#    find the contours
    tobecontourdetected = dilatedagain.copy()
    contours,hierarchy = cv2.findContours(tobecontourdetected,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    
#    find contour with maximum area and store it as best_contour
    max_area = 0
    for cnt in contours:
        area = cv2.contourArea(cnt)
        if area > max_area:
            max_area = area
            best_contour = cnt
    
    perimeter = cv2.arcLength(best_contour, True)
    real_area=max_area
    calculated_area=math.pow((perimeter/(2*math.pi)),2)*math.pi
    
    area_difference=abs(real_area-calculated_area)
    area_difference_to_area=int(area_difference/real_area*10)
    
    print(area_difference_to_area)
    
#    find centroids (fancy word for center of mass) of best_contour and draw a red circle there
    M = cv2.moments(best_contour)#an image moment is the weighted average of a blob
    cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
    cv2.circle(capture,(cx,cy),5,(0,0,255),-1)        
     
    if(area_difference_to_area<15):
        cv2.putText(capture,"Ball",(cx,cy),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,255))
    else:
        cv2.putText(capture,"Bumper",(cx,cy),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,255))       
#    show our image during different stages of processing
    cv2.imshow('capture',capture) 
    cv2.imshow('erodedbinary',dilatedagain)

    if cv2.waitKey(25) == 27:
        break
    
cv2.destroyAllWindows()
camera.release()
