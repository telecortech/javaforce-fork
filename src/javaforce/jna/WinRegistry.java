package javaforce.jna;

/** Windows Registry API
 *
 * @author pquiring
 *
 * Created : May 22, 2014
 */

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import com.sun.jna.win32.*;

import javaforce.*;

public class WinRegistry {

  public static Pointer malloc(int size) {
    return new Pointer(Native.malloc(size));
  }

  public static void free(Pointer ptr) {
    Native.free(Pointer.nativeValue(ptr));
  }

  public static interface Advapi extends StdCallLibrary {
    public int RegOpenKeyEx(Pointer hkey, String key, int res, int sam, PointerByReference hout);
    public int RegCloseKey(Pointer hkey);
    public int RegDeleteKey(Pointer hkey, String key);
    public int RegCreateKeyEx(Pointer hkey, String key, int res, String cls, int opts, int sam, Pointer sec, PointerByReference hout, Pointer disp);
    public int RegGetValue(Pointer hkey, String key, String value, int rrf_flgs, Pointer type, Pointer data, IntByReference dataSize);
    public int RegSetValueEx(Pointer hkey, String value, int res, int reg_type, Pointer data, int dataSize);
  }

  public static Advapi advapi;
  public static Pointer InvalidHandle = new Pointer(-1);

  public static Pointer HKEY_CLASSES_ROOT = new Pointer(0x80000000);
  public static Pointer HKEY_CURRENT_USER = new Pointer(0x80000001);
  public static Pointer HKEY_LOCAL_MACHINE = new Pointer(0x80000002);
  public static Pointer HKEY_USERS = new Pointer(0x80000003);
  public static Pointer HKEY_PERFORMANCE_DATA = new Pointer(0x80000004);
  public static Pointer HKEY_PERFORMANCE_TEXT = new Pointer(0x80000050);
  public static Pointer HKEY_PERFORMANCE_NLSTEXT = new Pointer(0x80000060);
  public static Pointer HKEY_CURRENT_CONFIG = new Pointer(0x80000005);
  public static Pointer HKEY_DYN_DATA = new Pointer(0x80000006);

  public static int KEY_ALL_ACCESS = 0xF003F;

  public static int RRF_RT_REG_NONE = 0x00000001;
  public static int RRF_RT_REG_SZ = 0x00000002;
  public static int RRF_RT_REG_EXPAND_SZ = 0x00000004;
  public static int RRF_RT_REG_BINARY = 0x00000008;
  public static int RRF_RT_REG_DWORD = 0x00000010;
  public static int RRF_RT_REG_MULTI_SZ = 0x00000020;
  public static int RRF_RT_REG_QWORD = 0x00000040;

  public static int RRF_RT_DWORD = (RRF_RT_REG_BINARY | RRF_RT_REG_DWORD);
  public static int RRF_RT_QWORD = (RRF_RT_REG_BINARY | RRF_RT_REG_QWORD);
  public static int RRF_RT_ANY = 0x0000ffff;

  public static int RRF_NOEXPAND = 0x10000000;
  public static int RRF_ZEROONFAILURE = 0x20000000;

  public static int REG_NONE = 0;
  public static int REG_SZ = 1;
  public static int REG_EXPAND_SZ = 2;
  public static int REG_BINARY = 3;
  public static int REG_DWORD = 4;
  public static int REG_DWORD_LITTLE_ENDIAN = 4;
  public static int REG_DWORD_BIG_ENDIAN = 5;
  public static int REG_LINK = 6;
  public static int REG_MULTI_SZ = 7;
  public static int REG_RESOURCE_LIST = 8;
  public static int REG_FULL_RESOURCE_DESCRIPTOR = 9;
  public static int REG_RESOURCE_REQUIREMENTS_LIST = 10;
  public static int REG_QWORD = 11;
  public static int REG_QWORD_LITTLE_ENDIAN = 11;

  /** Load the Windows Registry API dll.  Must call once per process. */
  public static boolean init() {
    if (advapi != null) return true;
    try {
      advapi = (Advapi)Native.loadLibrary("advapi32", Advapi.class);
      return true;
    } catch (Throwable t) {
      JFLog.log(t);
      return false;
    }
  }

  /** Open a registry key relative to hkey. */
  public Pointer open(Pointer hkey, String name) {
    PointerByReference ref = new PointerByReference();
    if (advapi.RegOpenKeyEx(hkey, name, 0, KEY_ALL_ACCESS, ref) != 0) return null;
    return ref.getValue();
  }

  /** Close a regsitry key. */
  public void close(Pointer hkey) {
    advapi.RegCloseKey(hkey);
  }

  /** Creates a new subkey. Not used to create values - see write(). */
  public Pointer create(Pointer hkey, String name) {
    PointerByReference ref = new PointerByReference();
    if (advapi.RegCreateKeyEx(hkey, name, 0, null, 0, KEY_ALL_ACCESS, null, ref, null) != 0) return null;
    return ref.getValue();
  }

  /** Returns size of a value. */
  public int getSize(Pointer hkey, String value) {
    IntByReference iref = new IntByReference();
    iref.setValue(0);
    if (advapi.RegGetValue(hkey, null, value, RRF_RT_ANY, null, null, iref) != 0) return -1;
    return iref.getValue();
  }

  /** Reads a value.  Must use free() to release returned Pointer. */
  public Pointer read(Pointer hkey, String value, int maxSize) {
    Pointer data = malloc(maxSize);
    IntByReference iref = new IntByReference();
    iref.setValue(maxSize);
    if (advapi.RegGetValue(hkey, null, value, RRF_RT_ANY, null, data, iref) != 0) return null;
    return data;
  }

  /** Writes a value. Creates it if it does not exist. */
  public boolean write(Pointer hkey, String value, int reg_type, Pointer data, int dataSize) {
    return advapi.RegSetValueEx(hkey, value, 0, reg_type, data, dataSize) == 0;
  }
}
