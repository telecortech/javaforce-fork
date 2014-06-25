/**
 *
 * @author pquiring
 *
 * Created : Sept 21, 2013
 */

import java.io.*;

import javaforce.*;
import javaforce.jna.*;

public class Decoder implements FFMPEGIO {
  FFMPEG.Decoder decoder = new FFMPEG.Decoder();
  RandomAccessFile fin;
  Object oout;
  public boolean decode(String in) {
    try {
      fin = new RandomAccessFile(in, "r");
      if (!decoder.start(this, -1, -1, -1, -1, true)) throw new Exception("Decoder Failed to start");
    } catch (Exception e) {
      JFLog.log(e);
      return false;
    }
    return true;
  }

  public short[] getSamples() {
    int type;
    do {
      type = decoder.read();
      if (type == FFMPEG.END_FRAME) return null;
    } while (type == FFMPEG.NULL_FRAME);
    return decoder.get_audio();
  }

  public int getSampleRate() {
    return decoder.getSampleRate();
  }

  public int getChannels() {
    return decoder.getChannels();
  }

  public void stop() {
    decoder.stop();
  }

  public int read(FFMPEG.Coder coder, byte[] bytes, int i) {
    if (coder == decoder) {
      try {
        return fin.read(bytes, 0, i);
      } catch (Exception e) {
        JFLog.log(e);
        return 0;
      }
    }
    return 0;
  }

  public int write(FFMPEG.Coder coder, byte[] bytes) {
    return 0;
  }

  public long seek(FFMPEG.Coder coder, long pos, int how) {
    if (coder == decoder) {
      try {
        switch (how) {
          case FFMPEG.SEEK_SET: break;
          case FFMPEG.SEEK_CUR: pos += fin.getFilePointer(); break;
          case FFMPEG.SEEK_END: pos += fin.length(); break;
        }
        fin.seek(pos);
        return pos;
      } catch (Exception e) {
        JFLog.log(e);
      }
      return 0;
    }
    return 0;
  }
}
