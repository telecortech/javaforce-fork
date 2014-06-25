package javaforce.jna.objc;

/** QTCaptureSession
 *
 * @author pquiring
 *
 * Created : Jun 8, 2014
 */

import com.sun.jna.ptr.*;

public class QTCaptureSession extends NSObject {
  public String getClassName() {
    return "QTCaptureSession";
  }

  public boolean addInput(QTCaptureInput input) {
    PointerByReference error = new PointerByReference();  //NSError**
    return ObjectiveC.invokeInt(obj, "addInput:error:", input.obj, error) != 0;
  }

  public boolean addOutput(QTCaptureOutput output) {
    PointerByReference error = new PointerByReference();  //NSError**
    return ObjectiveC.invokeInt(obj, "addOutput:error:", output.obj, error) != 0;
  }

  public void startRunning() {
    ObjectiveC.invokeVoid(obj, "startRunning");
  }

  public void stopRunning() {
    ObjectiveC.invokeVoid(obj, "stopRunning");
  }

  public boolean isRunning() {
    return ObjectiveC.invokeInt(obj, "isRunning") != 0;
  }

  public NSArray inputs() {
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer(obj, "inputs");
    return array;
  }

  public NSArray outputs() {
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer(obj, "outputs");
    return array;
  }
}
