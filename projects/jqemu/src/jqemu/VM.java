package jqemu;

/**
 * Created : Apr 21, 2012
 *
 * @author pquiring
 */

import java.util.ArrayList;

public class VM {
  public String name;
  public String folder;
  public String os;
  public String hda,hdb,hdc,hdd;  //if ends in .iso or starts with "/dev/" it's a CD-ROM
  public boolean hdacd, hdbcd, hdccd, hddcd;
  public String boot;
  public int memory = 256;  //in MBs
  public int cpuCount = 1;
  public String cpuType;  //i386 | x86_64
  public int serviceID;  //1-99 (-1 = none)
  public String video, sound;
  public String netModel, netCount;  //obsolete fields (v0.1)
  public String net1model, net1type, net2model, net2type, net3model, net3type;
  public boolean usb;
  public String[] getCMD(boolean asService) {
    ArrayList<String> cmd = new ArrayList<String>();
    if (net1model == null) net1model = "disabled";
    if (net2model == null) net2model = "disabled";
    if (net3model == null) net3model = "disabled";
    cmd.add("qemu-system-" + cpuType);
    if (hda.length() > 0) {
      cmd.add("-drive");
      cmd.add("file=" + hda + ",if=ide,index=0,media=" + (hdacd ? "cdrom" : "disk"));
    }
    if (hdb.length() > 0) {
      cmd.add("-drive");
      cmd.add("file=" + hdb + ",if=ide,index=1,media=" + (hdbcd ? "cdrom" : "disk"));
    }
    if (hdc.length() > 0) {
      cmd.add("-drive");
      cmd.add("file=" + hdc + ",if=ide,index=2,media=" + (hdccd ? "cdrom" : "disk"));
    }
    if (hdd.length() > 0) {
      cmd.add("-drive");
      cmd.add("file=" + hdd + ",if=ide,index=3,media=" + (hddcd ? "cdrom" : "disk"));
    }
    if (boot.equals("d")) {
      cmd.add("-boot");
      cmd.add("once=d");
    }
    cmd.add("-m");  //memory (megs)
    cmd.add("" + memory);
    if (!sound.equals("none")) {
      cmd.add("-soundhw");
      cmd.add(sound);
    }
    cmd.add("-vga");
    cmd.add(video);
    if (!net1model.equals("disabled")) {
      if (net1type.equals("user")) {
        cmd.add("-net");
        cmd.add("nic,model=" + net1model);
        cmd.add("-net");
        cmd.add("user");
      } else {
        cmd.add("-net");
        cmd.add("nic,model=" + net1model);
        cmd.add("-net");
        cmd.add("tap,ifname=" + net1type + ",script=no,downscript=no");
      }
    }
    if (!net2model.equals("disabled")) {
      if (net2type.equals("user")) {
        cmd.add("-net");
        cmd.add("nic,model=" + net2model);
        cmd.add("-net");
        cmd.add("user");
      } else {
        cmd.add("-net");
        cmd.add("nic,model=" + net2model);
        cmd.add("-net");
        cmd.add("tap,ifname=" + net2type + ",script=no,downscript=no");
      }
    }
    if (!net3model.equals("disabled")) {
      if (net3type.equals("user")) {
        cmd.add("-net");
        cmd.add("nic,model=" + net3model);
        cmd.add("-net");
        cmd.add("user");
      } else {
        cmd.add("-net");
        cmd.add("nic,model=" + net3model);
        cmd.add("-net");
        cmd.add("tap,ifname=" + net3type + ",script=no,downscript=no");
      }
    }
    if (usb) cmd.add("-usb");
    if (cpuCount > 1) {
      cmd.add("-smp");
      cmd.add("" + cpuCount);
    }
    if (asService) {
      cmd.add("-vnc");
      cmd.add("127.0.0.1:" + serviceID);
    }
    return (String[])cmd.toArray(new String[0]);
  }
}
