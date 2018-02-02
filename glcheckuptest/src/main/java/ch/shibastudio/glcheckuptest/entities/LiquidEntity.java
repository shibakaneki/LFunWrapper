package ch.shibastudio.glcheckuptest.entities;

import java.nio.ByteBuffer;
import ch.shibastudio.glcheckuptest.programs.IShaderProgram;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by didier on 16.08.17.
 */

public class LiquidEntity {
    private final static int POSITION_COMPONENT_COUNT = 2;
    private final static int COLOR_COMPONENT_COUNT = 4;
    private final static int POINT_SIZE_COMPONENT_COUNT = 1;
    private final static int VELOCITY_COMPONENT_COUNT = 2;
    private final static int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

    private final static int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + POINT_SIZE_COMPONENT_COUNT;// + TEXTURE_COORDINATES_COMPONENT_COUNT;

    public LiquidEntity(){ }

    /**
     * Binds the data to OpenGL.
     * @param program as the program used for the binding.
     */
    public void bindData(IShaderProgram program, ByteBuffer positionBuffer, ByteBuffer colorBuffer){
        glVertexAttribPointer(program.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, GL_FLOAT, false, POSITION_COMPONENT_COUNT * OpenGLUtils.BYTE_PER_FLOAT, positionBuffer);
        glEnableVertexAttribArray(program.getPositionAttributeLocation());
        glVertexAttribPointer(program.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, GL_UNSIGNED_BYTE, true, COLOR_COMPONENT_COUNT, colorBuffer);
        glEnableVertexAttribArray(program.getColorAttributeLocation());
    }

    /**
     * Render the liquid.
     */
    public void draw(int particleCount) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDrawArrays(GL_POINTS, 0, particleCount);
        glDisable(GL_BLEND);
    }
}
