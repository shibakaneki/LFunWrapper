package ch.shibastudio.glcheckuptest.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.shibastudio.glcheckuptest.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by didier on 16.08.17.
 */

public class LiquidShaderProgram extends ShaderProgram {
    private final static String A_POINT_SIZE = "a_PointSize";

    private final int uMatrixLocation;

    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aPointSizeLocation;

    public LiquidShaderProgram(@NonNull Context context) {
        super(context, R.raw.liquid_vertex_shader, R.raw.liquid_fragment_shader);

        this.uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        this.aPositionLocation = glGetAttribLocation(program, A_POSITION);
        this.aColorLocation = glGetAttribLocation(program, A_COLOR);
        this.aPointSizeLocation = glGetAttribLocation(program, A_POINT_SIZE);
    }

    public void setUniforms(float[] matrix){
        glUniformMatrix4fv(this.uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation(){ return this.aPositionLocation; }

    public int getColorAttributeLocation(){ return this.aColorLocation; }

    public int getPointSizeAttributeLocation(){ return this.aPointSizeLocation; }
}
