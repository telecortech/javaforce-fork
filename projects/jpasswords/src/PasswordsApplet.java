
import javaforce.JF;

/**
 * Created : Sept 28, 2012
 *
 * @author pquiring
 */

public class PasswordsApplet extends javax.swing.JApplet {

  /**
   * Initializes the applet PasswordsApplet
   */
  @Override
  public void init() {
    /* Create and display the applet */
    try {
      java.awt.EventQueue.invokeAndWait(new Runnable() {
        public void run() {
          setContentPane(new MainPanel());
          JF.loadCerts(getClass().getResourceAsStream("javaforce.crt")
            , getClass().getResourceAsStream("jpasswords.crt"), "jfpasswords.sourceforge.net");
        }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    JF.loadCerts(getClass().getResourceAsStream("javaforce.crt")
      , getClass().getResourceAsStream("jpasswords.crt"), "jfpasswords.sourceforge.net");
  }

  public void destroy() {
    System.exit(0);
  }

  /**
   * This method is called from within the init() method to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    getContentPane().setLayout(null);
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
