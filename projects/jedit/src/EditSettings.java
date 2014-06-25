/** Dialog to edit settings.
 *
 * Created on September 20, 2007, 7:17 PM
 * 
 * @author  pquiring
 */


public class EditSettings extends javax.swing.JDialog {

  /** Creates new form EditSettings */
  private EditSettings(java.awt.Frame parent, boolean modal, Settings settings) {
    super(parent, modal);
    this.settings = settings;
    initComponents();
    setTitle("Edit Settings");
    setComponentOrientation(((parent == null) ? javax.swing.JOptionPane.getRootFrame() : parent).getComponentOrientation());
    if (parent != null) setLocationRelativeTo(parent);
    load();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    bOk = new javax.swing.JButton();
    bCancel = new javax.swing.JButton();
    sFontSize = new javax.swing.JSpinner();
    jLabel1 = new javax.swing.JLabel();
    cbUnix = new javax.swing.JCheckBox();
    cbClean = new javax.swing.JCheckBox();
    jLabel2 = new javax.swing.JLabel();
    sTabSize = new javax.swing.JSpinner();
    cbAutoIndent = new javax.swing.JCheckBox();
    cbLineWrap = new javax.swing.JCheckBox();

    FormListener formListener = new FormListener();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
    addKeyListener(formListener);

    bOk.setMnemonic('o');
    bOk.setText("OK");
    bOk.addActionListener(formListener);
    bOk.addKeyListener(formListener);

    bCancel.setMnemonic('c');
    bCancel.setText("Cancel");
    bCancel.addActionListener(formListener);
    bCancel.addKeyListener(formListener);

    sFontSize.addChangeListener(formListener);
    sFontSize.addKeyListener(formListener);

    jLabel1.setText("Font Size");

    cbUnix.setText("Save in Unix format (no CR)");
    cbUnix.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    cbUnix.setMargin(new java.awt.Insets(0, 0, 0, 0));
    cbUnix.addKeyListener(formListener);

    cbClean.setText("Trim trailing spaces when saving");
    cbClean.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    cbClean.setMargin(new java.awt.Insets(0, 0, 0, 0));
    cbClean.addKeyListener(formListener);

    jLabel2.setText("Tab Size");

    sTabSize.addChangeListener(formListener);
    sTabSize.addKeyListener(formListener);

    cbAutoIndent.setText("Auto indent");
    cbAutoIndent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    cbAutoIndent.setMargin(new java.awt.Insets(0, 0, 0, 0));

    cbLineWrap.setText("Line wrap");
    cbLineWrap.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    cbLineWrap.setMargin(new java.awt.Insets(0, 0, 0, 0));
    cbLineWrap.addKeyListener(formListener);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(bOk)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bCancel))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel1)
              .addComponent(jLabel2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(sTabSize)
              .addComponent(sFontSize, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
          .addComponent(cbClean)
          .addComponent(cbUnix)
          .addComponent(cbAutoIndent)
          .addComponent(cbLineWrap))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(sFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(sTabSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cbUnix)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cbClean)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cbAutoIndent)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cbLineWrap)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bOk)
          .addComponent(bCancel))
        .addContainerGap())
    );

    pack();
  }

  // Code for dispatching events from components to event handlers.

  private class FormListener implements java.awt.event.ActionListener, java.awt.event.KeyListener, javax.swing.event.ChangeListener {
    FormListener() {}
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      if (evt.getSource() == bOk) {
        EditSettings.this.bOkActionPerformed(evt);
      }
      else if (evt.getSource() == bCancel) {
        EditSettings.this.bCancelActionPerformed(evt);
      }
    }

    public void keyPressed(java.awt.event.KeyEvent evt) {
      if (evt.getSource() == EditSettings.this) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == bOk) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == bCancel) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == sFontSize) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == cbUnix) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == cbClean) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == sTabSize) {
        EditSettings.this.anykey(evt);
      }
      else if (evt.getSource() == cbLineWrap) {
        EditSettings.this.anykey(evt);
      }
    }

    public void keyReleased(java.awt.event.KeyEvent evt) {
    }

    public void keyTyped(java.awt.event.KeyEvent evt) {
    }

    public void stateChanged(javax.swing.event.ChangeEvent evt) {
      if (evt.getSource() == sFontSize) {
        EditSettings.this.sFontSizeStateChanged(evt);
      }
      else if (evt.getSource() == sTabSize) {
        EditSettings.this.sTabSizeStateChanged(evt);
      }
    }
  }// </editor-fold>//GEN-END:initComponents

  private void anykey(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anykey
    int keyCode = evt.getKeyCode();
    int mods = evt.getModifiers();
    if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE && mods == 0) {
      bCancelActionPerformed(null);
    }
    if (keyCode == java.awt.event.KeyEvent.VK_ENTER && mods == 0) {
      bOkActionPerformed(null);
    }
  }//GEN-LAST:event_anykey

  private void sTabSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sTabSizeStateChanged
    int tmp = ((Integer)sTabSize.getValue()).intValue();
    if (tmp < MIN_TABSIZE) sTabSize.setValue(MIN_TABSIZE);
    if (tmp > MAX_TABSIZE) sTabSize.setValue(MAX_TABSIZE);
  }//GEN-LAST:event_sTabSizeStateChanged

  private void sFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sFontSizeStateChanged
    int tmp = ((Integer)sFontSize.getValue()).intValue();
    if (tmp < MIN_FONTSIZE) sFontSize.setValue(MIN_FONTSIZE);
    if (tmp > MAX_FONTSIZE) sFontSize.setValue(MAX_FONTSIZE);
  }//GEN-LAST:event_sFontSizeStateChanged

  private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
    dispose();
  }//GEN-LAST:event_bCancelActionPerformed

  private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
    save();
    dispose();
  }//GEN-LAST:event_bOkActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bCancel;
  private javax.swing.JButton bOk;
  private javax.swing.JCheckBox cbAutoIndent;
  private javax.swing.JCheckBox cbClean;
  private javax.swing.JCheckBox cbLineWrap;
  private javax.swing.JCheckBox cbUnix;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JSpinner sFontSize;
  private javax.swing.JSpinner sTabSize;
  // End of variables declaration//GEN-END:variables

  private Settings settings;
  private static final int MIN_FONTSIZE = 8;
  private static final int MAX_FONTSIZE = 24;
  private static final int MIN_TABSIZE = 2;
  private static final int MAX_TABSIZE = 8;

  public static void editSettings(java.awt.Frame parent, Settings settings) {
    EditSettings dialog = new EditSettings(parent, true, settings);
    dialog.setVisible(true);
  }

  private void load() {
    sFontSize.setValue(settings.fontSize);
    cbUnix.setSelected(settings.bUnix);
    cbClean.setSelected(settings.bClean);
    sTabSize.setValue(settings.tabSize);
    cbAutoIndent.setSelected(settings.bAutoIndent);
    cbLineWrap.setSelected(settings.bLineWrap);
  }

  private void save() {
    settings.fontSize = ((Integer)sFontSize.getValue()).intValue();
    settings.bUnix = cbUnix.isSelected();
    settings.bClean = cbClean.isSelected();
    settings.tabSize = ((Integer)sTabSize.getValue()).intValue();
    settings.bAutoIndent = cbAutoIndent.isSelected();
    settings.bLineWrap = cbLineWrap.isSelected();
  }

}