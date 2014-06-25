package javaforce.jna.objc;

/** QTFormatDescription
 *
 * @author pquiring
 *
 * Created : Jun 9, 2014
 */

import com.sun.jna.*;

public class QTFormatDescription extends NSObject {
  public String getClassName() {
    return "QTFormatDescription";
  }

  public static String QTFormatDescriptionVideoEncodedPixelsSizeAttribute = "videoEncodedPixelsSize";

  /** Returns either NSValue or NSData */
  public Pointer attributeForKey(String key) {
    NSString str = new NSString();
    str.alloc();
    str.initWithUTF8String(key);
    Pointer ptr = ObjectiveC.invokePointer(obj, "attributeForKey:", str.obj);
    str.dealloc();
    return ptr;
  }

  /** Returns the fourCC type. */
  public int formatType() {
    return ObjectiveC.invokeInt(obj, "formatType");
  }
}
