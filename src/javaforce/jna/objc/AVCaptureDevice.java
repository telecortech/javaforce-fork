package javaforce.jna.objc;

/** AVCaptureDevice
 *
 * Requires Mac OS X.VII
 *
 * @author pquiring
 *
 * Created : Jun 7, 2014
 */

public class AVCaptureDevice extends NSObject {
  public String getClassName() {
    return "AVCaptureDevice";
  }

  public static NSArray devices() {
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer("AVCaptureDevice", "devices");
    return array;
  }

  public String localizedName() {
    NSString string = new NSString();
    string.obj = ObjectiveC.invokePointer(obj, "localizedName");
    return string.cStringUsingEncoding(NSString.NSUTF8StringEncoding);
  }
}
