package javaforce.utils;

/**
 This is a Win32 tool to set the priority of a process.
 I use this to bump up Chrome.exe so multimedia plays nicely.
 ex:  SetPriority chrome.exe HIGH
*/

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import com.sun.jna.win32.*;

public class SetPriority {
  private static Pointer malloc(int size) {
    return new Pointer(Native.malloc(size));
  }

  private static void free(Pointer ptr) {
    Native.free(Pointer.nativeValue(ptr));
  }
  public static interface PSAPI extends StdCallLibrary {
    int EnumProcesses(int pids[], int cb, IntByReference cb_ret);
    int EnumProcessModules(Pointer hProcess,PointerByReference lphModule,int cb,IntByReference lpcbNeeded);
    int GetModuleBaseNameA(Pointer hProcess, Pointer hModule, Pointer lpBaseName, int nSize);
  }
  public static interface KERNEL extends StdCallLibrary {
    Pointer OpenProcess(int dwDesiredAccess, int bInheritHandle, int dwProcessId);
    void CloseHandle(Pointer handle);
    int SetPriorityClass(Pointer hProcess, int cls);
  }
  private static KERNEL kernel;
  private static PSAPI psapi;
  private static final int FALSE = 0;
  private static final int PROCESS_QUERY_INFORMATION = 0x400;
  private static final int PROCESS_VM_READ = 0x10;
  private static final int PROCESS_SET_INFORMATION = 0x200;
  private static final int ABOVE_NORMAL_PRIORITY_CLASS = 0x8000;
  private static final int BELOW_NORMAL_PRIORITY_CLASS = 0x4000;
  private static final int HIGH_PRIORITY_CLASS = 0x80;
  private static final int NORMAL_PRIORITY_CLASS = 0x20;
  private static final int REALTIME_PRIORITY_CLASS = 0x100;
  private static void usage() {
    System.out.println("Usage:setpriority process_name priority");
    System.out.println("Where:priority = {ABOVE_NORMAL | BELOW_NORMAL | HIGH | NORMAL | REALTIME}");
  }
  public static void main(String args[]) {
    if (!Platform.isWindows()) {System.out.println("For windows only"); return;}
    if (args.length != 2) {usage(); return;}
    int pri = -1;
    if (args[1].equalsIgnoreCase("ABOVE_NORMAL")) pri = ABOVE_NORMAL_PRIORITY_CLASS;
    if (args[1].equalsIgnoreCase("BELOW_NORMAL")) pri = BELOW_NORMAL_PRIORITY_CLASS;
    if (args[1].equalsIgnoreCase("HIGH")) pri = HIGH_PRIORITY_CLASS;
    if (args[1].equalsIgnoreCase("NORMAL")) pri = NORMAL_PRIORITY_CLASS;
    if (args[1].equalsIgnoreCase("REALTIME")) pri = REALTIME_PRIORITY_CLASS;
    if (pri == -1) {usage(); return;}
    try {
      psapi = (PSAPI) Native.loadLibrary("psapi", PSAPI.class);
      kernel = (KERNEL) Native.loadLibrary("kernel32", KERNEL.class);

      IntByReference cb_ret = new IntByReference();
      int pids[] = new int[1024];
      psapi.EnumProcesses(pids, 1024 * 4, cb_ret);

      int nb = cb_ret.getValue() / 4;

      Pointer baseName = malloc(1024);

      int cnt = 0;

      for(int a=0;a<nb;a++) {
        Pointer hProcess = kernel.OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ | PROCESS_SET_INFORMATION, FALSE, pids[a]);
        Pointer hMod;
        IntByReference cb_needed = new IntByReference();
        PointerByReference pref = new PointerByReference();
        if (psapi.EnumProcessModules(hProcess, pref, 4, cb_needed) > 0) {
          hMod = pref.getValue();
          psapi.GetModuleBaseNameA(hProcess, hMod, baseName, 1024);
          String name = baseName.getString(0);
          if (name.equals(args[0])) {
            kernel.SetPriorityClass(hProcess, pri);
            cnt++;
          }
        }
        kernel.CloseHandle(hProcess);
      }
      free(baseName);
      System.out.println("Set Priority on " + cnt + " processes");
    } catch (Throwable t) {
      System.out.println(t.toString());
    }
  }
};
