import cv2
import numpy as np
import math
import ConfigParser
import socket
import sys
"""
    looks for blobs.  calculates the center of mass (centroid) of the biggest blob.  sorts into ball and bumper, then send over tcp
"""
#parse config stuff
config = ConfigParser.RawConfigParser()
config.read("../vision.conf")
exposure = int(config.get('camera','exposure'))
height = int(config.get('camera','height'))
width = int(config.get('camera','width'))
hue_lower = int(config.get('pyballfinder','hue_lower'))
hue_upper = int(config.get('pyballfinder','hue_upper'))
saturation_lower = int(config.get('pyballfinder','saturation_lower'))
saturation_upper = int(config.get('pyballfinder','saturation_upper'))
value_lower = int(config.get('pyballfinder','value_lower'))
value_upper = int(config.get('pyballfinder','value_upper'))
min_contour_area = int(config.get('pyballfinder','min_contour_area'))
area_difference_to_area_for_circle_detect = int(config.get('pyballfinder','area_difference_to_area_for_circle_detect'))
skip_gui = len(sys.argv) >= 2 and sys.argv[1] == "--nogui"
#set up camera
camera = cv2.VideoCapture(0)
camera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 5 gives dark image. 100 gives bright image.
camera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
camera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
print camera.get(3),camera.get(4)

#set up server
crio_ip = config.get('network_communication','crio_ip')
crio_tcp_loc_coords_port = int(config.get('network_communication','crio_tcp_loc_coords_port'))
send_over_network = (config.get('pyballfinder','send_over_network'))
#set up socket connection - server
if(send_over_network == "True"):
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.connect((crio_ip, crio_tcp_loc_coords_port))

"""
    does two checks to figure out if it's a circle.  First: calculate the area and perimeter of the contour using opencv.  Then, calculate the area using the perimeter.  Are those two values similar?
    Then:find the minimum and maximum radius of the contour.  Are they similar?
"""
def is_contour_a_ball(contour,real_area,perimeter,(cx,cy)):
    calculated_area=math.pow((perimeter/(2*math.pi)),2)*math.pi
    area_difference=abs(real_area-calculated_area)
    area_difference_to_area=int(area_difference/real_area*10)
    min_radius = 99999999
    max_radius = 0
    for coord in contour:
        dist_from_center =  math.sqrt((coord[0][0]-cx)**2+(coord[0][1]-cy)**2)
        if dist_from_center < min_radius:
            min_radius=dist_from_center
        if dist_from_center > max_radius:
            max_radius=dist_from_center
    if(min_radius<=0):
        min_radius=1        
    check_one = area_difference_to_area<area_difference_to_area_for_circle_detect
    check_two = max_radius/min_radius < 3    
    return check_one and check_two # True if circle
    

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
            M = cv2.moments(contour) #an image moment is the weighted average of a blob
            cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
            cv2.circle(capture,(cx,cy),5,(0,0,255),-1)
            type = ""          
            if is_contour_a_ball(contour,real_area,perimeter,(cx,cy)):
                if(not skip_gui):
                    cv2.putText(capture,"Ball",(cx,cy),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,255))
                type = "BALL"
            else:
                cv2.putText(capture,"Bumper",(cx,cy),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,255))   
                type = "BUMP"
                     
            message+=(type + "," + str(cx) + "," + str(cy) +"," + str(int(real_area)) + ";\n")
            
    if(message and send_over_network == "True"):
        s.send(message)
#    show our image during different stages of processing
    if(not skip_gui):
        cv2.imshow('capture',capture) 
        cv2.imshow('erodedbinary',dilatedagain)

    if cv2.waitKey(1) == 27:
        break
    
s.close()    
cv2.destroyAllWindows()
camera.release()
