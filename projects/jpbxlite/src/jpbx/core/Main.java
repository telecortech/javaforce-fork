package jpbx.core;

import java.net.*;

import javaforce.*;

public class Main {
  private static String getVersion() {
    return "0.17";
  }
  public static void main(String args[]) {
    if (args.length > 0 && args[0].equals("create")) {
      if (!SQL.create()) {
        JFLog.log("Failed to create database");
      }
      return;
    }
    Paths.init();
    //load SQL database
    if (!SQL.initOnce()) {
      JFLog.log("Failed to init database");
      return;
    }
    //init log files
    JFLog.append(Paths.logs + "jpbx.log", true);
    JFLog.log("jPBXlite/" + getVersion() + " starting...");
//    Service.test();
    Service service = new Service();
    service.init();
    new WebConfig().start();
  }
  //linux shutdown function (from command line)
  private static String cfg[][];
  public static void shutdownService() {
    SQL sql = new SQL();
    if (!sql.init()) {
      System.out.println("SQL init failed");
      return;
    }
    cfg = sql.select("SELECT id,value FROM config");
    sql.uninit();
    //send "SHUTDOWN" command to SIP port (this command is only accepted from localhost)
    try {
      int port = Integer.valueOf(getCfg("port"));
      DatagramSocket sock = new DatagramSocket();  //any port
      byte data[] = "SHUTDOWN SIP/2.0\r\ni: null\r\nt: \"null\" <sip:0@127.0.0.1>\r\nf: \"null\" <sip:0@127.0.0.1>\r\n\r\n".getBytes();
      sock.send(new DatagramPacket(data, data.length, InetAddress.getByName("127.0.0.1"), port));
      sock.close();
    } catch (Exception e) {
      System.out.println("Err:" + e);
    }
  }
  private static String getCfg(String id) {
    for(int a=0;a<cfg.length;a++) {
      if (cfg[a][0].equalsIgnoreCase(id)) return cfg[a][1];
    }
    return null;
  }
}
