package javaforce.jna.objc;

/** NSOpenGLLayer
 *
 * @author pquiring
 *
 * Created : Jun 3, 2014
 */

public class NSOpenGLLayer extends NSObject /* CAOpenGLLayer */ {
  public String getClassName() {
    return "NSOpenGLLayer";
  }
  public void setView(NSView view) {
    ObjectiveC.invokeVoid(obj, "setView:", view.obj);
  }
  public void setAsynchronous(boolean state) {
    ObjectiveC.invokeVoid(obj, "setAsynchronous:", state);
  }
  public void setNeedsDisplayOnBoundsChange(boolean state) {
    ObjectiveC.invokeVoid(obj, "setNeedsDisplayOnBoundsChange:", state);
  }
  public void setOpaque(boolean state) {
    ObjectiveC.invokeVoid(obj, "setOpaque:", state);
  }
  public void setOpenGLContext(NSOpenGLContext ctx) {
    ObjectiveC.invokeVoid(obj, "setOpenGLContext:", ctx.obj);
  }
  public void setOpenGLPixelFormat(NSOpenGLPixelFormat fmt) {
    ObjectiveC.invokeVoid(obj, "setOpenGLPixelFormat:", fmt.obj);
  }
  public NSOpenGLContext openGLContextForPixelFormat(NSOpenGLPixelFormat fmt) {
    NSOpenGLContext ctx = new NSOpenGLContext();
    ctx.obj = ObjectiveC.invokePointer(obj, "openGLContextForPixelFormat:", fmt.obj);
    return ctx;
  }
}
