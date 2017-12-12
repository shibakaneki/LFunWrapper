package ch.shibastudio.glcheckuptest.utils;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by didier on 16.08.17.
 */

public class ShaderHelper {
    private static final String LOG_TAG = "SHADER_HELPER";

    /**
     * Compiles the given vertex shader.
     * @param shaderCode as the given vertex shader.
     * @return a handle of the compiled shader.
     */
    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * Compiles the given fragment shader.
     * @param shaderCode as the given fragment shader.
     * @return a handle of the compiled shader.
     */
    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * Compiles the given shader.
     * @param type as the shader type.
     * @param shaderCode as the given shader.
     * @return a handle of the compiled shader.
     */
    private static int compileShader(int type, String shaderCode){
        final int shaderObjectId = glCreateShader(type);
        if(0 == shaderObjectId){
            Log.w(LOG_TAG, "Couldn't create a new shader.");
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        if(0 == compileStatus[0]){
            glDeleteShader(shaderObjectId);
            Log.w(LOG_TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderObjectId;
    }

    /**
     * Link the given shaders.
     * @param vertexShaderId as the vertex shader id.
     * @param fragmentShaderId as the fragment shader id.
     * @return
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        final int programObjectId = glCreateProgram();

        if(0 == programObjectId){
            Log.w(LOG_TAG, "Couldn't create a new program");
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if(0 == linkStatus[0]){
            glDeleteProgram(programObjectId);
            return 0;
        }

        return programObjectId;
    }

    /**
     * Validates the given program.
     * @param programObjectId as the id of the program to validate.
     * @return true if valide; otherwise false.
     */
    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        return validateStatus[0] != 0;
    }

    /**
     * Builds a program.
     * @param vertexShaderSource as the vertex shader source.
     * @param fragmentShaderSource as the fragment shader source.
     * @return the program handle.
     */
    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
        int program;

        // Compile the shaders
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader, fragmentShader);
        validateProgram(program);

        return program;
    }
}
