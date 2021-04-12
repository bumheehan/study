package xyz.bumbing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JCLTest {

    private Log log = LogFactory.getLog(JCLTest.class);

    public void info(String info) {

	log.info(log.getClass() + " : " + info);
    }
}
