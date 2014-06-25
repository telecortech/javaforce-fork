package javaforce.jna.objc;

/**
 * NSWindow
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */

import com.sun.jna.*;

import javaforce.*;

public class NSWindow extends NSObject {

  public String getClassName() {
    return "NSWindow";
  }

  //styleMask
  public static int NSBorderlessWindowMask = 0;
  public static int NSTitledWindowMask = 1;
  public static int NSClosableWindowMask = 2;
  public static int NSMiniaturizableWindowMask  = 4;
  public static int NSResizableWindowMask  = 8;
  public static int NSTexturedBackgroundWindowMask = 256;

  //backing
  public static int NSBackingStoreRetained = 0;
  public static int NSBackingStoreNonretained = 1;
  public static int NSBackingStoreBuffered = 2;

  public void initWithContentRect(NSRect rect, int styleMask, int backing, boolean defer) {
    ObjectiveC.invokeVoid(obj, "initWithContentRect:styleMask:backing:defer:"
      , rect.origin.x, rect.origin.y, rect.size.width, rect.size.height
      , styleMask, backing, defer);
  }

  public void initWithPositionSize(int x, int y, int w, int h) {
    NSRect rect = new NSRect(x, y, w, h);
    initWithContentRect(rect, NSBorderlessWindowMask, NSBackingStoreRetained, false);
  }

  public NSView contentView() {
    NSView view = new NSView();
    view.obj = ObjectiveC.invokePointer(obj, "contentView");
    return view;
  }

  public void setContentView(NSView view) {
    ObjectiveC.invokePointer(obj, "setContentView:", view.obj);
  }

  public void orderFront() {
    NSApplication nsapp = NSApplication.sharedApplication();
    ObjectiveC.invokeVoid(obj, "orderFront:", nsapp.obj);
  }

  public void makeKeyWindow() {
    ObjectiveC.invokeVoid(obj, "makeKeyWindow");
  }

  public void makeMainWindow() {
    ObjectiveC.invokeVoid(obj, "makeMainWindow");
  }

  public void makeKeyAndOrderFront() {
    NSApplication nsapp = NSApplication.sharedApplication();
    ObjectiveC.invokeVoid(obj, "makeKeyAndOrderFront:", nsapp.obj);
  }

  public void display() {
    ObjectiveC.invokeVoid(obj, "display");
  }

  public static int NSWindowAbove = 1;
  public static int NSWindowBelow = -1;
  public static int NSWindowOut = 0;

  public void addChildWindow(NSWindow win, int ordered) {
    ObjectiveC.invokeVoid(obj, "addChildWindow:ordered:", win.obj, ordered);
  }

  public NSRect frame() {
    NSRect rect = new NSRect();
    ObjectiveC.invokeStruct(rect.getPointer(), obj, "frame");
    rect.read();  //was passed as pointer so autoRead was not used
    return rect;
  }

  public int windowNumber() {
    return ObjectiveC.invokeInt(obj, "windowNumber");
  }

  public void printTree() {
    NSView content = contentView();
    JFLog.log("NSWindow Tree:" + className().cStringUsingEncoding(NSString.NSUTF8StringEncoding));
    content.printTree("");
  }
}
