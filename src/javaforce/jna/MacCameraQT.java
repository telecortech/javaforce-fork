package javaforce.jna;

/** MacCamera (using QTKit)
 *
 * @author pquiring
 *
 * Created : Jun 7, 2014
 */

import javaforce.jna.objc.*;
import javaforce.media.*;
import javaforce.*;

public class MacCameraQT implements Camera.Input, QTInterface {

  private int width, height;
  private QTCaptureSession session;
  private QTCaptureDevice device;
  private QTCaptureDeviceInput input;
  private QTCaptureDecompressedVideoOutput output;
  private QTCaptureDecompressedVideoOutputDelegate outputDelegate;
  private VideoBuffer buffer;
  private Object lock = new Object();
  private MyNSObject nsobj;

  public static int frames = 10;

  public boolean init() {
    if (!ObjectiveC.init()) return false;
    return true;
  }

  public boolean uninit() {
    return true;
  }

  public String[] listDevices() {
    NSAutoreleasePool pool = new NSAutoreleasePool();
    pool.alloc();
    pool.init();
    QTCaptureDevice device = new QTCaptureDevice();
    NSArray array = QTCaptureDevice.inputDevicesWithMediaType(QTCaptureDevice.QTMediaTypeVideo);
    int cnt = array.count();
    String names[] = new String[cnt];
    for(int a=0;a<cnt;a++) {
      device.obj = array.objectAtIndex(a);
      names[a] = device.localizedDisplayName();
    }
    pool.release();
    return names;
  }

  public boolean start(int deviceIdx, int desiredWidth, int desiredHeight) {
    if (deviceIdx < 0) return false;
    if (desiredWidth == -1 || desiredHeight == -1) {
      desiredWidth = 640;
      desiredHeight = 640;
    }
    NSAutoreleasePool pool = new NSAutoreleasePool();
    pool.alloc();
    pool.init();
    device = new QTCaptureDevice();
    NSArray devices = QTCaptureDevice.inputDevicesWithMediaType(QTCaptureDevice.QTMediaTypeVideo);
    int cnt = devices.count();
    if (deviceIdx >= cnt) {
      JFLog.log("Invalid device index");
      pool.release();
      return false;
    }
    device.obj = devices.objectAtIndex(deviceIdx);
    if (!device.open()) {
      JFLog.log("[QTCaptureDevice open] failed");
      pool.release();
      return false;
    }
    //get device width/height
    NSArray formats = device.formatDescriptions();
    cnt = formats.count();
    JFLog.log("formats=" + cnt);
    if (cnt == 0) {
      JFLog.log("No media formats available");
      pool.release();
      return false;
    }
/*
    QTFormatDescription format = new QTFormatDescription();
    format.obj = formats.objectAtIndex(0);
    NSValue value = new NSValue();
    value.obj = format.attributeForKey(QTFormatDescription.QTFormatDescriptionVideoEncodedPixelsSizeAttribute);
    NSSize size = new NSSize();
    value.getValue(size.getPointer());
    size.read();  //autoread not used
    width = (int)size.width.doubleValue();
    height = (int)size.height.doubleValue();
    JFLog.log("size=" + width + "," + height);
    if (width <= 0 || height <= 0) {
      JFLog.log("Invalid size");
      pool.release();
      return false;
    }
*/
    //we will force the pixel size
    width = desiredWidth;
    height = desiredHeight;

    session = new QTCaptureSession();
    session.alloc();
    session.init();

    input = new QTCaptureDeviceInput();
    input.alloc();
    input.initWithDevice(device);
    if (!session.addInput(input)) {
      JFLog.log("Failed to add input");
      pool.release();
      return false;
    }

    output = new QTCaptureDecompressedVideoOutput();
    output.alloc();
    output.init();
    if (!session.addOutput(output)) {
      JFLog.log("Failed to add output");
      pool.release();
      return false;
    }

    outputDelegate = new QTCaptureDecompressedVideoOutputDelegate();
    outputDelegate.register(this);
    outputDelegate.alloc();
    outputDelegate.init();

    output.setDelegate(outputDelegate);

    //how to force the pixel format
    //https://developer.apple.com/library/mac/qa/qa1582/_index.html
    //list : value, key, value, key, value, key, null
    Object list[] = new Object[] {
      NSNumber.numberWithDouble(desiredWidth).obj,
      NSString.stringWithUTF8String(ObjectiveC.kCVPixelBufferWidthKey).obj,
      NSNumber.numberWithDouble(desiredHeight).obj,
      NSString.stringWithUTF8String(ObjectiveC.kCVPixelBufferHeightKey).obj,
      NSNumber.numberWithUnsignedInt(ObjectiveC.kCVPixelFormatType_32ARGB).obj,  //crashes if try to use kCVPixelFormatType_32BGRA
      NSString.stringWithUTF8String(ObjectiveC.kCVPixelBufferPixelFormatTypeKey).obj,
      null
    };
    NSDictionary dict = NSDictionary.dictionaryWithObjectsAndKeys(list);
    output.setPixelBufferAttributes(dict);
    dict.release();

    buffer = new VideoBuffer(width, height, frames);

    session.startRunning();

    pool.release();
    return true;
  }

  public boolean stop() {
    NSAutoreleasePool pool = new NSAutoreleasePool();
    pool.alloc();
    pool.init();
    if (session != null) {
      session.stopRunning();
      session.release();
      session = null;
    }
    if (input != null) {
      input.release();
      input = null;
    }
    if (output != null) {
      output.release();
      output = null;
    }
    if (outputDelegate != null) {
      outputDelegate.release();
      outputDelegate = null;
    }
    if (device != null) {
      device.close();
      device.release();
      device = null;
    }
    if (buffer != null) {
      buffer = null;
    }
    pool.release();
    return true;
  }

  public int[] getFrame() {
    buffer.freeNextFrame();
    JFImage img = buffer.getNextFrame();
    if (img == null) return null;
    return img.getBuffer();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void videoFrame(int width, int height, int[] px) {
    JFLog.log("videoFrame");
    if (width != this.width || height != this.height) {
      JFLog.log("Error:Received frame is different size than specified, dropping.");
      return;
    }
    JFImage img = buffer.getNewFrame();
    if (img == null) {
      JFLog.log("Unable to get video buffer frame, dropping frame.");
      return;
    }
    System.arraycopy(px, 0, img.getBuffer(), 0, width * height);
    buffer.freeNewFrame();
  }

  public boolean isRunning() {
    return session.isRunning();
  }

  public int inputs() {
    NSArray array = session.inputs();
    return array.count();
  }

  public int outputs() {
    NSArray array = session.outputs();
    return array.count();
  }
}
