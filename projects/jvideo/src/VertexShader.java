/**
 *
 * @author pquiring
 *
 * Created : Oct 7, 2013
 */

/*
 * uniform = set with glUniform...()
 * attribute = points to input array with glVertexAttribPointer()
 * varying = passed from vertex shader to fragment shader (shared memory)
 */

public class VertexShader {
  public static String source =
"attribute vec2 aTextureCoord;\n" +
"attribute vec3 aVertexPosition;\n" +
"\n" +
"uniform mat4 uPMatrix;\n" +
"uniform mat4 uVMatrix;\n" +
"uniform mat4 uMMatrix;\n" +
"\n" +
"varying vec2 vTextureCoord;\n" +
"varying float vLength;\n" +
"\n" +
"void main() {\n" +
"  gl_Position = uPMatrix * uVMatrix * uMMatrix * vec4(aVertexPosition, 1.0);\n" +  //NOTE:order of matrix multiply matters
"  vTextureCoord = aTextureCoord;\n" +
"  vLength = length(gl_Position);\n" +
"}\n";
}
