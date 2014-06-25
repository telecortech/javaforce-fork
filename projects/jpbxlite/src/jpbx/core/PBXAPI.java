package jpbx.core;

import javaforce.voip.*;

/** Low-level and high-level APIs used by Plugins for processing SIP messages. */

public interface PBXAPI {
  //High-level APIs (stable) (used for higher functions)
//  public void playSound(CallDetailsPBX cd, String sound, boolean interuptable);
//  public char getDigit(CallDetailsPBX cd);
//  public String getDigits(CallDetailsPBX cd);
//  public void clearDigits(CallDetailsPBX cd);

  //Low-level APIs (volatile) (used primaryly to process SIP messages)
  public void hookDialChain(DialChain chain);
  public void unhookDialChain(DialChain chain);
  public void schedule(Runnable plugin, int minutes);
  public void unschedule(Runnable plugin);
  public boolean isRegistered(String ext);
  public boolean issue(CallDetailsServer cd, String header, boolean sdp, boolean src);
  public boolean reply(CallDetailsServer cd, int code, String msg, String header, boolean sdp, boolean src);
  public boolean transfer_src(CallDetailsPBX cd, String number);
  public boolean transfer_dst(CallDetailsPBX cd, String number);
//  public String[] copyfield(String field[]);
  public Extension getExtension(String ext);
  public String getlocalhost(String host);
  public String[] getTrunks(String dialed, String ext, SQL sql);
  public String resolve(String host);  //resolve host to ip address
  public void releaseCallDetails(CallDetails cd);
  public String patternMatches(String pattern, String dialed);
  public void log(CallDetailsPBX cd, String msg);
  public void log(CallDetailsPBX cd, Exception e);
  public DialChain getDialChain(int pid);
  public void makePath(String path);
  public String convertString(String str);  //convert web string (%##) (+ -> ' ')
  public String getCfg(String id);
  public void connect(CallDetailsPBX cd);
  public void disconnect(CallDetailsPBX cd);
}
