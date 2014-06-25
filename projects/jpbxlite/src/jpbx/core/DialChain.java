package jpbx.core;

public interface DialChain {
  public int getPriority();  //value from 1-100
  //5=Extensions 10=VoiceMail 15=IVR 20=Trunks
  //Following method return value : true=continue false=stop
  public boolean onInvite(CallDetailsPBX cd, SQL sql, boolean src);
  public boolean onRinging(CallDetailsPBX cd, SQL sql, boolean src);
  public boolean onSuccess(CallDetailsPBX cd, SQL sql, boolean src);
  public boolean onCancel(CallDetailsPBX cd, SQL sql, boolean src);
  public boolean onBye(CallDetailsPBX cd, SQL sql, boolean src);
  public boolean onError(CallDetailsPBX cd, SQL sql, int code, boolean src);
  public boolean onTrying(CallDetailsPBX cd, SQL sql, boolean src);
  public boolean onFeature(CallDetailsPBX cd, SQL sql, String cmd, String cmddata, boolean src);
}
