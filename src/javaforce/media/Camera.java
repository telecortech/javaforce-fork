package javaforce.media;

/**
 * Common Camera (Native Code only)
 *
 * @author pquiring
 *
 * Created : Aug 20, 2013
 */

import javaforce.*;
import javaforce.jna.*;

public class Camera {
  public static interface Input {
    public boolean init();
    public boolean uninit();
    public String[] listDevices();
    public boolean start(int deviceIdx, int width, int height);
    public boolean stop();
    public int[] getFrame();
    public int getWidth();
    public int getHeight();
  }
  public static Input getInput() {
    if (JF.isWindows()) {
      return new WinCamera();
    } else if (JF.isMac()) {
      return new MacCameraQT();
    } else {
      return new LnxCamera();
    }
  }
/*
  //not working yet!
  private static Input getInputFFMPEG() {
    if (FFMPEG.init()) {
      return new FFMPEG.V4L2Camera();
    }
    return null;
  }
*/
}
