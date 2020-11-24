# 사운드



## 관련 정보 (용어 등)

PCM(Pulse Code Modulation) : 아날로그 -> 디지털 변환 방식, 자주쓰는 용어인 Sample Rate, Bit Depth 등 이있음

Nyquist Theorem : 샘플링 속도는 아날로그 신호의 최대 주파수(fmax)라고 할때 최소 d(샘플링 주파수)=2fmax(2배)여야 온전히 신호를 변환 할 수 있다. 

![Sampling of A/D converter | Renesas Customer Hub](https://app.na4.teamsupport.com/Wiki/WikiDocs/784358/images/rel/samp_1.gif)

d = sample period frequency

[Sampling ](http://microscopy.berkeley.edu/courses/dib/sections/02Images/sampling.html)

![img](http://microscopy.berkeley.edu/courses/dib/sections/02Images/images/sampling.gif)

d>2f

![img](http://microscopy.berkeley.edu/courses/dib/sections/02Images/images/sampling2.gif)

d=4fmax

![img](http://microscopy.berkeley.edu/courses/dib/sections/02Images/images/aliasing.gif)

d<fmax



가청주파수 20kHz 일때 나이쿼스트 샘플링 최소 주파수는 40kHz 이상이어야함 

오디오 CD경우 44.1kHz , 과거 TV 주사선에 샘플데이터  저장했는데 그와 관련하여 나온 주파수 이다.

- 44100 = 2^2 * 3^2 * 4^2 * 7^2 prime numer 제곱







[샘플링 관련 내용](https://blog.naver.com/wyepark/221012896754)

![](images/pcm.png)

ADC (Analog Digital Converter) : 아날로그 -> 디지털 변환기

DAC(Digital Analog Converter) : 디지털 -> 아날로그 변환기



channel : 한개의 채널 모노, 2개의 채널 스테레오, 5.1채널 사운드 = 6채널

frame : 모든 채널을 합쳐서 하나의 샘플로 구성하고 있는것

- frame = sample size * channel 

-  44100Hz샘플레이트로 만들어진 오디오에는 초당 44100개의 frame이 포함되어있음
  - 모노인 경우 샘플 개수가 프레임 개수랑 동일
  - 스테레오는 샘플 개수가 프레임 개수의 2배
- 샘플 한개의 크기를 1byte 로하면 256단계, 2byte로하면 65536개의 소리를 재현할 수 있다.
- 2byte 샘플크기 * 스테레오 = 4 byte frame size

- 4분 노래, 스테레오, 2byte 샘플 , 샘플속도 48000 hz=> 240s * 2 (channel) * 2 (samplesize) * 48000 (Hz) / 8(bit/byte) =5.5Mbyte





Mixer : 믹서란 한개 이상의 라인을 가진 한개의 오디오 장치

- 믹서는 여러개의 input(source) lines 를 mix 시켜 한개이상의 output(target)line으로 내보냄
- SourceDataLine 은 source line
- TargetDataLine.Port 은 output line

- 녹음된 반복 가능한 사운드(Clip 인스턴스)도 input으로 받을 수 있음



Clip : 실시간이 아닌 재생을 위해 사전에 로딩된 오디오 데이터 

- length 알 수 있음
- 어떤 포지션에서든 시작 할 수 있음
- 반복 가능
- Mixer 로 부터 얻어짐



Port : 포트는 오디오 장치에 대한 오디오 입력 또는 출력을 위한 간단한 라인입니다. 

- Source Line (Mixer inputs) 역할의 port는  Microphone(마이크), line input, CD-ROM.. 
- Target Line (Mixer outputs) 역할의 port는 스피커, 헤드폰, line output..
- Port.Info를 통하여 port에 접근 가능함



DataLine : Line을 통해서 start,stop,drain,flush 메소드를 가진 transport-control 을 포함함

- dataline은 현재 포지션, volume, audio format을 알 수 있음
- 오디오 output은 서브클래스 SourceDataLine, Clip에 의해서 처리됨
- 오디오 input은 서브클래스 TargetDataLine에 의해서 처리됨
- data line은 내부적으로 버퍼를 가지고 있음, drain()메소드는 내부 버퍼가 비어지게함, 보통 모든 큐데이터가 처리됨, flush()메소드는 내부 버퍼에있는 큐 데이터를 버림





### 튜토리얼 순서

클래스 구성

![The following context describes this figure](https://docs.oracle.com/javase/tutorial/figures/sound/chapter2.anc3.gif)

##### Getting a Mixer





##### Getting a Line of a Desired Type

Line 얻는법은 두개가있음

- AudioSystem에서 직접 얻기
  - DataLine 로부터와Port.Info 로부터 Line 얻는 방법이 있음
  - `isLineSupported` 는 Mixer가 적절한 line을 가지는지 체크

```java
//직접얻기
AudioSystem.getLine(Line.Info info)
//Line.Info는 추상 클래스로 서브클래스는 Port.Info , DataLine.Info가 있음
//다음 코드는 DataLie.Info로 부터 target data line을 얻음, 
TargetDataLine line;
DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
    format); // format is an AudioFormat object
if (!AudioSystem.isLineSupported(info)) {
    // Handle the error.
    }
    // Obtain and open the line.
try {
    line = (TargetDataLine) AudioSystem.getLine(info);
    line.open(format);
} catch (LineUnavailableException ex) {
        // Handle the error.
    //... 
}

//Port.Info 로 부터 얻기
if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
    try {
        line = (Port) AudioSystem.getLine(
            Port.Info.MICROPHONE);
    }
}


```

- AudioSystem을 통해 얻은 Mixer로부터 얻기

  - ```java
    Info[] mixerInfo = AudioSystem.getMixerInfo();
    Info info = mixerInfo[1]; //특정 믹서 인포 얻기
    Mixer mixer = AudioSystem.getMixer(info); // Mixer.Info 로부터 Mixer 얻기
    
    //Source Line
    mixer.getSourceLines()
    
    //Target Line
    mixer.getTargetLines()
    
    ```



##### source line /target line /port에 대한 이해

- Source line은 Mixer input , Target Line이 Mixer의 Output 임

  - 믹서가 오디오 입력장치

    - source line : Port 객체
    - target line : TargetDataLine 객체 
    - ![The following context describes this figure](https://docs.oracle.com/javase/tutorial/figures/sound/chapter2.anc2.gif)

  - 믹서가 오디오 출력장치

    - source line : SourceDataLine 또는 Clip 객체
    - target line : Port 객체
    - ![The following context describes this figure.](https://docs.oracle.com/javase/tutorial/figures/sound/chapter2.anc.gif)

  - 하드웨어 믹서가 연결이 없을 경우, 소프트웨어 전용 믹서

    - Input : SourceDataLine,Clip 객체
    - Output : TargetDataLine 객체

  - 정리

    - 하드웨어 장치 

      - ```
        source line(input) - Mixer - target line(output)
        Port - 입력장치 Mixer - TargetDataLine
        SourceDateLine or Clip - 출력장치 Mixer - Port
        ```

    - 소프트웨어 믹서

      - ```
        source line(input) - Mixer - target line(output)
        SourceDateLine or Clip  - 소프트웨어 Mixer - TargetDataLine
        ```

  - 내컴퓨터 상태

    - 한글버전 리얼택 사운드 드라이버 설치하면 깨짐-> 영어버전으로 설치하라고함 여튼

    - ```
      ***********************************************
      Port 스피커(High Definition Audio Devic , Unknown Vendor, 0.0, Port Mixer
      Source
      Target
      - SPEAKER target port
      ***********************************************
      Port Analog Audio In(FusionHDTV Expr , Unknown Vendor, 10.0, Port Mixer
      Source
      - MICROPHONE source port
      Target
      - ¸¶½º target port
      ***********************************************
      Port 마이크(USB Microphone) , Unknown Vendor, 1.1, Port Mixer
      Source
      - MICROPHONE source port
      Target
      - ¸¶½º target port
      ***********************************************
      주 사운드 드라이버 , Unknown Vendor, Unknown Version, Direct Audio Device: DirectSound Playback
      Source
      - interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes
      - interface Clip supporting 8 audio formats, and buffers of at least 32 bytes
      Target
      ***********************************************
      스피커(High Definition Audio Device) , Unknown Vendor, Unknown Version, Direct Audio Device: DirectSound Playback
      Source
      - interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes
      - interface Clip supporting 8 audio formats, and buffers of at least 32 bytes
      Target
      ***********************************************
      주 사운드 캡처 드라이버 , Unknown Vendor, Unknown Version, Direct Audio Device: DirectSound Capture
      Source
      Target
      - interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes
      ***********************************************
      마이크(USB Microphone) , Unknown Vendor, Unknown Version, Direct Audio Device: DirectSound Capture
      Source
      Target
      - interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes
      ***********************************************
      Analog Audio In(FusionHDTV Expr , Unknown Vendor, Unknown Version, Direct Audio Device: DirectSound Capture
      Source
      Target
      - interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes
      
      ```

    - Port 객체 3개

      - 스피커 , 마이크, 티비수신카드
      
      





### Playing Back Audio

Clip : non -real -time sound data

SourceDataLine : Streaming data 



Clip 얻기

- AudioSystem.getClip() : system default mixer로부터 clip을 얻음
- AudioSystem.getClip(mixer) : 특정 믹서로 부터 얻음



AudioInputStream 으로 mp3 열려고 하니까 javax.sound.sampled.UnsupportedAudioFileException 발생

- 기본 라이브러리는 mp3 지원안함
- Java media framWork(JMF) : Java 멀티미디어 API 보강
- JavaZoom jlayer : 외부 라이브러리 등 사용





###  포맷

#### Wave



##### 헤더

[참고 사이트 1](https://crystalcube.co.kr/123)

[참고사이트2(여기가 더좋음 )](http://soundfile.sapp.org/doc/WaveFormat/)

![img](http://soundfile.sapp.org/doc/WaveFormat/wave-bytes.gif)

**Little Endian 유의해서 읽을 것**



![img](https://blog.kakaocdn.net/dn/cUnIvC/btqAIDjGntK/N9hlSdW85Ak68vFDA7H3j0/img.jpg)



**Little Endian 유의해서 읽을 것**



![img](https://blog.kakaocdn.net/dn/brcJP0/btqAK4sV7Gg/Jd8eAUPpN8UStkMFmIsb0k/img.jpg)



**Little Endian 유의해서 읽을 것**



![img](https://blog.kakaocdn.net/dn/eDsk8a/btqAK4sWNRc/XnizjIQ7hvRN0yMStCyPck/img.jpg)



##### Little endian reverse 

1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 배열이 있을때

frame size가 4 이고 little endian 이면

reverse하면

17,18,19,20 ,13,14,15,16,9,10,11,12,5,6,7,8,1,2,3,4

순으로 됨





PCM Data : 헤더없이 Raw 오디오 데이터만 있는것

https://anythingcafe.tistory.com/2?category=905284







