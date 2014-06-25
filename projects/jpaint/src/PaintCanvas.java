import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import javaforce.*;

import com.jhlabs.image.*;

public class PaintCanvas extends JComponent implements MouseListener, MouseMotionListener, KeyListener {

  public static MainPanel panel;
  public int mx=0, my=0;  //current mouse pos
  public int sx=0, sy=0;  //starting draw pos
  public boolean drag = false;  //a draggable selection/text box is visible
  public JFImage img, bimg, fimg;  //the "real" image / background / foreground images
  public JFImage limg;  //alpha, color layers
  public JFImage cimg;   //current layer
  public boolean dirty = false, undoDirty = false;
  public Vector<PaintCanvas> undos = new Vector<PaintCanvas>();
  public int undoPos = -1;
  public static int MAX_UNDO_SIZE = 7;
  public boolean border = false;
  public enum BorderTypes {east, south, corner};
  public BorderTypes borderType;
  public PaintCanvas border_east, border_south, border_corner;
  public JPanel parentPanel;
  public int button = -1;
  public boolean textCursor;
  public int selBoxIdx = 0;  //dashed line
  public int scale = 100;
  public int layer = 0;
  public boolean disableScale = false;  //for file operations
  public PaintCanvas parentImage;

  private void init() {
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    setFocusable(true);
  }

  public PaintCanvas(JPanel parent) {
    init();
    parentPanel = parent;
    img = new JFImage();
    bimg = new JFImage();
    fimg = new JFImage();
    limg = new JFImage();
    cimg = img;
    setName("image");
  }

  public PaintCanvas(PaintCanvas parent, BorderTypes bt) {
    this.borderType = bt;
    init();
    img = new JFImage();
    setName("border");
    parentImage = parent;
    border = true;
  }

  public void setImageSize(int x,int y) {
    if (x <= 0) x = 1;
    if (y <= 0) y = 1;

    img.setImageSize(x, y);
    limg.setImageSize(x, y);

    bimg.setImageSize(x, y);

    fimg.setImageSize(x, y);

    setSize(x,y);

    setPreferredSize(new Dimension(x,y));
    backClear();
    foreClear();
    clearUndo();
  }

  public void paint(Graphics g) {
    if (img.getImage() == null) return;
    if (border) {
      g.drawImage(img.getImage(), 0, 0, null);
      return;
    }
    if (scale == 100) {
      if (layer == 0) g.drawImage(bimg.getImage(), 0, 0, null);
      g.drawImage(cimg.getImage(), 0, 0, null);
      g.drawImage(fimg.getImage(), 0, 0, null);
    } else {
      int w = getUnscaledWidth();
      int h = getUnscaledHeight();
      if (layer == 0) g.drawImage(bimg.getImage(), 0, 0, w * scale / 100, h * scale / 100, 0, 0, w, h, null);
      g.drawImage(cimg.getImage(), 0, 0, w * scale / 100, h * scale / 100, 0, 0, w, h, null);
      g.drawImage(fimg.getImage(), 0, 0, w * scale / 100, h * scale / 100, 0, 0, w, h, null);
    }
  }

  public void setSize(int x, int y) {
//    System.out.println("PaintCanvas.setSize:" + x + "," + y + ":" + getName());
    super.setSize(x,y);  //calls setBounds()
  }

  public void setSize(Dimension d) {
//    System.out.println("PaintCanvas.setSize:" + d + ":" + getName());
    super.setSize(d);  //calls setBounds()
  }

  public void setBounds(int x,int y,int w,int h) {
//    System.out.println("PaintCanvas.setBounds:" + x + "," + y + "," + w + "," + h + ":" + getName());
    super.setBounds(x,y,w,h);
    if (border) {
      img.setImageSize(w,h);
    }
  }

  public void createBorders() {
    border_east = new PaintCanvas(this, BorderTypes.east) {
      public void paint(Graphics g) {
        int x = getUnscaledWidth();
        int y = getUnscaledHeight();
        g.setColor(new Color(0x888888));
        g.fillRect(0,0, 10,y);
        g.setColor(new Color(0x000000));
        g.fillRect(0,y/2-5, 10,10);
      }
    };
    border_south = new PaintCanvas(this, BorderTypes.south) {
      public void paint(Graphics g) {
        int x = getUnscaledWidth();
        int y = getUnscaledHeight();
        g.setColor(new Color(0x888888));
        g.fillRect(0,0, x,10);
        g.setColor(new Color(0x000000));
        g.fillRect(x/2-5,0, 10,10);
      }
    };
    border_corner = new PaintCanvas(this, BorderTypes.corner) {
      public void paint(Graphics g) {
        g.setColor(new Color(0x000000));
        g.fillRect(0,0, 10,10);
      }
    };
  }

  public void resizeBorder() {
//    System.out.println("doLayout");
    parentPanel.doLayout();
  }

  public Dimension getPreferredSize() {
    Dimension ps = super.getPreferredSize();
//    System.out.println("PaintCanvas.getPreferredSize()" + ps);
    return ps;
  }

  public Dimension getMinimumSize() {
//    System.out.println("PaintCanvas.getMinimumSize()" + getPreferredSize());
    return getPreferredSize();
  }

  public Dimension getMaximumSize() {
//    System.out.println("PaintCanvas.getMaximumSize()" + getPreferredSize());
    return getPreferredSize();
  }

  public void setScale(int newScale) {
//    System.out.println("     setScale:" + newScale);
    int x = img.getWidth() * newScale / 100;
    int y = img.getHeight() * newScale / 100;
    setPreferredSize(new Dimension(x,y));
    scale = newScale;
  }

  public int getWidth() {
//    System.out.println("getWidth=" + super.getWidth());
    if (disableScale) return getUnscaledWidth();
    return getScaledWidth();
  }

  public int getHeight() {
//    System.out.println("getHeight=" + super.getHeight());
    if (disableScale) return getUnscaledHeight();
    return getScaledHeight();
  }

  public int getScaledWidth() {
//    System.out.println("getScaledWidth=" + super.getWidth());
    return img.getWidth() * scale / 100;
  }

  public int getScaledHeight() {
//    System.out.println("getScaledHeight=" + super.getHeight());
    return img.getHeight() * scale / 100;
  }

  public int getUnscaledWidth() {
    return img.getWidth();
  }

  public int getUnscaledHeight() {
    return img.getHeight();
  }

  public void setLineWidth(int w) {
    cimg.getGraphics2D().setStroke(new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
  }
  public void drawLine(int x1,int y1,int x2,int y2) {
    cimg.getGraphics().drawLine(x1,y1,x2,y2);
  }
  public void drawBox(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    cimg.getGraphics().drawRect(x1,y1,x2-x1,y2-y1);  //NOTE : left=x1 right=x1+width (strange)
  }
  public void drawRoundBox(int x1,int y1,int x2,int y2,int ax,int ay) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    cimg.getGraphics().drawRoundRect(x1,y1,x2-x1,y2-y1,ax,ay);  //NOTE : left=x1 right=x1+width (strange)
  }
  public void drawCircle(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    cimg.getGraphics().drawOval(x1,y1,x2-x1+1,y2-y1+1);
  }
  public void drawCurve(int cx[], int cy[]) {
    cimg.getGraphics2D().draw(new CubicCurve2D.Double(cx[0],cy[0], cx[2],cy[2], cx[3],cy[3], cx[1],cy[1]));
  }
  public void fillBox(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    cimg.getGraphics().fillRect(x1,y1,x2-x1+1,y2-y1+1);
  }
  public void fillRoundBox(int x1,int y1,int x2,int y2,int ax,int ay) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    cimg.getGraphics().fillRoundRect(x1,y1,x2-x1+1,y2-y1+1,ax,ay);
  }
  public void fillCircle(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    cimg.getGraphics().fillOval(x1,y1,x2-x1+1,y2-y1+1);
  }
  private class Point {
    public int x,y,dir;
    public Point(int x,int y,int dir) {this.x=x; this.y=y; this.dir=dir;}
  }
  public void fillFast(int x1,int y1,int clr, boolean hasAlpha) {
    int w = cimg.getWidth();
    int h = cimg.getHeight();
    int target = cimg.getPixel(x1,y1) & JFImage.RGB_MASK;
    if (target == (clr & JFImage.RGB_MASK)) return;
    int px[] = cimg.getBuffer();
    Vector<Point> pts = new Vector<Point>();
    pts.add(new Point(x1,y1,1));
    if (x1 > 0) pts.add(new Point(x1-1,y1,-1));
    int x,y,dir,p;
    boolean top,bottom;
    while (pts.size() > 0) {
      Point pt = pts.remove(0);
      x = pt.x;
      y = pt.y;
      dir = pt.dir;
      top = false;
      bottom = false;
      while ((x >= 0) && (x < w)) {
        p = y * w + x;
        if ((px[p] & JFImage.RGB_MASK) != target) break;
        if (hasAlpha) {
          px[p] = clr;
        } else {
          px[p] &= JFImage.ALPHA_MASK;
          px[p] |= clr;
        }
        if (y > 0) {
          if (!top) {
            if ((px[p-w] & JFImage.RGB_MASK) == target) {
              top=true;
              pts.add(new Point(x,y-1,1));
              if (x > 0) pts.add(new Point(x-1,y-1,-1));
            }
          } else {
            if ((px[p-w] & JFImage.RGB_MASK) != target) top=false;
          }
        }
        if (y < h-1) {
          if (!bottom) {
            if ((px[p+w] & JFImage.RGB_MASK) == target) {
              bottom=true;
              pts.add(new Point(x,y+1,1));
              if (x > 0) pts.add(new Point(x-1,y+1,-1));
            }
          } else {
            if ((px[p+w] & JFImage.RGB_MASK) != target) bottom=false;
          }
        }
        x += dir;
      }
    }
  }
  public void fillSlow(int x1,int y1) {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    Graphics2D g = img.getGraphics2D();
    int target = img.getPixel(x1,y1) & JFImage.RGB_MASK;
    g.fillRect(x1,y1,1,1);
    boolean done[] = new boolean[w * h];  //to keep track of what has been filled in already
    Vector<Point> pts = new Vector<Point>();
    pts.add(new Point(x1,y1,1));
    if (x1 > 0) pts.add(new Point(x1-1,y1,-1));
    int x,y,dir,p;
    boolean top,bottom;
    while (pts.size() > 0) {
      Point pt = pts.remove(0);
      x = pt.x;
      y = pt.y;
      dir = pt.dir;
      top = false;
      bottom = false;
      while ((x >= 0) && (x < w)) {
        p = y * w + x;
        if (((img.getPixel(x,y) & JFImage.RGB_MASK) != target) || (done[p])) break;
        g.fillRect(x,y,1,1);  //slow
        done[p] = true;
        if (y > 0) {
          if (!top) {
            if (((img.getPixel(x,y-1) & JFImage.RGB_MASK) == target) && (!done[p-w])) {
              top=true;
              pts.add(new Point(x,y-1,1));
              if (x > 0) pts.add(new Point(x-1,y-1,-1));
            }
          } else {
            if ((img.getPixel(x,y-1) & JFImage.RGB_MASK) != target) top=false;
          }
        }
        if (y < h-1) {
          if (!bottom) {
            if (((img.getPixel(x,y+1) & JFImage.RGB_MASK) == target) && (!done[p+w])) {
              bottom=true;
              pts.add(new Point(x,y+1,1));
              if (x > 0) pts.add(new Point(x-1,y+1,-1));
            }
          } else {
            if ((img.getPixel(x,y+1) & JFImage.RGB_MASK) != target) bottom=false;
          }
        }
        x += dir;
      }
    }
  }
  //changes clr1 -> clr2
  public void subBoxFast(int x1, int y1, int x2, int y2, int clr1, int clr2, int threshold) {
    int clr = clr1 & JFImage.RGB_MASK;
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    int px[] = img.getBuffer();
    int x,y,p,diff,c1,c2;
    //do clipping
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    if (x1 < 0) x1 = 0;
    if (y1 < 0) y1 = 0;
    if (x2 < 0) x2 = 0;
    if (y2 < 0) y2 = 0;
    if (x1 >= w) x1 = w-1;
    if (y1 >= h) y1 = h-1;
    if (x2 >= w) x2 = w-1;
    if (y2 >= h) y2 = h-1;
    for(y=y1;y<=y2;y++) {
      p = y * w + x1;
      for(x=x1;x<=x2;x++,p++) {
        if (threshold == 0) {
          if ((px[p] & JFImage.RGB_MASK) != clr) continue;
        } else {
          //check RGB values against threshold
          tmp = px[p] & JFImage.RGB_MASK;
          c1 = (tmp & 0xff0000) >> 16;
          c2 = (clr & 0xff0000) >> 16;
          diff = Math.abs(c1-c2);
          c1 = (tmp & 0xff00) >> 8;
          c2 = (clr & 0xff00) >> 8;
          diff += Math.abs(c1-c2);
          c1 = tmp & 0xff;
          c2 = clr & 0xff;
          diff += Math.abs(c1-c2);
          if (diff/3 > threshold) continue;
        }
        px[p] &= JFImage.ALPHA_MASK;
        px[p] |= clr2;
      }
    }
  }
  public void subBoxSlow(int x1, int y1, int x2, int y2, int clr1, int threshold) {
    int clr = clr1 & JFImage.RGB_MASK;
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    int px[] = img.getPixels();
    Graphics2D g = img.getGraphics2D();
    int x,y,p,diff,c1,c2;
    //do clipping
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    if (x1 < 0) x1 = 0;
    if (y1 < 0) y1 = 0;
    if (x2 < 0) x2 = 0;
    if (y2 < 0) y2 = 0;
    if (x1 >= w) x1 = w-1;
    if (y1 >= h) y1 = h-1;
    if (x2 >= w) x2 = w-1;
    if (y2 >= h) y2 = h-1;
    for(y=y1;y<=y2;y++) {
      p = y * w + x1;
      for(x=x1;x<=x2;x++,p++) {
        if (threshold == 0) {
          if ((px[p] & JFImage.RGB_MASK) != clr) continue;
        } else {
          //check RGB values against threshold
          tmp = px[p] & JFImage.RGB_MASK;
          c1 = (tmp & 0xff0000) >> 16;
          c2 = (clr & 0xff0000) >> 16;
          diff = Math.abs(c1-c2);
          c1 = (tmp & 0xff00) >> 8;
          c2 = (clr & 0xff00) >> 8;
          diff += Math.abs(c1-c2);
          c1 = tmp & 0xff;
          c2 = clr & 0xff;
          diff += Math.abs(c1-c2);
          if (diff/3 > threshold) continue;
        }
        g.fillRect(x,y,1,1);  //slow
      }
    }
  }
  public void setFont(Font font) {
    cimg.setFont(font);
  }
  public void drawText(String txt, int x, int y) {
    cimg.getGraphics().drawString(txt, x, y);
  }
  public void backClear() {
    //create a checkered board on the back image (only visible if there is any transparent parts)
    int x = getUnscaledWidth();
    int y = getUnscaledHeight();
//System.out.println("backclear:"+x+","+y);
    bimg.fill(0,0,x,y,0xffffff);
    JFImage tmp = new JFImage();
    tmp.setImageSize(16,16);
    Graphics g = tmp.getGraphics();
    g.setColor(new Color(0x888888));
    g.fillRect(0,0,8,8);
    g.fillRect(8,8,8,8);
    Graphics2D g2d = bimg.getGraphics2D();
    g2d.setPaint(new TexturePaint(tmp.getBufferedImage(), new Rectangle2D.Double(0,0,16,16) ));
    g2d.fillRect(0,0,x,y);
  }
  public void foreClear() {
    fimg.fillAlpha(0,0,img.getWidth(),img.getHeight(),0x00);  //transparent
    repaint();
  }
  public void foreDrawLine(int x1,int y1,int x2,int y2) {
    fimg.getGraphics().drawLine(x1,y1,x2,y2);
  }
  public void foreDrawBox(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fimg.getGraphics().drawRect(x1,y1,x2-x1,y2-y1);  //NOTE : left=x1 right=x1+width (strange)
  }
  public void foreDrawRoundBox(int x1,int y1,int x2,int y2,int ax, int ay) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fimg.getGraphics().drawRoundRect(x1,y1,x2-x1,y2-y1,ax,ay);  //NOTE : left=x1 right=x1+width (strange)
  }
  public void foreDrawCircle(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fimg.getGraphics().drawOval(x1,y1,x2-x1+1,y2-y1+1);
  }
  public void foreDrawSelBox(int x1,int y1,int x2,int y2) {
    int tmp;
    Graphics2D fg = fimg.getGraphics2D();
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fg.setColor(new Color(0x00));
    fg.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0, new float[]{9}, selBoxIdx++));
    if (selBoxIdx == 9) selBoxIdx = 0;
    fg.drawRect(x1,y1,x2-x1,y2-y1);  //NOTE : left=x1 right=x1+width (strange)
    fg.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
  }
  public void forePutPixels(int px[], int x, int y, int w, int h, int offset) {
    fimg.putPixels(px, x, y, w, h, offset);
  }
  public void forePutPixelsKeyClr(int px[], int x, int y, int w, int h, int offset, int keyclr) {
    fimg.putPixelsKeyClr(px, x, y, w, h, offset, keyclr);
  }
  public void forePutPixelsBlend(int px[], int x, int y, int w, int h, int offset) {
    fimg.putPixelsBlend(px, x, y, w, h, offset, true);
  }
  public void forePutPixelsBlendKeyClr(int px[], int x, int y, int w, int h, int offset, int keyclr) {
    fimg.putPixelsBlendKeyClr(px, x, y, w, h, offset, true, keyclr);
  }
  public void foreDrawCurve(int cx[], int cy[]) {
    fimg.getGraphics2D().draw(new CubicCurve2D.Double(cx[0],cy[0], cx[2],cy[2], cx[3],cy[3], cx[1],cy[1]));
  }
  public void foreFillBox(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fimg.getGraphics().fillRect(x1,y1,x2-x1+1,y2-y1+1);
  }
  public void foreFillRoundBox(int x1,int y1,int x2,int y2,int ax,int ay) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fimg.getGraphics().fillRoundRect(x1,y1,x2-x1+1,y2-y1+1,ax,ay);
  }
  public void foreFillCircle(int x1,int y1,int x2,int y2) {
    int tmp;
    if (x1 > x2) {tmp=x1; x1=x2; x2=tmp;}
    if (y1 > y2) {tmp=y1; y1=y2; y2=tmp;}
    fimg.getGraphics().fillOval(x1,y1,x2-x1+1,y2-y1+1);
  }
  public void foreSetFont(Font font) {
    fimg.setFont(font);
  }
  public void foreDrawText(String txt, int x, int y) {
    fimg.getGraphics().drawString(txt, x, y);
  }
  public void rotateCW() {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    int px1[] = img.getPixels();
    int p1;
    int px2[] = new int[w*h];
    int p2;
    for(int y=0;y<h;y++) {
      p1 = y * w;
      p2 = h - 1 - y;
      for(int x=0;x<w;x++) {
        px2[p2] = px1[p1++];
        p2 += h;
      }
    }
    setImageSize(h,w);
    img.putPixels(px2,0,0,h,w,0);
  }
  public void rotateCCW() {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    int px1[] = img.getPixels();
    int p1;
    int px2[] = new int[w*h];
    int p2;
    for(int y=0;y<h;y++) {
      p1 = y * w;
      p2 = h * (w-1) + y;
      for(int x=0;x<w;x++) {
        px2[p2] = px1[p1++];
        p2 -= h;
      }
    }
    setImageSize(h,w);
    img.putPixels(px2,0,0,h,w,0);
  }
  public void flipVert() {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    int px1[] = img.getPixels();
    int p1;
    int px2[] = new int[w*h];
    int p2;
    for(int y=0;y<h;y++) {
      p1 = y * w;
      p2 = p1 + w - 1;
      for(int x=0;x<w;x++) {
        px2[p2--] = px1[p1++];
      }
    }
    img.putPixels(px2,0,0,w,h,0);
  }
  public void flipHorz() {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    int px1[] = img.getPixels();
    int p1 = 0;
    int px2[] = new int[w*h];
    int p2 = w * (h-1);
    for(int y=0;y<h;y++) {
      System.arraycopy(px1, p1, px2, p2, w);
      p1 += w;
      p2 -= w;
    }
    img.putPixels(px2,0,0,w,h,0);
  }
  public void scaleImage(int ws, int hs) {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    if ((ws == 100) && (hs == 100)) return;
    int neww = w * ws / 100;
    int newh = h * hs / 100;
    if (neww < 1) neww = 1;
    if (newh < 1) newh = 1;
    JFImage tmp = new JFImage();
    tmp.setImageSize(neww, newh);
    tmp.getGraphics().drawImage(img.getImage(),0, 0, neww, newh, 0, 0, w, h, null);
    setImageSize(neww, newh);
    img.getGraphics().drawImage(tmp.getImage(), 0, 0, null);
  }
  public void createUndo() {
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    try {
      PaintCanvas undo = new PaintCanvas(parentPanel);
      undo.setImageSize(w, h);
      applyLayer();
      int px[] = img.getPixels();
      undo.img.putPixels(px, 0, 0, w, h, 0);
      while (undoPos != undos.size()-1) {
        undos.remove(undos.size()-1);  //remove redos
      }
      undos.add(undo);
      if (undos.size() > MAX_UNDO_SIZE) undos.remove(0);
      undoPos = undos.size()-1;
    } catch (Exception e) {
      System.out.println("createUndo:" + e);  //most likely out of memory
      JFLog.log(e);
    }
  }
  public void clearUndo() {
    undoPos = -1;
    undos.clear();
  }
  public void undo() {
    if (undoPos <= 0) return;
    try {
      if ((undoPos == undos.size()-1) && (undoDirty)) {
        //createRedo
        createUndo();
        undoDirty = false;
      }
      undoPos--;
      PaintCanvas undo = undos.get(undoPos);
      int orgLayer = layer;
      changeLayer(0);
      int w = getUnscaledWidth();
      int h = getUnscaledHeight();
      img.setImageSize(w, h);
      bimg.setImageSize(w, h);
      backClear();
      fimg.setImageSize(w, h);
      foreClear();
      int px[] = undo.img.getPixels();
      img.putPixels(px, 0, 0, w, h, 0);
      resizeBorder();
      if (orgLayer != 0) changeLayer(orgLayer);
      repaint();
    } catch (Exception e) {
      System.out.println("undo:" + e);  //most likely out of memory
    }
  }
  public void redo() {
    if (undos.isEmpty()) return;
    if (undoPos == undos.size()-1) return;
    try {
      undoPos++;
      PaintCanvas undo = undos.get(undoPos);
      int w = undo.getWidth();
      int h = undo.getHeight();
      img.setImageSize(w, h);
      bimg.setImageSize(w, h);
      backClear();
      fimg.setImageSize(w, h);
      foreClear();
      int px[] = undo.img.getPixels();
      img.putPixels(px, 0, 0, w, h, 0);
      resizeBorder();
      repaint();
    } catch (Exception e) {
      System.out.println("redo:" + e);  //most likely out of memory
    }
  }
  public void setDirty() {
    dirty = true;
    undoDirty = true;
  }

  public void changeLayer(int newLayer) {
    //merge current layer back to img
    applyLayer();
    layer = newLayer;
    int px[];
    int w = getUnscaledWidth();
    int h = getUnscaledHeight();
    switch (layer) {
      case 0:  //ARGB
        cimg = img;
        break;
      case 1:  //A---
        px = img.getAlphaLayer();
        limg.putPixels(px, 0, 0, w, h, 0);
        cimg = limg;
        break;
      case 2:  //-R--
        px = img.getLayer(0x00ff0000);
        limg.putPixels(px, 0, 0, w, h, 0);
        cimg = limg;
        break;
      case 3:  //--G-
        px = img.getLayer(0x0000ff00);
        limg.putPixels(px, 0, 0, w, h, 0);
        cimg = limg;
        break;
      case 4:  //---B
        px = img.getLayer(0x000000ff);
        limg.putPixels(px, 0, 0, w, h, 0);
        cimg = limg;
        break;
    }
  }

  public void applyLayer() {
    int px[];
    switch (layer) {
      case 0:  //ARGB : nothing to do
        break;
      case 1:  //A---
        px = limg.getBuffer();
        img.putAlphaLayer(px);
        break;
      case 2:  //-R--
        px = limg.getBuffer();
        img.putLayer(px, 0x00ff0000);
        break;
      case 3:  //--G-
        px = limg.getBuffer();
        img.putLayer(px, 0x0000ff00);
        break;
      case 4:  //---B
        px = limg.getBuffer();
        img.putLayer(px, 0x000000ff);
        break;
    }
  }

  public void blur(int x1,int y1,int x2,int y2,int radius) {
    int tmp;
    int width = img.getWidth(), height = img.getHeight();
    if (x1 < 0) x1 = 0;
    if (x2 < 0) x2 = 0;
    if (y1 < 0) y1 = 0;
    if (y2 < 0) y2 = 0;
    if (x1 >= width) x1 = width-1;
    if (x2 >= width) x2 = width-1;
    if (y1 >= height) y1 = height-1;
    if (y2 >= height) y2 = height-1;
    if (x1 > x2) {
      tmp = x1;
      x1 = x2;
      x2 = tmp;
    }
    if (y1 > y2) {
      tmp = y1;
      y1 = y2;
      y2 = tmp;
    }
    width = x2-x1+1;
    height = y2-y1+1;
    Kernel kernel = GaussianFilter.makeKernel(radius);
    int dst[] = new int[width * height];
    int inPixels[] = img.getPixels(x1,y1,width,height);
    int outPixels[] = dst;
    boolean alpha = false, premultiplyAlpha = false;
    if (radius > 0) {
      GaussianFilter.convolveAndTranspose(kernel, inPixels, outPixels, width, height, alpha, alpha && premultiplyAlpha, false, GaussianFilter.CLAMP_EDGES);
      GaussianFilter.convolveAndTranspose(kernel, outPixels, inPixels, height, width, alpha, false, alpha && premultiplyAlpha, GaussianFilter.CLAMP_EDGES);
    }
    img.putPixels(inPixels, x1, y1, width, height, 0);
  }

  public void mouseClicked(MouseEvent e) { panel.mouseClicked(e); }
  public void mouseEntered(MouseEvent e) { panel.mouseEntered(e); }
  public void mouseExited(MouseEvent e) { panel.mouseExited(e); }
  public void mousePressed(MouseEvent e) { panel.mousePressed(e); }
  public void mouseReleased(MouseEvent e) { panel.mouseReleased(e); }

  public void mouseDragged(MouseEvent e) { mx = e.getX() / (scale / 100); my = e.getY() / (scale / 100); panel.mouseDragged(e); }
  public void mouseMoved(MouseEvent e) { mx = e.getX() / (scale / 100); my = e.getY() / (scale / 100); panel.mouseMoved(e); }

  public void keyPressed(KeyEvent e) {}
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) { panel.keyTypedOnImage(e.getKeyChar()); }
}
