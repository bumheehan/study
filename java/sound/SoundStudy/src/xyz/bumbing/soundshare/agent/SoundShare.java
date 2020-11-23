package xyz.bumbing.soundshare.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundShare {

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    SoundShare soundShare = new SoundShare();
    soundShare.connect();
    // soundShare.change();
    System.out.println("끝 : " + (System.currentTimeMillis() - startTime));
  }

  private static List<byte[]> arr = new ArrayList<>();
  private List<Integer> arri = new ArrayList<>();

  private AudioFormat getAudioFormat(float sample) {
    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    int sampleSizeBits = 16;
    int channels = 2;
    int framesize = 4;
    boolean bigEndian = false;
    return new AudioFormat(encoding, sample, sampleSizeBits, channels, framesize, sample,
        bigEndian);
  }

  public void connect() {


    AudioFormat format = getAudioFormat(44100);

    TargetDataLine line = null;
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("line not supported:" + line);
    }

    try {
      line = (TargetDataLine) AudioSystem.getLine(info);
      line.open(format);
      System.out.println("line opened:" + line);

      long startTime = System.currentTimeMillis() / 1000;
      int timeout = 5;// sec
      line.start();

      byte[] buffer = new byte[1024];
      int numBytesRead = 0;
      while ((System.currentTimeMillis() / 1000 - startTime) < timeout) {
        // Read the next chunk of data from the TargetDataLine.
        numBytesRead = line.read(buffer, 0, buffer.length);

        System.out.println("\nnumBytesRead:" + numBytesRead);
        if (numBytesRead == 0)
          continue;

        // for (int i = 0; i < numBytesRead; i++) {
        // arri.add((int) buffer[i]);
        // }

        arr.add(Arrays.copyOfRange(buffer, 0, numBytesRead));

      }
      line.stop();

    } catch (LineUnavailableException ex) {
      ex.printStackTrace();
    }
  }

  public static IntStream intStream(byte[] array) {
    return IntStream.range(0, array.length).map(idx -> array[idx]);
  }

  public void change() {
    System.out.println("array length : " + arr.size());
    arr.stream().flatMapToInt(SoundShare::intStream).filter(s -> s > -1).toArray();
  }

  public void play() {
    int totalFramesRead = 0;
    File fileIn = new File("");
    // somePathName is a pre-existing string whose value was
    // based on a user selection.
    try {
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
      int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
      // Set an arbitrary buffer size of 1024 frames.
      int numBytes = 1024 * bytesPerFrame;
      byte[] audioBytes = new byte[numBytes];
      try {
        int numBytesRead = 0;
        int numFramesRead = 0;
        // Try to read numBytes bytes from the file.
        while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
          // Calculate the number of frames actually read.
          numFramesRead = numBytesRead / bytesPerFrame;
          totalFramesRead += numFramesRead;
          // Here, do something useful with the audio data that's
          // now in the audioBytes array...
        }
      } catch (Exception ex) {
        // Handle the error...
      }
    } catch (Exception e) {
      // Handle the error...
    }
  }

}


