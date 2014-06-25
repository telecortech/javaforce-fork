/**  Receives the encoded image, decodes it and displays it on the VideoWindow.
 *
 * @author pquiring
 */

import java.io.*;
import java.util.*;

import javaforce.*;
import javaforce.jna.*;
import javaforce.voip.*;

public class RemoteCamera extends Thread {
  private volatile boolean active = true;
  private volatile boolean done = false;
  public RTPChannel channel;
  private VideoWindow videoWindow;

  //    private RandomAccessFile raf;
  public RemoteCamera(RTPChannel channel, VideoWindow vw) {
    this.channel = channel;
    this.videoWindow = vw;
  }

  public void run() {
    //      try { raf = new RandomAccessFile("media_remote.h264", "rw"); } catch (Exception e) {}
    remoteImage = new JFImage();
    remoteImage.setImageSize(PhonePanel.vx, PhonePanel.vy);
    videoWindow.addCamera(channel);
    while (active) {
      synchronized (lock) {
        try {
          lock.wait();
        } catch (Exception e) {
        }
      }
      if (imageList.isEmpty()) {
        continue;
      }
      JFImage img = imageList.remove(0);
      remoteImage.getGraphics().drawImage(img.getImage(), 0, 0, PhonePanel.vx, PhonePanel.vy, null);
      videoWindow.setRemoteImage(channel, remoteImage);
    }
    if (decoder != null) {
      decoder.stop();
      decoder = null;
    }
    videoWindow.delCamera(channel);
    //      try { raf.close(); } catch (Exception e) {}
    done = true;
  }

  public void cancel() {
    active = false;
    while (!done) {
      synchronized (lock) {
        lock.notify();
      }
      JF.sleep(5);
    }
  }

  public void rtp_jpeg_data(byte[] data, int pos, int len) {
    if (!active) {
      return;
    }
    byte[] jpeg = rtpJpeg.decode(Arrays.copyOfRange(data, pos, pos + len));
    if (jpeg != null) {
      JFImage tmp = new JFImage();
      if (!tmp.load(new ByteArrayInputStream(jpeg))) {
        JFLog.log("RemoteCamera : failed to decode image");
        return;
      }
      imageList.add(tmp);
      synchronized (lock) {
        lock.notify();
      }
    }
  }

  public void rtp_h263_data(byte[] data, int pos, int len) {
    if (!active) {
      return;
    }
    byte[] pack = rtpH263.decode(Arrays.copyOfRange(data, pos, pos + len));
    if (pack == null) {
      return;
      //      printArray("decoded_h263", pack, 0, pack.length);
    }
    if (decoder == null) {
      decoder = new FFMPEG.VideoDecoder();
      if (!decoder.start(FFMPEG.AV_CODEC_ID_H263, PhonePanel.vx, PhonePanel.vy)) {
        JFLog.log("H263 Decoder failed to start");
        decoder.stop();
        decoder = null;
        return;
      }
    }
    int[] px = decoder.decode(pack);
    if (px == null) {
      return;
    }
    JFImage img = new JFImage();
    img.setSize(PhonePanel.vx, PhonePanel.vy);
    img.putPixels(px, 0, 0, PhonePanel.vx, PhonePanel.vy, 0);
    imageList.add(img);
    synchronized (lock) {
      lock.notify();
    }
  }

  public void rtp_h263_1998_data(byte[] data, int pos, int len) {
    if (!active) {
      return;
    }
    byte[] pack = rtpH263_1998.decode(Arrays.copyOfRange(data, pos, pos + len));
    if (pack == null) {
      return;
      //      printArray("decoded_1998", pack, 0, pack.length);
    }
    if (decoder == null) {
      decoder = new FFMPEG.VideoDecoder();
      if (!decoder.start(FFMPEG.AV_CODEC_ID_H263, PhonePanel.vx, PhonePanel.vy)) {
        JFLog.log("H263-1998 Decoder failed to start");
        decoder.stop();
        decoder = null;
        return;
      }
    }
    int[] px = decoder.decode(pack);
    if (px == null) {
      return;
    }
    JFImage img = new JFImage();
    img.setSize(PhonePanel.vx, PhonePanel.vy);
    img.putPixels(px, 0, 0, PhonePanel.vx, PhonePanel.vy, 0);
    imageList.add(img);
    synchronized (lock) {
      lock.notify();
    }
  }

  public void rtp_h263_2000_data(byte[] data, int pos, int len) {
    if (!active) {
      return;
    }
    byte[] pack = rtpH263_2000.decode(Arrays.copyOfRange(data, pos, pos + len));
    if (pack == null) {
      return;
      //      printArray("decoded_2000", pack, 0, pack.length);
    }
    if (decoder == null) {
      decoder = new FFMPEG.VideoDecoder();
      if (!decoder.start(FFMPEG.AV_CODEC_ID_H263, PhonePanel.vx, PhonePanel.vy)) {
        JFLog.log("H263-2000 Decoder failed to start");
        decoder.stop();
        decoder = null;
        return;
      }
    }
    int[] px = decoder.decode(pack);
    if (px == null) {
      return;
    }
    JFImage img = new JFImage();
    img.setSize(PhonePanel.vx, PhonePanel.vy);
    img.putPixels(px, 0, 0, PhonePanel.vx, PhonePanel.vy, 0);
    imageList.add(img);
    synchronized (lock) {
      lock.notify();
    }
  }

  public void rtp_h264_data(byte[] data, int pos, int len) {
    if (!active) {
      return;
    }
    //      printArray("i:rtpH264:", data, 0, len);
    byte[] pack = rtpH264.decode(Arrays.copyOfRange(data, pos, pos + len));
    if (pack == null) {
      return;
      //      printArray("decoded_h264", pack, 0, pack.length);
      //      try { raf.write(pack); } catch (Exception e) {}
    }
    if (decoder == null) {
      decoder = new FFMPEG.VideoDecoder();
      if (!decoder.start(FFMPEG.AV_CODEC_ID_H264, PhonePanel.vx, PhonePanel.vy)) {
        JFLog.log("H264 Decoder failed to start");
        decoder.stop();
        decoder = null;
        return;
      }
    }
    int[] px = decoder.decode(pack);
    if (px == null) {
      return;
    }
    //      JFLog.log("px[]=" + px[0] + "," + px[1] + "," + px[2]);
    JFImage img = new JFImage();
    img.setSize(PhonePanel.vx, PhonePanel.vy);
    img.putPixels(px, 0, 0, PhonePanel.vx, PhonePanel.vy, 0);
    imageList.add(img);
    synchronized (lock) {
      lock.notify();
    }
  }

  public void rtp_vp8_data(byte[] data, int pos, int len) {
    if (!active) {
      return;
    }
    //      printArray("i:rtpVP8:", data, 0, len);
    byte[] pack = rtpVP8.decode(Arrays.copyOfRange(data, pos, pos + len));
    if (pack == null) {
      return;
      //      printArray("decoded_VP8", pack, 0, pack.length);
      //      try { raf.write(pack); } catch (Exception e) {}
    }
    if (decoder == null) {
      decoder = new FFMPEG.VideoDecoder();
      if (!decoder.start(FFMPEG.AV_CODEC_ID_VP8, PhonePanel.vx, PhonePanel.vy)) {
        JFLog.log("VP8 Decoder failed to start");
        decoder.stop();
        decoder = null;
        return;
      }
    }
    int[] px = decoder.decode(pack);
    if (px == null) {
      return;
    }
    //      JFLog.log("px[]=" + px[0] + "," + px[1] + "," + px[2]);
    JFImage img = new JFImage();
    img.setSize(PhonePanel.vx, PhonePanel.vy);
    img.putPixels(px, 0, 0, PhonePanel.vx, PhonePanel.vy, 0);
    imageList.add(img);
    synchronized (lock) {
      lock.notify();
    }
  }

  private JFImage remoteImage;
  private final Object lock = new Object();
  private RTPJPEG rtpJpeg = new RTPJPEG();
  private RTPH263 rtpH263 = new RTPH263();
  private RTPH263_1998 rtpH263_1998 = new RTPH263_1998();
  private RTPH263_2000 rtpH263_2000 = new RTPH263_2000();
  private RTPH264 rtpH264 = new RTPH264();
  private RTPVP8 rtpVP8 = new RTPVP8();
  private FFMPEG.VideoDecoder decoder;
  private Vector<JFImage> imageList = new Vector<JFImage>();
}
