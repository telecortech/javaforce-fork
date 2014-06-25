package javaforce.jna.objc;

/**
 * Objective-C interface.
 *
 * Inspired by Rococoa.
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */

import java.util.*;

import com.sun.jna.*;

import javaforce.*;

public class ObjectiveC {
  public static Pointer malloc(int size) {
    return new Pointer(Native.malloc(size));
  }

  public static void free(Pointer ptr) {
    Native.free(Pointer.nativeValue(ptr));
  }

  public static interface Foundation extends Library {
    Pointer CFStringCreateWithCString(Pointer allocator, String string, int encoding);
    Pointer CFStringCreateWithBytes(Pointer allocator, byte[] bytes, int byteCount, int encoding, byte isExternalRepresentation);
    String CFStringGetCStringPtr(Pointer string, int encoding);
    byte CFStringGetCString(Pointer theString, byte[] buffer, int bufferSize, int encoding);
    int CFStringGetLength(Pointer theString);
    Pointer CFRetain(Pointer cfTypeRef);
    void CFRelease(Pointer cfTypeRef);
    int CFGetRetainCount(Pointer cfTypeRef);
    Pointer objc_getClass(String className);
    Pointer class_createInstance(Pointer pClass, int extraBytes);
    Pointer sel_registerName(String selectorName);
    Pointer objc_msgSend(Pointer receiver, Pointer selector, Object... args);
    Pointer objc_msgSend_stret(Pointer struct, Pointer receiver, Pointer selector, Object... args);
    Pointer objc_allocateClassPair(Pointer superclass, String name, int extraBytes);
    boolean class_addMethod(Pointer cls, Pointer selector, Callback callback, String types);
    void objc_registerClassPair(Pointer cls);
  }

  public static interface CoreVideo extends Library {
    int CVPixelBufferLockBaseAddress(Pointer pixelBuffer, int flags);  //return = CVReturn ???
    Pointer CVPixelBufferGetBaseAddress(Pointer pixelBuffer);
    int CVPixelBufferUnlockBaseAddress(Pointer pixelBuffer, int flags);  //return = CVReturn ???
    int CVPixelBufferGetWidth(Pointer pixelBuffer);
    int CVPixelBufferGetHeight(Pointer pixelBuffer);
    int CVPixelBufferGetPixelFormatType(Pointer pixelBuffer);  //returns fourCC code
  }

  public static interface QuartzCore extends Library {
    boolean CVImageBufferIsFlipped(Pointer image);
  }

  public static interface QTKit extends Library {
    //no C functions are needed, but the library must be loaded to use ObjC classes
  }

  private static int fourCC2int(String str) {
    char ca[] = str.toCharArray();
    int res = 0;
    for(int a=0;a<4;a++) {
      res <<= 8;
      res += ca[a];
    }
    return res;
  }

  //some core video constants
  public static int kCVPixelBufferLock_ReadOnly = 1; //for CoreVideo.CVPixelBufferLock/UnlockBaseAddress
  public static int kCVPixelFormatType_32BGRA = fourCC2int("BGRA");  //does not work
  public static int kCVPixelFormatType_32ARGB = 0x20;  //works
  public static String kCVPixelBufferWidthKey = "Width";
  public static String kCVPixelBufferHeightKey = "Height";
  public static String kCVPixelBufferPixelFormatTypeKey = "PixelFormatType";

  static Foundation foundation;
  static QuartzCore quartz;
  static CoreVideo video;
  static QTKit qtkit;

  private static Function objc_msgSend;  //for invokeInt, invokeLong

  /**
   * Initialize (call once per process).
   */
  public static boolean init() {
    if (foundation != null) return true;
    try {
      foundation = (Foundation) Native.loadLibrary("Foundation", Foundation.class);
      quartz = (QuartzCore) Native.loadLibrary("QuartzCore", QuartzCore.class);
      video = (CoreVideo) Native.loadLibrary("CoreVideo", CoreVideo.class);
      qtkit = (QTKit) Native.loadLibrary("QTKit", QTKit.class);
      objc_msgSend = Function.getFunction("Foundation", "objc_msgSend");
    } catch (Throwable t) {
      JFLog.log(t);
      return false;
    }
    return true;
  }

  private static HashMap<String, Pointer> classMap = new HashMap<String, Pointer>();

  public static Pointer getClass(String cls) {
    Pointer id = classMap.get(cls);
    if (id != null) {
      return id;
    }
    id = foundation.objc_getClass(cls);
//    JFLog.log("cls:" + cls + "=" + id);
    classMap.put(cls, id);
    return id;
  }

  private static HashMap<String, Pointer> selMap = new HashMap<String, Pointer>();

  public static Pointer getSelector(String method) {
    Pointer sel = selMap.get(method);
    if (sel != null) {
      return sel;
    }
    sel = foundation.sel_registerName(method);
//    JFLog.log("sel:" + method + "=" + sel);
    selMap.put(method, sel);
    return sel;
  }

  public static Pointer objc_allocateClassPair(Pointer superclass, String name, int extraBytes) {
    return foundation.objc_allocateClassPair(superclass, name, extraBytes);
  }

  public static boolean class_addMethod(Pointer cls, Pointer selector, Callback callback, String types) {
    return foundation.class_addMethod(cls, selector, callback, types);
  }

  public static void objc_registerClassPair(Pointer cls) {
    foundation.objc_registerClassPair(cls);
  }

  //invoke static methods

  public static void invokeVoid(String clsName, String funcName, Object... args) {
    Pointer cls = getClass(clsName);
    Pointer sel = getSelector(funcName);
    foundation.objc_msgSend(cls, sel, args);
  }

  public static Pointer invokePointer(String clsName, String funcName, Object... args) {
    Pointer cls = getClass(clsName);
    Pointer sel = getSelector(funcName);
    return foundation.objc_msgSend(cls, sel, args);
  }

  public static int invokeInt(String clsName, String funcName, Object... args) {
    Pointer cls = getClass(clsName);
    Pointer sel = getSelector(funcName);
    Object fullargs[] = new Object[args.length + 2];
    fullargs[0] = cls;
    fullargs[1] = sel;
    System.arraycopy(args, 0, fullargs, 2, args.length);
    return objc_msgSend.invokeInt(fullargs);
  }

  public static long invokeLong(String clsName, String funcName, Object... args) {
    Pointer cls = getClass(clsName);
    Pointer sel = getSelector(funcName);
    Object fullargs[] = new Object[args.length + 2];
    fullargs[0] = cls;
    fullargs[1] = sel;
    System.arraycopy(args, 0, fullargs, 2, args.length);
    return objc_msgSend.invokeLong(fullargs);
  }

  public static void invokeStruct(Pointer struct, String clsName, String funcName, Object... args) {
    Pointer cls = getClass(clsName);
    Pointer sel = getSelector(funcName);
    foundation.objc_msgSend_stret(struct, cls, sel, args);
  }

  //invoke members

  public static void invokeVoid(Pointer cls, String funcName, Object... args) {
    Pointer sel = getSelector(funcName);
    foundation.objc_msgSend(cls, sel, args);
  }

  public static Pointer invokePointer(Pointer cls, String funcName, Object... args) {
    Pointer sel = getSelector(funcName);
    return foundation.objc_msgSend(cls, sel, args);
  }

  public static int invokeInt(Pointer cls, String funcName, Object... args) {
    Pointer sel = getSelector(funcName);
    Object fullargs[] = new Object[args.length + 2];
    fullargs[0] = cls;
    fullargs[1] = sel;
    System.arraycopy(args, 0, fullargs, 2, args.length);
    return objc_msgSend.invokeInt(fullargs);
  }

  public static long invokeLong(Pointer cls, String funcName, Object... args) {
    Pointer sel = getSelector(funcName);
    Object fullargs[] = new Object[args.length + 2];
    fullargs[0] = cls;
    fullargs[1] = sel;
    System.arraycopy(args, 0, fullargs, 2, args.length);
    return objc_msgSend.invokeLong(fullargs);
  }

  public static void invokeStruct(Pointer struct, Pointer cls, String funcName, Object... args) {
    Pointer sel = getSelector(funcName);
    foundation.objc_msgSend_stret(struct, cls, sel, args);
  }
}
