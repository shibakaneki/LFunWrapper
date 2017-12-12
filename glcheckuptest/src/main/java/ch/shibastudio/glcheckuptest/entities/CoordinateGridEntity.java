package ch.shibastudio.glcheckuptest.entities;

import android.opengl.GLES20;

/**
 * Created by shibakaneki on 09.06.17.
 */

public class CoordinateGridEntity extends AbstractEntity {

	public CoordinateGridEntity(){
		super();

		super.color = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
		coords = new float[]{
				0.0f, -1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				-1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,

				// X Graduations
				-0.5f, 0.025f, 0.0f,
				-0.5f, -0.025f, 0.0f,
				0.5f, 0.025f, 0.0f,
				0.5f, -0.025f, 0.0f,
				-1.0f, 0.025f, 0.0f,
				-1.0f, -0.025f, 0.0f,
				1.0f, 0.025f, 0.0f,
				1.0f, -0.025f, 0.0f,

				// Y Graduations
				-0.025f, -0.5f, 0.0f,
				0.025f, -0.5f, 0.0f,
				-0.025f, -1.0f, 0.0f,
				0.025f, -1.0f, 0.0f,
				-0.025f, 0.5f, 0.0f,
				0.025f, 0.5f, 0.0f,
				-0.025f, 1.0f, 0.0f,
				0.025f, 1.0f, 0.0f
		};

		super.drawOrder = new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};

		super.init();
	}

	@Override
	public void render(float[] mvpMatrix){
		GLES20.glUseProgram(this.program);
		this.positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
		GLES20.glEnableVertexAttribArray(this.positionHandle);
		GLES20.glVertexAttribPointer(this.positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertextStride, vertexBuffer);
		this.colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
		GLES20.glUniform4fv(this.colorHandle, 1, color, 0);

		this.modelViewProjectionMatrixHandle = GLES20.glGetUniformLocation(this.program, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(this.modelViewProjectionMatrixHandle, 1, false, mvpMatrix, 0);

		GLES20.glDrawElements(GLES20.GL_LINES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		GLES20.glDisableVertexAttribArray(this.positionHandle);
	}
}
