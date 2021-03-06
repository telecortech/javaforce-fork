import javaforce.JF;
import javaforce.gl.GL;

/**
 *
 * @author pquiring
 *
 * Created : Sept 18, 2013
 */

public class TestApp extends javax.swing.JFrame {

  /**
   * Creates new form TestApp
   */
  public TestApp() {
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jButton3 = new javax.swing.JButton();
    glcanvas = new javax.swing.JButton();
    glwindow = new javax.swing.JButton();
    glframe = new javax.swing.JButton();
    gljpanel = new javax.swing.JButton();

    jButton3.setText("jButton3");

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    glcanvas.setText("Test GLCanvas");
    glcanvas.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        glcanvasActionPerformed(evt);
      }
    });

    glwindow.setText("Test GLWindow");
    glwindow.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        glwindowActionPerformed(evt);
      }
    });

    glframe.setText("Test GLFrame");
    glframe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        glframeActionPerformed(evt);
      }
    });

    gljpanel.setText("Test GLJPanel");
    gljpanel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        gljpanelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(glwindow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(glframe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(glcanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(gljpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(glcanvas)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(glwindow)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(glframe)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(gljpanel)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void glcanvasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glcanvasActionPerformed
    dispose();
    TestGLCanvas w = new TestGLCanvas();
    w.setVisible(true);
    w.init();
  }//GEN-LAST:event_glcanvasActionPerformed

  private void glwindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glwindowActionPerformed
//    dispose();  //crashes
    TestGLWindow w = new TestGLWindow(null);
    w.setVisible(true);
    w.init();
  }//GEN-LAST:event_glwindowActionPerformed

  private void glframeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glframeActionPerformed
    dispose();
    TestGLFrame w = new TestGLFrame();
    w.setVisible(true);
    w.init();
  }//GEN-LAST:event_glframeActionPerformed

  private void gljpanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gljpanelActionPerformed
    dispose();
    TestGLJPanel w = new TestGLJPanel();
    w.setVisible(true);
    w.init();
  }//GEN-LAST:event_gljpanelActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    if (!GL.init()) {
      JF.showError("Error", "OpenGL init failed");
      System.exit(0);
    }
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new TestApp().setVisible(true);
      }
    });
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton glcanvas;
  private javax.swing.JButton glframe;
  private javax.swing.JButton gljpanel;
  private javax.swing.JButton glwindow;
  private javax.swing.JButton jButton3;
  // End of variables declaration//GEN-END:variables
}
