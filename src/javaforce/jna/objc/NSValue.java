package javaforce.jna.objc;

/** NSValue
 *
 * @author pquiring
 *
 * Created : Jun 10, 2014
 */

import com.sun.jna.*;

import javaforce.*;

public class NSValue extends NSObject {
  public String getClassName() {
    return "NSValue";
  }

  public void getValue(Pointer ptr) {
    ObjectiveC.invokeVoid(obj, "getValue:", ptr);
  }

/*
  //it's technically not possible to implement this function in 64bit mode
  public NSSize sizeValue() {
    //because sizeof(NSSize) is small, it's actually returned in registers
    // like a normal return value so invokeStruct is NOT used.
    // Google "objc_msgSend_stret" to see lots of headaches...
    switch (Pointer.SIZE) {
      case 4: {
        NSSize size = new NSSize();
        long bits = ObjectiveC.invokeLong(obj, "sizeValue");
        Pointer ptr = size.getPointer();
        ptr.setLong(0, bits);
        size.read();
        return size;
      }
      case 8: {
        //BUG : HOW TO DO THIS ???
        //The return value is 128bits (2 doubles)
        //it is returned in xmm0 and xmm1
        NSSize size = new NSSize();
        longdouble bits = ObjectiveC.invokeLongDouble(obj, "sizeValue");
        Pointer ptr = size.getPointer();
        ptr.setLongDouble(0, bits);
        size.read();
        return size;
      }
    }
    return null;
  }
*/
}
