package javaforce.jna.objc;

/**
 *
 * @author pquiring
 *
 * Created : May 29, 2014
 */

import com.sun.jna.*;

public class NSOpenGLPixelFormat extends NSObject {
  public String getClassName() {
    return "NSOpenGLPixelFormat";
  }

  public Pointer CGLPixelFormatObj() {
    return ObjectiveC.invokePointer(obj, "CGLPixelFormatObj");
  }

  public static int NSOpenGLPFAAllRenderers       =   1;
  public static int NSOpenGLPFATripleBuffer       =   3;
  public static int NSOpenGLPFADoubleBuffer       =   5;
  public static int NSOpenGLPFAStereo             =   6;
  public static int NSOpenGLPFAAuxBuffers         =   7;
  public static int NSOpenGLPFAColorSize          =   8;
  public static int NSOpenGLPFAAlphaSize          =  11;
  public static int NSOpenGLPFADepthSize          =  12;
  public static int NSOpenGLPFAStencilSize        =  13;
  public static int NSOpenGLPFAAccumSize          =  14;
  public static int NSOpenGLPFAMinimumPolicy      =  51;
  public static int NSOpenGLPFAMaximumPolicy      =  52;
  public static int NSOpenGLPFAOffScreen          =  53;
  public static int NSOpenGLPFAFullScreen         =  54;
  public static int NSOpenGLPFASampleBuffers      =  55;
  public static int NSOpenGLPFASamples            =  56;
  public static int NSOpenGLPFAAuxDepthStencil    =  57;
  public static int NSOpenGLPFAColorFloat         =  58;
  public static int NSOpenGLPFAMultisample        =  59;
  public static int NSOpenGLPFASupersample        =  60;
  public static int NSOpenGLPFASampleAlpha        =  61;
  public static int NSOpenGLPFARendererID         =  70;
  public static int NSOpenGLPFASingleRenderer     =  71;
  public static int NSOpenGLPFANoRecovery         =  72;
  public static int NSOpenGLPFAAccelerated        =  73;
  public static int NSOpenGLPFAClosestPolicy      =  74;
  public static int NSOpenGLPFARobust             =  75;
  public static int NSOpenGLPFABackingStore       =  76;
  public static int NSOpenGLPFAMPSafe             =  78;
  public static int NSOpenGLPFAWindow             =  80;
  public static int NSOpenGLPFAMultiScreen        =  81;
  public static int NSOpenGLPFACompliant          =  83;
  public static int NSOpenGLPFAScreenMask         =  84;
  public static int NSOpenGLPFAPixelBuffer        =  90;
  public static int NSOpenGLPFARemotePixelBuffer  =  91;
  public static int NSOpenGLPFAAllowOfflineRenderers = 96;
  public static int NSOpenGLPFAAcceleratedCompute =  97;
  public static int NSOpenGLPFAOpenGLProfile      =  99;
  public static int NSOpenGLPFAVirtualScreenCount = 128;

  public void initWithAttributes(int args[]) {
    obj = ObjectiveC.invokePointer(obj, "initWithAttributes:", args);
  }
}
