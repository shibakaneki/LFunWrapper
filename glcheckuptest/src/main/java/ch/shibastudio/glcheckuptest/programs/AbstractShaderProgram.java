package ch.shibastudio.glcheckuptest.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.shibastudio.glcheckuptest.utils.FileHelper;
import ch.shibastudio.glcheckuptest.utils.ShaderHelper;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by didier on 16.08.17.
 */

public abstract class AbstractShaderProgram implements IShaderProgram {
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
    private final static String A_POINT_SIZE = "a_PointSize";

    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aPointSizeLocation;
    private final int aTextureCoordinatesLocation;

    private int textureId = 0;

    // Shader program
    protected final int program;

    public AbstractShaderProgram(@NonNull Context context, int vertexShaderResId, int fragmentShaderResId){
        this.program =
                ShaderHelper.buildProgram(
                        FileHelper.readTextFileFromResource(context, vertexShaderResId),
                        FileHelper.readTextFileFromResource(context, fragmentShaderResId));

        this.uMatrixLocation = glGetUniformLocation(this.program, U_MATRIX);
        this.uTextureUnitLocation = glGetUniformLocation(this.program, U_TEXTURE_UNIT);

        this.aPositionLocation = glGetAttribLocation(this.program, A_POSITION);
        this.aColorLocation = glGetAttribLocation(this.program, A_COLOR);
        this.aPointSizeLocation = glGetAttribLocation(this.program, A_POINT_SIZE);
        this.aTextureCoordinatesLocation = glGetAttribLocation(this.program, A_TEXTURE_COORDINATES);
    }

    @Override
    public void setTexture(int textureId){
        this.textureId = textureId;
    }

    @Override
    public boolean hasTexture(){
        return  this.textureId != 0;
    }

    @Override
    public void setUniforms(float[] matrix){
        glUniformMatrix4fv(this.uMatrixLocation, 1, false, matrix, 0);

        if(this.textureId > 0){
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId);
            glUniform1i(this.uTextureUnitLocation, 0);
        }
    }

    @Override
    public void useProgram(){
        glUseProgram(this.program);
    }

    @Override
    public int getPositionAttributeLocation(){ return this.aPositionLocation; }

    @Override
    public int getColorAttributeLocation(){ return this.aColorLocation; }

    @Override
    public int getPointSizeAttributeLocation(){ return this.aPointSizeLocation; }

    @Override
    public int getTextureCoordinatesAttributeLocation() { return this.aTextureCoordinatesLocation; }
}
