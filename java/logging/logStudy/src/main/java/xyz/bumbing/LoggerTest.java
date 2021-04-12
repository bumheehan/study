package xyz.bumbing;

public class LoggerTest {

    public static void main(String[] args) {

	JCLTest jcl = new JCLTest();
	jcl.info("jcl");

	Log4JTest log4j = new Log4JTest();
	log4j.info("log4j");

	Slf4jTest slf4j = new Slf4jTest();
	slf4j.info("slf4j");
    }
}
