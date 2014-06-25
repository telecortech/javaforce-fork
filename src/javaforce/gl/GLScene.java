package javaforce.gl;

import java.util.*;

import javaforce.*;

/** GLScene is a primitive 3D framework.
 *
 * - holds all GLModel's and can then render them.
 *
 */

public class GLScene {
  private static final boolean DEBUG = false;

  private int iwx, iwy; //window size (int)
  private float dwx, dwy; //window size (float)
  private float ratio;  //dwx / dwy
  private boolean needinittex = true;

  private ArrayList<GLModel> ml;
  private HashMap<Integer, GLTexture> tl; //texture list
  private HashMap<Integer, GLModel> mtl; //model templates list

  private ArrayList<Integer> freeglidlist;

  private GLTexture blankTexture;

  public GLScene() {
    freeglidlist = new ArrayList<Integer>();
    reset();
    texturePath = "";
    blankTexture = new GLTexture();
    blankTexture.set(new int[] {-1},1,1);  //white pixel
    modelPath = "";
  }

  public String texturePath;
  public GLMatrix m_camera;  //camera matrix (rotation/translation)
  public GLMatrix m_model;  //model matrix (translation only)

  public int fragShader, vertexShader, program;
  public int vpa, tca;  //attribs
  public int mpu, mmu, mvu;  //uniform matrix'es (perspective, model, view)

//code
  public void init(GL gl, int x, int y, String vertex, String fragment) {  //must give size of render window
    gl.glFrontFace(GL.GL_CCW);  //3DS uses GL_CW
    gl.glEnable(GL.GL_CULL_FACE);  //don't draw back sides
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LEQUAL);
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glActiveTexture(GL.GL_TEXTURE0);

    vertexShader = gl.glCreateShader(GL.GL_VERTEX_SHADER);
    String vertexLines[] = vertex.split("\n");
    gl.glShaderSource(vertexShader, vertexLines.length, vertexLines, null);
    gl.glCompileShader(vertexShader);
    JFLog.log("vertex log=" + gl.glGetShaderInfoLog(vertexShader));

    fragShader = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
    String fragmentLines[] = fragment.split("\n");
    gl.glShaderSource(fragShader, fragmentLines.length, fragmentLines, null);
    gl.glCompileShader(fragShader);
    JFLog.log("fragment log=" + gl.glGetShaderInfoLog(fragShader));

    program = gl.glCreateProgram();
    gl.glAttachShader(program, vertexShader);
    gl.glAttachShader(program, fragShader);
    gl.glLinkProgram(program);
    JFLog.log("program log=" + gl.glGetProgramInfoLog(program));
    gl.glUseProgram(program);

    tca = gl.glGetAttribLocation(program, "aTextureCoord");
    gl.glEnableVertexAttribArray(tca);
    vpa = gl.glGetAttribLocation(program, "aVertexPosition");
    gl.glEnableVertexAttribArray(vpa);

    mpu = gl.glGetUniformLocation(program, "uPMatrix");
    mmu = gl.glGetUniformLocation(program, "uMMatrix");
    mvu = gl.glGetUniformLocation(program, "uVMatrix");

//    JFLog.log("vpa=" + vpa + ",tca=" + tca + ",mpu=" + mpu + ",mmu=" + mmu + ",mvu=" + mvu + ",program=" + program);

    initTextures(gl);

    resize(x,y);
  }
  public void resize(int x, int y) {
    iwx = x;
    iwy = y;
    dwx = (float)x;
    dwy = (float)y;
    ratio = dwx/dwy;
  }
  /** Creates a fog in the scene.<br>
   * Note that when using fog the camera and model matrix'es must not include both a rotation and translation, <br>
   * instead rotate the camera and translate the model only.<br>
   */
  public void reset() {
    if (tl != null) releaseTextures();
    ml = new ArrayList<GLModel>();
    tl = new HashMap<Integer, GLTexture>();
    mtl = new HashMap<Integer, GLModel>();
    m_camera = new GLMatrix();
    m_model = new GLMatrix();
  }
  private void releaseTextures() {
    Set keyset = tl.keySet();
    Iterator iter = keyset.iterator();
    int texidx;
    GLTexture tex;
    while (iter.hasNext()) {
      texidx = (Integer)iter.next();
      tex = tl.get(texidx);
      if (tex.glid != -1) {
        releaseTexture(tex.glid);
        tex.glid = -1;
      }
    }
  }
  private void releaseTexture(int glid) {
    freeglidlist.add(glid);
  }
  public void cameraReset() {
    m_camera.setIdentity();
  }
  public void cameraSet(float angle, float ax, float ay, float az, float tx, float ty, float tz) {
    m_camera.setAATranslate(angle, ax, ay, az, tx, ty, tz);
  }
  public void cameraRotate(float angle, float ax, float ay, float az) {
    m_camera.addRotate(angle, ax, ay, az);
  }
  public void cameraTranslate(float tx, float ty, float tz) {
    m_camera.addTranslate(tx, ty, tz);
  }
  public void modelReset() {
    m_model.setIdentity();
  }
/*
  public void modelSet(float angle, float rx, float ry, float rz, float tx, float ty, float tz) {
    m_model.setAATranslate(angle, rx, ry, rz, tx, ty, tz);
  }
  public void modelRotate(float angle, float ax, float ay, float az) {
    m_model.addRotate(angle, ax, ay, az);
  }
*/
  public void modelTranslate(float tx, float ty, float tz) {
    m_model.addTranslate(tx, ty, tz);
  }
//load textures from disk to general-purpose memory
  public boolean loadTextures() {
    //scan thru object list and load them all
    int size1 = ml.size();
    GLObject obj;
    for(int a=0;a<size1;a++) {
      int size2 = ml.get(a).ol.size();
      for(int b=0;b<size2;b++) {
        obj = ml.get(a).ol.get(b);
        if (obj.texloaded) continue;
        if (!loadTexture(ml.get(a).ol.get(b).texidx)) return false;
        obj.texloaded = true;
      }
    }
    return true;
  }
  private boolean loadTexture(int texidx) {
    String fn;
    GLTexture tex;

    if (texidx == -1) return true;
    if (tl.get(texidx) != null) {
      tl.get(texidx).refcnt++;
      return true;
    }
    needinittex = true;
    fn = texturePath + String.format("%08x.png", texidx);
    tex = new GLTexture();
    if (!tex.load(fn)) return false;
    tex.refcnt = 1;
    tl.put(texidx, tex);
    return true;
  }
//directly load a texture
  public boolean setTexture(int texidx, int px[], int w, int h) {
    GLTexture tex = tl.get(texidx);
    if (tex == null) {
      tex = new GLTexture();
      tl.put(texidx, tex);
    } else {
      tex.needload = true;
    }
    tex.set(px, w, h);
    needinittex = true;
    return false;
  }
//load textures into video memory (texture objects)
  private boolean initTextures(GL gl) {
    //setup blankTexture
    if (blankTexture.glid == -1) initTexture(gl, blankTexture);
    //first uninit any that have been deleted
    if (freeglidlist.size() > 0) uninitTextures(gl);
    //scan thru object list and load them all
    Set keyset = tl.keySet();
    Iterator iter = keyset.iterator();
    int texidx;
    while (iter.hasNext()) {
      texidx = (Integer)iter.next();
      if (!initTexture(gl, tl.get(texidx))) return false;
    }
    return true;
  }
  private boolean mipmaps = false;
  private boolean initTexture(GL gl, GLTexture tex) {
    if (tex.glid == -1) {
      int id[] = new int[1];
      id[0] = -1;
      gl.glGenTextures(1, id);
      if (id[0] == -1) {
        JFLog.log("glGenTextures failed:Error=0x" + Integer.toString(gl.glGetError(), 16));
        return false;
      }
      tex.glid = id[0];
    }
    if (!tex.needload) return true;
    gl.glBindTexture(GL.GL_TEXTURE_2D, tex.glid);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
    if (mipmaps) {
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST_MIPMAP_NEAREST);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST_MIPMAP_NEAREST);
    } else {
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
    }
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, tex.bitmap.getWidth(), tex.bitmap.getHeight(), 0, GL.GL_BGRA
      , GL.GL_UNSIGNED_BYTE, tex.bitmap.getPixels());
    return true;
  }
  private boolean uninitTextures(GL gl) {
    while (freeglidlist.size() > 0) {
      uninitTexture(gl, freeglidlist.get(0));
      freeglidlist.remove(0);
    }
    return true;
  }
  private boolean uninitTexture(GL gl, int glid) {
    int id[] = new int[1];
    id[0] = glid;
    gl.glDeleteTextures(1, id, 0);
    return true;
  }
  public void releaseUnusedTextures() {
    Set keyset = tl.keySet();
    Iterator iter = keyset.iterator();
    int texidx;
    while (iter.hasNext()) {
      texidx = (Integer)iter.next();
      if (tl.get(texidx).refcnt == 0) releaseTexture(tl.get(texidx).glid);
    }
  }
  public void releaseModel(int idx) {
    GLModel mod;
    GLObject obj;
    mod = ml.get(idx);
    int size = mod.ol.size();
    for(int a=0;a<size;a++) {
      obj = mod.ol.get(a);
      tl.get(obj.texidx).refcnt--;
    }
    ml.remove(idx);
  }
  public int modelCount() { return ml.size(); }
  public boolean addModel(GLModel mod) { return addModel(mod, 0); }  //places Model at start of list
  public boolean addModel(GLModel mod, int idx) { if (mod == null) return false; ml.add(idx, mod); return true;}  //place Models with transparent textures last
  public int indexOfModel(GLModel mod) { return ml.indexOf(mod); }
  public void removeModel(int idx) { ml.remove(idx); }
  public void removeModel(GLModel mod) { ml.remove(mod); }
  public void nextFrame(int objidx) { ml.get(objidx).nextFrame(); }
  public void setFrame(int objidx, int frame) { ml.get(objidx).setFrame(frame); }
  public void modelTranslate(int idx, float x, float y, float z) { ml.get(idx).translate(x,y,z); }
  public void modelRotate(int idx, float angle, float x, float y, float z) { ml.get(idx).rotate(angle,x,y,z); }
  public void modelScale(int idx, float x, float y, float z) { ml.get(idx).scale(x,y,z); }
  public float fovy = 60.0f;
  public float zNear = 0.1f;  //Do NOT use zero!!!
  public float zFar = 10000.0f;
  public void render(GL gl) {
    if (needinittex) {
      initTextures(gl);
      needinittex = false;
    }
    GLModel mod;
    GLObject obj;
    GLMatrix mat = new GLMatrix();
    //setup camera view
    gl.glViewport(0, 0, iwx, iwy);
    //setup model view
    //setup background clr
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
    //render models
    int size_ml = ml.size();
    mat.setIdentity();
    mat.perspective(fovy, ratio, zNear, zFar);
    gl.glUniformMatrix4fv(mpu, 1, GL.GL_FALSE, mat.m);  //perspective matrix
    for(int a=0;a<size_ml;a++) {
      mod = ml.get(a);
      if (!mod.visible) continue;
      mat.setIdentity();
      mat.mult4x4(m_camera);
      gl.glUniformMatrix4fv(mvu, 1, GL.GL_FALSE, mat.m);  //view matrix
      int size_ol = mod.ol.size();
      for(int b=0;b<size_ol;b++) {
        obj = mod.ol.get(b);
        if (!obj.visible) continue;
        mat.setIdentity();
        mat.mult4x4(m_model);
        mat.mult4x4(mod.m);
        mat.mult4x4(obj.m);
        gl.glUniformMatrix4fv(mmu, 1, GL.GL_FALSE, mat.m);  //model matrix
        //setup rotate/translation/scale
        if ((obj.texidx != -1) && (tl.get(obj.texidx).glid != -1)) {
          int glid = tl.get(obj.texidx).glid;
          gl.glBindTexture(GL.GL_TEXTURE_2D, glid);
        } else {
          gl.glBindTexture(GL.GL_TEXTURE_2D, blankTexture.glid);
        }
        obj.bindBuffers(this, gl);
        obj.render(gl);
      }
    }
    gl.glFlush();
  }
  public String modelPath;
  public GLModel load3DS(int idx) {
    String fn;
    GLModel mod;

    mod = mtl.get(idx);
    if (mod != null) {
      mod.refcnt++;
      mod = (GLModel)mod.clone();
      return mod;
    }
    fn = modelPath + String.format("%08x.3ds", idx);
    mod = GLModel.load3DS(fn);
    if (mod == null) return null;
    mtl.put(idx, mod);
    mod.refcnt = 1;
    mod = (GLModel)mod.clone();
    return mod;
  }
  public void unload3DS(GLModel mod) {
    mod.refcnt--;
  }
  //this will release all unused 3DS models
  public void release3DS() {
    Set keyset = mtl.keySet();
    Iterator iter = keyset.iterator();
    int idx;
    GLModel mod;
    while (iter.hasNext()) {
      idx = (Integer)iter.next();
      mod = mtl.get(idx);
      if (mod.refcnt == 0) {
        mtl.remove(idx);
      }
    }
  }
};
