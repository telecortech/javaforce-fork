package javaforce.jna.objc;

/** QTMovie
 *
 * @author pquiring
 *
 * Created : Jun 11, 2014
 */

public class QTMovie extends NSObject {
  public String getClassName() {
    return "QTMovie";
  }

  public static void enterQTKitOnThread() {
    ObjectiveC.invokeVoid("QTMovie", "enterQTKitOnThread");
  }

  public static void exitQTKitOnThread() {
    ObjectiveC.invokeVoid("QTMovie", "exitQTKitOnThread");
  }
}
