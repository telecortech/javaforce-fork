package javaforce.jna.objc;

/** MyNSOpenGLView
 *
 * Same as NSOpenGLView except drawRect() is overridden.
 *
 * This class is not used and untested.
 *
 * @author pquiring
 *
 * Created : Jun 4, 2014
 */

import com.sun.jna.*;

import javaforce.*;

public class MyNSOpenGLView extends NSOpenGLView {
  private int instance = 0;
  private String className;
  private Runnable run;
  public String getClassName() {
    return className;
  }
  //void drawRect(NSRect rect);
  public static interface IMP_32 extends Callback {
    public void drawRect(Pointer id, Pointer sel, float x, float y, float w, float h);
  }
  public static interface IMP_64 extends Callback {
    public void drawRect(Pointer id, Pointer sel, double x, double y, double w, double h);
  }

  public synchronized void register(Runnable _run) {
    this.run = _run;
    className = "MyNSOpenGLView" + instance++;
    Pointer superClass = ObjectiveC.getClass("NSOpenGLView");
    Pointer thisClass = ObjectiveC.objc_allocateClassPair(superClass, getClassName(), 0);
    Pointer selector = ObjectiveC.getSelector("drawRect:");
    final NSOpenGLView view = this;
    boolean ok;
    if (Pointer.SIZE == 4) {
      ok = ObjectiveC.class_addMethod(thisClass, selector, new IMP_32() {
        public void drawRect(Pointer id, Pointer sel, float x, float y, float w, float h) {
          run.run();
        }
      }, "v@:{NSRect={NSPoint=ff}{NSSize=ff}}");
    } else {
      ok = ObjectiveC.class_addMethod(thisClass, selector, new IMP_64() {
        public void drawRect(Pointer id, Pointer sel, double x, double y, double w, double h) {
          run.run();
        }
      }, "v@:{NSRect={NSPoint=dd}{NSSize=dd}}");
    }
    if (!ok) {
      JFLog.log("MyNSOpenGLView:Failed to add method");
    }
    ObjectiveC.objc_registerClassPair(thisClass);
  }
  public void setRunnable(Runnable run) {
    this.run = run;
  }
}
