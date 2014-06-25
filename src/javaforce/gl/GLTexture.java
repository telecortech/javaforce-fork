package javaforce.gl;

import javaforce.jna.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import javaforce.*;

/** Stores a texture (image).
 * Textures are usually loaded after a new model is loaded.
 * All model's share the same set of textures. */

public class GLTexture {
  public JFImage bitmap;
  public int refcnt;
  public int glid;
  public boolean needload;

  public GLTexture() {
    refcnt = 0;
    glid = -1;
    bitmap = new JFImage();
    needload = true;
  }

  public boolean load(String filename) {
    try { return load(new FileInputStream(filename)); } catch (Exception e) {return false;}
  }

  public boolean load(InputStream is) {
    if (!bitmap.load(is)) return false;
    return true;
  }

  public void set(int pixels[], int x, int y) {
    bitmap.setSize(x,y);
    bitmap.putPixels(pixels, 0, 0, x, y, 0);
  }
}

