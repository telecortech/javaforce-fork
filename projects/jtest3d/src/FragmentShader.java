/**
 *
 * @author pquiring
 *
 * Created : Sept 17, 2013
 */

/*
 * uniform = set with glUniform...()
 * attribute = points to input array with glVertexAttribPointer()
 * varying = passed from vertex shader to fragment shader (shared memory)
 */

public class FragmentShader {
  public static String source =
"varying vec2 vTextureCoord;\n" +
"varying float vLength;\n" +
"\n" +
"uniform sampler2D uSampler;\n" +  //this is not set with glUniform...()
"\n" +
"void main() {\n" +
"  vec4 textureColor = texture2D(uSampler, vTextureCoord);\n" +
"  gl_FragColor = textureColor;\n" +
"}\n";
}
