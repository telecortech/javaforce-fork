package javaforce.utils;

/**
 * Fuse .zip
 *
 * Currently only supports reading zip files.
 * Does NOT support password protected zips.
 *
 * Created : Feb 7, 2014
 */

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.sun.jna.*;

import javaforce.*;
import javaforce.jna.*;

public class jfusezip extends Fuse {
  private ZipFile zip;

  public static void main(String args[]) {
    if (args.length == 0) {
      System.out.println("Usage : jfuse-zip zip_file mount");
      return;
    }
    new jfusezip().main2(args);
  }

  public boolean auth(String args[], String passwd) {
    try {
      //TODO : if ever support passwords check it here
      zip = new ZipFile(args[0]);
      return true;
    } catch (Exception e) {
      JFLog.log("Error:" + e);
      return false;
    }
  }

  public void main2(String args[]) {
    if (!init()) return;
    try {
      //auth first
      System.out.print("Password:");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String pass = br.readLine();
      if (!auth(args, pass)) throw new Exception("bad password");
      System.out.println("Ok");
      System.out.flush();
      start(args);
    } catch (Exception e) {
      JFLog.log("Error:" + e);
    }
  }

  public int getattr(String path, Stat stat) {
    JFLog.log("getattr:" + path);
    if (path.equals("/")) {
      stat.folder = true;
      return 0;
    }
    if (path.startsWith("/")) path = path.substring(1);
    try {
      ZipEntry entry = zip.getEntry(path + "/");
      if (entry == null) {
        entry = zip.getEntry(path);
        if (entry == null) {
          return -ENOENT;  //not found
        }
      }
      if (entry.isDirectory()) {
        stat.folder = true;
      } else {
        stat.size = entry.getSize();
      }
      return 0;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int mkdir(String path, int mode) {
    JFLog.log("mkdir:" + path);
    if (!path.endsWith("/")) path += "/";
    try {
      //TODO
      return -1;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int unlink(String path) {
    JFLog.log("unlink:" + path);
    try {
      //TODO
      return -1;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int rmdir(String path) {
    JFLog.log("rmdir:" + path);
    if (!path.endsWith("/")) path += "/";
    try {
      //TODO
      return -1;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int symlink(String target, String link) {
    JFLog.log("symlink:" + link + "->" + target);
    return -1;
  }

  public int link(String target, String link) {
    JFLog.log("link:" + link + "->" + target);
    return -1;
  }

  public int chmod(String path, int mode) {
    JFLog.log("chmod:" + path);
    return -1;
  }

  public int chown(String path, int mode) {
    JFLog.log("chown:" + path);
    return -1;
  }

  public int truncate(String path, long size) {
    JFLog.log("truncate:" + path);
    try {
      //TODO
      return -1;
    } catch (Exception e) {
      JFLog.log(e);
    }
    return -1;
  }

  private class FileState {
    InputStream is;
    OutputStream os;
    long offset;
    boolean canWrite;
    boolean canRead;
  }

  public int open(String path, Pointer ffi) {
    JFLog.log("open:" + path);
    if (path.startsWith("/")) path = path.substring(1);
    try {
      ZipEntry ze = zip.getEntry(path);
      if (ze == null) return -1;
      if (ze.isDirectory()) return -1;
      FileState fs = new FileState();
      String mode = "";
      fs.canRead = true;
      if (fs.canRead) mode += "r";
      fs.canWrite = false;
      if (fs.canWrite) mode += "w";
      if (mode.length() == 0) {JFLog.log("open:access denied"); return -1;}
      fs.is = zip.getInputStream(ze);
      attachObject(ffi, fs);
      return 0;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int read(String path, Pointer buf, int size, long offset, Pointer ffi) {
    JFLog.log("read:" + path);
    FileState fs = (FileState)getObject(ffi);
    if (fs == null) {JFLog.log("no fs");return -1;}
    if (!fs.canRead) {JFLog.log("!read");return -1;}
    byte data[] = new byte[size];
    try {
      if (offset != fs.offset) return -1;
      int read = 0;
      int pos = 0;
      while (read != size) {
        int left = size - read;
        int amt = fs.is.read(data, 0, left);
        if (amt <= 0) break;
        buf.write(pos, data, 0, amt);
        read += amt;
        pos += amt;
      }
      JFLog.log("read=" + read);
      fs.offset += size;
      return read;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int write(String path, Pointer buf, int size, long offset, Pointer ffi) {
    JFLog.log("write:" + path);
    FileState fs = (FileState)getObject(ffi);
    if (fs == null) return -1;
    if (!fs.canWrite) return -1;
    byte data[] = new byte[size];
    buf.read(0, data, 0, size);
    try {
      if (offset != fs.offset) return -1;
      fs.os.write(data);
      fs.offset += size;
      return size;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int statfs(String path, Pointer statvfs) {
    JFLog.log("statfs:" + path);
    return -1;
  }

  public int release(String path, Pointer ffi) {
    JFLog.log("release:" + path);
    detachObject(ffi);
    return 0;
  }

  public int readdir(String path, Pointer buf, Pointer filler, Pointer ffi) {
    JFLog.log("readdir:" + path);
    if (!path.endsWith("/")) path += "/";
    try {
      Enumeration e = zip.entries();
      while (e.hasMoreElements()) {
        ZipEntry ze = (ZipEntry)e.nextElement();
        String name = "/" + ze.getName();
        if (name.endsWith("/")) name = name.substring(0, name.length() - 1);
        int idx = name.lastIndexOf("/");
        String filepath = name.substring(0, idx+1);
//        JFLog.log("file=" + name + ",path=" + filepath);
        if (!filepath.equals(path)) continue;
        JFLog.log("invokeFiller:" + name);
        if (invokeFiller(filler, buf, name.substring(idx+1), null) == 1) break; //full???
      }
      JFLog.log("readdir done");
      return 0;
    } catch (Exception e) {
      JFLog.log(e);
      return -1;
    }
  }

  public int create(String path, int mode, Pointer ffi) {
    JFLog.log("create:" + path);
    try {
      //TODO
      return -1;
    } catch (Exception e) {
      JFLog.log(e);
    }
    return -1;
  }
}
