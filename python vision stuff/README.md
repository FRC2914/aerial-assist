The actual script is eyeofsauron.py.  It's all in there.  The other files are some sample scripts that I used to test some of this stuff out.

The protocol between the cRio and Pandaboard is this:
cRIO sends:

	-mode packets on mode switch.
		-mautonomous
		-mtrackball
		-mtrackbump
		-mshooting
		-mnone
Pandaboard sends:

	-Autonomous
		-hhot
		-hnot
	-Trackball
		-tball,x,y,size
	-Trackbump
		-tbump,x,y,size
	-Shoot
		-shit
		-smiss
Ping Packets

	-looks like:
		-p
	-sent both ways.  Whenever one receives it, it returns the same thing.
	-the cRIO j2me library doesn't have any way to detect if a tcp connection has died.  This is our (crummy) workaround	
