package ch.shibastudio.glcheckuptest.entities;

import android.graphics.Color;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;

import ch.shibastudio.glcheckuptest.data.VertexArray;
import ch.shibastudio.glcheckuptest.programs.LiquidShaderProgram;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;

import static android.opengl.GLES20.GL_BLEND;
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
    private final static int POSITION_COMPONENT_COUNT = 2;
    private final static int COLOR_COMPONENT_COUNT = 4;
    private final static int POINT_SIZE_COMPONENT_COUNT = 1;
    private final static int VELOCITY_COMPONENT_COUNT = 2;

    private final static int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + POINT_SIZE_COMPONENT_COUNT;
    private final static int STRIDE = TOTAL_COMPONENT_COUNT * OpenGLUtils.BYTE_PER_FLOAT;

    /***********************************************************************************************
     *
     *      -- Structure of the particles --
     *
     *      | x0 | y0 | r0 | g0 | b0 | a0 | point size 0 | x1 | y1 | r1 | g1 | b1 | a1 | ...
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

    /**
     * Adds a particle.
     * @param index as the particle index.
     * @param x as the X coordinate of the particle.
     * @param y as the Y coordinate of the particle.
     * @param color as the particle color.
     * @param particleSize as the particle size.
     */
    public void addParticle(int index, float x, float y, int color, float particleSize){
        int currentOffset = index * TOTAL_COMPONENT_COUNT;

        if(this.currentParticleCount < this.maxParticleCount){
            this.currentParticleCount++;
        }

        this.particles[currentOffset++] = x;
        this.particles[currentOffset++] = y;

        this.particles[currentOffset++] = (float)Color.red(color) / 255f;
        this.particles[currentOffset++] = (float)Color.green(color) / 255f;
        this.particles[currentOffset++] = (float)Color.blue(color) / 255f;
        this.particles[currentOffset++] = (float)Color.alpha(color) / 255f;

        this.particles[currentOffset++] = particleSize;

        this.vertexArray.updateBuffer(this.particles, index * TOTAL_COMPONENT_COUNT, TOTAL_COMPONENT_COUNT);
    }

    /**
     * Updates the particles.
     * @param positionBuffer as the position buffer.
     * @param colorBuffer as the color buffer.
     * @param velocityBuffer as the velocity buffer.
     */
    public void updateParticles(ByteBuffer positionBuffer, ByteBuffer colorBuffer, ByteBuffer velocityBuffer, boolean[] indexToDelete){

        // x0, y0, x1, y1, ...,xn-1, yn-1
        FloatBuffer posBuffer = positionBuffer.asFloatBuffer();

        // vx0, vy0, vx1, vy1, ...,vxn-1, vyn-1
        FloatBuffer veloBuffer = velocityBuffer.asFloatBuffer();

        int offset = 0;

        for(int n=0; n<this.currentParticleCount; n++){
            float veloX = veloBuffer.get(VELOCITY_COMPONENT_COUNT*n);
            float veloY = veloBuffer.get(VELOCITY_COMPONENT_COUNT*n+1);

            // Position
            float x = posBuffer.get((POSITION_COMPONENT_COUNT)*n);
            float y = posBuffer.get((POSITION_COMPONENT_COUNT)*n+1);

            this.particles[offset++] = x;
            this.particles[offset++] = y;

            boolean isToBeDeleted =
                    (x < -3.5) & (y < 9) |
                    (x > 3.5) & (y < 9) |
                    (y < -0.1)/* |
                    ((y < 1.8) & (y > 1.6) & (veloY < 0.000001))*/;

            // Color
            this.particles[offset++] = (colorBuffer.get(COLOR_COMPONENT_COUNT*n) & 0xFF) / 255f;
            this.particles[offset++] = (colorBuffer.get(COLOR_COMPONENT_COUNT*n+1) & 0xFF) / 255f;
            this.particles[offset++] = (colorBuffer.get(COLOR_COMPONENT_COUNT*n+2) & 0xFF) / 255f;
            this.particles[offset++] = (colorBuffer.get(COLOR_COMPONENT_COUNT*n+3) & 0xFF) / 255f;

            // Point size
            offset++;

            if(isToBeDeleted){
                indexToDelete[n] = true;
            }
        }

        // Update all the buffer in one shot.
        this.vertexArray.updateBuffer(this.particles, 0, this.currentParticleCount * TOTAL_COMPONENT_COUNT);
    }

    /**
     * Binds the data to OpenGL.
     * @param program as the program used for the binding.
     */
    public void bindData(LiquidShaderProgram program){
        int dataOffset = 0;
        this.vertexArray.setVertexAttribPointer(dataOffset, program.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;
        this.vertexArray.setVertexAttribPointer(dataOffset, program.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;
        this.vertexArray.setVertexAttribPointer(dataOffset, program.getPointSizeAttributeLocation(), POINT_SIZE_COMPONENT_COUNT, STRIDE);
    }

    /**
     * Render the liquid.
     */
    public void draw() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDrawArrays(GL_POINTS, 0, this.currentParticleCount);
        glDisable(GL_BLEND);
    }
}
