package javaforce.jna.objc;

import com.sun.jna.Pointer;

/**
 *
 * @author pquiring
 *
 * Created : May 29, 2014
 */

public class NSWindowController extends NSObject /* NSResponder */ {
  public String getClassName() {
    return "NSWindowController";
  }

  public void initWithWindow(NSWindow window) {
    ObjectiveC.invokeVoid(obj, "initWithWindow:", window.obj);
  }

  public void showWindow() {
    ObjectiveC.invokeVoid(obj, "showWindow:", Pointer.NULL);
  }

  public void close() {
    ObjectiveC.invokeVoid(obj, "close");
  }
}
