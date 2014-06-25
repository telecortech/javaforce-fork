package jpbx.core;

import java.util.*;
import javaforce.voip.*;
import jpbx.plugins.core.*;

/** Extends CallDetailsServer to add more details. */

public class CallDetailsPBX extends CallDetailsServer {
  public RTPRelay audioRelay;  //relay audio
  public RTPRelay videoRelay;  //relay video (may become optional)
  public String trunks[];  //trunks to try and dial
  public int trunkidx;
  public boolean trunkok;
  public int trunkdelay;  //time to give trunk more time
  public String trunkhost;
  public int trunkport;
  public Timer timer;
  public int lastcode;
  public boolean invited;
  public boolean connected;
  public boolean cancelled;
  public boolean anon, route;

  //voicemail details

  public VoiceMail.State vmstate;
  public String vmpass;
  public int vmattempts;
  public String vmstr;
  public String vmlistnew[];
  public String vmlistold[];
  public boolean vmnew;
  public int vmpos;
  public String vmrecfn;

  //IVR details

  public IVR.State ivrstate;
  public String ivrscript[];
  public boolean ivrint;
  public String ivrvar;
  public int ivrtag;
  public StringBuffer ivrstring;
  public HashMap<String, String> ivrvars;

  //Conference details (IVR)

  public Conference.State confstate;
  public Conference.Member confmember;
  public int conftimer;
  public boolean confVideo = false;
}
