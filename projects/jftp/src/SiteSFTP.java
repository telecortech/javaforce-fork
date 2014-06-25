/** FTP over SSH
 *
 * Requires jcraft/jsch
 *
 * @author pquiring
 */

import java.io.*;
import java.util.*;

import com.jcraft.jsch.*;

import javaforce.*;

public class SiteSFTP extends SiteFTP {
  private ChannelSftp channel;
  private Session jschsession;
  private JSch jsch;

  private class MyUserInfo implements UserInfo {
    public String password;
    public MyUserInfo(String password) {this.password = password;}
    public String getPassword(){
      return password;
    }
    public boolean promptYesNo(String str){
      return true;
    }
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    public boolean promptPassword(String message){ return true; }
    public void showMessage(String message){
      JFLog.log(message);
    }
  }

  @Override
  public boolean connect(SiteDetails sd) {
    try {
      jsch = new JSch();
      setStatus("Connecting...");
      jschsession = jsch.getSession(sd.username, sd.host, Integer.valueOf(sd.port));
//System.out.println("session = " + jschsession);
      jschsession.setPassword(sd.password);
      jschsession.setUserInfo(new MyUserInfo(sd.password));
      setStatus("Login...");
      jschsession.connect(30000);
      channel = (ChannelSftp) jschsession.openChannel("sftp");
//System.out.println("channel = " + channel);
      channel.connect(30000);
      if (sd.remoteDir.length() > 0) {
        channel.cd(sd.remoteDir);
      }
      remote_ls();
      setStatus(null);
    } catch (Exception e) {
      JF.showMessage("Error", "Error:" + e);
      JFLog.log(e);
      closeSite();
      return false;
    }
    return true;
  }

  @Override
  public void disconnect() {
    try {
      channel.disconnect();
      jschsession.disconnect();
    } catch (Exception e) {}
  }

  @Override
  public String remote_pwd() {
    String wd;
    try {
      wd = channel.pwd();
      int i1 = wd.indexOf("\"");
      if (i1 != -1) {
        //the reply is ["path" is your current location]
        int i2 = wd.lastIndexOf("\"");
        if (i1 == i2) return "/";
        wd = wd.substring(i1+1, i2);
      }
    } catch (Exception e) {
      return "/";
    }
    return wd;
  }

  @Override
  public void remote_ls() {
    try {
      String wd = remote_pwd();
      remoteDir.setText(wd);
      remoteDir.setText(wd);
      Vector<ChannelSftp.LsEntry> ls;
      ls = channel.ls(".");
      String lsstr = "";
      for(int a=0;a<ls.size();a++) {
        lsstr += ls.get(a).toString() + "\n";
      }
      parseRemote(wd, lsstr);
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void remote_chdir(String path) {
    try {
      channel.cd(path);
      remote_ls();
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void download_file(File remote, File local) {
    try {
      FileOutputStream fos = new FileOutputStream(local);
      channel.get(remote.getName(), fos);
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void upload_file(File local, File remote) {
    try {
      FileInputStream fis = new FileInputStream(local);
      channel.put(fis, remote.getName());
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void abort() {
    aborted = true;
    //TODO : channel.abort() ?
  }

  @Override
  public void remote_mkdir(String file) {
    try {
      channel.mkdir(file);
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void remote_delete_file(String file) {
    try {
      channel.rm(file);
   } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void remote_delete_folder(String file) {
    try {
      channel.rmdir(file);
      //update remoteTree
      int idx = file.lastIndexOf("/");
      if (idx != -1) file = file.substring(idx+1);
      for(int a=0;a<remoteTag.getChildCount();a++) {
        XML.XMLTag child = remoteTag.getChildAt(a);
        if (child.getName().equals(file)) {
          remoteFolders.deleteTag(child);
          break;
        }
      }
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void remote_rename(String from, String to) {
    try {
      channel.rename(from, to);
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }

  @Override
  public void setPerms(int value, String remoteFile) {
    try {
      channel.chmod(value, remoteFile);
      remote_chdir(".");  //refresh
    } catch (Exception e) {
      setStatus("Error:" + e);
      JFLog.log(e);
      addLog("Error:" + e);
    }
  }
}
