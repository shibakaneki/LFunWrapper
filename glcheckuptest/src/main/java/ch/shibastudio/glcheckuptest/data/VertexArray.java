package ch.shibastudio.glcheckuptest.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static ch.shibastudio.glcheckuptest.utils.OpenGLUtils.BYTE_PER_FLOAT;

/**
 * Created by didier on 16.08.17.
 */

public class VertexArray {
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData){
        this.floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    /**
     * Sets the vertex attribute pointer.
     * @param dataOffset as the data offset.
     * @param attributeLocation as the attribute location.
     * @param componentCount as the number of components.
     * @param stride as the stride.
     */
    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride){
        this.floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, this.floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        this.floatBuffer.position(0);
    }

    /**
     * Updates the buffer.
     * @param vertexData as the data.
     * @param start as the start index.
     * @param count as the number of items to update.
     */
    public void updateBuffer(float[] vertexData, int start, int count){
        this.floatBuffer.position(start);
        this.floatBuffer.put(vertexData, start, count);
        this.floatBuffer.position(0);
    }
}
