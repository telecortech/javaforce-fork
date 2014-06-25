package javaforce.jna.objc;

/** NSThread
 *
 * @author pquiring
 *
 * Created : Jun 5, 2014
 */

public class NSThread extends NSObject {
  public String getClassName() {
    return "NSThread";
  }

  public static boolean isMainThread() {
    return ObjectiveC.invokeInt("NSThread", "isMainThread") != 0;
  }
}
