package ch.shibastudio.glcheckuptest.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.shibastudio.glcheckuptest.utils.FileHelper;
import ch.shibastudio.glcheckuptest.utils.ShaderHelper;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by didier on 16.08.17.
 */

public class ShaderProgram {
    // Uniform constants
    protected final static String U_MATRIX = "u_Matrix";
    protected final static String U_TEXTURE_UNIT = "u_TextureUnit";
    protected final static String U_COLOR = "u_Color";
    protected final static String U_TIME = "u_Time";

    // Attribute constants
    protected final static String A_POSITION = "a_Position";
    protected final static String A_COLOR = "a_Color";
    protected final static String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected final static String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected final static String A_PARTICLE_START_TIME = "a_ParticleStartTime";

    // Shader program
    protected final int program;

    protected ShaderProgram(@NonNull Context context, int vertexShaderResId, int fragmentShaderResId){
        this.program =
                ShaderHelper.buildProgram(
                        FileHelper.readTextFileFromResource(context, vertexShaderResId),
                        FileHelper.readTextFileFromResource(context, fragmentShaderResId));

    }

    /**
     * Uses the program.
     */
    public void useProgram(){
        glUseProgram(this.program);
    }
}
