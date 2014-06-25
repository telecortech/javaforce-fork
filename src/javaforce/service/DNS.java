package javaforce.service;

/**
 * Basic DNS Server
 *
 * Supports : A,CNAME,MX,AAAA
 *
 * Note : IP6 must be in full notation : xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx
 *
 * @author pquiring
 *
 * Created : Nov 17, 2013
 */

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

import javaforce.*;
import javaforce.jbus.JBusClient;

public class DNS extends Thread {
  public static boolean SystemService = false;

  private final static String UserConfigFile = JF.getUserPath() + "/.jdns.cfg";
  private final static String SystemConfigFile = "/etc/jdns.cfg";

  private DatagramSocket ds;
  private static int maxmtu = 512;  //standard
  private String uplink;
  private ArrayList<String> records = new ArrayList<String>();

  public void run() {
    if (SystemService)
      JFLog.init("/var/log/jdns.log", false);
    else
      JFLog.init(JF.getUserPath() + "/.jdns.log", false);
    try {
      loadConfig();
      for(int a=0;a<5;a++) {
        try {
          ds = new DatagramSocket(53);
        } catch (Exception e) {
          if (a == 4) {
            JFLog.log(e);
            return;
          }
          JF.sleep(1000);
          continue;
        }
        break;
      }
      while (true) {
        byte data[] = new byte[maxmtu];
        DatagramPacket packet = new DatagramPacket(data, maxmtu);
        ds.receive(packet);
        new Worker(packet).start();
      }
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  public void close() {
    try {
      ds.close();
    } catch (Exception e) {}
  }

  enum Section {None, Global, Records};

  private final static String defaultConfig
    = "[global]\r\n" + "uplink=8.8.8.8\r\n" + "[records]\r\n" + "#name,type,ttl,value\r\n"
    + "#mydomain.com,cname,3600,www.mydomain.com\r\n"
    + "#www.mydomain.com,a,3600,192.168.0.2\r\n"
    + "#mydomain.com,mx,3600,50,mail.mydomain.com\r\n"
    + "#mail.mydomain.com,a,3600,192.168.0.3\r\n"
    + "#www.mydomain.com,aaaa,1234:1234:1234:1234:1234:1234:1234:1234\r\n";

  private void loadConfig() {
    Section section = Section.None;
    try {
      BufferedReader br = new BufferedReader(new FileReader(SystemService ? SystemConfigFile : UserConfigFile));
      while (true) {
        String ln = br.readLine();
        if (ln == null) break;
        ln = ln.trim().toLowerCase();
        int idx = ln.indexOf('#');
        if (idx != -1) ln = ln.substring(0, idx).trim();
        if (ln.length() == 0) continue;
        if (ln.equals("[global]")) {
          section = Section.Global;
          continue;
        }
        if (ln.equals("[records]")) {
          section = Section.Records;
          continue;
        }
        switch (section) {
          case Global:
            if (ln.startsWith("uplink=")) {
              uplink = ln.substring(7);
            }
            break;
          case Records:
            records.add(ln);
            break;
        }
      }
    } catch (FileNotFoundException e) {
      //create default config
      uplink = "8.8.8.8";
      try {
        FileOutputStream fos = new FileOutputStream(SystemService ? SystemConfigFile : UserConfigFile);
        fos.write(defaultConfig.getBytes());
        fos.close();
      } catch (Exception e2) {
        JFLog.log(e2);
      }
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  //flags
  private static final int QR = 0x8000;  //1=response (0=query)
  //4 bits opcode
  private static final int OPCODE_QUERY = 0x0000;
  private static final int OPCODE_IQUERY = 0x4000;
  private static final int AA = 0x0400;  //auth answer
  private static final int TC = 0x0200;  //truncated (if packet > 512 bytes)
  private static final int RD = 0x0100;  //recursive desired
  private static final int RA = 0x0080;  //recursive available
  private static final int Z =  0x0040;  //???
  private static final int AD = 0x0020;  //auth data???
  private static final int CD = 0x0010;  //checking disabled???
  //4 bits result code (0=no error)

  private static final int A = 1;
  private static final int CNAME = 5;
  private static final int MX = 15;
  private static final int AAAA = 28;

  private class Worker extends Thread {
    private DatagramPacket packet;
    private byte data[];
    private int nameLength;  //bytes used decoding name

    private byte reply[];
    private int replyOffset;
    private ByteBuffer replyBuffer;

    public Worker(DatagramPacket packet) {
      this.packet = packet;
    }
    public void run() {
      try {
        data = packet.getData();
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        short id = bb.getShort(0);
        short flgs = bb.getShort(2);
        if ((flgs & QR) != 0) {
          throw new Exception("response sent to server");
        }
        if ((flgs & OPCODE_IQUERY) != 0) {
          throw new Exception("inverse query not supported");
        }
        short cndQ = bb.getShort(4);
        if (cndQ != 1) throw new Exception("only 1 question supported");
        short cndA = bb.getShort(6);
        if (cndA != 0) throw new Exception("query with answers?");
        short cndS = bb.getShort(8);
        if (cndS != 0) throw new Exception("query with auth names?");
        short cndAdd = bb.getShort(10);
        if (cndAdd != 0) throw new Exception("query with adds?");
        int offset = 12;
        for(int a=0;a < cndQ; a++) {
          String domain = getName(data, offset);
          offset += nameLength;
          int type = bb.getShort(offset);
          offset += 2;
          int cls = bb.getShort(offset);
          if (cls != 1) throw new Exception("only internet class supported");
          offset += 2;
          if (queryLocal(domain, type, id)) continue;
          queryRemote(domain, type);
        }
      } catch (Exception e) {
        JFLog.log(e);
      }
    }

    private String getName(byte data[], int offset) {
      StringBuilder name = new StringBuilder();
      boolean jump = false;
      nameLength = 0;
      do {
        if (!jump) nameLength++;
        int length = ((int)data[offset++]) & 0xff;
        if (length == 0) break;
        if (length >= 0xc0) {
          //pointer
          if (!jump) nameLength += 2;
          jump = true;
          offset = (length & 0x6f) << 8;
          offset += ((int)data[offset]) & 0xff;
        } else {
          if (!jump) nameLength += length;
          if (name.length() != 0) name.append(".");
          name.append(new String(data, offset, length));
          offset += length;
        }
      } while (true);
      return name.toString();
    }

    private int encodeName(String domain) {
      //TODO : compression
      String p[] = domain.split("[.]");
      int length = 0;
      int strlen;
      for(int a=0;a<p.length;a++) {
        strlen = p[a].length();
        reply[replyOffset++] = (byte)strlen;
        length++;
        System.arraycopy(p[a].getBytes(), 0, reply, replyOffset, strlen);
        replyOffset += strlen;
        length += strlen;
      }
      //zero length part ends string
      reply[replyOffset++] = 0;
      length++;
      return length;
    }

    private boolean queryLocal(String domain, int type, int id) {
      //TODO : query derby and add answer if available
      //NOTE : set aa if found in local db and do not add anything to nameservers
      int rc = records.size();
      String match = null;
      switch (type) {
        case A: match = domain + ",a,"; break;
        case CNAME: match = domain + ",cname,"; break;
        case MX: match = domain + ",mx,"; break;
        case AAAA: match = domain + ",aaaa,"; break;
      }
      if (match == null) return false;
      match = match.toLowerCase();
      for(int a=0;a<rc;a++) {
        String record = records.get(a);
        if (record.startsWith(match)) {
          //type,name,ttl,values...
          sendReply(domain, record, type, id);
          return true;
        }
      }
      return false;
    }

    private void queryRemote(String domain, int type) {
      //query remote DNS server and simple relay the reply "as is"
      //TODO : need to actually remove AA flag if present and fill in other sections as needed
      try {
        DatagramPacket out = new DatagramPacket(data, data.length);
        out.setAddress(InetAddress.getByName(uplink));
        out.setPort(53);
        DatagramSocket sock = new DatagramSocket();  //bind to anything
        sock.send(out);
        reply = new byte[maxmtu];
        DatagramPacket in = new DatagramPacket(reply, reply.length);
        sock.receive(in);
        sendReply(reply, in.getLength());
      } catch (Exception e) {
        JFLog.log(e);
      }
    }

    private void sendReply(byte outData[], int outDataLength) {
      try {
        DatagramPacket out = new DatagramPacket(outData, outDataLength);
        out.setAddress(packet.getAddress());
        out.setPort(packet.getPort());
        ds.send(out);
      } catch (Exception e) {
        JFLog.log(e);
      }
    }

    private void sendReply(String query, String record, int type, int id) {
      int rdataOffset, rdataLength;
      //record = type,name,ttl,value
      String f[] = record.split(",");
      int ttl = JF.atoi(f[2]);
      reply = new byte[maxmtu];
      replyBuffer = ByteBuffer.wrap(reply);
      replyBuffer.order(ByteOrder.BIG_ENDIAN);
      replyOffset = 0;
      putShort((short)id);
      putShort((short)(QR | AA | RA));
      putShort((short)1);  //questions
      putShort((short)1);  //answers
      putShort((short)0);  //name servers
      putShort((short)0);  //additionals
      encodeName(query);
      putShort((short)type);
      putShort((short)1);  //class
      switch(type) {
        case A:
          encodeName(query);
          putShort((short)type);
          putShort((short)1);  //class
          putInt(ttl);
          putShort((short)4);  //Rdata length
          putIP4(f[3]);  //Rdata
          break;
        case CNAME:
          encodeName(query);
          putShort((short)type);
          putShort((short)1);  //class
          putInt(ttl);
          rdataOffset = replyOffset;
          putShort((short)0);  //Rdata length (patch 2 lines down)
          rdataLength = encodeName(f[3]);
          replyBuffer.putShort(rdataOffset, (short)rdataLength);
          break;
        case MX:
          encodeName(query);
          putShort((short)type);
          putShort((short)1);  //class
          putInt(ttl);
          rdataOffset = replyOffset;
          putShort((short)0);  //Rdata length (patch 3 lines down)
          putShort((short)JF.atoi(f[3]));  //preference (1-99)
          rdataLength = encodeName(f[4]);  //cname
          replyBuffer.putShort(rdataOffset, (short)(rdataLength + 2));
          break;
        case AAAA:
          encodeName(query);
          putShort((short)type);
          putShort((short)1);  //class
          putInt(ttl);
          putShort((short)16);  //Rdata length
          putIP6(f[3]);  //Rdata
          break;
      }
      sendReply(reply, replyOffset);
    }

    private void putIP4(String ip) {
      String p[] = ip.split("[.]");
      for(int a=0;a<4;a++) {
        reply[replyOffset++] = (byte)JF.atoi(p[a]);
      }
    }

    private void putIP6(String ip) {
      String p[] = ip.split(":");
      for(int a=0;a<8;a++) {
        putShort((short)JF.atox(p[a]));
      }
    }

    private void putShort(short value) {
      replyBuffer.putShort(replyOffset, value);
      replyOffset += 2;
    }

    private void putInt(int value) {
      replyBuffer.putInt(replyOffset, value);
      replyOffset += 4;
    }
  }
  //this is service entry, see DNSApp for user app entry
  private static JBusClient jbusClient;
  public static void main(String args[]) {
    DNS.SystemService = true;
    jbusClient = new JBusClient("org.jflinux.service.jdns", new JBusMethods());
    jbusClient.start();
    new DNS().start();
  }
  public static class JBusMethods {
    //standard service methods
    public void stop() {
      System.exit(0);
    }
    public void status(String pack) {
      jbusClient.call(pack, "serviceStatus", "\"jDNS running:" + JF.getPID() + "\"");
    }
  }
}

/*
struct Packet {
  short id;
  short flgs;
  short queries_cnt;
  short anwsers_cnt;
  short auth_servers_cnt;
  short additional_cnt;
  Queries[];
  AnwserResources[];
  AuthServerResources[];
  AdditionalResources[];
};
struct Query {
  DNSName query;
  short type;
  short cls;
};
struct Resource {
  DNSName name;
  short type;
  short cls;
  int ttl;
  short rdataLength;
  byte rdata[rdataLength];
};
DNS Names compression:
  [3]www[6]google[3]com[0]
  Any length that starts with 11 binary is a 2 byte (14bits) pointer from the first byte of the packet.
  [4]mail[pointer:33]  -> assuming 33 points to [6]google[3]com[0] this would => mail.google.com
Types: 1=IP4 5=CNAME 15=MX 28=IP6 etc.
Cls : 1=Internet
*/
