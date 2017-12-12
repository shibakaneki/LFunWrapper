package ch.shibastudio.glcheckuptest.entities;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;

/**
 * Created by shibakaneki on 23.06.17.
 */

public class TargetGuideRectEntity extends AbstractEntity {
	private final static float SLOPE_RATIO = 1.4f;

	private final float[] lineColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
	private final float[] backgroundColor = new float[]{0.313f, 0.823f, 0.145f, 0.3f};

	private short[] lineDrawOrder;
	private short[] backgroundDrawOrder;

	private ShortBuffer lineDrawListBuffer;
	private ShortBuffer backgroundDrawListBuffer;

	private boolean isInTarget = false;

	public TargetGuideRectEntity(float x, float y, float w, float h) {
		this(x, y, w, h, new float[]{0.0f, 0.0f, 0.0f, 1.0f});
	}

	public TargetGuideRectEntity(float x, float y, float w, float h, float[] color) {
		super(color);

		super.setX(x);
		super.setY(y);

		coords = new float[]{
				x-w/2f, y-h/2f, 0.0f,
				x-SLOPE_RATIO*w/2f, y+h/2f, 0.0f,
				x+SLOPE_RATIO*w/2f, y+h/2f, 0.0f,
				x+w/2f, y-h/2f, 0.0f
		};
		this.lineDrawOrder = new short[]{0, 1, 2, 3};
		this.backgroundDrawOrder = new short[]{0, 1, 2, 0, 2, 3};

		super.lineWidth = 4.0f;

		this.init();
	}

	/**
	 * Sets the current state.
	 * @param isInTarget as an indication whether the pressur is in target.
	 */
	public void setState(boolean isInTarget){
		this.isInTarget = isInTarget;
	}

	protected void init(){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * 4); // 4 bytes per float
		byteBuffer.order(ByteOrder.nativeOrder());
		super.vertexBuffer = byteBuffer.asFloatBuffer();
		super.vertexBuffer.put(coords);
		super.vertexBuffer.position(0);

		ByteBuffer lineDrawListByteBuffer = ByteBuffer.allocateDirect(this.lineDrawOrder.length * 2); // 2 bytes per short
		lineDrawListByteBuffer.order(ByteOrder.nativeOrder());
		this.lineDrawListBuffer = lineDrawListByteBuffer.asShortBuffer();
		this.lineDrawListBuffer.put(this.lineDrawOrder);
		this.lineDrawListBuffer.position(0);

		ByteBuffer backgroundDrawListByteBuffer = ByteBuffer.allocateDirect(this.backgroundDrawOrder.length * 2); // 2 bytes per short
		backgroundDrawListByteBuffer.order(ByteOrder.nativeOrder());
		this.backgroundDrawListBuffer = backgroundDrawListByteBuffer.asShortBuffer();
		this.backgroundDrawListBuffer.put(backgroundDrawOrder);
		this.backgroundDrawListBuffer.position(0);

		int vertexShader = OpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = OpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		GLES20.glAttachShader(this.program, vertexShader);
		GLES20.glAttachShader(this.program, fragmentShader);
		GLES20.glLinkProgram(this.program);

		Matrix.setIdentityM(super.rotationMatrix, 0);
		Matrix.setIdentityM(super.translationMatrix, 0);
		Matrix.setIdentityM(super.scaleMatrix, 0);
		Matrix.setIdentityM(super.transformationMatrix, 0);
	}

	public void render(float[] mvpMatrix){
		float[] resultMatrix = mvpMatrix.clone();

		super.updateTansformationMatrix(mvpMatrix);

		multiplyMM(resultMatrix, 0, mvpMatrix, 0, super.transformationMatrix, 0);

		glUseProgram(this.program);
		this.positionHandle = glGetAttribLocation(this.program, "vPosition");
		glEnableVertexAttribArray(this.positionHandle);
		glVertexAttribPointer(this.positionHandle, COORDS_PER_VERTEX, GL_FLOAT, false, vertextStride, vertexBuffer);

		this.colorHandle = glGetUniformLocation(this.program, "vColor");
		this.modelViewProjectionMatrixHandle = glGetUniformLocation(this.program, "uMVPMatrix");
		glUniformMatrix4fv(this.modelViewProjectionMatrixHandle, 1, false, resultMatrix, 0);

		if(this.isInTarget){
			// Draw the background
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_BLEND);
			glUniform4fv(this.colorHandle, 1, this.backgroundColor, 0);
			glLineWidth(0.0f);
			glDrawElements(GL_TRIANGLES, this.backgroundDrawOrder.length, GL_UNSIGNED_SHORT, this.backgroundDrawListBuffer);
		}

		// Draw the line
		glUniform4fv(this.colorHandle, 1, this.lineColor, 0);
		glLineWidth(super.lineWidth);
		glDrawElements(GL_LINE_LOOP, this.lineDrawOrder.length, GL_UNSIGNED_SHORT, this.lineDrawListBuffer);

		glDisableVertexAttribArray(this.positionHandle);
	}
}