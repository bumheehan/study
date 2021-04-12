package xyz.bumbing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTest {

    private Logger log = LoggerFactory.getLogger(Slf4jTest.class);

    public void info(String info) {

	log.info(log.getClass() + " : " + info);
    }
}
