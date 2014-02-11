import glob
import logging
import logging.handlers
import ConfigParser
    
#configure logger
config = ConfigParser.RawConfigParser()
config.read("settings.conf")
lvl = int(config.get('debug','log_level'))#anything below this level will get ignored, e.g. set to 20 in conf file to not log every single packet sent    

def log(message, lvl):
    print message
    
    logCounter = len(glob.glob1("../LOGS","*.log"))
 
    logging.basicConfig(filename='../LOGS/runtime' + str(logCounter) + '.log',level=lvl)
    
    if lvl == 10:
        logging.debug(message)
    elif lvl == 20:
        logging.info(message)
    elif lvl == 30:
        logging.warning(message)
    elif lvl == 40:
        logging.error(message)
    elif lvl == 50:
        logging.critical(message)
    else:
        logging.error("Bad error level (" + str(lvl) + "): " + message)
