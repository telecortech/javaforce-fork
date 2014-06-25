package javaforce.jna.objc;

/**
 * NSObject
 * Base class for all ObjectiveC classes
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */
import com.sun.jna.*;

import javaforce.*;

public class NSObject {

  /** Pointer to the ObjectiveC class instance. */
  public Pointer obj;

  /** Returns the class name (override in derived classes) */
  public String getClassName() {
    return "NSObject";
  }

  public void alloc() {
    obj = ObjectiveC.invokePointer(getClassName(), "alloc");
  }

  /** same as alloc() and init() */
  public void _new() {
    obj = ObjectiveC.invokePointer(getClassName(), "new");
  }

  public void init() {
    obj = ObjectiveC.invokePointer(obj, "init");
  }

  public void dealloc() {
    ObjectiveC.invokeVoid(obj, "dealloc");
    obj = null;
  }

  //BUG : This func should be static - but needs to call getClassName()
  public Pointer _class() {
    return ObjectiveC.invokePointer(getClassName(), "class");
  }

  public NSString className() {
    NSString string = new NSString();
    string.obj = ObjectiveC.invokePointer(obj, "className");
    return string;
  }

  public boolean isKindOfClass(Pointer cls) {
    int value = ObjectiveC.invokeInt(obj, "isKindOfClass:", cls);
    return value != 0;
  }

  public void performSelectorOnMainThread(Pointer sel, Pointer arg, boolean wait) {
    ObjectiveC.invokeVoid(obj, "performSelectorOnMainThread:withObject:waitUntilDone:", sel, arg, wait);
  }

  //reference counting
  public void retain() {
    ObjectiveC.invokeVoid(obj, "retain");
  }
  public void release() {
    ObjectiveC.invokeVoid(obj, "release");
  }
  public void autorelease() {
    ObjectiveC.invokeVoid(obj, "autorelease");
  }
  public int retainCount() {
    return ObjectiveC.invokeInt(obj, "retainCount");
  }
}
