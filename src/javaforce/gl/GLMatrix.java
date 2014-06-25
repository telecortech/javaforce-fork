package javaforce.gl;

/** 4x4 matrix */

public class GLMatrix implements Cloneable {
  public float m[];
  public GLMatrix() {
    m = new float[16];
    setIdentity();
  }
  public Object clone() {
    GLMatrix cln = new GLMatrix();
    for(int a=0;a<16;a++) cln.m[a] = m[a];
    return cln;
  }
  public void setIdentity() {
    for(int a=1;a<15;a++) m[a] = 0.0f;
    m[0] = 1.0f;
    m[5] = 1.0f;
    m[10] = 1.0f;
    m[15] = 1.0f;
  }
  public void setIdentity3x3() {  //effectively reset rotation
    for(int a=1;a<12;a++) m[a] = 0.0f;
    m[0] = 1.0f;
    m[5] = 1.0f;
    m[10] = 1.0f;
  }
  public void set(GLMatrix src) {
    for(int a=0;a<16;a++) m[a] = src.m[a];
  }
  //convert angle-axis(vector) into matrix (en.wikipedia.org/wiki/Axis_angle)
  public void setAA(float angle, float x, float y, float z) {
    float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c, s, c;

    s = (float)Math.sin( angle * (float)Math.PI / 180.0f );
    c = (float)Math.cos( angle * (float)Math.PI / 180.0f );

    setIdentity();

    if (x == 0.0f) {
      if (y == 0.0f) {
        if (z != 0.0f) {
          /* rotate only around z-axis */
          m[0+0*4] = c;
          m[1+1*4] = c;
          if (z < 0.0) {
            m[0+1*4] = s;
            m[1+0*4] = -s;
          } else {
            m[0+1*4] = -s;
            m[1+0*4] = s;
          }
          return;
        }
      } else if (z == 0.0f) {
        /* rotate only around y-axis */
        m[0+0*4] = c;
        m[2+2*4] = c;
        if (y < 0.0) {
          m[0+2*4] = -s;
          m[2+0*4] = s;
        } else {
          m[0+2*4] = s;
          m[2+0*4] = -s;
        }
        return;
      }
    } else if (y == 0.0f) {
      if (z == 0.0f) {
        /* rotate only around x-axis */
        m[1+1*4] = c;
        m[2+2*4] = c;
        if (x < 0.0) {
          m[1+2*4] = s;
          m[2+1*4] = -s;
        } else {
          m[1+2*4] = -s;
          m[2+1*4] = s;
        }
        return;
      }
    }

    float mag;
    //complex rotation
    mag = (float)Math.sqrt(x * x + y * y + z * z);
    if (mag <= 1.0e-4f) return;    //rotation too small
    x /= mag;
    y /= mag;
    z /= mag;
    xx = x * x;
    yy = y * y;
    zz = z * z;
    xy = x * y;
    yz = y * z;
    zx = z * x;
    xs = x * s;
    ys = y * s;
    zs = z * s;
    one_c = 1.0f - c;
    m[0+0*4] = (one_c * xx) + c;
    m[0+1*4] = (one_c * xy) - zs;
    m[0+2*4] = (one_c * zx) + ys;
    m[1+0*4] = (one_c * xy) + zs;
    m[1+1*4] = (one_c * yy) + c;
    m[1+2*4] = (one_c * yz) - xs;
    m[2+0*4] = (one_c * zx) - ys;
    m[2+1*4] = (one_c * yz) + xs;
    m[2+2*4] = (one_c * zz) + c;
  }

  public void setAATranslate(float angle, float x, float y, float z, float tx, float ty, float tz) {
    GLMatrix tmp = new GLMatrix();
    setAA(angle,x,y,z);  //sets identity
    addTranslate(tx, ty, tz);
  }

  public void setTranslate(float x, float y, float z) {
    setIdentity();
    m[0+3*4] = x;
    m[1+3*4] = y;
    m[2+3*4] = z;
  }

  public void setScale(float x, float y, float z) {
    setIdentity();
    m[0+0*4] = x;
    m[1+1*4] = y;
    m[3+3*4] = z;
  }

  /** Adds rotation assuming there is currently no translation. */
  public void addRotate(float angle, float ax, float ay, float az) {
    GLMatrix tmp = new GLMatrix();
    tmp.setAA(angle, ax, ay, az);
    mult3x3(tmp);
  }

  /** Adds rotation with current translation. */
  public void addRotate2(float angle, float ax, float ay, float az) {
    GLMatrix tmp = new GLMatrix();
    tmp.setAA(angle, ax, ay, az);
    mult4x4(tmp);
  }

  /** Adds translation assuming there is currently no rotation. */
  public void addTranslate(float tx, float ty, float tz) {
    //this will translate assuming no rotation
    m[0+3*4] += tx;
    m[1+3*4] += ty;
    m[2+3*4] += tz;
  }

  /** Adds translation assuming there is currently no rotation. */
  public void addTranslate(GLMatrix src) {
    addTranslate(src.m[12], src.m[13], src.m[14]);
  }

  /** Adds translation with current rotation. */
  public void addTranslate2(float tx, float ty, float tz) {
    //this will translate along the current angle
    GLMatrix tmp = new GLMatrix();
    tmp.setTranslate(tx, ty, tz);
    mult4x4(tmp);
  }

  public void addScale(float sx, float sy, float sz) {
    GLMatrix tmp = new GLMatrix();
    tmp.setScale(sx, sy, sz);
    mult4x4(tmp);
  }

  /** Multiply this matrix with another */
  public void mult4x4(GLMatrix src) {
    //64 mult
    float a0, a1, a2, a3;
    float r[] = new float[16];
    for(int i=0;i<4;i++) {
      a0 = m[i+0*4];
      a1 = m[i+1*4];
      a2 = m[i+2*4];
      a3 = m[i+3*4];
      r[i+0*4] = a0 * src.m[0+0*4] + a1 * src.m[1+0*4] + a2 * src.m[2+0*4] + a3 * src.m[3+0*4];
      r[i+1*4] = a0 * src.m[0+1*4] + a1 * src.m[1+1*4] + a2 * src.m[2+1*4] + a3 * src.m[3+1*4];
      r[i+2*4] = a0 * src.m[0+2*4] + a1 * src.m[1+2*4] + a2 * src.m[2+2*4] + a3 * src.m[3+2*4];
      r[i+3*4] = a0 * src.m[0+3*4] + a1 * src.m[1+3*4] + a2 * src.m[2+3*4] + a3 * src.m[3+3*4];
    }
    m = r;
  }

  /** Multiply this matrix with another (rotation only) */
  public void mult3x3(GLMatrix src) {
    //27 mult
    float a0, a1, a2;
    float r[] = new float[16];
    for(int i=0;i<3;i++) {
      a0 = m[i+0*4];
      a1 = m[i+1*4];
      a2 = m[i+2*4];
      r[i+0*4] = a0 * src.m[0+0*4] + a1 * src.m[1+0*4] + a2 * src.m[2+0*4];
      r[i+1*4] = a0 * src.m[0+1*4] + a1 * src.m[1+1*4] + a2 * src.m[2+1*4];
      r[i+2*4] = a0 * src.m[0+2*4] + a1 * src.m[1+2*4] + a2 * src.m[2+2*4];
    }
    for(int a=0;a<4;a++) r[a+3*4] = m[a+3*4];
    m = r;
  }

  /** 3x3 matrix multiple (rotation only) */
  public void mult(GLMatrix src) {
    mult3x3(src);
  }

  /** Multiply another 3x1 matrix with this one (3x3 part only)
   *  Effectively rotates the GLVector3 by the rotation of this matrix
   */
  public void mult(GLVector3 dest) {
    float nx, ny, nz;
    nx = m[0+0*4] * dest.v[0] + m[1+0*4] * dest.v[1] + m[2+0*4] * dest.v[2];
    ny = m[0+1*4] * dest.v[0] + m[1+1*4] * dest.v[1] + m[2+1*4] * dest.v[2];
    nz = m[0+2*4] * dest.v[0] + m[1+2*4] * dest.v[1] + m[2+2*4] * dest.v[2];
    dest.v[0] = nx;
    dest.v[1] = ny;
    dest.v[2] = nz;
  }

  /** Multiply another 4x1 matrix with this one (full matrix) */
  public void mult(GLVector4 dest) {
    float nx, ny, nz, nw;
    nx = m[0+0*4] * dest.v[0] + m[1+0*4] * dest.v[1] + m[2+0*4] * dest.v[2] + m[3+0*4] * dest.v[3];
    ny = m[0+1*4] * dest.v[0] + m[1+1*4] * dest.v[1] + m[2+1*4] * dest.v[2] + m[3+1*4] * dest.v[3];
    nz = m[0+2*4] * dest.v[0] + m[1+2*4] * dest.v[1] + m[2+2*4] * dest.v[2] + m[3+2*4] * dest.v[3];
    nw = m[0+3*4] * dest.v[0] + m[1+3*4] * dest.v[1] + m[2+3*4] * dest.v[2] + m[3+3*4] * dest.v[3];
    dest.v[0] = nx;
    dest.v[1] = ny;
    dest.v[2] = nz;
    dest.v[3] = nw;
  }

  public float get(int i, int j) {return m[j + i * 4];}
  public void set(int i, int j, float v) {m[j + i * 4] = v;}

  /** Transpose this matrix in place. */
  public void transpose() {
    float t;
    t = get(0, 1);
    set(0, 1, get(1, 0));
    set(1, 0, t);

    t = get(0, 2);
    set(0, 2, get(2, 0));
    set(2, 0, t);

    t = get(1, 2);
    set(1, 2, get(2, 1));
    set(2, 1, t);
  }

  /** Return the determinant. Computed across the zeroth row. */
  public float determinant() {
    return (get(0, 0) * (get(1, 1) * get(2, 2) - get(2, 1) * get(1, 2)) +
            get(0, 1) * (get(2, 0) * get(1, 2) - get(1, 0) * get(2, 2)) +
            get(0, 2) * (get(1, 0) * get(2, 1) - get(2, 0) * get(1, 1)));
  }

  /** Full matrix inversion in place. If matrix is singular, returns
      false and matrix contents are untouched. If you know the matrix
      is orthonormal, you can call transpose() instead. */
  public boolean invert() {
    float det = determinant();
    if (det == 0.0f) return false;

    // Form cofactor matrix
    GLMatrix cf = new GLMatrix();
    cf.set(0, 0, get(1, 1) * get(2, 2) - get(2, 1) * get(1, 2));
    cf.set(0, 1, get(2, 0) * get(1, 2) - get(1, 0) * get(2, 2));
    cf.set(0, 2, get(1, 0) * get(2, 1) - get(2, 0) * get(1, 1));
    cf.set(1, 0, get(2, 1) * get(0, 2) - get(0, 1) * get(2, 2));
    cf.set(1, 1, get(0, 0) * get(2, 2) - get(2, 0) * get(0, 2));
    cf.set(1, 2, get(2, 0) * get(0, 1) - get(0, 0) * get(2, 1));
    cf.set(2, 0, get(0, 1) * get(1, 2) - get(1, 1) * get(0, 2));
    cf.set(2, 1, get(1, 0) * get(0, 2) - get(0, 0) * get(1, 2));
    cf.set(2, 2, get(0, 0) * get(1, 1) - get(1, 0) * get(0, 1));

    // Now copy back transposed
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        set(i, j, cf.get(j, i) / det);
    return true;
  }

  public void reverseTranslate() {
    m[0+3*4] *= -1.0f;
    m[1+3*4] *= -1.0f;
    m[2+3*4] *= -1.0f;
  }

  public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {
    float temp, temp2, temp3, temp4;
    temp = 2.0f * znear;
    temp2 = right - left;
    temp3 = top - bottom;
    temp4 = zfar - znear;
    m[0] = temp / temp2;
    m[1] = 0.0f;
    m[2] = 0.0f;
    m[3] = 0.0f;
    m[4] = 0.0f;
    m[5] = temp / temp3;
    m[6] = 0.0f;
    m[7] = 0.0f;
    m[8] = (right + left) / temp2;
    m[9] = (top + bottom) / temp3;
    m[10] = (-zfar - znear) / temp4;
    m[11] = -1.0f;
    m[12] = 0.0f;
    m[13] = 0.0f;
    m[14] = (-temp * zfar) / temp4;
    m[15] = 0.0f;
  }

  private float degToRad(float x) {
    return (float)(x * (Math.PI/180.0f));
  }

  public void perspective(float fovyInDegrees, float aspectRatio, float znear, float zfar)
  {
    float ymax, xmax;
    ymax = (float)(znear * Math.tan(degToRad(fovyInDegrees) / 2.0f));
    xmax = ymax * aspectRatio;
    frustum(-xmax, xmax, -ymax, ymax, znear, zfar);
  }

  public void ortho(float left, float right, float bottom, float top, float near, float far) {
    float h = right - left, i = top - bottom, j = far - near;
    m[0] = 2 / h;
    m[1] = 0;
    m[2] = 0;
    m[3] = 0;
    m[4] = 0;
    m[5] = 2 / i;
    m[6] = 0;
    m[7] = 0;
    m[8] = 0;
    m[9] = 0;
    m[10] = -2 / j;
    m[11] = 0;
    m[12] = -(left + right) / h;
    m[13] = -(top + bottom) / i;
    m[14] = -(far + near) / j;
    m[15] = 1;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\r\n");
    for(int a=0;a<16;a+=4) {
      sb.append(String.format("%.3f,%.3f,%.3f,%.3f\r\n", m[a + 0], m[a + 1], m[a + 2], m[a + 3]));
    }
    return sb.toString();
  }

}

/*

Identity (.=never used - always zero)

1 0 0 .
0 1 0 .
0 0 1 .
0 0 0 1

Translation

1 0 0 0
0 1 0 0
0 0 1 0
x y z 1

Rotation

 c   zs -ys  0
-zs  c   xs  0
 ys -xs  c   0
 0   0   0   1

Scaling

x 0 0 0
0 y 0 0
0 0 1 0
0 0 0 z

*/
