package LogBack_Prac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ch2 {

    public static void main(String[] args) {
	// logback.xml 설정 읽어줌
	// logback.xml configuration debug ="true" 사용하는거랑 같은 결과 => 설정파일 잘 입력되었는지 확인하는 방법
//	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//	StatusPrinter.print(lc);

	Logger logger = LoggerFactory.getLogger(Ch2.class);

	CommonLog.writeStaticLog();
    }
}
