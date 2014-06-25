/** Manages the local camera and sends the encoded image to all remote parties.
 *
 * @author pquiring
 */

import java.io.*;
import java.util.*;
import javaforce.*;
import javaforce.jna.*;
import javaforce.media.*;
import javaforce.voip.*;

public class LocalCamera extends Thread implements FFMPEGIO {
  private volatile boolean active = false;
  private volatile boolean main_done = false;
  private Camera.Input camera;
  private static Codec codec;
  private static volatile boolean inuse[] = new boolean[6];
  private static PhoneLine lines[];
  private static LocalCamera thread;
  private static Object useLock = new Object();
  //    private RandomAccessFile raf;

  public static void setCodec(Codec codec) {
    LocalCamera.codec = codec;
  }

  public static Codec getCodec() {
    return codec;
  }

  public static void init(PhoneLine lines[]) {
    LocalCamera.lines = lines;
  }

  public static void enable(int line) {
    synchronized(useLock) {
      inuse[line] = true;
      if (thread == null) {
        thread = new LocalCamera();
        thread.start();
      }
    }
  }

  public static void disable(int line) {
    synchronized(useLock) {
      if (inuse[line] == false) return;
      inuse[line] = false;
      for(int a=0;a<6;a++) {
        if (inuse[a] == true) return;
      }
    }
    if (thread != null) {
      thread.cancel();
      thread = null;
    }
  }

  public static boolean isRunning() {
    return thread != null;
  }

  public void run() {
    active = true;
    main_done = false;
    try {
      int[] res = Settings.current.getVideoResolution();
      camera = Camera.getInput();
      //      try { raf = new RandomAccessFile("media_local." + codec.name, "rw"); } catch (Exception e) {}
      if (!camera.init()) {
        JF.showError("Error", "Failed to init camera");
        return;
      }
      String[] devices = camera.listDevices();
      if (devices == null || devices.length == 0) {
        JF.showError("Error", "Failed to find a camera");
        return;
      }
      int idx = 0;
      for(int a=0;a<devices.length;a++) {
        if (devices[a].equals(Settings.current.videoDevice)) {
          idx = a;
          break;
        }
      }
      if (!camera.start(idx, PhonePanel.vx, PhonePanel.vy)) {
        JF.showError("Error", "Failed to start camera");
        main_done = true;
        return;
      }
      int cx = camera.getWidth();
      int cy = camera.getHeight();
      JFLog.log("camera size=" + cx + "x" + cy);
      localImage = new JFImage();
      localImage.setImageSize(PhonePanel.vx, PhonePanel.vy);
      localCameraSender = new LocalCameraSender();
      localCameraSender.start();
      JFLog.log("LocalCamera starting");
      while (active) {
        while (active && list.size() > 0) {
          JF.sleep(10);
        }
        JF.sleep(1000 / Settings.current.videoFPS);
        int[] px = camera.getFrame();
        if (px == null) {
          continue;
        }
        JFImage tmp = new JFImage(cx, cy);
        tmp.putPixels(px, 0, 0, cx, cy, 0);
        localImage.getGraphics().drawImage(tmp.getImage(), 0, 0, PhonePanel.vx, PhonePanel.vy, null);
        synchronized(useLock) {
          for(int a=0;a<6;a++) {
            if (inuse[a]) {
              VideoWindow vw = lines[a].videoWindow;
              if (vw != null) vw.setLocalImage(localImage);
            }
          }
        }
        if (codec.name.equals("JPEG")) {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          localImage.saveJPG(baos);
          byte[][] packets = rtpJpeg.encode(baos.toByteArray(), PhonePanel.vx, PhonePanel.vy);
          for (int a = 0; a < packets.length; a++) {
            list.add(packets[a]);
          }
          synchronized (lock) {
            lock.notify();
          }
          continue;
        }
        if (codec.name.startsWith("H263")) {
          //H263,H263-1998,H263-2000
          px = localImage.getPixels();
          if (encoder == null) {
            encoder = new FFMPEG.Encoder();
            encoder.config_gop_size = 5;
            if (!encoder.start(this, PhonePanel.vx, PhonePanel.vy, 24, -1, -1, "h263", true, false)) {
              JFLog.log("H263 encoder failed to start");
              encoder.stop();
              encoder = null;
              continue;
            }
          }
          encoder.add_video(px);
          continue;
        }
        if (codec.name.equals("H264")) {
          px = localImage.getPixels();
          if (encoder == null) {
            encoder = new FFMPEG.Encoder();
            encoder.config_gop_size = 5;
            if (!encoder.start(this, PhonePanel.vx, PhonePanel.vy, 24, -1, -1, "h264", true, false)) {
              JFLog.log("H264 encoder failed to start");
              encoder.stop();
              encoder = null;
              continue;
            }
          }
          encoder.add_video(px);
          continue;
        }
        if (codec.name.equals("VP8")) {
          px = localImage.getPixels();
          if (encoder == null) {
            encoder = new FFMPEG.Encoder();
            encoder.config_gop_size = 5;
            if (!encoder.start(this, PhonePanel.vx, PhonePanel.vy, 24, -1, -1, "vpx", true, false)) {
              JFLog.log("VP8 encoder failed to start");
              encoder.stop();
              encoder = null;
              continue;
            }
          }
          encoder.add_video(px);
          continue;
        }
        JFLog.log("err:local camera running without a valid codec");
      }
      camera.stop();
      camera.uninit();
      if (encoder != null) {
        encoder.stop();
        encoder = null;
      }
      while (!localCameraSender.sender_done) {
        JFLog.log("Waiting for LocalCameraSender to stop");
        JF.sleep(500);
        synchronized (lock) {
          lock.notify();
        }
      }
      //      try { raf.close(); } catch (Exception e) {}
    } catch (Exception e) {
      JFLog.log(e);
    }
    JFLog.log("LocalCamera done");
    main_done = true;
  }

  public void cancel() {
    active = false;
    while (!main_done) {
      JFLog.log("Waiting for LocalCamera to stop");
      JF.sleep(500);
    }
  }

  private class LocalCameraSender extends Thread {
    private volatile boolean sender_done = false;
    public void run() {
      try {
        while (active) {
          if (list.isEmpty()) {
            synchronized (lock) {
              try {
                lock.wait();
              } catch (Exception e) {
              }
            }
          }
          if (list.isEmpty()) {
            continue;
          }
          byte[] data = list.remove(0);
          if (data == null) {
            continue;
          }
          synchronized(useLock) {
            for(int a=0;a<6;a++) {
              if (!inuse[a]) continue;
              RTP rtp = lines[a].videoRTP;
              if (rtp == null) continue;
              if (!rtp.active) continue;
              RTPChannel channels[] = (RTPChannel[])rtp.channels.toArray(new RTPChannel[0]);
              for(int r=0;r<channels.length;r++) {
                RTPChannel channel = channels[r];
                if (channel.stream.type == SDP.Type.video && channel.stream.canSend()) {
                  channel.writeRTP(data, 0, data.length);
                }
              }
            }
          }
        }
      } catch (Exception e) {
        JFLog.log(e);
      }
      JFLog.log("LocalCameraSender done");
      sender_done = true;
    }
  }

  private LocalCamera.LocalCameraSender localCameraSender;
  private JFImage localImage;
  private RTPJPEG rtpJpeg = new RTPJPEG();
  private RTPH263 rtpH263 = new RTPH263();
  private RTPH263_1998 rtpH263_1998 = new RTPH263_1998();
  private RTPH263_2000 rtpH263_2000 = new RTPH263_2000();
  private RTPH264 rtpH264 = new RTPH264();
  private RTPVP8 rtpVP8 = new RTPVP8();
  private Object lock = new Object();
  private Vector<byte[]> list = new Vector<byte[]>();
  private FFMPEG.Encoder encoder;

  public int read(FFMPEG.Coder coder, byte[] bytes, int i) {
    return 0;
  }

  public int write(FFMPEG.Coder coder, byte[] bytes) {
    int len = bytes.length;
    byte[][] tmp;
    if (codec.name.equals("H263")) {
      //        printArray("encoded_h263", bytes, 0, bytes.length);
      tmp = rtpH263.encode(bytes, PhonePanel.vx, PhonePanel.vy, codec.id);
      if (tmp != null) {
        for (int a = 0; a < tmp.length; a++) {
          list.add(tmp[a]);
        }
      }
    } else if (codec.name.equals("VP8")) {
      //        printArray("encoded_vp8", bytes, 0, bytes.length);
      //        try { raf.write(bytes); } catch (Exception e) {}
      tmp = rtpVP8.encode(bytes, PhonePanel.vx, PhonePanel.vy, codec.id);
      if (tmp != null) {
        for (int a = 0; a < tmp.length; a++) {
          //            printArray("o:rtpVP8:"+a, tmp[a], 0, tmp[a].length);
          list.add(tmp[a]);
        }
      }
    } else if (codec.name.equals("H264")) {
      //        printArray("encoded_h264", bytes, 0, bytes.length);
      //        try { raf.write(bytes); } catch (Exception e) {}
      tmp = rtpH264.encode(bytes, PhonePanel.vx, PhonePanel.vy, codec.id);
      if (tmp != null) {
        for (int a = 0; a < tmp.length; a++) {
          //            printArray("o:rtpH264:"+a, tmp[a], 0, tmp[a].length);
          list.add(tmp[a]);
        }
      }
    } else if (codec.name.equals("H263-1998")) {
      //        printArray("encoded_1998", bytes, 0, bytes.length);
      tmp = rtpH263_1998.encode(bytes, PhonePanel.vx, PhonePanel.vy, codec.id);
      if (tmp != null) {
        for (int a = 0; a < tmp.length; a++) {
          list.add(tmp[a]);
        }
      }
    } else if (codec.name.equals("H263-2000")) {
      //        printArray("encoded_2000", bytes, 0, bytes.length);
      tmp = rtpH263_2000.encode(bytes, PhonePanel.vx, PhonePanel.vy, codec.id);
      if (tmp != null) {
        for (int a = 0; a < tmp.length; a++) {
          list.add(tmp[a]);
        }
      }
    }
    synchronized (lock) {
      lock.notify();
    }
    return len;
  }

  public long seek(FFMPEG.Coder coder, long l, int i) {
    return 0;
  }
}
