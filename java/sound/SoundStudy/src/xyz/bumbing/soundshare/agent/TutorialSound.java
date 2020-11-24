package xyz.bumbing.soundshare.agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TutorialSound {

  public static void main(String[] args) {

    // displayMixerInfo();

    // playback("test.wav");
    // playbackSource("test.wav");
    // playbackSourceReverse("testReverse.wav");
    // capture("test.wav");
    // captureReverse("testReverse.wav");

    // playbackLeft("lol.wav");
    playbackSurround("lol.wav");

  }

  public static void displayMixerInfo() {
    // 컴퓨터 Mixer.Info 얻기
    Info[] mixerInfo = AudioSystem.getMixerInfo();
    for (int i = 0; i < mixerInfo.length; i++) {
      System.out.println("-------------------------------------------");
      Info info = mixerInfo[i];
      // Mixer.Info 로부터 Mixer 얻기
      Mixer mixer = AudioSystem.getMixer(info);

      System.out.println(String.format("%s , %s, %s, %s", info.getName(), info.getVendor(),
          info.getVersion(), info.getDescription()));
      System.out.println("Source");
      // Mixer로 부터 SourceLine.Info 얻기
      Arrays.stream(mixer.getSourceLineInfo())
          .forEach(s -> System.out.println("- " + s.toString()));
      System.out.println("Target");
      // Mixer로 부터 TargetLine.Info 얻기
      Arrays.stream(mixer.getTargetLineInfo())
          .forEach(s -> System.out.println("- " + s.toString()));

    }


  }

  public static AudioFormat getAudioFormat(float sample) {
    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    int sampleSizeBits = 16;
    int channels = 2;
    int framesize = 4;
    boolean bigEndian = false;
    return new AudioFormat(encoding, sample, sampleSizeBits, channels, framesize, sample,
        bigEndian);
  }


  public static void getMaxLines() {

    AudioFormat audioFormat = getAudioFormat(44100);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("not supported");
    }

    try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);) {
      line.open(audioFormat);
    } catch (LineUnavailableException ex) {
      ex.printStackTrace();
    }



  }

  public static DataLine.Info getTargetDataLine(int samplate) {
    AudioFormat audioFormat = getAudioFormat(samplate);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

    if (!AudioSystem.isLineSupported(info)) {
      throw new IllegalStateException("지원하는 라인이 없음");
    }
    return info;
  }

  public static void playback(String filepath) {
    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        Clip clip = AudioSystem.getClip();) {
      // void open(AudioInputStream stream)
      // void open(AudioFormat format, byte[] data, int offset, int bufferSize)
      clip.open(audioInputStream);
      clip.start();
      Thread.sleep(5000);
      clip.stop();
      System.out.println("frame size :" + clip.getFrameLength());
      clip.setFramePosition(clip.getFrameLength() / 2);
      Thread.sleep(1000);
      clip.start();
      Thread.sleep(5000);
      clip.stop();
      clip.drain();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void playbackSource(String filepath) {
    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));) {
      AudioFormat format = audioInputStream.getFormat();
      SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);
      sourceDataLine.open();
      int buffersize = 1024;
      byte[] buffer = new byte[buffersize];
      long total = 0;
      int numBytesRead = 0;
      long totalToRead = 10726272;
      sourceDataLine.start();

      while (total < totalToRead) {
        numBytesRead = audioInputStream.read(buffer, 0, buffersize);
        if (numBytesRead == -1)
          break;
        total += numBytesRead;
        sourceDataLine.write(buffer, 0, numBytesRead);
      }
      sourceDataLine.drain();
      sourceDataLine.stop();

      System.out.println(total);

    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void playbackSourceReverse(String filepath) {
    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        SourceDataLine sourceDataLine =
            AudioSystem.getSourceDataLine(audioInputStream.getFormat());) {

      sourceDataLine.open();
      int buffersize = 1024;
      byte[] buffer = new byte[buffersize];
      long total = 0;
      int numBytesRead = 0;
      long totalToRead = 42905088;
      sourceDataLine.start();

      byte[] readAllBytes = audioInputStream.readAllBytes();
      System.out.println(readAllBytes.length);

      reverseLittleEndian(readAllBytes, 4);
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(readAllBytes);

      while (total < totalToRead) {
        numBytesRead = byteArrayInputStream.read(buffer, 0, buffersize);
        if (numBytesRead == -1)
          break;
        total += numBytesRead;
        sourceDataLine.write(buffer, 0, numBytesRead);
      }
      sourceDataLine.drain();
      sourceDataLine.stop();

      // 10726272
      System.out.println(total);

    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void playbackLeft(String filepath) {
    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        SourceDataLine sourceDataLine =
            AudioSystem.getSourceDataLine(audioInputStream.getFormat());) {
      AudioFormat format = audioInputStream.getFormat();
      sourceDataLine.open();
      int buffersize = 1024;
      byte[] buffer = new byte[buffersize];
      long total = 0;
      int numBytesRead = 0;
      int timeout = 20;// second
      long totalToRead = (long) (format.getFrameSize() * format.getSampleRate() * timeout);
      sourceDataLine.start();

      byte[] readAllBytes = audioInputStream.readAllBytes();
      System.out.println(readAllBytes.length);

      byte[][] divideChannel =
          divideChannel(readAllBytes, format.getSampleSizeInBits(), format.getChannels());
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(divideChannel[0]);

      while (total < totalToRead) {
        numBytesRead = byteArrayInputStream.read(buffer, 0, buffersize);
        if (numBytesRead == -1)
          break;
        total += numBytesRead;
        sourceDataLine.write(buffer, 0, numBytesRead);
      }
      sourceDataLine.drain();
      sourceDataLine.stop();

      // 10726272
      System.out.println(total);

    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void playbackSurround(String filepath) {
    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        SourceDataLine sourceDataLine =
            AudioSystem.getSourceDataLine(audioInputStream.getFormat());) {
      AudioFormat format = audioInputStream.getFormat();
      sourceDataLine.open();
      int buffersize = 1024;
      byte[] buffer = new byte[buffersize];
      long total = 0;
      int numBytesRead = 0;
      int timeout = 30;// second
      long totalToRead = (long) (format.getFrameSize() * format.getSampleRate() * timeout);
      sourceDataLine.start();

      byte[] readAllBytes = audioInputStream.readAllBytes();
      System.out.println(readAllBytes.length);
      int surroundTime = 5;// 5초
      surroundStereoSound(readAllBytes, format.getSampleSizeInBits(), format.getSampleRate(),
          surroundTime);
      saveFile(readAllBytes, format, filepath + "_surround");
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(readAllBytes);

      while (total < totalToRead) {
        numBytesRead = byteArrayInputStream.read(buffer, 0, buffersize);
        if (numBytesRead == -1)
          break;
        total += numBytesRead;
        sourceDataLine.write(buffer, 0, numBytesRead);
      }
      sourceDataLine.drain();
      sourceDataLine.stop();

      // 10726272
      System.out.println(total);

    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 1speed = 1Frame에 1회, 초당 44100
  private static void surroundStereoSound(byte[] rawdata, int sampleBit, float sampleRate,
      int surroundTime) {
    int channel = 2;
    int sameplesize = sampleBit / 8;
    if (rawdata.length % (sameplesize * channel) != 0) {
      throw new IllegalArgumentException("endian 크기 안맞음");
    }

    int sampleTotal = rawdata.length / sameplesize;
    int iter = 0;
    double cal = 0;
    float surroundTimeVal = sampleRate * surroundTime; //
    for (int i = 0; i < sampleTotal; i++) {
      byte[] sample = new byte[sameplesize];
      if (i == 50000) {
        System.out.println();
      }
      System.arraycopy(rawdata, i * sameplesize, sample, 0, 2);
      short soundInt = littleEndianByteToShort(sample);
      // 왼쪽 샘플
      if (i % 2 == 0) {
        soundInt = (short) (soundInt * cal);
      } else {
        // 오른쪽 샘플
        soundInt = (short) (soundInt * (1 - cal));
        iter++;
        cal = (Math.sin(Math.toRadians(360 * iter / surroundTimeVal)) + 1) / 2;

      }
      byte[] endian = shortTolittleEndianByte((short) soundInt);
      System.arraycopy(endian, 0, rawdata, i * sameplesize, 2);

    }

  }

  private static short littleEndianByteToShort(byte[] bytes) {
    return (short) ((bytes[1] & 0xff) << 8 | (bytes[0] & 0xff));
  }

  private static byte[] shortTolittleEndianByte(short shortVar) {
    byte littleShort[] = new byte[2];
    littleShort[0] = (byte) ((shortVar >> 0) & 0xff);
    littleShort[1] = (byte) ((shortVar >> 8) & 0xff);
    return littleShort;
  }



  private static void reverseLittleEndian(byte[] rawdata, int bytenum) {

    /*
     * rawdata length / bytenum 등분
     * 
     */
    if (rawdata.length % bytenum != 0) {
      throw new IllegalArgumentException("endian 크기 안맞음");
    }

    byte[] temp = new byte[bytenum];


    for (int i = 0; i < rawdata.length / 2; i += bytenum) {
      for (int j = 0; j < bytenum; j++) {
        temp[j] = rawdata[i + j];
      }
      for (int j = 0; j < bytenum; j++) {
        rawdata[i + j] = rawdata[rawdata.length - i - bytenum + j];
        rawdata[rawdata.length - i - bytenum + j] = temp[j];
      }
    }
  }

  private static byte[][] divideChannel(byte[] rawdata, int sameplebit, int channel) {
    int sameplesize = sameplebit / 8;
    if (rawdata.length % (sameplesize * channel) != 0) {
      throw new IllegalArgumentException("endian 크기 안맞음");
    }

    byte[][] div = new byte[channel][];
    for (int i = 0; i < channel; i++) {
      div[i] = new byte[rawdata.length];
      System.arraycopy(rawdata, 0, div[i], 0, rawdata.length);
    }

    for (int i = 0; i < rawdata.length; i++) {
      for (int j = 0; j < channel; j++) {
        if (j != ((i / sameplesize) % channel)) {
          div[j][i] = 0;
        }
      }
    }


    return div;
  }

  /*
   * 녹음
   */
  public static void capture(String path) {
    AudioFormat audioFormat = getAudioFormat(44100);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("지원안함");
    }
    try (ByteArrayOutputStream bao = new ByteArrayOutputStream();
        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);) {

      microphone.open(audioFormat);
      microphone.start();

      long start = System.currentTimeMillis();
      int timeout = 5;
      int buffersize = microphone.getBufferSize() / 5;
      byte[] buffer = new byte[buffersize];

      while ((System.currentTimeMillis() - start) / 1000 < timeout) {
        int read = microphone.read(buffer, 0, buffersize);
        bao.write(buffer, 0, read);
      }
      byte[] audioData = bao.toByteArray();
      saveFile(audioData, audioFormat, path);
    } catch (LineUnavailableException ex) {
      ex.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * 녹음
   */
  public static void captureReverse(String path) {
    AudioFormat audioFormat = getAudioFormat(44100);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("지원안함");
    }
    try (ByteArrayOutputStream bao = new ByteArrayOutputStream();
        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);) {

      microphone.open(audioFormat);
      microphone.start();

      long start = System.currentTimeMillis();
      int timeout = 5;
      int buffersize = microphone.getBufferSize() / 5;
      byte[] buffer = new byte[buffersize];

      while ((System.currentTimeMillis() - start) / 1000 < timeout) {
        int read = microphone.read(buffer, 0, buffersize);
        bao.write(buffer, 0, read);
      }
      byte[] audioData = bao.toByteArray();
      reverseLittleEndian(audioData, audioFormat.getFrameSize());
      saveFile(audioData, audioFormat, path);

    } catch (LineUnavailableException ex) {
      ex.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void saveFile(byte[] audioData, AudioFormat audioFormat, String dest)
      throws IOException {
    InputStream input = new ByteArrayInputStream(audioData);
    AudioInputStream audioInputStream =
        new AudioInputStream(input, audioFormat, audioData.length / audioFormat.getFrameSize());
    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(dest));
  }
}
