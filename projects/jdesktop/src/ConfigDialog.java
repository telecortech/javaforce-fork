/**
 * Created : Mar 30, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import java.io.File;
import javaforce.JF;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

public class ConfigDialog extends javax.swing.JDialog {

  /**
   * Creates new form ConfigDialog
   */
  public ConfigDialog(java.awt.Frame parent, boolean modal, Dock.Config config) {
    super(parent, modal);
    initComponents();
    autoHide.setSelected(config.autoHide);
    showClock.setSelected(config.showClock);
    showKeyboard.setSelected(config.showKeyboard);
    compact.setSelected(config.compact);
    mountAudio.setSelected(config.mountAudio);
    desktopMode.setSelectedIndex(config.desktopMode);
    desktopFile.setText(config.desktopFile);
    if (config.desktopFile.length() == 0) {
      noWallpaper.setSelected(true);
    } else {
      wallpaper.setSelected(true);
    }
    Dimension d = this.getPreferredSize();
    setLocation((Dock.sx - d.width) / 2, (Dock.sy - d.height) / 2);
    bc.setBackground(config.bc);
    fc.setBackground(config.fc);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup1 = new javax.swing.ButtonGroup();
    autoHide = new javax.swing.JCheckBox();
    accept = new javax.swing.JButton();
    cancel = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    desktopMode = new javax.swing.JComboBox();
    desktopFile = new javax.swing.JTextField();
    selectDesktopFile = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    bc = new javax.swing.JButton();
    fc = new javax.swing.JButton();
    noWallpaper = new javax.swing.JRadioButton();
    wallpaper = new javax.swing.JRadioButton();
    showClock = new javax.swing.JCheckBox();
    themes = new javax.swing.JButton();
    compact = new javax.swing.JCheckBox();
    showKeyboard = new javax.swing.JCheckBox();
    mountAudio = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Settings");
    setResizable(false);

    autoHide.setText("Auto Hide");

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

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Desktop"));

    desktopMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Centered", "Tile", "Full", "Fill" }));

    selectDesktopFile.setText("Select...");
    selectDesktopFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectDesktopFileActionPerformed(evt);
      }
    });

    jLabel1.setText("Background Color:");

    jLabel2.setText("Foreground Color:");

    bc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bcActionPerformed(evt);
      }
    });

    fc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fcActionPerformed(evt);
      }
    });

    buttonGroup1.add(noWallpaper);
    noWallpaper.setText("No Wallpaper");

    buttonGroup1.add(wallpaper);
    wallpaper.setSelected(true);
    wallpaper.setText("Wallpaper");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(wallpaper)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(desktopMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(desktopFile)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(selectDesktopFile))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fc))
              .addComponent(noWallpaper))
            .addGap(0, 160, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(noWallpaper)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(wallpaper)
          .addComponent(desktopMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(desktopFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(selectDesktopFile))
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1))
              .addComponent(fc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(bc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(14, 14, 14)
            .addComponent(jLabel2)))
        .addContainerGap(14, Short.MAX_VALUE))
    );

    showClock.setText("Show Clock");

    themes.setText("Themes...");
    themes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        themesActionPerformed(evt);
      }
    });

    compact.setText("Compact controls");

    showKeyboard.setText("Show Keyboard");

    mountAudio.setText("Auto Mount Audio Discs (jMedia)");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(themes)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cancel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(accept))
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(showKeyboard)
              .addComponent(compact)
              .addComponent(showClock)
              .addComponent(autoHide)
              .addComponent(mountAudio))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(autoHide)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(showClock)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(showKeyboard)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(compact)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(mountAudio)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(themes)
          .addComponent(cancel)
          .addComponent(accept))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
    dispose();
  }//GEN-LAST:event_cancelActionPerformed

  private void acceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptActionPerformed
    accepted = true;
    dispose();
  }//GEN-LAST:event_acceptActionPerformed

  private void selectDesktopFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDesktopFileActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    File path = new File("/usr/share/backgrounds/gnome/");
    chooser.setCurrentDirectory(path);
    if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
    String filename = chooser.getSelectedFile().getAbsolutePath();
    desktopFile.setText(filename);
    wallpaper.setSelected(true);
  }//GEN-LAST:event_selectDesktopFileActionPerformed

  private void themesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themesActionPerformed
    try {
      Runtime.getRuntime().exec("obconf");
    } catch (Exception e) {}
  }//GEN-LAST:event_themesActionPerformed

  private void bcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcActionPerformed
    Color newClr = JColorChooser.showDialog(this, "Select Background Colour", bc.getBackground());
    if (newClr == null) return;
    bc.setBackground(newClr);
  }//GEN-LAST:event_bcActionPerformed

  private void fcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fcActionPerformed
    Color newClr = JColorChooser.showDialog(this, "Select Foreground Colour", fc.getBackground());
    if (newClr == null) return;
    fc.setBackground(newClr);
  }//GEN-LAST:event_fcActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton accept;
  private javax.swing.JCheckBox autoHide;
  private javax.swing.JButton bc;
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.JButton cancel;
  private javax.swing.JCheckBox compact;
  private javax.swing.JTextField desktopFile;
  private javax.swing.JComboBox desktopMode;
  private javax.swing.JButton fc;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JCheckBox mountAudio;
  private javax.swing.JRadioButton noWallpaper;
  private javax.swing.JButton selectDesktopFile;
  private javax.swing.JCheckBox showClock;
  private javax.swing.JCheckBox showKeyboard;
  private javax.swing.JButton themes;
  private javax.swing.JRadioButton wallpaper;
  // End of variables declaration//GEN-END:variables

  public boolean accepted;

  public void updateConfig(Dock.Config config) {
    config.autoHide = autoHide.isSelected();
    config.desktopMode = desktopMode.getSelectedIndex();
    if (wallpaper.isSelected()) {
      config.desktopFile = desktopFile.getText();
    } else {
      config.desktopFile = "";
    }
    config.showClock = showClock.isSelected();
    config.showKeyboard = showKeyboard.isSelected();
    config.compact = compact.isSelected();
    config.mountAudio = mountAudio.isSelected();
    config.bc = bc.getBackground();
    config.fc = fc.getBackground();
  }
}
