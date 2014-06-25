package jfile;

/*
 * SiteMgr.java
 *
 * Created on July 31, 2007, 8:38 AM
 *
 * @author  pquiring
 */

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import javaforce.*;

public class SiteMgr extends javax.swing.JDialog {

  public SiteMgr(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    setComponentOrientation(((parent == null) ? javax.swing.JOptionPane.getRootFrame() : parent).getComponentOrientation());
    if (parent != null) setLocationRelativeTo(parent);
    clearFields();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    bConnect = new javax.swing.JButton();
    bSave = new javax.swing.JButton();
    bDelete = new javax.swing.JButton();
    bNewSite = new javax.swing.JButton();
    settings = new javax.swing.JPanel();
    lHost = new javax.swing.JLabel();
    tHost = new javax.swing.JTextField();
    lProtocol = new javax.swing.JLabel();
    cbProtocol = new javax.swing.JComboBox();
    lPort = new javax.swing.JLabel();
    tPort = new javax.swing.JTextField();
    lUsername = new javax.swing.JLabel();
    tUsername = new javax.swing.JTextField();
    lPassword = new javax.swing.JLabel();
    tPassword = new javax.swing.JPasswordField();
    lName = new javax.swing.JLabel();
    tName = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    localDir = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    remoteDir = new javax.swing.JTextField();
    listScroll = new javax.swing.JScrollPane();
    tree = new javax.swing.JTree();
    bNewFolder = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("Site Manager");
    setResizable(false);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    bConnect.setText("Connect");
    bConnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bConnectActionPerformed(evt);
      }
    });

    bSave.setText("Save");
    bSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bSaveActionPerformed(evt);
      }
    });

    bDelete.setText("Delete");
    bDelete.setEnabled(false);
    bDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bDeleteActionPerformed(evt);
      }
    });

    bNewSite.setText("New Site");
    bNewSite.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bNewSiteActionPerformed(evt);
      }
    });

    settings.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

    lHost.setText("Host");

    tHost.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        anykey(evt);
      }
    });

    lProtocol.setText("Protocol");

    cbProtocol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FTP (21)", "FTPS (990)", "SFTP (22)", "SMB (445)" }));
    cbProtocol.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbProtocolItemStateChanged(evt);
      }
    });

    lPort.setText("Port");

    tPort.setText("23");
    tPort.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        anykey(evt);
      }
    });

    lUsername.setText("Username");

    tUsername.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        anykey(evt);
      }
    });

    lPassword.setText("Password");

    tPassword.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        anykey(evt);
      }
    });

    lName.setText("Name");

    tName.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        anykey(evt);
      }
    });

    jLabel1.setText("Init Local Folder");

    jLabel2.setText("Init Remote Folder");

    javax.swing.GroupLayout settingsLayout = new javax.swing.GroupLayout(settings);
    settings.setLayout(settingsLayout);
    settingsLayout.setHorizontalGroup(
      settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(tPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
          .addComponent(lName, javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(lHost, javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, settingsLayout.createSequentialGroup()
            .addComponent(lProtocol)
            .addGap(46, 46, 46)
            .addComponent(lPort)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE))
          .addComponent(lUsername, javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(lPassword, javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(remoteDir, javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(localDir, javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tUsername, javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, settingsLayout.createSequentialGroup()
              .addComponent(cbProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(tPort))
            .addComponent(tHost, javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)))
        .addContainerGap())
    );
    settingsLayout.setVerticalGroup(
      settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(settingsLayout.createSequentialGroup()
        .addComponent(lName)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lHost)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lProtocol)
          .addComponent(lPort))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cbProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(tPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lUsername)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lPassword)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(localDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(remoteDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(53, Short.MAX_VALUE))
    );

    tree.setDragEnabled(true);
    tree.setEditable(true);
    tree.setModel(xml.getTreeModel());
    tree.setShowsRootHandles(true);
    tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        treeValueChanged(evt);
      }
    });
    listScroll.setViewportView(tree);

    bNewFolder.setText("New Folder");
    bNewFolder.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bNewFolderActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(listScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(bConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bNewFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bNewSite, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(settings, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
            .addComponent(bConnect)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bSave)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bDelete)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bNewSite)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(bNewFolder))
          .addComponent(listScroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void anykey(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anykey
    if (evt.getKeyChar() == '\"') evt.consume();
    if (evt.getKeyChar() == '\'') evt.consume();
  }//GEN-LAST:event_anykey

  private void bNewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNewFolderActionPerformed
    String name = JOptionPane.showInputDialog(this, null, "Enter Folder Name",
      JOptionPane.QUESTION_MESSAGE);
    if (name == null) return;
    if (!validField(name)) return;
    XML.XMLTag parent = selectedTag;
    if (parent == null) parent = sitesTag;
    if (parent.name.equalsIgnoreCase("site")) parent = parent.getParent();
    xml.addTag(parent, "folder", " name=\"" + name + "\"", "").isNotLeaf = true;
  }//GEN-LAST:event_bNewFolderActionPerformed

  private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeValueChanged
    XML.XMLTag tag = getSelectedTag(), child;
    if (tag == null) return;
    clearFields();
    selectedTag = tag;
    if (tag.isLeaf) {
      bDelete.setEnabled(true);
    } else {
      //must be folder
      if (selectedTag.getChildCount() == 0) bDelete.setEnabled(true);
      return;
    }
    isNew = false;
    tName.setText(tag.getName());
    for(int a=0;a<tag.getChildCount();a++) {
      child = tag.getChildAt(a);
      if (child.name.equalsIgnoreCase("host")) tHost.setText(child.content);
      if (child.name.equalsIgnoreCase("protocol")) {
        if (child.content.equalsIgnoreCase("ftp")) cbProtocol.setSelectedIndex(0);
        if (child.content.equalsIgnoreCase("ftps")) cbProtocol.setSelectedIndex(1);
        if (child.content.equalsIgnoreCase("sftp")) cbProtocol.setSelectedIndex(2);
        if (child.content.equalsIgnoreCase("smb")) cbProtocol.setSelectedIndex(3);
      }
      if (child.name.equalsIgnoreCase("port")) tPort.setText(child.content);
      if (child.name.equalsIgnoreCase("username")) tUsername.setText(child.content);
      if (child.name.equalsIgnoreCase("password")) tPassword.setText(decodePassword(child.content));
      if (child.name.equalsIgnoreCase("localDir")) localDir.setText(child.content);
      if (child.name.equalsIgnoreCase("remoteDir")) remoteDir.setText(child.content);
    }
    tName.setEditable(false);
  }//GEN-LAST:event_treeValueChanged

  private void cbProtocolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbProtocolItemStateChanged
    tPort.setText(ports[cbProtocol.getSelectedIndex()]);
  }//GEN-LAST:event_cbProtocolItemStateChanged

  private void bDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDeleteActionPerformed
    if (selectedTag == null) return;
    if (selectedTag.getParent() == null) return;
    xml.deleteTag(selectedTag);
    clearFields();
  }//GEN-LAST:event_bDeleteActionPerformed

  private void bNewSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNewSiteActionPerformed
    clearFields();
    tree.clearSelection();
  }//GEN-LAST:event_bNewSiteActionPerformed

  private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
    XML.XMLTag parent;
    if (!validFields()) return;
    if (isNew) {
      //save as new connection
      parent = getSelectedTag();
      if (parent == null) parent = sitesTag;
      if (parent.isLeaf) parent = parent.getParent();
      selectedTag = xml.addTag(parent, tName.getText(), "", "");
      selectedTag.isLeaf = true;
      isNew = false;
      tName.setEditable(false);
      bDelete.setEnabled(true);
    }  else {
      //save existing connection to selectedTag
      xml.setTag(selectedTag, tName.getText(), "", "");
    }
    xml.addSetTag(selectedTag, "name", "", tName.getText());
    xml.addSetTag(selectedTag, "host", "", tHost.getText());
    xml.addSetTag(selectedTag, "protocol", "", protocols[cbProtocol.getSelectedIndex()]);
    xml.addSetTag(selectedTag, "port", "", tPort.getText());
    xml.addSetTag(selectedTag, "username", "", tUsername.getText());
    xml.addSetTag(selectedTag, "password", "", encodePassword(new String(tPassword.getPassword())));
    xml.addSetTag(selectedTag, "localDir", "", localDir.getText());
    xml.addSetTag(selectedTag, "remoteDir", "", remoteDir.getText());
    show(selectedTag);
  }//GEN-LAST:event_bSaveActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    saveAll();
    dispose();
  }//GEN-LAST:event_formWindowClosing

  private void bConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bConnectActionPerformed
    XML.XMLTag child, child2;
    if ((selectedTag != null) && (selectedTag.name.equals("folder"))) {
      //return all sub-children
      int cnt = 0;
      for(int a=0;a<selectedTag.getChildCount();a++) {
        if (selectedTag.getChildAt(a).name.equals("site")) cnt++;
      }
      if (cnt == 0) return;
      retValue = new SiteDetails[cnt];
      cnt = 0;
      for(int a=0;a<selectedTag.getChildCount();a++) {
        child = selectedTag.getChildAt(a);
        if (child.name.equals("site")) {
          retValue[cnt] = new SiteDetails();
          retValue[cnt].name = child.getName();
          for(int b=0;b<child.getChildCount();b++) {
            child2 = child.getChildAt(b);
            if (child2.name.equals("host")) retValue[cnt].host = child2.content;
            if (child2.name.equals("protocol")) retValue[cnt].protocol = child2.content;
            if (child2.name.equals("port")) retValue[cnt].port = child2.content;
            if (child2.name.equals("username")) retValue[cnt].username = child2.content;
            if (child2.name.equals("password")) retValue[cnt].password = decodePassword(child2.content);
            if (child2.name.equals("localDir")) retValue[cnt].localDir = child2.content;
            if (child2.name.equals("remoteDir")) retValue[cnt].remoteDir = child2.content;
          }
          cnt++;
        }
      }
      setVisible(false);
      return;
    }
    if (!validFields()) return;
    bSaveActionPerformed(null);
    saveAll();
    retValue = new SiteDetails[1];
    retValue[0] = new SiteDetails();
    retValue[0].name = tName.getText();
    retValue[0].host = tHost.getText();
    retValue[0].protocol = protocols[cbProtocol.getSelectedIndex()];
    retValue[0].port = tPort.getText();
    retValue[0].username = tUsername.getText();
    retValue[0].password = new String(tPassword.getPassword());
    retValue[0].localDir = localDir.getText();
    retValue[0].remoteDir = remoteDir.getText();
    setVisible(false);
  }//GEN-LAST:event_bConnectActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bConnect;
  private javax.swing.JButton bDelete;
  private javax.swing.JButton bNewFolder;
  private javax.swing.JButton bNewSite;
  private javax.swing.JButton bSave;
  private javax.swing.JComboBox cbProtocol;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel lHost;
  private javax.swing.JLabel lName;
  private javax.swing.JLabel lPassword;
  private javax.swing.JLabel lPort;
  private javax.swing.JLabel lProtocol;
  private javax.swing.JLabel lUsername;
  private javax.swing.JScrollPane listScroll;
  private javax.swing.JTextField localDir;
  private javax.swing.JTextField remoteDir;
  private javax.swing.JPanel settings;
  private javax.swing.JTextField tHost;
  private javax.swing.JTextField tName;
  private javax.swing.JPasswordField tPassword;
  private javax.swing.JTextField tPort;
  private javax.swing.JTextField tUsername;
  private javax.swing.JTree tree;
  // End of variables declaration//GEN-END:variables

  private boolean isNew = true;
  private String protocols[] = { "ftp", "ftps", "sftp", "smb" };
  private String ports[] = { "21", "990", "22", "445" };
  private XML xml = new XML();
  private XML.XMLTag selectedTag = null;
  private SiteDetails retValue[] = null;
  private XML.XMLTag sitesTag = null;
  public static SiteDetails[] showSiteMgr(Frame parent) {
    SiteMgr mgr = new SiteMgr(parent, true);
    mgr.loadAll();
    mgr.setVisible(true);
    return mgr.retValue;
  }
  private void show(XML.XMLTag tag) {
    tree.makeVisible(new TreePath(tag.getPath()));  //ensure everything is visible
//    if (tag.isLeaf) return;
    for(int a=0;a<tag.getChildCount();a++) show(tag.getChildAt(a));
  }
  private void loadSite(XML.XMLTag tag, Settings.Site site) {
    XML.XMLTag child = xml.addTag(tag, site.name, "", "");
    child.isLeaf = true;
    show(child);
    xml.addTag(child, "host", "", site.host);
    xml.addTag(child, "protocol", "", site.protocol);
    xml.addTag(child, "port", "", site.port);
    xml.addTag(child, "username", "", site.username);
    xml.addTag(child, "password", "", decodePassword(site.password));
    xml.addTag(child, "localDir", "", site.localDir);
    xml.addTag(child, "remoteDir", "", site.remoteDir);
  }
  private void loadFolder(XML.XMLTag tag, Settings.Folder folder) {
    show(tag);
    tag.setName(folder.name);
    for(int a=0;a<folder.site.length;a++) {
      loadSite(tag, folder.site[a]);
    }
    for(int a=0;a<folder.folder.length;a++) {
      XML.XMLTag child = xml.addTag(tag, folder.folder[a].name, "", "");
      loadFolder(child, folder.folder[a]);
    }
  }
  private void loadAll() {
    sitesTag = xml.root;
    loadFolder(xml.root, Settings.settings.sites);
  }
  private void saveSite(XML.XMLTag tag, Settings.Site site) {
    site.name = tag.getXMLName();
    for(int b=0;b<tag.getChildCount();b++) {
      XML.XMLTag child = tag.getChildAt(b);
      if (child.name.equals("host")) site.host = child.content;
      if (child.name.equals("protocol")) site.protocol = child.content;
      if (child.name.equals("port")) site.port = child.content;
      if (child.name.equals("username")) site.username = child.content;
      if (child.name.equals("password")) site.password = encodePassword(child.content);
      if (child.name.equals("localDir")) site.localDir = child.content;
      if (child.name.equals("remoteDir")) site.remoteDir = child.content;
    }
  }
  private void saveFolder(XML.XMLTag tag, Settings.Folder folder) {
    folder.name = tag.getName();
    for(int a=0;a<tag.getChildCount();a++) {
      XML.XMLTag child = tag.getChildAt(a);
      if (child.isLeaf) {
        folder.site = Arrays.copyOf(folder.site, folder.site.length+1);
        folder.site[folder.site.length-1] = new Settings.Site();
        saveSite(child, folder.site[folder.site.length-1]);
      } else {
        folder.folder = Arrays.copyOf(folder.folder, folder.folder.length+1);
        folder.folder[folder.folder.length-1] = new Settings.Folder();
        saveFolder(child, folder.folder[folder.folder.length-1]);
      }
    }
  }
  private void saveAll() {
    Settings.settings.sites = new Settings.Folder();
    saveFolder(xml.root, Settings.settings.sites);
    Settings.saveSettings();
  }
  private boolean validField(String str) {
    if (str.indexOf('\"') != -1) return false;
    if (str.indexOf('\'') != -1) return false;
    return true;
  }
  private boolean validFieldNumber(String str) {
    if (!validField(str)) return false;
    for(int a=0;a<str.length();a++) if ((str.charAt(a) < '0') || (str.charAt(a) > '9')) return false;
    return true;
  }
  private boolean validFields() {
    if (!validField(tName.getText())) return false;
    if (tName.getText().length() == 0) return false;
    if (!validField(tHost.getText())) return false;
    if (tHost.getText().length() == 0) return false;
    if (!validFieldNumber(tPort.getText())) return false;
    if (tPort.getText().length() == 0) return false;
    if (!validField(tUsername.getText())) return false;
    if (!validField(new String(tPassword.getPassword()))) return false;
    return true;
  }
  public static String encodePassword(String in) {
    char ch[] = in.toCharArray();
    char xor[] = "jfftp".toCharArray();
    char out[] = new char[ch.length * 2];
    int xorpos = 0;
    for(int a=0;a<ch.length;a++) {
      ch[a] ^= xor[xorpos++];
      if (xorpos == xor.length) xorpos = 0;
      out[a * 2 + 0] = (char)((ch[a] & 0x0f) + 'a');
      out[a * 2 + 1] = (char)(((ch[a] & 0xf0) >> 4) + 'b');
    }
    return new String(out);
  }
  public static String decodePassword(String in) {
    char ch[] = in.toCharArray();
    if (ch.length % 2 == 1) return "";
    char xor[] = "jfftp".toCharArray();
    char out[] = new char[ch.length / 2];
    int xorpos = 0;
    for(int a=0;a<out.length;a++) {
      if ((ch[a * 2 + 0] < 'a') || (ch[a * 2 + 0] > 'p')) return "";
      if ((ch[a * 2 + 1] < 'b') || (ch[a * 2 + 1] > 'q')) return "";
      out[a] = (char)((ch[a * 2 + 0] - 'a') + ((ch[a * 2 + 1] - 'b') << 4));
      out[a] ^= xor[xorpos++];
      if (xorpos == xor.length) xorpos = 0;
    }
    return new String(out);
  }
  private XML.XMLTag getSelectedTag() {
    TreePath path = tree.getSelectionPath();
    if (path == null) return null;
    return xml.getTag(path);
  }
  private void clearFields() {
    tName.setText("");
    tHost.setText("");
    cbProtocol.setSelectedIndex(0);
    tPort.setText("21");
    tUsername.setText("");
    tPassword.setText("");
    bDelete.setEnabled(false);
    isNew = true;
    tName.setEditable(true);
    bDelete.setEnabled(false);
    localDir.setText("");
    remoteDir.setText("");
    selectedTag = null;
  }
}
