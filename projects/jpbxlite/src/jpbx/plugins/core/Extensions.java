package jpbx.plugins.core;

import java.util.*;

import javaforce.*;

import jpbx.core.*;

/** Low-level plugin for handling INVITEs to extensions. */

public class Extensions implements Plugin, DialChain {
  public final static int pid = 5;  //priority
  private PBXAPI api;
//interface Plugin
  public void init(PBXAPI api) {
    this.api = api;
    api.hookDialChain(this);
    JFLog.log("Extensions plugin init");
  }
  public void uninit(PBXAPI api) {
    api.unhookDialChain(this);
    JFLog.log("Extensions plugin uninit");
  }
  public void install(PBXAPI api) {
    //nothing to do
  }
  public void uninstall(PBXAPI api) {
    //nothing to do
  }
//interface DialChain
  public int getPriority() {return pid;}
  public boolean onInvite(CallDetailsPBX cd, SQL sql, boolean src) {
    if (cd.invited) {
      //reINVITE
      api.connect(cd);
      if (src) {
        cd.pbxdst.branch = cd.src.branch;
        cd.pbxsrc.branch = cd.src.branch;
      } else {
        cd.pbxdst.branch = cd.dst.branch;
        cd.pbxsrc.branch = cd.dst.branch;
      }
      cd.sip.buildsdp(cd, src ? cd.pbxdst : cd.pbxsrc);
      api.issue(cd, null, true, !src);
      return false;
    }
    if (!src) return false;
    if (!cd.authorized) {
      if (!apply_inbound_routes(cd, sql)) return true;
    }
    //dial inbound to an extension
    String ext = sql.select1value("SELECT ext FROM exts WHERE ext=" + sql.quote(cd.dialed));
    if (ext == null) return true;  //an extension is not being dialed
    cd.pbxdst.contact = cd.src.contact;
    cd.pbxdst.branch = cd.src.branch;
    cd.pbxdst.to = cd.src.to.clone();
    cd.pbxdst.from = cd.src.from.clone();
    if (!api.isRegistered(cd.dialed)) {
      api.reply(cd, 404, "NOT ONLINE", null, false, true);
      return false;  //phone is not online
    }
    if (cd.user.equals(cd.dialed)) {  //did someone call themself?
      api.log(cd, "caller is calling themself");
      return true;  //voicemail will handle this call
    }
    api.reply(cd, 100, "TRYING", null, false, true);
    Extension x = api.getExtension(cd.dialed);
    if (x == null) {
      api.reply(cd, 404, "NOT ONLINE", null, false, true);
      return false;  //phone is not online
    }
    cd.pbxdst.cseq = 1;
    cd.pid = pid;  //mark call leg as mine
    cd.pbxdst.host = x.remoteip;
    cd.pbxdst.port = x.remoteport;
    cd.pbxdst.uri = "sip:" + cd.dialed + "@" + cd.pbxdst.host + ":" + cd.pbxdst.port;
    api.connect(cd);
    cd.sip.buildsdp(cd, cd.pbxdst);
    api.issue(cd, null, true, false);
    cd.invited = true;
    setTimer(cd, 15000);
    api.log(cd, "DEBUG:INVITE:" + cd.dialed + ":dst=" + cd.pbxdst.host + ":" + cd.pbxdst.port);
    return false;
  }
  public boolean onRinging(CallDetailsPBX cd, SQL sql, boolean src) {
    if (src) return false;
    api.reply(cd, 180, "RINGING", null, false, true);
    return false;
  }
  public boolean onSuccess(CallDetailsPBX cd, SQL sql, boolean src) {
    if (!cd.cmd.equals("INVITE")) return false;
    synchronized(cd.lock) {
      if (cd.cancelled) return false;
      cd.connected = true;
    }
    clearTimer(cd);
    //send ack
    cd.cmd = "ACK";
    api.issue(cd, null, false, src);  //send ACK
    if (src) {
      cd.pbxdst.to = cd.src.to.clone();
      cd.pbxdst.from = cd.src.from.clone();
    } else {
      cd.pbxsrc.to = cd.dst.to.clone();
      cd.pbxsrc.from = cd.dst.from.clone();
    }
    api.connect(cd);
    cd.sip.buildsdp(cd, src ? cd.pbxdst : cd.pbxsrc);
    cd.cmd = "INVITE";
    api.reply(cd, 200, "OK", null, true, !src);  //send 200 (NOTE : ACK is ignored)
    return false;
  }
  public boolean onCancel(CallDetailsPBX cd, SQL sql, boolean src) {
    if (!src) return false;
    synchronized(cd.lock) {
      if (cd.connected) return false;  //TODO : should return error already connected
      cd.cancelled = true;
    }
    api.reply(cd, 200, "OK", null, false, true);
    api.issue(cd, null, false, false);
    api.disconnect(cd);
    return false;
  }
  public boolean onError(CallDetailsPBX cd, SQL sql, int code, boolean src) {
    api.reply(cd, code, "RELAY", null, false, !src);
    cd.cmd = "ACK";
    api.issue(cd, null, false, src);
    return false;
  }
  public boolean onTrying(CallDetailsPBX cd, SQL sql, boolean src) {
    return false;
  }
  public boolean onBye(CallDetailsPBX cd, SQL sql, boolean src) {
    if (src) {
      //NOTE:to/from have been swapped
      cd.pbxdst.to = cd.src.to.clone();
      cd.pbxdst.from = cd.src.from.clone();
      cd.pbxdst.cseq++;
      api.issue(cd, null, false, false);
      cd.pbxsrc.to = cd.src.to.clone();
      cd.pbxsrc.from = cd.src.from.clone();
      cd.pbxsrc.cseq++;
      api.reply(cd, 200, "OK", null, false, true);
    } else {
      //NOTE:to/from have been swapped
      cd.pbxsrc.to = cd.dst.to.clone();
      cd.pbxsrc.from = cd.dst.from.clone();
      cd.pbxsrc.cseq++;
      api.issue(cd, null, false, true);
      cd.pbxdst.to = cd.dst.to.clone();
      cd.pbxdst.from = cd.dst.from.clone();
      cd.pbxdst.cseq++;
      api.reply(cd, 200, "OK", null, false, false);
    }
    api.disconnect(cd);
    return false;
  }
  public boolean onFeature(CallDetailsPBX cd, SQL sql, String cmd, String cmddata, boolean src) {
    return true;
  }
  private void setTimer(CallDetailsPBX cd, int timeout) {
    api.log(cd, "setting timer for call");
    cd.timer = new Timer();
    cd.timer.schedule(new TimerTask() {
      private CallDetailsPBX cd;
      public void run() {
        synchronized (cd.lock) {
          cd.timer = null;
          if (cd.connected) return;
      	  api.log(cd, "Trying to do voicemail for ext=" + cd.dialed);
          //send CANCEL to dst
          cd.cmd = "CANCEL";
          api.issue(cd, null, false, false);
          SQL sql = new SQL();
          if (!sql.init()) return;  //ohoh
          //check if ext has voicemail?
          String value = sql.select1value("SELECT value FROM extopts WHERE ext=" + sql.quote(cd.dialed) + " AND id='vm'");
          if ((value != null) && (value.equals("yes"))) {
            DialChain dc = api.getDialChain(VoiceMail.pid);
            cd.invited = false;
            dc.onInvite(cd, sql, true);  //transfer call to voicemail after timeout
          } else {
            //reply 486 (no one here)
            cd.cmd = "INVITE";
            api.reply(cd, 486, "NO ONE HERE", null, false, true);
          }
          sql.uninit();
        }
      }
      public TimerTask init(CallDetailsPBX cd) {
        this.cd = cd;
        return this;
      }
    }.init(cd), timeout);  //give ext timeout millisec to respond
  }
  private void clearTimer(CallDetailsPBX cd) {
    synchronized (cd.lock) {
      if (cd.timer == null) return;
      cd.timer.cancel();
      cd.timer = null;
    }
  }
  private boolean apply_inbound_routes(CallDetailsPBX cd, SQL sql) {
    String routes[][] = sql.select("SELECT cid,did,dest FROM inroutes");
    //cd.user = cid
    //cd.dialed = did
    for(int route=0;route<routes.length;route++) {
      if (routes[route][0].length() == 0 && routes[route][1].length() == 0) continue;  //bad route
      if (routes[route][2].length() == 0) continue;  //bad route
      if ((routes[route][0].length() > 0) && !routes[route][0].equals(cd.user)) continue;  //no cid match
      if ((routes[route][1].length() > 0) && !routes[route][1].equals(cd.dialed)) continue;  //no did match
      cd.dialed = routes[route][2];  //set new destination
      return true;
    }
    String anon = sql.select1value("SELECT value FROM config WHERE id='anon'");
    String route = sql.select1value("SELECT value FROM config WHERE id='route'");
    cd.anon = anon.equals("true");
    cd.route = route.equals("true");
    return cd.anon;  //FALSE = no anonymous inbound calls
  }
}
