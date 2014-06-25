package javaforce.jna.objc;

/** NSOpenGLView
 *
 * @author pquiring
 *
 * Created : May 29, 2014
 */

public class NSOpenGLView extends NSView {
  public String getClassName() {
    return "NSOpenGLView";
  }

  public void initWithFrame(NSRect rect, NSOpenGLPixelFormat format) {
    obj = ObjectiveC.invokePointer(obj, "initWithFrame:pixelFormat:"
      , rect.origin.x, rect.origin.y, rect.size.width, rect.size.height
      , format.obj);
  }

  public NSOpenGLContext openGLContext() {
    NSOpenGLContext ctx = new NSOpenGLContext();
    ctx.obj = ObjectiveC.invokePointer(obj, "openGLContext");
    return ctx;
  }

  public void setOpenGLContext(NSOpenGLContext ctx) {
    ObjectiveC.invokeVoid(obj, "setOpenGLContext:", ctx.obj);
  }

  public static NSOpenGLPixelFormat defaultPixelFormat() {
    NSOpenGLPixelFormat format = new NSOpenGLPixelFormat();
    format.obj = ObjectiveC.invokePointer("NSOpenGLView", "defaultPixelFormat");
    return format;
  }
}
