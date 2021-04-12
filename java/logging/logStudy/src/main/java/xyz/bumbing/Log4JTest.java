package xyz.bumbing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Log4JTest {
    private static final Logger logger = LogManager.getLogger(Log4JTest.class);

    public void info(String info) {

	logger.info(logger.getClass() + " : " + info);
    }
}
