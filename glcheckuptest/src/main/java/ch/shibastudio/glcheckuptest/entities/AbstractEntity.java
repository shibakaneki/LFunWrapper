package ch.shibastudio.glcheckuptest.entities;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;

/**
 * Created by shibakaneki on 09.06.17.
 */

public abstract class AbstractEntity {
	protected final static int COORDS_PER_VERTEX = 3;

	protected final String vertexShaderCode =
			"uniform mat4 uMVPMatrix;" +
			"attribute vec4 vPosition;" +
					"void main() {" +
					"  gl_Position = uMVPMatrix * vPosition;" +
					"}";

	protected final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";

	protected int modelViewProjectionMatrixHandle;

	protected float coords[];
	protected short drawOrder[];

	protected FloatBuffer vertexBuffer;
	protected ShortBuffer drawListBuffer;

	protected final int program;
	protected final int vertextStride = COORDS_PER_VERTEX * 4;

	protected int positionHandle;
	protected int colorHandle;

	protected float color[];

	protected float rotationMatrix[] = new float[16];
	protected float translationMatrix[] = new float[16];
	protected float scaleMatrix[] = new float[16];
	protected float transformationMatrix[] = new float[16];
	private float tempMatrix[] = new float[16];

	private float x;
	private float y;
	private float angle;
	private float scale;

	protected int drawingMode = GLES20.GL_TRIANGLES;
	protected float lineWidth = 0.0f;

	public AbstractEntity(){
		this(new float[]{0.0f, 0.0f, 0.0f, 0.0f});
	}

	public AbstractEntity(float[] color){
		this.color = color;
		this.program = GLES20.glCreateProgram();
	}

	/**
	 * Gets the X value.
	 * @return the X value.
	 */
	public float getX(){ return this.x;}

	/**
	 * Sets the X value.
	 * @param x as the given X value.
	 */
	public void setX(float x){
		this.x = x;
	}

	/**
	 * Gets the Y value.
	 * @return the Y value.
	 */
	public float getY(){return this.y;}

	/**
	 * Sets the Y value.
	 * @param y as the given Y value.
	 */
	public void setY(float y){
		this.y = y;
	}

	/**
	 * Sets the position of the entity.
	 * @param x as the given X position.
	 * @param y as the given Y position.
	 */
	public void setPosition(float x, float y){
		Matrix.setIdentityM(this.translationMatrix, 0);
		Matrix.translateM(this.translationMatrix, 0, x, y, 0.0f);
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the angle.
	 * @return the angle.
	 */
	public float getAngle(){return this.angle;}

	/**
	 * Sets the angle.
	 * @param angle as the given angle in degree.
	 */
	public void setAngle(float angle){
		this.angle = -angle;
		Matrix.setIdentityM(this.rotationMatrix, 0);
		Matrix.rotateM(this.rotationMatrix, 0, -angle, 0, 0, -1.0f);
	}

	/**
	 * Gets the scale.
	 * @return the scale.
	 */
	public float getScale(){ return this.scale; }

	/**
	 * Sets the scale.
	 * @param scale as the scale to set.
	 */
	public void setScale(float scale){
		Matrix.setIdentityM(this.scaleMatrix, 0);
		Matrix.scaleM(this.scaleMatrix, 0, scale, scale, 1.0f);
	}

	protected void init(){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * 4); // 4 bytes per float
		byteBuffer.order(ByteOrder.nativeOrder());
		this.vertexBuffer = byteBuffer.asFloatBuffer();
		this.vertexBuffer.put(coords);
		this.vertexBuffer.position(0);

		ByteBuffer drawListByteBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2); // 2 bytes per short
		drawListByteBuffer.order(ByteOrder.nativeOrder());
		this.drawListBuffer = drawListByteBuffer.asShortBuffer();
		this.drawListBuffer.put(drawOrder);
		this.drawListBuffer.position(0);

		int vertexShader = OpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = OpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		GLES20.glAttachShader(this.program, vertexShader);
		GLES20.glAttachShader(this.program, fragmentShader);
		GLES20.glLinkProgram(this.program);

		Matrix.setIdentityM(this.rotationMatrix, 0);
		Matrix.setIdentityM(this.translationMatrix, 0);
		Matrix.setIdentityM(this.scaleMatrix, 0);
		Matrix.setIdentityM(this.transformationMatrix, 0);
	}

	/**
	 * Renders the entity. (should abstract this)
	 */
	public void render(float[] mvpMatrix){
		float[] resultMatrix = mvpMatrix.clone();

		this.updateTansformationMatrix(mvpMatrix);

		Matrix.multiplyMM(resultMatrix, 0, mvpMatrix, 0, this.transformationMatrix, 0);

		GLES20.glUseProgram(this.program);
		this.positionHandle = GLES20.glGetAttribLocation(this.program, "vPosition");
		GLES20.glEnableVertexAttribArray(this.positionHandle);
		GLES20.glVertexAttribPointer(this.positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertextStride, vertexBuffer);
		this.colorHandle = GLES20.glGetUniformLocation(this.program, "vColor");
		GLES20.glUniform4fv(this.colorHandle, 1, color, 0);

		this.modelViewProjectionMatrixHandle = GLES20.glGetUniformLocation(this.program, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(this.modelViewProjectionMatrixHandle, 1, false, resultMatrix, 0);
		GLES20.glLineWidth(this.lineWidth);
		GLES20.glDrawElements(this.drawingMode, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		GLES20.glDisableVertexAttribArray(this.positionHandle);
	}

	/**
	 * Updates the transformation matrix.
	 */
	protected void updateTansformationMatrix(float[] mvpMatrix){
		// /!\ The correct order is: translation * rotation * scaling
		Matrix.setIdentityM(this.transformationMatrix, 0);
		Matrix.multiplyMM(this.tempMatrix, 0, this.translationMatrix, 0, this.rotationMatrix, 0);
		Matrix.multiplyMM(this.transformationMatrix, 0, this.tempMatrix, 0, this.scaleMatrix, 0);
	}
}
