#!/usr/bin/python2
'''
calibration utility made by Seb.  some code was written by Nic.  Some was copied fom here: this: http://stackoverflow.com/questions/16195190/python-cv2-how-do-i-draw-a-line-on-an-image-with-mouse-then-return-line-coord

Instructions:
Right click to draw a line horizontal line there.  Space to remove the most recent line.
',' and '.' increase and decrease exposure
left click an drag selects a rectangle, then the upper and lower bound of h,s&v are printed to the console

'''

import cv2
import numpy as np
import socket
import ConfigParser
import sys
import os

cap = cv2.VideoCapture(1)
cap.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,320)
cap.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,240)
os.system("v4l2-ctl --set-ctrl gain_automatic=0 --device=0")
os.system("v4l2-ctl --set-ctrl gain_automatic=0 --device=1")
os.system("v4l2-ctl --set-ctrl gain_automatic=0 --device=2")

start=[]
def on_mouse(event, x, y, flags, params):
    if event == cv2.cv.CV_EVENT_LBUTTONDOWN:
        print 'Start Mouse Position: '+str(x)+', '+str(y)
        sbox = [x, y]
        start.append(sbox)
    elif event == cv2.cv.CV_EVENT_LBUTTONUP:
        print 'End Mouse Position: '+str(x)+', '+str(y)
        _,img=cap.read()
        img = cv2.medianBlur(img,17)#Change blur amount here!
        hsvcapture = cv2.cvtColor(img,cv2.COLOR_BGR2HSV) 
        hsvroi = hsvcapture[start[len(start)-1][1]:y,start[len(start)-1][0]:x]
        minh = 9999
        maxh = 0          
        mins = 9999
        maxs = 0 
        minv = 9999
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
                                   
        print "Min H: " + str(minh) 
        print "Max H: " + str(maxh) + "\n"
        print "Min S: " + str(mins)
        print "Max S: " + str(maxs) + "\n"
        print "Min V: " + str(minv)
        print "Max V: " + str(maxv) + "\n"
        
    elif event == cv2.cv.CV_EVENT_RBUTTONDOWN:
        lines.append(y)
    
        
print __doc__
exposure=0.25
cap.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure)
print "exposure: " + str(exposure) 
lines=[]
while(1):
    _,img = cap.read()
    cv2.namedWindow('real image')
    cv2.cv.SetMouseCallback('real image', on_mouse, 0)
    for y in lines:
        cv2.line(img,(0,y),(360,y),(255,0,0))
        cv2.putText(img,str(y),(10,y+15),cv2.FONT_HERSHEY_PLAIN,1,(255,0,0)) 
    cv2.imshow('real image', img)
    key_pressed = cv2.waitKey(25)
    if key_pressed == 65364:#2621440 on windows
        exposure=exposure-0.03
	print "expected exposure: " + str(exposure)
        cap.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure)
	cv2.waitKey(250)
        print "exposure: " + str(cap.get(cv2.cv.CV_CAP_PROP_EXPOSURE))
    elif key_pressed == 65362:#2490368 on widows
        exposure=exposure+0.03
	print "expected exposure: " + str(exposure)
        cap.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure)
	cv2.waitKey(250)
        print "exposure: " + str(cap.get(cv2.cv.CV_CAP_PROP_EXPOSURE))
    elif key_pressed == 32:
        lines=lines[:len(lines)-1]#remove most recent
