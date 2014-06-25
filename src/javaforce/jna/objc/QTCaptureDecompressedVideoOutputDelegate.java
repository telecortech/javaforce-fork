package javaforce.jna.objc;

/** QTCaptureDecompressedVideoOutputDelegate
 *
 * @author pquiring
 *
 * Created : Jun 8, 2014
 */

import com.sun.jna.*;
import javaforce.JFLog;

public class QTCaptureDecompressedVideoOutputDelegate extends NSObject {
  private static int instance = 0;
  private String className;
  private QTInterface iface;
  private int width, height;
  private int px[] = null;

  public String getClassName() {
    return className;
  }

  //- (void) captureOutput:(QTCaptureOutput *)captureOutput didOutputVideoFrame:(CVImageBufferRef)videoFrame withSampleBuffer:(QTSampleBuffer *)sampleBuffer fromConnection:(QTCaptureConnection *)connection
  public static interface IMP_1 extends Callback {
    public void callback(Pointer id, Pointer sel, Pointer qtCaptureOutput, Pointer cvImageRef, Pointer qtSampleBuffer, Pointer qtCaptureConnection);
  }

  //- (void) captureOutput:(QTCaptureOutput *)captureOutput didDropVideoFrameWithSampleBuffer:(QTSampleBuffer *)sampleBuffer fromConnection:(QTCaptureConnection *)connection
  public static interface IMP_2 extends Callback {
    public void callback(Pointer id, Pointer sel, Pointer qtCaptureOutput, Pointer qtSampleBuffer, Pointer qtCaptureConnection);
  }

  //NOTE : use printf("%s", @encode(type)) to find encoding strings them with gcc
  //@ is good enough for ^{__CVBuffer=}

  public void register(QTInterface _iface) {
    className = "QTCaptureDecompressedVideoOutputDelegate" + instance++;
    this.iface = _iface;
    Pointer superClass = ObjectiveC.getClass("QTCaptureDecompressedVideoOutput");
    Pointer thisClass = ObjectiveC.objc_allocateClassPair(superClass, getClassName(), 0);
    Pointer selector = ObjectiveC.getSelector("captureOutput:didOutputVideoFrame:withSampleBuffer:fromConnection:");
    JFLog.log(getClassName() + ":" + superClass + "," + thisClass + "," + selector);
    if (!ObjectiveC.class_addMethod(thisClass, selector, new IMP_1() {
      public void callback(Pointer id, Pointer sel, Pointer qtCaptureOutput, Pointer cvImageBufferRef, Pointer qtSampleBuffer, Pointer qtCaptureConnection) {
        int fourcc = ObjectiveC.video.CVPixelBufferGetPixelFormatType(cvImageBufferRef);
        JFLog.log("fourcc=0x" + Integer.toString(fourcc, 16));
        if (fourcc != ObjectiveC.kCVPixelFormatType_32ARGB && fourcc != ObjectiveC.kCVPixelFormatType_32BGRA) {
          JFLog.log("unknown fourcc:" + Integer.toString(fourcc, 16));
          return;
        }
        int _width = ObjectiveC.video.CVPixelBufferGetWidth(cvImageBufferRef);
        int _height = ObjectiveC.video.CVPixelBufferGetHeight(cvImageBufferRef);
        int _res1 = ObjectiveC.video.CVPixelBufferLockBaseAddress(cvImageBufferRef, ObjectiveC.kCVPixelBufferLock_ReadOnly);
        Pointer pxptr = ObjectiveC.video.CVPixelBufferGetBaseAddress(cvImageBufferRef);
        int length = _width * _height;
        if (px == null || width != _width || height != _height) {
          px = new int[length];
          width = _width;
          height = _height;
        }
        if (true) {
          pxptr.read(0, px, 0, length);
        } else {
          for(int a=0;a<length;a++) {
            px[a] = pxptr.getInt(a*4);
          }
        }
        if (fourcc == ObjectiveC.kCVPixelFormatType_32ARGB) {
          //the colors are reversed
          int r,g,b;
          for(int a=0;a<length;a++) {
            b = px[a] & 0xff000000;
            g = px[a] & 0xff0000;
            r = px[a] & 0xff00;
            //alpha = px[a] & 0xff;  //always 0xff
            px[a] = 0xff000000 | (b >>> 24) | (g >> 8) | (r << 8);
          }
        }
        int _res2 = ObjectiveC.video.CVPixelBufferUnlockBaseAddress(cvImageBufferRef, ObjectiveC.kCVPixelBufferLock_ReadOnly);
        iface.videoFrame(width, height, px);
      }
    }, "v@:@@@@")) {
      JFLog.log("Failed to addMethod1");
    }
    Pointer selector2 = ObjectiveC.getSelector("captureOutput:didDropVideoFrameWithSampleBuffer:fromConnection:");
    if (!ObjectiveC.class_addMethod(thisClass, selector2, new IMP_2() {
      public void callback(Pointer id, Pointer sel, Pointer qtCaptureOutput, Pointer qtSampleBuffer, Pointer qtCaptureConnection) {
        JFLog.log("QTCaptureDecompressedVideoOutput:lost frame");
      }
    }, "v@:@@@")) {
      JFLog.log("Failed to addMethod2");
    }
    ObjectiveC.objc_registerClassPair(thisClass);
  }
}
