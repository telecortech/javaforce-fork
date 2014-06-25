package javaforce.gl;

import java.util.*;

import javaforce.*;

/** <code>GLObject</code> consists of vertex points, and polygons (usually triangles).
 * All polygons share the same orientation (rotation, translation, scale).
 */

public class GLObject implements Cloneable {
  public JFArrayFloat vpl;  //vertex position list
  public JFArrayFloat tcl;  //texture coords list
  public JFArrayInt vil;  //vertex index list
  public int texidx;     //texture index (-1 = none)
  public boolean texloaded;
  public boolean visible;
//animation data
  public HashMap<Integer, GLTranslate> tl;  //move list
  public HashMap<Integer, GLRotate> rl;  //rotation list
  public HashMap<Integer, GLScale> sl;  //scale list
  public int frameIndex;
  public GLMatrix m;  //current rotation, translation, scale
  public float color[];  //RGBA (default = 1.0f,1.0f,1.0f,1.0f)
  public GLVertex org;  //origin (default = 0.0f,0.0f,0.0f)
  public int parent;
  public int maxframeCount;
  public int vpb = -1, tcb = -1, vib = -1;  //GL Buffers
  public GLObject() {
    texidx = -1;
    texloaded = false;
    frameIndex = 0;
    vpl = new JFArrayFloat();
    tcl = new JFArrayFloat();
    vil = new JFArrayInt();
    tl = new HashMap<Integer, GLTranslate>();
    rl = new HashMap<Integer, GLRotate>();
    sl = new HashMap<Integer, GLScale>();
    color = new float[4];
    for(int a=0;a<4;a++) color[a] = 1.0f;
    visible = true;
    org = new GLVertex();
    parent = -1;
    maxframeCount = 0;
    m = new GLMatrix();
  }
  public Object clone() {
    GLObject cln = new GLObject();
    cln.vpl = vpl;
    cln.tcl = tcl;
    cln.texidx = texidx;
    cln.texloaded = texloaded;
    cln.visible = visible;
    cln.tl = tl;
    cln.rl = rl;
    cln.sl = sl;
    cln.frameIndex = frameIndex;
    cln.m = (GLMatrix)m.clone();  //super.clone() would use an assignment
    cln.color = new float[4];
    for(int a=0;a<4;a++) cln.color[a] = color[a];
    cln.org = org;
    cln.parent = parent;
    cln.maxframeCount = maxframeCount;
    return cln;
  }
  public void setVisible(boolean state) {visible = state;}
  public void addRotate(float angle, float x, float y, float z, GLVertex org) {
    GLMatrix tmp = new GLMatrix();
    //rotates relative to org
    tmp.setAA(angle, x, y, z);  //set rotation
    tmp.addTranslate(org.x, org.y, org.z);  //set translation
    m.mult4x4(tmp);  //add it
    //now undo translation
    tmp.setIdentity3x3();  //remove rotation
    tmp.reverseTranslate();
    m.mult4x4(tmp);
  }
  public void addTranslate(float x, float y, float z) {
    m.addTranslate(x,y,z);
  }
  public void addScale(float x, float y, float z) {
    m.addScale(x,y,z);
  }
  public void setFrame(int idx) {  //0=init state
    GLRotate _r;
    GLTranslate _t;
    GLScale _s;
    frameIndex = idx;
    if (idx == 0) {
      m.setIdentity();
      return;
    }
    _t = tl.get(idx);
    if (_t != null) {
      addTranslate((_t.x - org.x),(_t.y - org.y),(_t.z - org.z));
    }
    _r = rl.get(idx);
    if (_r != null) {
      addRotate(_r.angle,_r.x,_r.y,_r.z,org);
    }
    _s = sl.get(idx);
    if (_s != null) {
      addScale(_s.x, _s.y, _s.z);
    }
  }
  public void nextFrame() {
    setFrame(frameIndex+1);
  }
  public int frameCount() {
    return maxframeCount;
  }
  public void addVertex(float xyz[]) {
    vpl.append(xyz);
  }
  public void addVertex(float xyz[], float txy[]) {
    vpl.append(xyz);
    tcl.append(txy);
  }
  public void addText(float txy[]) {
    tcl.append(txy);
  }
  public void addPoly(int pts[]) {
    vil.append(pts);
  }
  public void copyBuffers(GL gl) {
    int ids[] = new int[1];

    if (vpb == -1) {
      gl.glGenBuffers(1, ids);
      vpb = ids[0];
    }
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vpb);
    gl.glBufferData(GL.GL_ARRAY_BUFFER, vpl.size() * 4, vpl.toArray(), GL.GL_STATIC_DRAW);

    if (tcb == -1) {
      gl.glGenBuffers(1, ids);
      tcb = ids[0];
    }
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, tcb);
    gl.glBufferData(GL.GL_ARRAY_BUFFER, tcl.size() * 4, tcl.toArray(), GL.GL_STATIC_DRAW);

    if (vib == -1) {
      gl.glGenBuffers(1, ids);
      vib = ids[0];
    }
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vib);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, vil.size() * 4, vil.toArray(), GL.GL_STREAM_DRAW);
  }
  public void bindBuffers(GLScene scene, GL gl) {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vpb);
    gl.glVertexAttribPointer(scene.vpa, 3, GL.GL_FLOAT, GL.GL_FALSE, 0, 0);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, tcb);
    gl.glVertexAttribPointer(scene.tca, 2, GL.GL_FLOAT, GL.GL_FALSE, 0, 0);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vib);
  }
  public void render(GL gl) {
    gl.glDrawElements(GL.GL_TRIANGLES, vil.size(), GL.GL_UNSIGNED_INT, 0);
  }
};

