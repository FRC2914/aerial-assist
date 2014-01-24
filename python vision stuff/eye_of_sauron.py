"""
    This is the actual vision program to be run on the pandaboard.
    Lots of code is copied directly from the other samples in "python vision stuff".
    
    There are 3 modes: 
        -Autonomous (based on hotornot)
        -Target Tracking (based on pyballfinder)
        -Shooting
    
    Here's a flowchart:
        -Pandaboard Boots and runs this script.
        -Read settings.conf file from usb drive.  Abort if not found.  Use blue USB drive with blue bumpers, red with red.
        -Try to connect to server running on pandaboard.  Keep trying until connection is established.
        -get mode from cRio
        -loop:
            -get image
            -run mode program on that image (1 of the three modes)
            -write log files
            -New mode yet from cRio?
        -endloop
        
        Autonomous:
            -filter out non-yellow pixels, yellow specified in conf file
            -count the pixels
            -threshold that number, send "HOT" or "NOT"
            
        Target Tracking:
            -Filter out by color specified in conf file
            -Contour detect
            -ignore contours smaller than threshold
            -send <BALL/BUMP>,<x-coord>,<y-coord>,<area>; for each detected object, \n after last object.
            
        Shooting:
            -Filter out by color except green specified in conf file
            -Contour detect
            -???
            -Profit!
    
"""

