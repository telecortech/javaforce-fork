package javaforce.gl;

/**
 * OpenGL rendering in a lightweight JPanel.
 * NOTE : Requires OpenGL 3.0+
 *
 * The rendering is done offscreen then paint()ed using Graphics.drawImage()
 * So performance may be less than other containers (and uses lots more memory).
 * Recommend using GLCanvas instead.
 *
 * Not tested on Mac OS X (I only have OpenGL 2.x).
 *
 * @author pquiring
 *
 * Created : Sept 18, 2013
 */

import java.awt.*;
import javax.swing.*;

public class GLJPanel extends JPanel {
  private GLInterface iface;
  private GL gl;
  public boolean init(GLInterface iface) {
    this.iface = iface;
    gl = GL.createOffscreen(SwingUtilities.getWindowAncestor(this), this, iface);
    if (gl == null) return false;
    gl.setRenderOffscreen(true);
    gl.makeCurrent();
    iface.init(gl, this);
    return true;
  }
  public void paint(Graphics g) {
    if (gl == null) return;
    gl.render();
    g.drawImage(gl.getOffscreen(), 0, 0, null);
  }
  public void update(Graphics g) {
    paint(g);
  }
  public void setSize(int w, int h) {
    super.setSize(w,h);
    if (gl != null) gl.resizeOffscreen(w,h);
    if (iface != null) iface.resize(gl, w, h);
  }
  public void setSize(Dimension d) {
    super.setSize(d);
    if (gl != null) gl.resizeOffscreen(d.width, d.height);
    if (iface != null) iface.resize(gl, d.width, d.height);
  }
  public void setBounds(int x,int y,int w,int h) {
    super.setBounds(x,y,w,h);
    if (gl != null) gl.resizeOffscreen(w,h);
    if (iface != null) iface.resize(gl, w, h);
  }
  public void setBounds(Rectangle r) {
    super.setBounds(r);
    if (gl != null) gl.resizeOffscreen(r.width, r.height);
    if (iface != null) iface.resize(gl, r.width, r.height);
  }
  public void destroy() {
    gl.destroy();
  }
  public GL getGL() {
    return gl;
  }
}
