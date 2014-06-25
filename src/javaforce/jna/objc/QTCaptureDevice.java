package javaforce.jna.objc;

/** QTCaptureDevice
 *
 * Requires Mac OS X.V
 * Deprecated in Mac OS X.IX
 *
 * NOTE : Must load QTKit library to use (see ObjectiveC)
 *
 * @author pquiring
 *
 * Created : Jun 8, 2014
 */

import com.sun.jna.ptr.*;

import javaforce.*;

public class QTCaptureDevice extends NSObject {
  public String getClassName() {
    return "QTCaptureDevice";
  }

  public static NSArray inputDevices() {
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer("QTCaptureDevice", "inputDevices");
    return array;
  }

  public static String QTMediaTypeVideo = "vide";
  public static String QTMediaTypeSound = "soun";

  public static NSArray inputDevicesWithMediaType(String type) {
    NSString str = new NSString();
    str.alloc();
    str.initWithUTF8String(type);
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer("QTCaptureDevice", "inputDevicesWithMediaType:", str.obj);
    str.dealloc();
    return array;
  }

  public static QTCaptureDevice defaultInputDeviceWithMediaType(String type) {
    NSString str = new NSString();
    str.alloc();
    str.initWithUTF8String(type);
    QTCaptureDevice device = new QTCaptureDevice();
    device.obj = ObjectiveC.invokePointer("QTCaptureDevice", "defaultInputDeviceWithMediaType:", str.obj);
    str.dealloc();
    if (device.obj == null) return null;
    return device;
  }

  public String localizedDisplayName() {
    NSString string = new NSString();
    string.obj = ObjectiveC.invokePointer(obj, "localizedDisplayName");
    return string.cStringUsingEncoding(NSString.NSUTF8StringEncoding);
  }

  public boolean open() {
    PointerByReference error = new PointerByReference();  //NSError **
    boolean ok = ObjectiveC.invokeInt(obj, "open:", error) != 0;
    return ok;
  }

  public void close() {
    ObjectiveC.invokeVoid(obj, "close");
  }

  /** Returns array of QTFormatDescription (each one describes one format : width/height/fourcc/etc.) */
  public NSArray formatDescriptions() {
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer(obj, "formatDescriptions");
    return array;
  }
}
