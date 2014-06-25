package javaforce.jna.objc;

/** QTCaptureDecompressedVideoOutput
 *
 * @author pquiring
 *
 * Created : Jun 8, 2014
 */

public class QTCaptureDecompressedVideoOutput extends QTCaptureOutput {
  public String getClassName() {
    return "QTCaptureDecompressedVideoOutput";
  }

  public void setDelegate(QTCaptureDecompressedVideoOutputDelegate delegate) {
    ObjectiveC.invokeVoid(obj, "setDelegate:", delegate.obj);
  }

  public void setPixelBufferAttributes(NSDictionary dict) {
    ObjectiveC.invokeVoid(obj, "setPixelBufferAttributes:", dict.obj);
  }
}
