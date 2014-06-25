/** Convert different arrays fast.
 *
 * @author pquiring
 *
 * Created : Sept 20, 2013
 */

import java.nio.*;

public class ArrayConv {
  public static short[] byteArray2shortArray(byte input[]) {
    short output[] = new short[input.length/2];
    ByteBuffer.wrap(input).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(output);
    return output;
  }
  public static int[] byteArray2intArray(byte input[]) {
    int output[] = new int[input.length/4];
    ByteBuffer.wrap(input).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(output);
    return output;
  }
  public static byte[] shortArray2byteArray(short input[]) {
    ByteBuffer bb = ByteBuffer.allocate(input.length * 2);
    bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(input);
    return bb.array();
  }
  public static byte[] intArray2byteArray(int input[]) {
    ByteBuffer bb = ByteBuffer.allocate(input.length * 4);
    bb.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().put(input);
    return bb.array();
  }
}
