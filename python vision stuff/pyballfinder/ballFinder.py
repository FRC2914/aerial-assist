import cv2
import numpy as np
import math
import ConfigParser
import socket
"""
    looks for blobs.  calculates the center of mass (centroid) of the biggest blob.  sorts into ball and bumper, then send over tcp
"""
#parse config stuff
config = ConfigParser.RawConfigParser()
config.read("../vision.conf")
exposure = int(config.get('camera','exposure'))
hue_lower = int(config.get('pyballfinder','hue_lower'))
hue_upper = int(config.get('pyballfinder','hue_upper'))
saturation_lower = int(config.get('pyballfinder','saturation_lower'))
saturation_upper = int(config.get('pyballfinder','saturation_upper'))
value_lower = int(config.get('pyballfinder','value_lower'))
value_upper = int(config.get('pyballfinder','value_upper'))
min_contour_area = int(config.get('pyballfinder','min_contour_area'))
area_difference_to_area_for_circle_detect = int(config.get('pyballfinder','area_difference_to_area_for_circle_detect'))
crio_ip = config.get('network_communication','crio_ip')
crio_tcp_loc_coords_port = int(config.get('network_communication','crio_tcp_loc_coords_port'))
send_over_network = (config.get('pyballfinder','send_over_network'))
draw_windows = config.get('pyballfinder','draw_windows')
#set up camera
camera = cv2.VideoCapture(0)
width,height = camera.get(3),camera.get(4)
camera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 5 gives dark image. 100 gives bright image.
#set up socket onnection
if(send_over_network == "True"):
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.connect((crio_ip,crio_tcp_loc_coords_port))

while(1):
    _,capture = camera.read()
    capture = cv2.flip(capture,1)   
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)   
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture,np.array((hue_lower,saturation_lower,value_lower)),np.array((hue_upper,saturation_upper,value_upper)))#in opencv, HSV is 0-180,0-255,0-255
#    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
    dilate = cv2.dilate(inrangepixels,None,iterations = 5)
    erode = cv2.erode(dilate,None,iterations = 10)
    dilatedagain = cv2.dilate(erode,None,iterations = 5)   
#    find the contours
    tobecontourdetected = dilatedagain.copy()
    contours,hierarchy = cv2.findContours(tobecontourdetected,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    
    message = ""
    
    for contour in contours:  
        real_area = cv2.contourArea(contour)
        if real_area > min_contour_area:
            perimeter = cv2.arcLength(contour, True)
            calculated_area=math.pow((perimeter/(2*math.pi)),2)*math.pi
            area_difference=abs(real_area-calculated_area)
            area_difference_to_area=int(area_difference/real_area*10)
  
#    find centroids (fancy word for center of mass) of best_contour and draw a red circle there
            M = cv2.moments(contour)#an image moment is the weighted average of a blob
            cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
            cv2.circle(capture,(cx,cy),5,(0,0,255),-1) 
            type = ""                
            if(area_difference_to_area<area_difference_to_area_for_circle_detect):
                if(draw_windows == "True"):
                    cv2.putText(capture,"Ball",(cx,cy),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,255))
                type = "ball"
            else:
                if(draw_windows == "True"):
                    cv2.putText(capture,"Bumper",(cx,cy),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,255))   
                type = "bumper"      
            message+=(type + ":" + str(cx) + "," + str(cy) +"," + str(int(real_area)) + "\n")
            
    if(message and send_over_network == "True"):
        s.send(message)
#    show our image during different stages of processing
    if(draw_windows == "True"):
        cv2.imshow('capture',capture) 
        cv2.imshow('erodedbinary',dilatedagain)

    if cv2.waitKey(25) == 27:
        break
    
s.close()    
cv2.destroyAllWindows()
camera.release()
