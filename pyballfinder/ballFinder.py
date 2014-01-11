''' v 0.1 - It tracks two objects of blue and yellow color each '''

import cv2
import numpy as np

def getthresholdedimg(hsv):
    redLow = cv2.inRange(hsv,np.array((150,50,50)),np.array((180,200,200)))#in opencv, HSV is 0-180,0-255,0-255
    redHigh = cv2.inRange(hsv,np.array((0,50,50)),np.array((30,200,200)))
    red = cv2.add(redLow,redHigh)
    return red

c = cv2.VideoCapture(0)
width,height = c.get(3),c.get(4)
print "frame width and height : ", width, height
storage = cv2.cv.CreateMat(image.width, 1, cv.CV_32FC3)

while(1):
    _,f = c.read()
    f = cv2.flip(f,1)
    blur = cv2.medianBlur(f,5)
    hsv = cv2.cvtColor(f,cv2.COLOR_BGR2HSV)
    img = getthresholdedimg(hsv)
    erode = cv2.erode(img,None,iterations = 1)
    #dilate = cv2.dilate(erode,None,iterations = 1)

    #contours,hierarchy = cv2.findContours(dilate,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    circles=cv2.HoughCircles(erode,)
    
    for cnt in contours:
        x,y,w,h = cv2.boundingRect(cnt)
        cx,cy = x+w/2, y+h/2
        
        if 20 < hsv.item(cy,cx,0) < 30:
            cv2.rectangle(f,(x,y),(x+w,y+h),[0,255,255],2)
            print "yellow :", x,y,w,h
        elif 100 < hsv.item(cy,cx,0) < 120:
            cv2.rectangle(f,(x,y),(x+w,y+h),[255,0,0],2)
            print "blue :", x,y,w,h

    cv2.imshow('img',f)

    if cv2.waitKey(25) == 27:
        break

cv2.destroyAllWindows()
c.release()