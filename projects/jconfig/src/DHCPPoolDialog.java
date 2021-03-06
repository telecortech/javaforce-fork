/**
 * Created : Mar 18, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javaforce.*;

public class DHCPPoolDialog extends javax.swing.JDialog implements KeyListener {

  /**
   * Creates new form DHCPPoolDialog
   */
  public DHCPPoolDialog(java.awt.Frame parent, boolean modal, DHCPPanel.Pool pool) {
    super(parent, modal);
    initComponents();
    setPosition();
    if (pool == null) return;
    enabled.setSelected(pool.enabled);
    ip.setText(pool.ip);
    mask.setText(pool.mask);
    ipFirst.setText(pool.ipfirst);
    ipLast.setText(pool.iplast);
    domain.setText(pool.domain);
    router.setText(pool.router);
    dns.setText(pool.dns);
    wins.setText(pool.wins);
    extra.setText(pool.extra);

    ip.addKeyListener(this);
    mask.addKeyListener(this);
    ipFirst.addKeyListener(this);
    ipLast.addKeyListener(this);
    domain.addKeyListener(this);
    router.addKeyListener(this);
    dns.addKeyListener(this);
    wins.addKeyListener(this);
    extra.addKeyListener(this);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        mask = new javax.swing.JTextField();
        ip = new javax.swing.JTextField();
        dns = new javax.swing.JTextField();
        router = new javax.swing.JTextField();
        wins = new javax.swing.JTextField();
        apply = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        extra = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        domain = new javax.swing.JTextField();
        enabled = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ipFirst = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        ipLast = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DHCP Pool");
        setAlwaysOnTop(true);

        jLabel1.setText("Subnet");

        jLabel2.setText("Subnet Mask");

        jLabel3.setText("DNS");

        jLabel4.setText("Router");

        jLabel5.setText("WINS");

        jLabel6.setText("Extra");

        apply.setText("Apply");
        apply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        extra.setColumns(20);
        extra.setRows(5);
        jScrollPane1.setViewportView(extra);

        jLabel7.setText("Domain");

        enabled.setSelected(true);
        enabled.setText("Enabled");

        jLabel8.setText("Note : Seperate multiple IPs with a comma");

        jLabel9.setText("IP Range");

        jLabel11.setText("to");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(enabled)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dns)
                            .addComponent(router)
                            .addComponent(wins)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(domain)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ip, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addComponent(mask, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ipFirst, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ipLast))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apply)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enabled)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(mask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(ipFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(ipLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(domain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(router, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(wins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apply)
                    .addComponent(cancel)
                    .addComponent(jLabel8))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
    dispose();
  }//GEN-LAST:event_cancelActionPerformed

  private void applyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyActionPerformed
    attempted = true;
    if (!valid()) return;
    accepted = true;
    dispose();
  }//GEN-LAST:event_applyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton apply;
    private javax.swing.JButton cancel;
    private javax.swing.JTextField dns;
    private javax.swing.JTextField domain;
    private javax.swing.JCheckBox enabled;
    private javax.swing.JTextArea extra;
    private javax.swing.JTextField ip;
    private javax.swing.JTextField ipFirst;
    private javax.swing.JTextField ipLast;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField mask;
    private javax.swing.JTextField router;
    private javax.swing.JTextField wins;
    // End of variables declaration//GEN-END:variables

  public boolean accepted = false;
  public boolean attempted = false;

  public boolean getEnabled() { return enabled.isSelected(); }
  public String getIP() { return ip.getText(); }
  public String getMask() { return mask.getText(); }
  public String getIPFirst() { return ipFirst.getText(); }
  public String getIPLast() { return ipLast.getText(); }
  public String getDomain() { return domain.getText(); }
  public String getDNS() { return dns.getText(); }
  public String getRouter() { return router.getText(); }
  public String getWINS() { return wins.getText(); }
  public String getExtra() { return extra.getText(); }

  private boolean isIP4ListValid(JTextField tf, boolean nullAllowed, int maxips, boolean isMask) {
    String str = tf.getText();
    int test;
    tf.setBackground(new Color(0xff0000));
    if (str.length() == 0) return nullAllowed;
    String ips[] = str.split(",");
    if (ips.length > maxips) return false;
    for(int i=0;i<ips.length;i++) {
      String octs[] = ips[i].split("[.]", -1);
      if (octs.length != 4) return false;
      int mask = 0;
      try {
        for(int a=0;a<4;a++) {
          test = Integer.valueOf(octs[a]);
          if ((test < 0) || (test > 255)) return false;
          if (isMask) {
            mask <<= 8;
            mask += test;
          }
        }
        if (isMask) {
          boolean one = false;
          for(int a=0;a<32;a++) {
            if ((mask & 0x01) == 0x01) {one = true;} else {if (one) return false;}
            mask >>= 1;
          }
        }
      } catch (Exception e) {
        JFLog.log(e);
        return false;
      }
    }
    tf.setBackground(new Color(0xffffff));
    return true;
  }

  private long getIP(JTextField tf) {
    //assumes IP is valid
    String octs[] = tf.getText().split("[.]", -1);
    long ipValue = 0;
    for(int a=0;a<4;a++) {
      int oct = Integer.valueOf(octs[a]);
      ipValue <<= 8;
      ipValue += oct;
    }
    return ipValue;
  }

  private boolean isDomainValid(JTextField tf, boolean nullAllowed) {
    String str = tf.getText();
    //check a few conditions
    if (str.length() == 0) return nullAllowed;
//    if (str.indexOf(".") == -1) return false;  //???
    if (str.startsWith(".")) return false;
    if (str.endsWith(".")) return false;
    char c[] = str.toCharArray();
    for(int a=0;a<c.length;a++) {
      if (c[a] == '~') return false;
      if (c[a] == '!') return false;
      if (c[a] == '@') return false;
      if (c[a] == '#') return false;
      if (c[a] == '$') return false;
      if (c[a] == '%') return false;
      if (c[a] == '^') return false;
      if (c[a] == '&') return false;
      if (c[a] == '*') return false;
      if (c[a] == '(') return false;
      if (c[a] == ')') return false;
      if (c[a] == ' ') return false;
      if (c[a] == '?') return false;
      if (c[a] == '+') return false;
      if (c[a] == '/') return false;
      if (c[a] == '\\') return false;
      if (c[a] == '{') return false;
      if (c[a] == '}') return false;
    }
    return true;
  }

  private boolean valid() {
    boolean ok = true;
    if (!isIP4ListValid(ip, false, 1, false)) ok = false;
    if (!isIP4ListValid(mask, false, 1, true)) ok = false;
    if (!isIP4ListValid(ipFirst, false, 1, false)) ok = false;
    if (!isIP4ListValid(ipLast, false, 1, false)) ok = false;
    if (!isIP4ListValid(dns, true, 3, false)) ok = false;
    if (!isIP4ListValid(router, true, 3, false)) ok = false;
    if (!isIP4ListValid(wins, true, 3, false)) ok = false;
    if (!isDomainValid(domain, true)) ok = false;
    if (!ok) return ok;
    //make sure ips are in valid ranges
    long i_ip = getIP(ip);
    long i_mask = getIP(mask);
    long i_first = getIP(ipFirst);
    long i_last = getIP(ipLast);
    if (i_last < i_first) {JFLog.log("ipLast < ipFirst"); ok = false;}
    long i_ip_mask = i_ip & i_mask;
    if ((i_first & i_mask) != i_ip_mask) {JFLog.log("ipFirst is not in subnet"); ok = false;}
    if ((i_last & i_mask) != i_ip_mask) {JFLog.log("ipLast is not in subnet"); ok = false;}
    if (!ok) {
      //set ipFirst & ipLast background = red
      ipFirst.setBackground(new Color(0xff0000));
      ipLast.setBackground(new Color(0xff0000));
    }
    return ok;
  }

  public void keyPressed(KeyEvent e) {}
  public void keyReleased(KeyEvent e) {if (attempted) valid();}
  public void keyTyped(KeyEvent e) {}

  private void setPosition() {
    Rectangle s = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    Dimension d = getSize();
    setLocation(s.width/2 - d.width/2, s.height/2 - (d.height/2));
  }

}
