/**
 * Created : Mar 4, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;

import javaforce.*;
import javaforce.jbus.*;
import javaforce.linux.*;

public class ConfigApp extends javax.swing.JFrame {

  /**
   * Creates new form ConfigApp
   */
  public ConfigApp() {
    initComponents();
    JFLog.init(JF.getUserPath() + "/.jconfig.log", true);
    This = this;
    Linux.detectDistro();
    switch (Linux.distro) {
      case Ubuntu: break;
      case Fedora: break;
      default: JF.showError("Error", "Unsupported Distro"); System.exit(0);
    }
    jbusClient = new JBusClient("org.jflinux.jconfig." + new Random().nextInt(0x7fffff), new JBusMethods());
    jbusClient.start();
    if ((args.length > 0) && (args[0].length() > 0)) {
      if (args[0].equals("users")) panel = new UsersPanel();
      else if (args[0].equals("groups")) panel = new GroupsPanel();
      else if (args[0].equals("interfaces")) panel = new InterfacePanel();
      else if (args[0].equals("vpn")) panel = new VPNPanel();
      else if (args[0].equals("firewall")) panel = new FirewallPanel();
      else if (args[0].equals("routing")) panel = new RoutingPanel();
      else if (args[0].equals("servers")) panel = new ServersPanel();
      else if (args[0].equals("display")) panel = new DisplayPanel();
      else if (args[0].equals("sound")) panel = new SoundPanel();
      else if (args[0].equals("datetime")) panel = new DateTimePanel();
      else if (args[0].equals("date")) panel = new DateTimePanel();
      else if (args[0].equals("time")) panel = new DateTimePanel();
      else if (args[0].equals("printers")) panel = new PrintersPanel();
    }
    if (panel == null) panel = new MainPanel();
    setContentPane(panel);
    setPosition();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Config");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 715, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    ConfigApp.args = args;
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new ConfigApp().setVisible(true);
      }
    });
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

  public static ConfigApp This;
  public static String args[];
  public static JBusClient jbusClient;
  public static JPanel panel;

  public static void setPanel(JPanel panel) {
    This.panel = panel;
    This.setContentPane(panel);
    panel.revalidate();
    This.repaint();
  }

  public static class JBusMethods {
    public void videoChanged(String reason) {
      if (!reason.equals("udev")) return;
      if (panel instanceof DisplayPanel) {
        ((DisplayPanel)panel).rescan();
      }
    }
  }

  private void setPosition() {
    Dimension d = getSize();
    Rectangle s = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    if ((d.width > s.width) || (d.height > s.height)) {
      if (d.width > s.width) d.width = s.width;
      if (d.height > s.height) d.height = s.height;
      setSize(d);
    }
    setLocation(s.width/2 - d.width/2, s.height/2 - d.height/2);
  }
}
