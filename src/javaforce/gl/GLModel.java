package javaforce.gl;

import java.util.*;
import java.io.*;

import javaforce.*;

/** <code>GLModel</code> is a set of <code>GLObject</code>'s that all share the same base orientation (rotation, translation, scale)
 * Each object can also have its own orientation in addition to this
 * Usually a 3DS file is loaded into one GLModel.
 * Each Object in the 3DS file will be stored into GLObject's.
 */

public class GLModel implements Cloneable {
  public ArrayList<GLObject> ol;  //obj list
  public GLMatrix m;  //translation, rotation, scale matrix for all sub-objects
  public boolean visible = true;
  public int refcnt;

  public GLModel() {
    m = new GLMatrix();
    m.setIdentity();
    ol = new ArrayList<GLObject>();
  }
  public GLModel(GLMatrix m) {  //for clone()
    this.m = m;
    ol = new ArrayList<GLObject>();
  }
  /**
  * Clones deep enough so that the cloned object will include seperate GLObjects, but share vertex, vertex point,
  * and animation data (except for the frame position).
  */
  public Object clone() {
    GLModel c = new GLModel((GLMatrix)m.clone());
    int objs = ol.size();
    for(int a=0;a<objs;a++) c.ol.add((GLObject)ol.get(a).clone());
    return c;
  }
  public void setVisible(boolean state) {visible = state;}
  public void addObject(GLObject obj) {
    ol.add(obj);
  }
  public void setIdentity() {
    m.setIdentity();
  }
  //these are additive
  public void rotate(float angle, float x, float y, float z) {
    m.addRotate(angle, x, y, z);
  }
  public void translate(float x, float y, float z) {
    m.addTranslate(x, y, z);
  }
  public void scale(float x, float y, float z) {
    m.addScale(x, y, z);
  }
  public void nextFrame() {
    GLObject obj;
    int size = ol.size();
    for(int i=0;i<size;i++) {
      obj = ol.get(i);
      obj.nextFrame();
    }
  }
  public void setFrame(int idx) {
    GLObject obj;
    int size = ol.size();
    for(int i=0;i<size;i++) {
      obj = ol.get(i);
      obj.setFrame(idx);
    }
  }
//Loading//
  private static final int _3DS_FLG_TENSION     =  0x01;
  private static final int _3DS_FLG_CONTINUITY  =  0x02;
  private static final int _3DS_FLG_BIAS        =  0x04;
  private static final int _3DS_FLG_EASE_TO     =  0x08;
  private static final int _3DS_FLG_EASE_FROM   =  0x10;
  //*.3DS loading (loades meshes, textures and animation - lights are not supported)
  //You should now use GLSCene.load3DS() instead
  public static GLModel load3DS(String filename) {
//System.out.println("load3DS:" + filename);
    try {
      return load3ds(new FileInputStream(filename));
    } catch (Exception e) {
      System.out.println("Error:" + e);
      return null;
    }
  }
  public static GLModel load3DS(InputStream is) {
    try {
      return load3ds(is);
    } catch (Exception e) {
      System.out.println("Error:" + e);
      return null;
    }
  }
  private static GLModel load3ds(InputStream is) throws Exception {
    GLObject obj = null;
    int off = 0;
    int head_id;
    int head_len;
    int _siz;
    float _float[];
    int _pts[];
    boolean done_vertex = false;
    boolean done_pts = false;
    boolean done_texvertex = false;
    int vertexidx = -1;
    int vertexcnt = -1;
    String name;
    ArrayList<material> matlist;  //materials (objects refer to material name)
    ArrayList<String> objlist;  //object names (keyframe data refers to object name)
    String objname = "";
    int objidx = -1;
    material mat;
    String matname = "", fn;
    int a, b, keys, u32;
    boolean ok;
    int u16;
    GLTranslate pos;
    GLRotate rot;
    GLScale scale;
    GLModel mod;
    int skip=0;
    int parent;
    int frameno;

    matlist = new ArrayList<material>();
    objlist = new ArrayList<String>();

    mod = new GLModel();  //you must add to scene with GLScene.addModel()

    while (!JF.eof(is)) {
      if (skip > 0) {
        try { is.skip(skip); } catch (Exception e) {throw new Exception("skip() failed");}
        skip = 0;
      }
      head_id = JF.readuint16(is);
      head_len = JF.readuint32(is);
      if (head_len == -1) break;  //this does happen in some files
      if (head_len < 6) throw new Exception("head_len < 6 (" + head_len + ")");  //bad file
      head_len -= 6;
//System.out.println("id="+Integer.toString(head_id,16));
      switch (head_id) {
        case 0x4d4d:  //main chunk
          break;
        case 0x3d3d:  //mesh chunk
          break;
        case 0xafff:  //material chunk
          matname = "";
          break;
        case 0xa000:  //material chunk name
          matname = readname(is, head_len);
          break;
        case 0xa200:  //texture details
          break;
        case 0xa300:  //texture filename
          mat = new material();
          fn = readname(is, head_len);
//System.out.println("texture filename" + fn);
          mat.name = matname;
          mat.texidx = Integer.parseInt(fn.substring(0, fn.indexOf('.')), 16);
          matlist.add(mat);
          break;
        case 0x4000:  //object chunk
          objname = readname(is, -1);  //don't skip the whole chunk
          done_vertex = false;
          done_pts = false;
          break;
        case 0x4100:  //triangular object chunk
          break;
        case 0x4110:  //vertex list of a polygon
          skip = head_len;
          if (done_vertex) break;  //Warning : 2 vertex lists found for 1 object?
          obj = new GLObject();
          mod.ol.add(obj);
          objlist.add(objname);
          _siz = JF.readuint16(is);
          skip-=2;
          vertexidx = obj.vpl.size();
          vertexcnt = _siz;
          if (_siz == 0) break;
          _float = new float[_siz * 3];
          for(a=0;a<_siz;a++) {
            for(b=0;b<3;b++) {
              u32 = JF.readuint32(is);  //TODO : buffer this
              _float[a*3+b] = Float.intBitsToFloat(u32);
              skip-=4;
            }
//System.out.println("ver:v0=" + _float[a*3+0] + "v1=" + _float[a*3+1] + "v2=" + _float[a*3+2]);
          }
          obj.addVertex(_float);
          _float = null;
          done_vertex = true;
          break;
        case 0x4120:  //Points list
          _siz = JF.readuint16(is);
          skip = _siz * 2 * 4;
          if (!done_vertex) break;  //Warning : pts list before vertex list?
          if (done_pts) break; //Warning : 2 pts lists found for 1 object?
          if (_siz == 0) break;
          _pts = new int[3];  //p1,p2,p3,flgs per triangle
          for(a=0;a<_siz;a++) {
            for(b=0;b<3;b++) {
              _pts[b] = (short)JF.readuint16(is);
              skip -= 2;
            }
//System.out.println("pts:p0=" + _pts[0] + "p1=" + _pts[1] + "p2=" + _pts[2]);
            JF.readuint16(is);  //skip flgs
            skip -= 2;
            obj.addPoly(_pts);
          }
          _pts = null;
          done_pts = true;
          break;
        case 0x4130:  //object material name
          name = readname(is, head_len);
          if (obj != null) {
            //find name in matlist
            ok = false;
            for(a=0;a<matlist.size();a++) {
              if (matlist.get(a).name.equals(name)) {
                obj.texidx = matlist.get(a).texidx;
                ok = true;
                break;
              }
            }
//            if (!ok) throw new Exception("0x4130 : object material name not found in list : " + name);
          }
          break;
        case 0x4140:  //texture vertex list
          _siz = JF.readuint16(is);
          skip = _siz * 2 * 4;
          if (!done_vertex) {System.out.println("Warning:Texture list before vertex list");break;}
          if (done_texvertex) {System.out.println("Warning:2 texture lists for 1 object found");break;}
          if (_siz != vertexcnt) {System.out.println("Warning:texture list siz != vertex list siz");break;}
          if (_siz == 0) break;
          _float = new float[2];
          for(a=0;a<_siz;a++) {
            for(b=0;b<2;b++) {
              u32 = JF.readuint32(is);  //TODO : buffer this
              _float[b] = 1.0f - Float.intBitsToFloat(u32);  //3DS are inverted to what I expect
              skip-=4;
            }
//System.out.println("tex:f0=" + _float[0] + "f1=" + _float[1]);
//            obj.tcl.get(vertexidx + a).tx = _float[0];
//            obj.tcl.get(vertexidx + a).ty = _float[1];
            obj.addText(new float[] {_float[0], _float[1]});
          }
          _float = null;
          done_vertex = true;
          break;
        case 0x4160:  //obj matrix
          //read in float[4][3] and show for now
          for(a=0;a<3*3;a++) {  //don't need these
            u32 = JF.readuint32(is);
          }
          u32 = JF.readuint32(is);
          obj.org.x = Float.intBitsToFloat(u32);
          u32 = JF.readuint32(is);
          obj.org.y = Float.intBitsToFloat(u32);
          u32 = JF.readuint32(is);
          obj.org.z = Float.intBitsToFloat(u32);
//System.out.println("org:"+obj.org.x+","+obj.org.y+","+obj.org.z);
          break;
        case 0xb000:  //keyframe header
          break;
        case 0xb002:  //object node chunk
          objidx = -1;
          break;
        case 0xb010:  //keyframe object name
          name = readname(is, -1);
          JF.readuint16(is);  //f1
          JF.readuint16(is);  //f2
          parent = JF.readuint16(is);  //parent
          //find name in objlist
          objidx = 0;
          ok = false;
          for(a=0;a<objlist.size();a++) {
            if (objlist.get(a).equals(name)) {
              ok = true;
              break;
            }
            objidx++;
          }
          if (!ok) {
            objidx = -1;
          } else {
            obj = mod.ol.get(objidx);
            if (parent != 65535) obj.parent = parent;
            obj = null;
          }
//System.out.println("0xb010 : name=" + name + ":objidx=" + objidx + ":parent=" + parent);
          break;
        case 0xb020:  //keyframe pos
          skip = head_len;
          if (objidx == -1) break;
          obj = mod.ol.get(objidx);
          u16 = JF.readuint16(is);  //flgs
          skip -= 2;
          u32 = JF.readuint32(is);  //r1
          skip -= 4;
          u32 = JF.readuint32(is);  //r2
          skip -= 4;
          keys = JF.readuint32(is);  //keys
          skip -= 4;
          _float = new float[3];
          for(a=0;a<keys;a++) {
            frameno = JF.readuint32(is);  //frame #
            skip -= 4;
            u16 = JF.readuint16(is);  //flgs
            skip -= 2;
            u32 = 0;
            if ((u16 & _3DS_FLG_TENSION) != 0) u32++;
            if ((u16 & _3DS_FLG_CONTINUITY) != 0) u32++;
            if ((u16 & _3DS_FLG_BIAS) != 0) u32++;
            if ((u16 & _3DS_FLG_EASE_TO) != 0) u32++;
            if ((u16 & _3DS_FLG_EASE_FROM) != 0) u32++;
            if (u32 > 0) {
              JF.read(is, u32 * 4);    //all ignored
              skip -= u32 * 4;
            }
            pos = new GLTranslate();
            for(b=0;b<3;b++) {
              u32 = JF.readuint32(is);  //TODO : buffer this
              _float[b] = Float.intBitsToFloat(u32);
              skip -= 4;
            }
            pos.x = _float[0];
            pos.y = _float[1];
            pos.z = _float[2];
//System.out.println("pos["+frameno+"]:"+pos.x+","+pos.y+","+pos.z+":flgs="+u16);
            obj.tl.put(frameno, pos);
            if (obj.maxframeCount < frameno) obj.maxframeCount = frameno;
          }
          _float = null;
          obj = null;
          break;
        case 0xb021:  //keyframe rotate
          skip = head_len;
          if (objidx == -1) break;
          obj = mod.ol.get(objidx);
          u16 = JF.readuint16(is);  //flgs
          skip -= 2;
          u32 = JF.readuint32(is);  //r1
          skip -= 4;
          u32 = JF.readuint32(is);  //r2
          skip -= 4;
          keys = JF.readuint32(is);  //keys
          skip -= 4;
          _float = new float[4];
          for(a=0;a<keys;a++) {
            frameno = JF.readuint32(is);  //frame #
            skip -= 4;
            u16 = JF.readuint16(is);  //flgs
            skip -= 2;
            u32 = 0;
            if ((u16 & _3DS_FLG_TENSION) != 0) u32++;
            if ((u16 & _3DS_FLG_CONTINUITY) != 0) u32++;
            if ((u16 & _3DS_FLG_BIAS) != 0) u32++;
            if ((u16 & _3DS_FLG_EASE_TO) != 0) u32++;
            if ((u16 & _3DS_FLG_EASE_FROM) != 0) u32++;
            if (u32 > 0) {
              JF.read(is, u32 * 4);    //all ignored
              skip -= u32 * 4;
            }
            rot = new GLRotate();
            for(b=0;b<4;b++) {
              u32 = JF.readuint32(is);  //TODO : buffer this
              _float[b] = Float.intBitsToFloat(u32);
              skip -= 4;
            }
            rot.angle = _float[0] * 57.2957795f;  //convert to degrees
            rot.x = _float[1];
            rot.y = _float[2];
            rot.z = _float[3];
//System.out.println("rot["+frameno+"]:"+rot.angle+","+rot.x+","+rot.y+","+rot.z+":flgs="+u16);
            obj.rl.put(frameno, rot);
            if (obj.maxframeCount < frameno) obj.maxframeCount = frameno;
          }
          _float = null;
          obj = null;
          break;
        case 0xb022:  //keyframe scale
          skip = head_len;
          if (objidx == -1) break;
          obj = mod.ol.get(objidx);
          u16 = JF.readuint16(is);  //flgs
          skip -= 2;
          u32 = JF.readuint32(is);  //r1
          skip -= 4;
          u32 = JF.readuint32(is);  //r2
          skip -= 4;
          keys = JF.readuint32(is);  //keys
          skip -= 4;
          _float = new float[3];
          for(a=0;a<keys;a++) {
            frameno = JF.readuint32(is);  //frame #
            skip -= 4;
            u16 = JF.readuint16(is);  //flgs
            skip -= 2;
            u32 = 0;
            if ((u16 & _3DS_FLG_TENSION) != 0) u32++;
            if ((u16 & _3DS_FLG_CONTINUITY) != 0) u32++;
            if ((u16 & _3DS_FLG_BIAS) != 0) u32++;
            if ((u16 & _3DS_FLG_EASE_TO) != 0) u32++;
            if ((u16 & _3DS_FLG_EASE_FROM) != 0) u32++;
            if (u32 > 0) {
              JF.read(is, u32 * 4);    //all ignored
              skip -= u32 * 4;
            }
            scale = new GLScale();
            for(b=0;b<3;b++) {
              u32 = JF.readuint32(is);  //TODO : buffer this
              _float[b] = Float.intBitsToFloat(u32);
              skip -= 4;
            }
            scale.x = _float[0];
            scale.y = _float[1];
            scale.z = _float[2];
//System.out.println("scale["+frameno+"]:"+scale.x+","+scale.y+","+scale.z+":flgs="+u16);
            obj.sl.put(frameno, scale);
            if (obj.maxframeCount < frameno) obj.maxframeCount = frameno;
          }
          _float = null;
          obj = null;
          break;
        default:
          skip = head_len;
          break;
      }
    }
    //setup any lights
    _siz = mod.ol.size();
    for(a=0;a<_siz;a++) {
      obj = mod.ol.get(a);
    }
    //delete temp lists
    matlist.clear();
    objlist.clear();
    return mod;
  }
  //for load3DS()
  private static String readname(InputStream is, int maxread) {
    String ret = "";
    char ch;
    while (!JF.eof(is)) {
      ch = (char)JF.read(is);
      if (maxread != -1) {
        maxread--;
        if (maxread == 0) break;
      }
      if (ch == 0) break;
      ret += ch;
    }
    while (maxread > 0) {JF.read(is); maxread--;}
    return ret;
  }
  //private class for load3DS()
  private static class material {
    String name;
    int texidx;
  }
};

