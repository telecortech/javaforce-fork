package javaforce.jna.objc;

/** NSNumber
 *
 * @author pquiring
 *
 * Created : Jun 11, 2014
 */
public class NSNumber extends NSObject {
  public String getClassName() {
    return "NSNumber";
  }

  public static NSNumber numberWithDouble(double value) {
    NSNumber number = new NSNumber();
    number.obj = ObjectiveC.invokePointer("NSNumber", "numberWithDouble:", value);
    return number;
  }

  public static NSNumber numberWithUnsignedInt(int value) {
    NSNumber number = new NSNumber();
    number.obj = ObjectiveC.invokePointer("NSNumber", "numberWithUnsignedInt:", value);
    return number;
  }

}
