package javaforce.jna.objc;

/** MyNSObject
 *
 * Use register() to create a callback to a Runnable when MyNSObject.callback() is invoked.
 * Try with NSObject.performSelectorOnMainThread
 *
 * Use registerNotfication() to create a callback for use with NSNoticationCenter.
 *
 * @author pquiring
 *
 * Created : Jun 5, 2014
 */

import com.sun.jna.*;

import javaforce.*;

public class MyNSObject extends NSObject {
  private int instance = 0;
  private String className;

  private Runnable runnable;

  public String getClassName() {
    return className;
  }

  //- (void) callback
  public static interface IMP extends Callback {
    public void callback(Pointer id, Pointer sel);
  }

  public synchronized void register(Runnable _run) {
    className = "MyNSObject" + instance++;
    this.runnable = _run;
    Pointer superClass = ObjectiveC.getClass("NSObject");
    Pointer thisClass = ObjectiveC.objc_allocateClassPair(superClass, getClassName(), 0);
    Pointer selector = ObjectiveC.getSelector("callback");
    JFLog.log("MyNSObject:" + superClass + "," + thisClass + "," + selector);
    if (!ObjectiveC.class_addMethod(thisClass, selector, new IMP() {
      public void callback(Pointer id, Pointer sel) {
        runnable.run();
      }
    }, "v@:")) {
      JFLog.log("MyNSObject:Failed to add method");
    }
    ObjectiveC.objc_registerClassPair(thisClass);
  }

  public static interface Notification {
    public void callback(NSNotification notification);
  }

  private Notification notification;

  //- (void) callback:NSNotification
  public static interface IMP_Notification extends Callback {
    public void callback(Pointer id, Pointer sel, Pointer notification);
  }

  public synchronized void registerNotification(Notification _notification) {
    className = "MyNSObject" + instance++;
    this.notification = _notification;
    Pointer superClass = ObjectiveC.getClass("NSObject");
    Pointer thisClass = ObjectiveC.objc_allocateClassPair(superClass, getClassName(), 0);
    Pointer selector = ObjectiveC.getSelector("callback:");
    JFLog.log("MyNSObject:" + superClass + "," + thisClass + "," + selector);
    ObjectiveC.class_addMethod(thisClass, selector, new IMP_Notification() {
      public void callback(Pointer id, Pointer sel, Pointer notificationObj) {
        NSNotification notif = new NSNotification();
        notif.obj = notificationObj;
        notification.callback(notif);
      }
    }, "v@:@");
    ObjectiveC.objc_registerClassPair(thisClass);
  }

  public void setRunnable(Runnable run) {
    this.runnable = run;
  }

  public void run() {
    runnable.run();
  }
}
