package javaforce.jna.objc;

/**
 *
 * @author pquiring
 *
 * Created : Jun 2, 2014
 */

import com.sun.jna.*;

public class NSString extends NSObject {
  public String getClassName() {
    return "NSString";
  }
  public static int NSASCIIStringEncoding = 1;
  public static int NSUTF8StringEncoding = 4;
  public int length() {
    return ObjectiveC.invokeInt(obj, "length");
  }
  public String cStringUsingEncoding(int encoding) {
    Pointer ptr = ObjectiveC.invokePointer(obj, "cStringUsingEncoding:", encoding);
    return ptr.getString(0);
  }
  public void initWithUTF8String(String str) {
    obj = ObjectiveC.invokePointer(obj, "initWithUTF8String:", str);
  }
  public static NSString stringWithUTF8String(String str) {
    NSString string = new NSString();
    string.alloc();
    string.initWithUTF8String(str);
    return string;
  }
}
