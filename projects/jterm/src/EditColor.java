/*
 * GetColor.java
 *
 * Created on August 8, 2007, 1:17 PM
 *
 * @author  pquiring
 */

import java.awt.Color;

public class EditColor extends javax.swing.JDialog {
  
  /** Creates new form GetColor */
  private EditColor(java.awt.Window parent) {
    super(parent, "EditColor", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
    initComponents();
    setComponentOrientation(((parent == null) ? javax.swing.JOptionPane.getRootFrame() : parent).getComponentOrientation());
    if (parent != null) setLocationRelativeTo(parent);
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    colorChooser = new javax.swing.JColorChooser();
    bSelect = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Choose Color");

    bSelect.setText("Select");
    bSelect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bSelectActionPerformed(evt);
      }
    });

    bCancel.setText("Cancel");
    bCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bCancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(colorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bSelect)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bCancel)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(colorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bSelect)
          .addComponent(bCancel))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    dispose();
  }//GEN-LAST:event_bCancelActionPerformed

  private void bSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSelectActionPerformed
    retColor = colorChooser.getColor();
    dispose();
  }//GEN-LAST:event_bSelectActionPerformed
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bSelect;
  private javax.swing.JColorChooser colorChooser;
  // End of variables declaration//GEN-END:variables
  
  private Color retColor;
  
  public static Color editColor(java.awt.Window parent, Color oldColor) {
    EditColor dialog = new EditColor(parent);
    dialog.retColor = oldColor;
    dialog.colorChooser.setColor(oldColor);
    dialog.setVisible(true);  //does not return until dialog is closed
    return dialog.retColor;
  }
}
