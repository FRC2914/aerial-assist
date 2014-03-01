#!/usr/bin/python2

import cv2
import numpy as np
import socket
import ConfigParser
import sys


cap = cv2.VideoCapture(0)
cap.set(cv2.cv.CV_CAP_PROP_EXPOSURE,50) #time in milliseconds. 5 gives dark image. 100 gives bright image.
cap.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,320)
cap.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,240)
start=[]
def on_mouse(event, x, y, flags, params):
    if event == cv2.cv.CV_EVENT_LBUTTONDOWN:
        print 'Start Mouse Position: '+str(x)+', '+str(y)
        sbox = [x, y]
        start.append(sbox)
    elif event == cv2.cv.CV_EVENT_LBUTTONUP:
        print 'End Mouse Position: '+str(x)+', '+str(y)
        _,img=cap.read()
        hsvcapture = cv2.cvtColor(img,cv2.COLOR_BGR2HSV) 
        hsvroi = hsvcapture[start[0][1]:y,start[0][0]:x]
        cv2.imshow("hsvroi",hsvroi)
        minh = 300
        maxh = 0
                   
        mins = 300
        maxs = 0
                    
        minv = 300
        maxv = 0
                    
        for x in hsvroi:
            for y in x:                         
                if minh > y[0]:
                    minh = y[0]
                                     
                if mins > y[1]:
                    mins = y[1]
                
                if minv > y[2]:
                    minv = y[2]
                     
                if maxh < y[0]:
                    maxh = y[0]
                     
                if maxs < y[1]:
                    maxs = y[1]
                
                if maxv < y[2]:
                    maxv = y[2]
                                   
        print "Min H: " + str(minh) + "\n"
        print "Max H: " + str(maxh) + "\n\n"
        print "Min S: " + str(mins) + "\n"
        print "Max S: " + str(maxs) + "\n\n"
        print "Min V: " + str(minv) + "\n"
        print "Max V: " + str(maxv) + "\n\n"
                    
        cv2.waitKey(5000)
        sys.exit(0)

count = 0
while(1):
    _,img = cap.read()

    cv2.namedWindow('real image')
    cv2.cv.SetMouseCallback('real image', on_mouse, 0)
    cv2.imshow('real image', img)
    cv2.waitKey(50)