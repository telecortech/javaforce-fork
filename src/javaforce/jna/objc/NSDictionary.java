package javaforce.jna.objc;

/** NSDictionary
 *
 * @author pquiring
 *
 * Created : Jun 11, 2014
 */

public class NSDictionary extends NSObject {
  public String getClassName() {
    return "NSDictionary";
  }

  public static NSDictionary dictionaryWithObjectsAndKeys(Object... list) {
    NSDictionary dict = new NSDictionary();
    dict.obj = ObjectiveC.invokePointer("NSDictionary", "dictionaryWithObjectsAndKeys:", list);
    return dict;
  }
}
