package javaforce.utils;

/**
 * Monitor Dir (Linux : inotify)
 *
 * Uses JNA
 *
 * @author pquiring
 *
 * Created : Nov 3, 2013
 */

import java.util.*;

import com.sun.jna.*;

import javaforce.*;


public class monitordir {
  public static interface Listener {
    public void folderChangeEvent(String event, String path);
  }

  public static interface C extends Library {
    public int inotify_init();  //return fd
    public int inotify_add_watch(int fd, String path, int mask);  //return wd
    public int inotify_rm_watch(int fd, int wd);
    public NativeLong read(int wd, Pointer ptr, NativeLong size);
    public int close(int fd);
    public Pointer malloc(int size);
    public void free(Pointer ptr);
  }
  private static C c;
  private static int fd;
  /** Loads native library.  Only need to call once per process. */
  public static boolean init() {
    if (c != null) return true;
    try {
      c = (C)Native.loadLibrary("c", C.class);
      fd = c.inotify_init();
      if (fd == -1) throw new Exception("inotify_init failed");
      new Worker().start();
      return true;
    } catch (Exception e) {
      JFLog.log(e);
      return false;
    }
  }
  /** Stops all watches. */
  public static void uninit() {
    if (c == null) return;
    active = false;
    c.close(fd);
    c = null;
  }
  /** Start watching a folder. */
  public static int add(String path) {
    if (c == null) return -1;
    int wd = c.inotify_add_watch(fd, path, IN_CREATE | IN_DELETE);
//    JFLog.log("wd=" + wd);
    return wd;
  }
  /** Stops watching a folder. */
  public static void remove(int wd) {
    if (c == null) return;
    c.inotify_rm_watch(fd, wd);
    map.remove(wd);
  }
  private static boolean active = true;
  private static HashMap<Integer, Listener> map = new HashMap<Integer, Listener>();
  public static void setListener(int wd, Listener listener) {
    map.put(wd, listener);
  }
  public static void main(String args[]) {
    if (args.length == 0) {
      System.out.println("Usage:jf-monitor-dir folder");
      return;
    }
    if (!init()) return;
    add(args[0]);
    try { new Object().wait(); } catch (Exception e) {}  //wait forever
  }
  public static class Worker extends Thread {
    Pointer buf;
    public void run() {
      buf = c.malloc(1024);
//      JFLog.log("worker start");
      while (active) {
        NativeLong ret = c.read(fd, buf, new NativeLong(1024));
        long read = ret.longValue();
//        JFLog.log("read=" + read);
        if (read <= 0) break;
        while (read >= 16) {
          int _wd = buf.getInt(0);
          int _mask = buf.getInt(4);
//          int _cookie = buf.getInt(8);
          int _len = buf.getInt(12);
          String _name = _len > 0 ? buf.getString(16) : null;
          read -= 16 + _len;
          String _event = null;
          switch (_mask & IN_MASK) {
            case IN_CREATE: _event = "CREATED"; break;
            case IN_DELETE: _event = "DELETED"; break;
          }
          if (_event == null) continue;
          Listener listener = map.get(_wd);
          if (listener != null) {
            listener.folderChangeEvent(_event, _name);
          } else {
            JFLog.log(_event + ":" + _name);
          }
        }
      }
//      JFLog.log("worker end");
      c.free(buf);
    }
  }
  private static final int IN_CREATE = 0x100;
  private static final int IN_DELETE = 0x200;
  private static final int IN_MASK = 0x300;  //mask off other event bits (ie: IN_ISDIR)
/*
  public class inotify_event extends Structure {
     public int wd;       // Watch descriptor
     public int mask;     // Mask of events
     public int cookie;   // Unique cookie associating related
                        //   events (for rename(2))
     public int len;      // Size of name field
     public char name[];   // Optional null-terminated name
   };
*/
}
