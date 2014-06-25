package javaforce.jna.objc;

/** QTCaptureDeviceInput
 *
 * Requires Mac OS X.V
 * Deprecated in Mac OS X.IX
 *
 * @author pquiring
 *
 * Created : Jun 8, 2014
 */

public class QTCaptureDeviceInput extends QTCaptureInput {
  public String getClassName() {
    return "QTCaptureDeviceInput";
  }

  public void initWithDevice(QTCaptureDevice device) {
    obj = ObjectiveC.invokePointer(obj, "initWithDevice:", device.obj);
  }

  public QTCaptureDevice device() {
    QTCaptureDevice device = new QTCaptureDevice();
    device.obj = ObjectiveC.invokePointer(obj, "device");
    return device;
  }
}
