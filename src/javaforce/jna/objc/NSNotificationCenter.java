package javaforce.jna.objc;

/** NSNotificationCenter
 *
 * @author pquiring
 *
 * Created : Jun 9, 2014
 */

import com.sun.jna.*;

public class NSNotificationCenter extends NSObject {
  public String getClassName() {
    return "NSNotificationCenter";
  }

  public static NSNotificationCenter defaultCenter() {
    NSNotificationCenter center = new NSNotificationCenter();
    center.obj = ObjectiveC.invokePointer("NSNotificationCenter", "defaultCenter");
    return center;
  }

  public Pointer addObserver(Pointer cls, Pointer sel, String notice, Pointer sender) {
    NSString nsNotice = new NSString();
    nsNotice.alloc();
    nsNotice.initWithUTF8String(notice);
    Pointer id = ObjectiveC.invokePointer(obj, "addObserver:selector:name:object:", cls, sel, nsNotice.obj, sender);
    nsNotice.dealloc();
    return id;
  }

  public void removeObserver(Pointer id) {
    ObjectiveC.invokeVoid(id, "removeObserver:", id);
  }
}
