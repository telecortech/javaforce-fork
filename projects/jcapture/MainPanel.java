/**
 * Created : Apr 10, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import java.io.*;
import javax.swing.*;

import javaforce.*;

public class MainPanel extends javax.swing.JPanel {

  /**
   * Creates new form MainPanel
   */
  public MainPanel() {
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    save = new javax.swing.JButton();
    preview = new javax.swing.JPanel();
    edit = new javax.swing.JButton();

    save.setText("Save");
    save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveActionPerformed(evt);
      }
    });

    preview.setLayout(new java.awt.GridLayout(1, 0));

    edit.setText("Edit");
    edit.setEnabled(false);
    edit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(edit)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(save))
          .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(23, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(save)
          .addComponent(edit))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(30, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setCurrentDirectory(new File(JF.getUserPath() + "/Pictures"));
    javax.swing.filechooser.FileFilter ff_png = new javax.swing.filechooser.FileFilter() {
      public boolean accept(File file) {
        if (file.isDirectory()) return true;
        if (file.getName().endsWith(".png")) return true;
        return false;
      }
      public String getDescription() {
        return "PNG Files (*.png)";
      }
    };
    chooser.addChoosableFileFilter(ff_png);
    chooser.setFileFilter(ff_png);
    if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
    filename = chooser.getSelectedFile().getAbsolutePath();
    if (!filename.endsWith(".png")) {
      filename += ".png";
    }
    try {
      Runtime.getRuntime().exec(new String[] {"cp", png, filename});
    } catch (Exception e) {
      JF.showError("Error", "Failed to copy file");
    }
    edit.setEnabled(true);
  }//GEN-LAST:event_saveActionPerformed

  private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
    try {
      Runtime.getRuntime().exec(new String[] {"jpaint", filename});
    } catch (Exception e) {
      JF.showError("Error", "Failed to edit file");
    }
  }//GEN-LAST:event_editActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton edit;
  private javax.swing.JPanel preview;
  private javax.swing.JButton save;
  // End of variables declaration//GEN-END:variables

  private String png;
  private String filename;

  public void loadPNG(String png) {
    this.png = png;
    JFImage jfimage = new JFImage();
    jfimage.loadPNG(png);
    Dimension d = new Dimension(preview.getWidth(), preview.getHeight());
    System.out.println("preview=" + d);
    JFImage scale = new JFImage(d.width, d.height);
    scale.getGraphics().drawImage(jfimage.getImage(), 0,0, d.width-1,d.height-1, 0,0,
      jfimage.getWidth()-1,jfimage.getHeight()-1, null);
    scale.setResizeOperation(JFImage.ResizeOperation.CHOP);
    preview.add(scale);
  }
}