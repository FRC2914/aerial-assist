import glob
import logging
import logging.handlers

def log(message, lvl):
    print message
    
    logCounter = len(glob.glob1("../LOGS","*.log"))
 
    logging.basicConfig(filename='../LOGS/runtime' + str(logCounter) + '.log',level=logging.DEBUG)
    
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
        logging.error("Bad error level: " + message)
