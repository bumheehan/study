# LogBack.xml



```xml
<configuration>
  <!-- timestamp : 유니크 이름 사용할때 많이사용 -->
  <!-- timeReference="contextBirth" 는 컨텍스트 기준시간 -->
  <timestamp key="bySecond" datePattern="yyyyMMdd'T'HHmmss" timeReference="contextBirth"/>
  <timestamp key="byDay" datePattern="yyyyMMdd"/>
  <timestamp key="byYear" datePattern="yyyy"/>
  <timestamp key="byMonth" datePattern="MM"/>
  <timestamp key="byDate" datePattern="dd"/>
  <timestamp key="byTime" datePattern="HH"/>
  <timestamp key="byMin" datePattern="mm"/>
  <timestamp key="bySec" datePattern="ss"/>

  <!-- 메세지Caller패턴에 사용   -->
  <evaluator name="DISP_CALLER_EVAL">
    <expression>logger.contains("chapters.layouts") &amp;&amp; \
      message.contains("who calls thee")</expression>
  </evaluator>


  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <!-- Filter : 필터를 거친 이벤트만 로그에 작성됨 -->
    <!-- LevelFilter : 정확한 레벨 일치를 기반으로 이벤트를 필터링 한다. 
    이벤트 레벨이 구성된 레벨이 같으면 onMatch onMismatch 특성의 구성에 따라 필터가 이벤트를 승인하거나 거부 하게 할 수 있다. -->
    <filter class="ch.qos.logback.classic.filter.LevelFilter"> 
        <level>INFO</level> 
        <onMatch>ACCEPT</onMatch> 
        <onMismatch>DENY</onMismatch>
    </filter>

    
    <!-- ThreshholdFilter 지정된 입계 값 아래 이벤트를 필터링 한다. 임계값보다 낮은 레벨의 경우 이벤트는 거부된다. -->
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter"> 
        <level>INFO</level> 
    </filter>

    <!-- EvaluatorFilter :EventEvaluator를 캡슐화하는 일반 필터입니다. -->
    <filter class="ch.qos.logback.core.filter.EvaluatorFilter"> 
        <evaluator class="ch.qos.logback.classic.boolex.GEventEvaluator"> 
            <expression> e.level.toInt() >= WARN.toInt() &amp;&amp; <!-- Stands for && in XML --> !(e.mdc?.get("req.userAgent") =~ /Googlebot|msnbot|Yahoo/ ) 
            </expression> 
        </evaluator> 
        <OnMismatch>DENY</OnMismatch> 
        <OnMatch>NEUTRAL</OnMatch> 
    </filter>

    <!-- EvaluatorFilter : 마커 넣고 다음과같이 사용 -->
    <filter class="ch.qos.logback.core.filter.EvaluatorFilter"> 
        <evaluator class="ch.qos.logback.classic.boolex.OnMarkerEvaluator"> 
            <marker>NAVER_CRAWLER</marker> 
        </evaluator> 
        <OnMatch>ACCEPT</OnMatch> 
        <OnMismatch>DENY</OnMismatch> 
    </filter>



    <!-- encoders are assigned the type
         ch.qos.logback.classic.encoder.PatternLayoutEncoder by default -->

    <!-- encoder : OutputStreamAppender의 인터페이스 설정 , 패턴 정의 -->
    <encoder> 
      <!-- layout 직접 패턴 설정 정의할수 있음   //요즘은 패턴 Layout으로 아래같은상황은 잘안씀-->
      <layout class="chapters.layouts.MySampleLayout"> 
        <prefix>MyPrefix</prefix>
        <printThreadName>false</printThreadName>
      </layout>
      <!-- PatternLayout 이거 많이씀-->
      <!-- <pattern>%-4relative [%thread] %-5level %logger{35} - %msg %n</pattern> -->

      <!-- 패턴 정의
        %logger : 클래스 이름 {length}  
        %logger	    mainPackage.sub.sample.Bar	mainPackage.sub.sample.Bar
        %logger{0}	mainPackage.sub.sample.Bar	Bar
        %logger{5}	mainPackage.sub.sample.Bar	m.s.s.Bar
        %logger{10}	mainPackage.sub.sample.Bar	m.s.s.Bar
        %logger{15}	mainPackage.sub.sample.Bar	m.s.sample.Bar
        %logger{16}	mainPackage.sub.sample.Bar	m.sub.sample.Bar
        %logger{26}	mainPackage.sub.sample.Bar	mainPackage.sub.sample.Bar
      
        $class{length} : 클래스 풀네임 ? 속도차원에서 쓰지말라고함

        $contextName : LoggerContext name

        $d{pattern}  : 날짜
        $date{pattern}
        $d{pattern, timezone}
        $date{pattern, timezone}

        Conversion Pattern	                    Result
        %d	                                    2006-10-20 14:06:49,812
        %date	                                2006-10-20 14:06:49,812
        %date{ISO8601}	                        2006-10-20 14:06:49,812
        %date{HH:mm:ss.SSS}	                    14:06:49.812
        %date{dd MMM yyyy;HH:mm:ss.SSS}	        20 oct. 2006;14:06:49.812

        F / file	: 파일 저장소 이름 보여줌 , 속도차원에서 사용금지

        caller{depth} : trace 불려진곳 다나옴
        caller{depthStart..depthEnd} 
        caller{depth, evaluator-1, ... evaluator-n}  //evaluator에 의해서 검증된 곳 위치 보여줌
        caller{depthStart..depthEnd, evaluator-1, ... evaluator-n}

        예제 
                For example, %caller{2} would display the following excerpt:

                0    [main] DEBUG - logging statement 
                Caller+0   at mainPackage.sub.sample.Bar.sampleMethodName(Bar.java:22)
                Caller+1   at mainPackage.sub.sample.Bar.createLoggingRequest(Bar.java:17)
                And %caller{3} would display this other excerpt:

                16   [main] DEBUG - logging statement 
                Caller+0   at mainPackage.sub.sample.Bar.sampleMethodName(Bar.java:22)
                Caller+1   at mainPackage.sub.sample.Bar.createLoggingRequest(Bar.java:17)
                Caller+2   at mainPackage.ConfigTester.main(ConfigTester.java:38)
                A range specifier can be added to the caller conversion specifier's options to configure the depth range of the information to be displayed.

                For example, %caller{1..2} would display the following excerpt:

                0    [main] DEBUG - logging statement
                Caller+0   at mainPackage.sub.sample.Bar.createLoggingRequest(Bar.java:17)


        L / line	: 아웃풋 라인수 ? 이것도 속도차원 사용금지
        m / msg / message : 로깅 메세지
        M / method : 메소드이름 , 속도차원 사용금지
        n : line separator strings such as "\n", or "\r\n"
        p / le / level : 로깅레벨
        r / relative : 입력까지 시스템시간
        t / thread	:쓰레드이름

        X{key:-defaultVal}  : MDC 관련설정(MDC.put으로 넣은 value 넣기)
        mdc{key:-defaultVal}


        ex{depth}      : 에러 
        exception{depth}
        throwable{depth}
        ex{depth, evaluator-1, ..., evaluator-n}
        exception{depth, evaluator-1, ..., evaluator-n}
        throwable{depth, evaluator-1, ..., evaluator-n}

        예제 
                Conversion Pattern	Result
                %ex	
                                    mainPackage.foo.bar.TestException: Houston we have a problem
                                    at mainPackage.foo.bar.TestThrower.fire(TestThrower.java:22)
                                    at mainPackage.foo.bar.TestThrower.readyToLaunch(TestThrower.java:17)
                                    at mainPackage.ExceptionLauncher.main(ExceptionLauncher.java:38)
                %ex{short}	
                                    mainPackage.foo.bar.TestException: Houston we have a problem
                                    at mainPackage.foo.bar.TestThrower.fire(TestThrower.java:22)
                %ex{full}	
                                    mainPackage.foo.bar.TestException: Houston we have a problem
                                    at mainPackage.foo.bar.TestThrower.fire(TestThrower.java:22)
                                    at mainPackage.foo.bar.TestThrower.readyToLaunch(TestThrower.java:17)
                                    at mainPackage.ExceptionLauncher.main(ExceptionLauncher.java:38)
                %ex{2}	
                                    mainPackage.foo.bar.TestException: Houston we have a problem
                                    at mainPackage.foo.bar.TestThrower.fire(TestThrower.java:22)
                                    at mainPackage.foo.bar.TestThrower.readyToLaunch(TestThrower.java:17)

        xEx{depth}          : 에러표시인데 패키징 이름 까지 뜸
        xException{depth}
        xThrowable{depth}
        xEx{depth, evaluator-1, ..., evaluator-n}
        xException{depth, evaluator-1, ..., evaluator-n}
        xThrowable{depth, evaluator-1, ..., evaluator-n}

        예제
                java.lang.NullPointerException
                at com.xyz.Wombat(Wombat.java:57) ~[wombat-1.3.jar:1.3]
                at  com.xyz.Wombat(Wombat.java:76) ~[wombat-1.3.jar:1.3]
                at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.5.0_06]
                at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39) ~[na:1.5.0_06]
                at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25) ~[na:1.5.0_06]
                at java.lang.reflect.Method.invoke(Method.java:585) ~[na:1.5.0_06]
                at org.junit.internal.runners.TestMethod.invoke(TestMethod.java:59) [junit-4.4.jar:na]
                at org.junit.internal.runners.MethodRoadie.runTestMethod(MethodRoadie.java:98) [junit-4.4.jar:na]
                ...etc 


        rEx{depth}                      : 로깅 이벤트와 연관된 예외의 스택 추적을 출력합니다 (있는 경우). 
        rootException{depth}
        rEx{depth, evaluator-1, ..., evaluator-n}
        rootException{depth, evaluator-1, ..., evaluator-n}

        nopex             :에러 표시안됨
        nopexception    

        
        marker : 마킹
        property{key} : 
        replace(p){r, t}




        포맷 % 의미

        Format modifier	  왼쪽정렬	      최소길이	       최대길이	    Comment
        %20logger	      false	         20	             none	            Left pad with spaces if the logger name is less than 20 characters long.
        %-20logger	      true	         20	             none	            Right pad with spaces if the logger name is less than 20 characters long.
        %.30logger	      NA	         none	         30	                Truncate from the beginning if the logger name is longer than 30 characters.
        %20.30logger	  false	         20	             30	                Left pad with spaces if the logger name is shorter than 20 characters. However, if logger name is longer than 30 characters, then truncate from the beginning.
        %-20.30logger	  true	         20	             30	                Right pad with spaces if the logger name is shorter than 20 characters. However, if logger name is longer than 30 characters, then truncate from the beginning.
        %.-30logger	      NA	         none	         30	                Truncate from the end if the logger name is longer than 30 characters.
       
       Format modifier	Logger name	                    Result
        [%20.20logger]	main.Name	                    [           main.Name]
        [%-20.20logger]	main.Name	                    [main.Name           ]
        [%10.10logger]	main.foo.foo.bar.Name	        [o.bar.Name]
        [%10.-10logger]	main.foo.foo.bar.Name	        [main.foo.f]
       




        괄호의 중요성
        앞선에서 정렬먼저 할수있음

        패턴 : %d{HH:mm:ss.SSS} [%thread]

        13:09:30 [main] DEBUG c.q.logback.demo.ContextListener - Classload hashcode is 13995234
        13:09:30 [main] DEBUG c.q.logback.demo.ContextListener - Initializing for ServletContext
        13:09:30 [main] DEBUG c.q.logback.demo.ContextListener - Trying platform Mbean server
        13:09:30 [pool-1-thread-1] INFO  ch.qos.logback.demo.LoggingTask - Howdydy-diddly-ho - 0
        13:09:38 [btpool0-7] INFO c.q.l.demo.lottery.LotteryAction - Number: 50 was tried.
        13:09:40 [btpool0-7] INFO c.q.l.d.prime.NumberCruncherImpl - Beginning to factor.
        13:09:40 [btpool0-7] DEBUG c.q.l.d.prime.NumberCruncherImpl - Trying 2 as a factor.
        13:09:40 [btpool0-7] INFO c.q.l.d.prime.NumberCruncherImpl - Found factor 2
            
        패턴 : %-30(%d{HH:mm:ss.SSS} [%thread]) %-5level %logger{32} - %msg%n

        13:09:30 [main]            DEBUG c.q.logback.demo.ContextListener - Classload hashcode is 13995234
        13:09:30 [main]            DEBUG c.q.logback.demo.ContextListener - Initializing for ServletContext
        13:09:30 [main]            DEBUG c.q.logback.demo.ContextListener - Trying platform Mbean server
        13:09:30 [pool-1-thread-1] INFO  ch.qos.logback.demo.LoggingTask - Howdydy-diddly-ho - 0
        13:09:38 [btpool0-7]       INFO  c.q.l.demo.lottery.LotteryAction - Number: 50 was tried.
        13:09:40 [btpool0-7]       INFO  c.q.l.d.prime.NumberCruncherImpl - Beginning to factor.
        13:09:40 [btpool0-7]       DEBUG c.q.l.d.prime.NumberCruncherImpl - Trying 2 as a factor.
        13:09:40 [btpool0-7]       INFO  c.q.l.d.prime.NumberCruncherImpl - Found factor 2


        컬러넣을수있음
        %black", "%red", "%green","%yellow","%blue", "%magenta","%cyan", "%white", "%gray", "%boldRed","%boldGreen", "%boldYellow", "%boldBlue", "%boldMagenta""%boldCyan", "%boldWhite" and "%highlight  
        예제
            %highlight(%-5level) %cyan(%logger{15})

       -->
    </encoder>
    <!-- target : System.out or System.err. The default target is System.out. -->
    <target>System.out</target>
  </appender>
  <appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <!-- file : 저장 위치 -->
    <!-- <file>testFile.log</file> -->
    <file>log-${bySecond}.txt</file>
    <!-- append : true 면 존재하는 파일에 추가 , false면 파일 삭제 후 새로만듦 , 기본값 true -->
    <append>true</append> 
    <!-- set immediateFlush to false for much higher logging throughput -->
    <immediateFlush>true</immediateFlush>
    <!-- encoders are assigned the type
         ch.qos.logback.classic.encoder.PatternLayoutEncoder by default -->
    <encoder>
      <!-- outputPatternAsHeader : 로그 헤더에 패턴 입력해줌 기본 false -->
      <outputPatternAsHeader>true</outputPatternAsHeader>
      <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>

    </encoder>
  </appender>


  <!-- RollingAppender 많이 사용할 어펜더임 , Console ,Rolling 두가지로 사용할듯 -->
  <appender name="ROLLING" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>mylog.txt</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
      <!-- rollover daily -->
      <fileNamePattern>mylog-%d{yyyy-MM-dd}.%i.txt</fileNamePattern>
       <!-- each file should be at most 100MB, keep 60 days worth of history, but at most 20GB -->
       <maxFileSize>100MB</maxFileSize>    
       <maxHistory>60</maxHistory>
       <totalSizeCap>20GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
      <pattern>%msg%n</pattern>
    </encoder>
  </appender>

  </root>
  <root level="DEBUG">
    <appender-ref ref="STDOUT" />
    <appender-ref ref="FILE" />
  </root>
</configuration>

<!-- MDC -->
<!-- Mapped Diagnostic Contexts shine brightest within client server architectures. Typically, multiple clients will be served by multiple threads on the server. Although the methods in the MDC class are static, the diagnostic context is managed on a per thread basis, allowing each server thread to bear a distinct MDC stamp. MDC operations such as put() and get() affect only the MDC of the current thread, and the children of the current thread. The MDC in other threads remain unaffected. Given that MDC information is managed on a per thread basis, each thread will have its own copy of the MDC. Thus, there is no need for the developer to worry about thread-safety or synchronization when programming with the MDC because it handles these issues safely and transparently. -->
<!-- MDC 는 쓰레드마다 관리되기때문에 동기화 걱정안하고 짜도 됨! -->
```