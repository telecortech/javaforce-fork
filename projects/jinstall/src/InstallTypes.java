/**
 *
 * @author pquiring
 */

import jparted.*;
import javaforce.*;

public class InstallTypes extends IPanel {

  /**
   * Creates new form InstallTypes
   */
  public InstallTypes() {
    initComponents();
    System.out.println("InstallTypes");
    switch (Data.installType) {
      case LINUX: removeLinux.setSelected(true); break;
      case ALL: removeAll.setSelected(true); break;
      case CUSTOM: custom.setSelected(true); break;
    }
    Data.root = null;
    Data.swap = null;
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
        removeLinux = new javax.swing.JRadioButton();
        removeAll = new javax.swing.JRadioButton();
        custom = new javax.swing.JRadioButton();

        buttonGroup1.add(removeLinux);
        removeLinux.setSelected(true);
        removeLinux.setText("Remove Linux Partitions and install Linux (recommended)");

        buttonGroup1.add(removeAll);
        removeAll.setText("Remove ALL Partitions and install Linux");

        buttonGroup1.add(custom);
        custom.setText("Custom Partition and install Linux");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeLinux)
                    .addComponent(removeAll)
                    .addComponent(custom))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(removeLinux)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(custom)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton custom;
    private javax.swing.JRadioButton removeAll;
    private javax.swing.JRadioButton removeLinux;
    // End of variables declaration//GEN-END:variables

  public IPanel next() {
    Data.getDevices();
    Data.getPartitions();  //required to find uninit devices
    for(int idx=0;idx<Data.devices.size();idx++) {
      if (Data.devices.get(idx).uninit) {
        if (JF.showConfirm("Warning", "Device " + Data.devices.get(idx).dev + " needs a partition table.\rCreate it now?")) {
          Data.createPartTable(Data.devices.get(idx).dev);
          return null;
        }
      }
    }
    if (Data.devices.size() == 0) {
      JF.showError("Error", "No devices found to install Linux onto.");
      return null;
    }
    if (custom.isSelected()) {
      Data.installType = Data.installTypes.CUSTOM;
      CustomPartitioning cp = new CustomPartitioning(false);
      return cp.getThis();
    }
    if (removeLinux.isSelected()) {
      Data.installType = Data.installTypes.LINUX;
    }
    if (removeAll.isSelected()) {
      Data.installType = Data.installTypes.ALL;
    }
    if (Data.getDeviceCount() > 1) {
      return new GuidedPartSelectDevice();
    }
    Data.guidedTarget = Data.devices.get(0);
    return new GuidedPartitioning();
  }
  public IPanel prev() {return new Welcome();}
  public IPanel getThis() {return this;}
}
