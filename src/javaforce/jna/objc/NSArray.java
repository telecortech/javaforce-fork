package javaforce.jna.objc;

/** NSArray
 *
 * @author pquiring
 *
 * Created : Jun 2, 2014
 */

import com.sun.jna.*;

public class NSArray extends NSObject {
  public String getClassName() {
    return "NSArray";
  }

  public int count() {
    return ObjectiveC.invokeInt(obj, "count");
  }

  public Pointer objectAtIndex(int idx) {
    return ObjectiveC.invokePointer(obj, "objectAtIndex:", idx);
  }
}
