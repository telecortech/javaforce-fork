package javaforce.jna.objc;

/** NSOpenGLContext
 *
 * @author pquiring
 *
 * Created : May 29, 2014
 */

import com.sun.jna.*;

import javaforce.*;

public class NSOpenGLContext extends NSObject {
  public String getClassName() {
    return "NSOpenGLContext";
  }

  public void initWithFormat(NSOpenGLPixelFormat format) {
    ObjectiveC.invokePointer(obj, "initWithFormat:shareContext:", format.obj, /*shareContext*/false);
  }

  public void initWithCGLContextObj(Pointer context) {
    ObjectiveC.invokePointer(obj, "initWithCGLContextObj:", context);
  }

  public void setView(NSView view) {
    ObjectiveC.invokeVoid(obj, "setView:", view.obj);
  }

  public NSView view() {
    NSView view = new NSView();
    view.obj = ObjectiveC.invokePointer(obj, "view");
    return view;
  }

  public void makeCurrentContext() {
    ObjectiveC.invokeVoid(obj, "makeCurrentContext");
  }

  public static void clearCurrentContext() {
    ObjectiveC.invokeVoid("NSOpenGLContext", "clearCurrentContext");
  }

  public void flushBuffer() {
    ObjectiveC.invokeVoid(obj, "flushBuffer");
  }

}
