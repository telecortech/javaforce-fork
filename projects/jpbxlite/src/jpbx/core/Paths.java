package jpbx.core;

/**
 *
 * @author pquiring
 *
 * Created : Sept 16, 2013
 */

import javaforce.*;

public class Paths {
  public static String sounds;
  public static String lang;
  public static String plugins;
  public static String logs;
  public static String lib;
  public static String etc;

  public static void init() {
    lang = "en";
    if (JF.isWindows()) {
      sounds = "./sounds/";
      plugins = "./plugins/";
      logs = "./logs/";
      lib = "./";
      etc = "./";
    } else {
      //Linux
      sounds = "/usr/share/sounds/jpbx/";
      plugins = "/usr/share/java/jpbx/plugins/";
      logs = "/var/log/jpbx/";
      lib = "/var/lib/jpbx/";
      etc = "/etc/";
    }
  }

  public static void setLang(String newLang) {
    lang = newLang;
  }
}
