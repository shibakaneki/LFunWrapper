package ch.shibastudio.glcheckuptest.entities;

import android.graphics.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import ch.shibastudio.glcheckuptest.data.VertexArray;
import ch.shibastudio.glcheckuptest.geometry.Point;
import ch.shibastudio.glcheckuptest.geometry.Vector;
import ch.shibastudio.glcheckuptest.programs.LiquidShaderProgram;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

/**
 * Created by didier on 16.08.17.
 */

public class LiquidEntity {
    private final static int POSITION_COMPONENT_COUNT = 3;
    private final static int COLOR_COMPONENT_COUNT = 4;
    private final static int POINT_SIZE_COMPONENT_COUNT = 2;

    private final static int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + POINT_SIZE_COMPONENT_COUNT;
    private final static int STRIDE = TOTAL_COMPONENT_COUNT * OpenGLUtils.BYTE_PER_FLOAT;

    /***********************************************************************************************
     *
     *      -- Structure of the particles --
     *
     *      | x0 | y0 | z0 | r0 | g0 | b0 | a0 | pointSize0 | screen point size | x1 | y1 | z1 | r1 | g1 | b1 | a1 | ...
     *
     **********************************************************************************************/
    private final float[] particles;
    private final VertexArray vertexArray;
    private final int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    private final Random random = new Random();

    public LiquidEntity(int maxParticleCount){
        this.maxParticleCount = maxParticleCount;

        this.particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        this.vertexArray = new VertexArray(this.particles);
    }

    /**
     * Gets the number of particle.
     * @return the particle count.
     */
    public int getParticleCount(){
        return this.currentParticleCount;
    }

    public void addParticle(Point position, int color, float particleSize){
        final int particleOffset = this.nextParticle * TOTAL_COMPONENT_COUNT;
        int currentOffset = particleOffset;
        this.nextParticle++;

        if(this.currentParticleCount < this.maxParticleCount){
            this.currentParticleCount++;
        }

        if(this.nextParticle == this.maxParticleCount){
            this.nextParticle = 0;
        }

        this.particles[currentOffset++] = position.x;
        this.particles[currentOffset++] = position.y;
        this.particles[currentOffset++] = position.z;

        this.particles[currentOffset++] = (float)Color.red(color) / 255f;
        this.particles[currentOffset++] = (float)Color.green(color) / 255f;
        this.particles[currentOffset++] = (float)Color.blue(color) / 255f;
        this.particles[currentOffset++] = (float)Color.alpha(color) / 255f;

        this.particles[currentOffset++] = Math.max(0.40f /*0.71f*/, this.random.nextFloat());
        this.particles[currentOffset++] = 1.4f*particleSize;

        this.vertexArray.updateBuffer(this.particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    /**
     * Updates the particles.
     * @param positionBuffer as the position buffer.
     * @param colorBuffer as the color buffer.
     */
    public void updateParticles(ByteBuffer positionBuffer, ByteBuffer colorBuffer){
        final int POS_ELEM_COUNT = 2;
        final int COLOR_ELEM_COUNT = 4;

        // x0, y0, x1, y1, ...,xn-1, yn-1
        FloatBuffer posBuffer = positionBuffer.asFloatBuffer();

        // r0, g0, b0, a0, r1, g1, b1, a1, ..., rn-1, gn-1, bn-1, an-1
        FloatBuffer colBuffer = colorBuffer.asFloatBuffer();

        int offset = 0;

        for(int n=0; n<this.currentParticleCount; n++){
            // Position
            this.particles[offset++] = posBuffer.get(POS_ELEM_COUNT*n);
            this.particles[offset++] = posBuffer.get(POS_ELEM_COUNT*n+1);
            this.particles[offset++] = 0f;

            // Color
            this.particles[offset++] = (colorBuffer.get(COLOR_ELEM_COUNT*n) & 0xFF) / 255f;
            this.particles[offset++] = (colorBuffer.get(COLOR_ELEM_COUNT*n+1) & 0xFF) / 255f;
            this.particles[offset++] = (colorBuffer.get(COLOR_ELEM_COUNT*n+2) & 0xFF) / 255f;
            this.particles[offset++] = (colorBuffer.get(COLOR_ELEM_COUNT*n+3) & 0xFF) / 255f;

            offset++;
            offset++;
        }

        // Update all the buffer in one shot.
        this.vertexArray.updateBuffer(this.particles, 0, this.currentParticleCount * TOTAL_COMPONENT_COUNT);
    }

    public void bindData(LiquidShaderProgram program){
        int dataOffset = 0;
        this.vertexArray.setVertexAttribPointer(dataOffset, program.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;
        this.vertexArray.setVertexAttribPointer(dataOffset, program.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;
        this.vertexArray.setVertexAttribPointer(dataOffset, program.getPointSizeAttributeLocation(), POINT_SIZE_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDrawArrays(GL_POINTS, 0, this.currentParticleCount);
        glDisable(GL_BLEND);
    }
}
