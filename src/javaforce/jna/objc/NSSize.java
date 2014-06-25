package javaforce.jna.objc;

/**
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */

import java.awt.geom.*;
import java.util.*;

import com.sun.jna.*;

public class NSSize extends Structure implements Structure.ByValue {

  public CGFloat width, height;

  public NSSize() {
    this(0, 0);
  }

  public NSSize(Pointer ptr) {
    super(ptr);
  }

  public NSSize(double width, double height) {
    this.width = new CGFloat(width);
    this.height = new CGFloat(height);
  }

  public NSSize(Dimension2D pSize) {
    this(pSize.getWidth(), pSize.getHeight());
  }

  protected List getFieldOrder() {
    return Arrays.asList(new String[]{"width", "height"});
  }
}
