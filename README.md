2914FRC2014
===========
This code is released under the BSD 2-clause license. Check out the file called LICENSE.

Robot code and vision tracking code for 2914's 2014 Bot

===========
###Prerequisites when developing on windows:

Eclipse w/git w/pydev

install python 2.7

install opencv 2.4.8 to C:\opencv\

install scipy 0.13.2-win32-superpack-python2.7

install numpy-1.8.0-win32-superpack-python2.7

===========

###How does it work and what cool features does it have?

The cRIO and and pandaboard boot on startup.  The cRIO runs the project located under `./robot drive`.  The pandaboard runs `./python vision stuff/Eye Of Sauron/CODE/eyeofsauron.py`.    They then set up a tcp socket.  The protocol between the two is documented [here]( https://github.com/FRC2914/2914FRC2014/tree/master/python%20vision%20stuff ).The OS running on the pandaboard can be configured using the instructions [here]( https://github.com/FRC2914/2914FRC2014/wiki/Setting-Up-Arch-Linux).

The cRIO has multiple "modes".  Each mode controls rotation.  When the user "locks on" to a target, rotation is controlled by that mode.  Otherwise rotation is "released" and controlled by rotating the (3d) joystick.

The pandaboard can track different types of objects.  It can track game balls, alliance members (by their bumpers), the goal itself (requires calibration to get it right).  It can also detect when the goal is lit yellow ("hot").  There is a usb webcam mounted at the front (bumper level) to do this.  Tracking balls uses its own webcam, which is mounted about 3 feet off the ground on our intake system.

Log files were supposed to be written to `../LOGS/runtimeXX.log` but the buffers wouldn't flush when the power was cut (after every game).  We just `print()`ed everything we wanted to log and we `>>./runtime.log` using bash.

Different cameras need to be calibrated.  Is is currently calibrated for a logitech c270 on a properly illuminated FRC competition field.  HSV ranges are specified in `/python vision stuff/Eye Of Sauron/CODE/settings.conf.<blue/red>`.  Before a match tell it what team you are on by copying the right settings to settings.conf, e.g. w/ red bumpers just
`cp settings.conf.red settings.conf`




