package LogBack_Prac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class CommonLog {

    static final int DEFAULT_LOG = 0;
    static final int ADCMOND_LOG = 1;
    static final int SYSMOND_LOG = 2;
    static final int SYSLOGD_LOG = 3;

    static final Logger log = LoggerFactory.getLogger(CommonLog.class);

    static void writeStaticLog() {
	log.info("static writeLog method");
    }

    public static void writeDebug(int logcode, String message) {

	switch (logcode) {
	case DEFAULT_LOG:
	    MDC.put("daemon", "default");
	    break;
	case ADCMOND_LOG:
	    MDC.put("daemon", "adcmon");
	    break;
	case SYSMOND_LOG:
	    MDC.put("daemon", "sysmon");
	    break;
	case SYSLOGD_LOG:
	    MDC.put("daemon", "syslog");
	    break;
	}
	log.debug(message);
    }

}
