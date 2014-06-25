package javaforce.jna.objc;

import com.sun.jna.Pointer;
import javaforce.JFLog;

/** NSView
 *
 * @author pquiring
 *
 * Created : May 29, 2014
 */

public class NSView extends NSObject {
  public String getClassName() {
    return "NSView";
  }

  public void initWithFrame(NSRect frame) {
    ObjectiveC.invokePointer(obj, "initWithFrame:", frame);
  }

  public void addSubview(NSView view) {
    ObjectiveC.invokeVoid(obj, "addSubview:", view.obj);
  }

  public boolean wantsLayer() {
    int value = ObjectiveC.invokeInt(obj, "wantsLayer");
    return value != 0;
  }

  public void setWantsLayer(boolean state) {
    ObjectiveC.invokeVoid(obj, "setWantsLayer:", state);
  }

  public void setNeedsDisplay(boolean state) {
    ObjectiveC.invokeVoid(obj, "setNeedsDisplay:", state);
  }

  public void setFrame(NSRect frame) {
    ObjectiveC.invokeVoid(obj, "setFrame:", frame);
  }

  public NSWindow window() {
    NSWindow win = new NSWindow();
    win.obj = ObjectiveC.invokePointer(obj, "window");
    return win;
  }

  public void lockFocus() {
    ObjectiveC.invokeVoid(obj, "lockFocus");
  }

  public void unlockFocus() {
    ObjectiveC.invokeVoid(obj, "unlockFocus");
  }

  public NSArray subviews() {
    NSArray array = new NSArray();
    array.obj = ObjectiveC.invokePointer(obj, "subviews");
    return array;
  }

  public void setLayer(NSOpenGLLayer layer) {
    ObjectiveC.invokeVoid(obj, "setLayer:", layer.obj);
  }

  public void updateLayer() {
    ObjectiveC.invokeVoid(obj, "updateLayer");
  }

  public boolean wantsUpdateLayer() {
    int value = ObjectiveC.invokeInt(obj, "wantsUpdateLayer");
    return value != 0;
  }

  public void printTree(String indent) {
    JFLog.log(indent + "View=" + className().cStringUsingEncoding(NSString.NSUTF8StringEncoding));
    NSArray array = subviews();
    int count = array.count();
    NSView view = new NSView();
    for(int a=0;a<count;a++) {
      view.obj = array.objectAtIndex(a);
      view.printTree(indent + "  ");
    }
  }

}
