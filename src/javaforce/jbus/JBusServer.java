package javaforce.jbus;

/**
 * Created : Apr 9, 2012
 *
 * @author pquiring
 */
import java.io.*;
import java.net.*;
import java.util.*;

import javaforce.*;

public class JBusServer extends Thread {

  public final static int port = 777;  //port should be < 1024
  public static volatile boolean ready = false;
  private Vector<Client> clients = new Vector<Client>();
  private boolean active = true;
  private ServerSocket ss;
  private Object lock = new Object();

  public void run() {
    try {
      ss = new ServerSocket(port, 1024, InetAddress.getByName("127.0.0.1"));
      JFLog.log("JBusServer starting");
      ready = true;
      while (active) {
        try {
          Socket s = ss.accept();
          //reject s if it's not localhost
          String ip = s.getInetAddress().toString().substring(1);  //strip leading '/'
          if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {  //ip4 || ip6 - localhost
            Client client = new Client(s);
            client.start();
          } else {
            JFLog.log("JBus : Unauthorized client:" + ip);
          }
        } catch (Exception e1) {
          JFLog.log(e1);
        }
      }
      ss.close();
    } catch (Exception e2) {
      JFLog.log(e2);
    }
  }

  public void close() {
    if (ss == null) {
      return;
    }
    active = false;
    try {
      ss.close();
    } catch (Exception e) {
    }
    ss = null;
  }

  private class Client extends Thread {

    private String pack;
    private Socket s;
    private InputStream is;
    private OutputStream os;

    public Client(Socket s) {
      this.s = s;
      try {
        is = s.getInputStream();
        os = s.getOutputStream();
      } catch (Exception e) {
        JFLog.log(e);
      }
    }

    public void run() {
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (s.isConnected()) {
          String cmd = br.readLine();
          if (cmd == null) {
            break;
          }
          doCmd(cmd);
        }
      } catch (Exception e) {
        JFLog.log(e);
      }
      synchronized (lock) {
        clients.remove(this);
      }
      if (pack != null) {
        JFLog.log("JBus : package unregistered:" + pack);
      }
    }

    private void doCmd(String cmd) throws Exception {
      if (cmd.startsWith("cmd.")) {
        if (cmd.startsWith("cmd.package=")) {
          if (pack != null) {
            return;  //ignore
          }
          pack = cmd.substring(12);
          JFLog.log("JBus : package registered:" + pack);
          synchronized (lock) {
            clients.add(this);
          }
          return;
        }
        //unknown cmd
        JFLog.log("JBus : unknown cmd:" + cmd);
        return;
      }
      //must be a remote function call
      //general format : org.package.func(args)
      //supported args : "String", int
      int b1 = cmd.indexOf("(");
      int b2 = cmd.length() - 1;
//      String args = cmd.substring(b1+1, b2);
      String packFunc = cmd.substring(0, b1);
      int idx = packFunc.lastIndexOf('.');
      if (idx == -1) {
        return;
      }
      String call_pack = packFunc.substring(0, idx);
      String call_func = packFunc.substring(idx + 1);
      cmd += "\n";
      synchronized (lock) {
        for (int a = 0; a < clients.size(); a++) {
          Client client = clients.get(a);
          if (client.pack.equals(call_pack)) {
            client.os.write(cmd.getBytes());
            client.os.flush();
            return;
          }
        }
      }
      JFLog.log("JBus : call to unregistered package.func:" + call_pack + "." + call_func);
    }

    public boolean call(String pack, String func, String args) {
      return call(pack + "." + func + "(" + args + ")\n");
    }

    public boolean call(String pfa) {
      try {
        os.write(pfa.getBytes());
        os.flush();
        return true;
      } catch (Exception e) {
        JFLog.log(e);
        return false;
      }
    }
  }

  /**
   * Broadcasts to call clients that <b>start</b> with the <i>pack</i>.
   */
  public void broadcast(String pack, String func, String args) {
    synchronized (lock) {
      for (int a = 0; a < clients.size(); a++) {
        Client client = clients.get(a);
        if (client.pack != null) {
          if (client.pack.startsWith(pack)) {
            client.call(client.pack, func, args);
          }
        }
      }
    }
  }
}
