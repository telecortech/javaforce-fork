package javaforce.jna.objc;

/** NSApplication
 *
 * @author pquiring
 *
 * Created : Jun 5, 2014
 */

public class NSApplication extends NSObject {
  public String getClassName() {
    return "NSApplication";
  }

  public static NSApplication sharedApplication() {
    NSApplication app = new NSApplication();
    app.obj = ObjectiveC.invokePointer("NSApplication", "sharedApplication");
    return app;
  }
}
