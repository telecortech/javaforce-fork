/**
 * Created : Mar 18, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import javax.swing.*;

import javaforce.*;

public class DNSZoneDialog extends javax.swing.JDialog {

  /**
   * Creates new form DNSZoneDialog
   */
  public DNSZoneDialog(java.awt.Frame parent, boolean modal, DNSPanel.Zone zone) {
    super(parent, modal);
    initComponents();
    setPosition();
    if (zone == null) return;
    name.setText(zone.name);
    master.setSelected(zone.master);
    others.setText(zone.others);
    ttl.setText("" + zone.ttl);
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
        name = new javax.swing.JTextField();
        master = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        others = new javax.swing.JTextField();
        accept = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        ttl = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Name");

        master.setSelected(true);
        master.setText("Zone Master");

        jLabel2.setText("Other Servers");

        accept.setText("Accept");
        accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        jLabel3.setText("TTL (seconds)");

        ttl.setText("86400");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(name))
                    .addComponent(master, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(others))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accept))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ttl, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(master)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(others, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ttl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accept)
                    .addComponent(cancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
    dispose();
  }//GEN-LAST:event_cancelActionPerformed

  private void acceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptActionPerformed
    if (!valid()) return;
    accepted = true;
    dispose();
  }//GEN-LAST:event_acceptActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accept;
    private javax.swing.JButton cancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JCheckBox master;
    private javax.swing.JTextField name;
    private javax.swing.JTextField others;
    private javax.swing.JTextField ttl;
    // End of variables declaration//GEN-END:variables

  public boolean accepted = false;

  public String getName() { return name.getText(); }
  public boolean getMaster() { return master.isSelected(); }
  public String getOthers() { return others.getText(); }
  public int getTTL() { return JF.atoi(ttl.getText()); }

  private boolean isIP4ListValid(JTextField tf, boolean nullAllowed, int maxips, boolean isMask) {
    String str = tf.getText();
    int test;
    tf.setBackground(new Color(0xff0000));
    if (str.length() == 0) return nullAllowed;
    String ips[] = str.split(" ");
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

  private boolean valid() {
    boolean ok = true;
    if (name.getText().length() == 0) {
      name.setBackground(new Color(0xff0000));
      ok = false;
    } else {
      name.setBackground(new Color(0xffffff));
    }
    if (!isIP4ListValid(others, true, 3, false)) ok = false;
    return ok;
  }

  private void setPosition() {
    Rectangle s = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    Dimension d = getSize();
    setLocation(s.width/2 - d.width/2, s.height/2 - (d.height/2));
  }
}
