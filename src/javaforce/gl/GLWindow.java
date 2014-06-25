package javaforce.gl;

/**
 * OpenGL rendering in a Window.
 * Do not add components to this window.  The OpenGL rendering uses the entire window.
 * I do not recommend using this, windows do not receive focus which causes issues,
 * and crashes sometimes.
 *
 * @author pquiring
 *
 * Created : Sept 18, 2013
 */

import java.awt.*;

//extending Window causes flickering
//extending JWindow works

public class GLWindow extends Window {
  private GLInterface iface;
  private GL gl;
  public GLWindow(Window w) {
    super(w);
  }
  public boolean init(GLInterface iface) {
    this.iface = iface;
    gl = GL.createWindow(this, iface);
    if (gl == null) return false;
    gl.makeCurrent();
    iface.init(gl, this);
    return true;
  }
  public void paint(Graphics g) {
    if (gl == null) return;
    gl.render();
  }
  public void update(Graphics g) {
    paint(g);
  }
  public void setSize(int w, int h) {
    super.setSize(w,h);
    if (iface != null) iface.resize(gl, w, h);
  }
  public void setSize(Dimension d) {
    super.setSize(d);
    if (iface != null) iface.resize(gl, d.width, d.height);
  }
  public void setBounds(int x,int y,int w,int h) {
    super.setBounds(x,y,w,h);
    if (iface != null) iface.resize(gl, w, h);
  }
  public void setBounds(Rectangle r) {
    super.setBounds(r);
    if (iface != null) iface.resize(gl, r.width, r.height);
  }
  public void destroy() {
    gl.destroy();
  }
  public GL getGL() {
    return gl;
  }
}
