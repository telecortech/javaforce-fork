package jparted;

/**
 *
 * Created : Feb 17, 2012
 *
 * @author pquiring
 */

public abstract class IPanel extends javax.swing.JPanel {
  public abstract IPanel next();
  public abstract IPanel prev();
  public abstract IPanel getThis();
}
