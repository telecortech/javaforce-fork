package javaforce.jna.objc;

/**
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */
import com.sun.jna.Structure;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class NSPoint extends Structure implements Structure.ByValue {
  public CGFloat x, y;

  public NSPoint() {
    this(0, 0);
  }

  public NSPoint(double x, double y) {
    this.x = new CGFloat(x);
    this.y = new CGFloat(y);
  }

  public NSPoint(Point2D point) {
    this(point.getX(), point.getY());
  }

  public Point2D getPoint() {
    return new Point2D.Double(x.doubleValue(), y.doubleValue());
  }

  protected List getFieldOrder() {
    return Arrays.asList(new String[]{"x", "y"});
  }
}
