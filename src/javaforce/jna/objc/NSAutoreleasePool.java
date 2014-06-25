package javaforce.jna.objc;

/**
 *
 * @author pquiring
 *
 * Created : May 29, 2014
 */

public class NSAutoreleasePool extends NSObject {
  public String getClassName() {
    return "NSAutoreleasePool";
  }
  public void release() {
    ObjectiveC.invokeVoid(obj, "release");
  }
}
