package LogBack_Prac;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Model1 {

    public static void main(String[] args) {
	// logback.xml 설정 읽어줌
	// logback.xml configuration debug ="true" 사용하는거랑 같은 결과 => 설정파일 잘 입력되었는지 확인하는 방법
//	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//	StatusPrinter.print(lc);
	MDC.put("daemon", "default");
	Logger logger = LoggerFactory.getLogger(Model1.class);
	logger.debug("Hello world.");
	MDC.clear();
	MDC.put("daemon", "adcmon");
	Thread thread = new Thread() {
	    Map<String, String> parentMDC = MDC.getCopyOfContextMap();

	    @Override
	    public void run() {
		MDC.setContextMap(parentMDC);
		logger.debug("데몬");
		Thread thread = new Thread() {

		    @Override
		    public void run() {

			logger.debug("데몬2");
			CommonLog.writeStaticLog();
		    }
		};
		thread.start();

	    }
	};
	thread.start();
	MDC.clear();
	MDC.put("daemon", "sysmond");
	Thread thread2 = new Thread() {
	    @Override
	    public void run() {
		logger.debug("데몬");

		Thread thread = new Thread() {
		    @Override
		    public void run() {
			logger.debug("데몬2");
			CommonLog.writeStaticLog();
		    }
		};
		thread.start();
	    }
	};
	thread2.start();
	MDC.clear();
	MDC.put("daemon", "syslogd");
	Thread thread3 = new Thread() {
	    @Override
	    public void run() {
		logger.debug("데몬");
	    }
	};
	thread3.start();

    }

}
