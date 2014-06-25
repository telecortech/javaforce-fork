package javaforce.jna.objc;

/** NSRect
 *
 * BUG : Passing this Structure ByValue should work but it does NOT!!!
 * So I just pass the 4 fields (x,y,width,height) to the function call and it works.
 * Has something to do with CGFloat but I can't make it work.
 * Could be a JNA bug ???  Bad calling convention on Mac???
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */
import java.awt.geom.*;
import java.util.*;

import com.sun.jna.*;

import javaforce.*;

public class NSRect extends Structure implements Structure.ByValue {
  public NSPoint origin;
  public NSSize size;

  public NSRect() {
    this(new NSPoint(0, 0), new NSSize());
  }

  public NSRect(NSPoint origin, NSSize size) {
    this.origin = origin;
    this.size = size;
  }

  public NSRect(Point2D origin, Dimension2D size) {
    this.origin = new NSPoint(origin);
    this.size = new NSSize(size);
  }

  public NSRect(Rectangle2D rect) {
    this.origin = new NSPoint(rect.getX(), rect.getY());
    this.size = new NSSize(rect.getWidth(), rect.getHeight());
  }

  public NSRect(double w, double h) {
    this.origin = new NSPoint(0, 0);
    this.size = new NSSize(w, h);
  }

  public NSRect(double x, double y, double w, double h) {
    this.origin = new NSPoint(x, y);
    this.size = new NSSize(w, h);
  }

  public Rectangle2D getBounds() {
    return new Rectangle2D.Double(origin.x.doubleValue(), origin.y.doubleValue(), size.width.doubleValue(), size.height.doubleValue());
  }

  protected List getFieldOrder() {
    return Arrays.asList(new String[]{"origin", "size"});
  }

  public void print() {
    JFLog.log("rect:" + origin.x + "," + origin.y + ":" + size.width + "," + size.height);
  }
}
