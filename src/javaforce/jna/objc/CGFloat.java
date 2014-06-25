package javaforce.jna.objc;

/** CGFloat is float on 32bit and double on 64bit system.
 *
 * @author pquiring
 *
 * Created : May 28, 2014
 */

import com.sun.jna.*;
import java.util.Arrays;
import java.util.List;

public class CGFloat extends Structure implements Structure.ByValue {
  public NativeLong value;

  public CGFloat() {
    value = new NativeLong(0);
  }

  public CGFloat(double d) {
    switch (NativeLong.SIZE) {
      case 4:
        int bits32 = Float.floatToIntBits((float)d);
        value = new NativeLong(bits32);
        break;
      case 8:
        long bits64 = Double.doubleToLongBits(d);
        value = new NativeLong(bits64);
        break;
    }
  }

  public double doubleValue() {
    switch (NativeLong.SIZE) {
      case 4:
        int bits32 = value.intValue();
        return Float.intBitsToFloat(bits32);
      case 8:
        long bits64 = value.longValue();
        return Double.longBitsToDouble(bits64);
    }
    return 0.0;
  }

  protected List getFieldOrder() {
    return Arrays.asList(new String[]{"value"});
  }
}
