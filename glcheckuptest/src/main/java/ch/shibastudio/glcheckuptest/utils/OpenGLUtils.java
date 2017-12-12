package ch.shibastudio.glcheckuptest.utils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by shibakaneki on 07.06.17.
 */
public class OpenGLUtils {
	private final static String TAG = "GLES";
	public final static boolean IS_DEBUG = false;
	public final static int BYTE_PER_FLOAT = 4;

	/**
	 * Loads the given shader.
	 *
	 * @param type as the shader type.
	 * @param code as the shader code.
	 * @return the shader.
	 */
	public static int loadShader(int type, String code) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, code);
		GLES20.glCompileShader(shader);
		return shader;
	}

	/**
	 * Converts the given world coordinates to OpenGL coordinates.
	 *
	 * @param worldCoords as the given world coordinates (x, y, z).
	 * @param wWorld      as the world width.
	 * @return the opengl coordinates as an array of [x, y, z]
	 */
	public static float[] convertToOpenGLCoordinates(float[] worldCoords, float wWorld) {
		float openglCoord[] = new float[3];

		openglCoord[0] = getOpenGLDimension(worldCoords[0], wWorld);
		openglCoord[1] = convertToOpenGLYCoordinate(worldCoords[1], wWorld);
		openglCoord[2] = worldCoords[2];

		return openglCoord;
	}

	/**
	 * Converts the given world coordinate to the OpenGL coordinate.
	 *
	 * @param coord  as the given world coordinate.
	 * @param wWorld as the world width.
	 * @return the OpenGL coordinate.
	 */
	public float convertToOpenGLXCoordinate(float coord, float wWorld) {
		float deltaGL = 2.0f;
		return coord * deltaGL / wWorld;
	}

	/**
	 * Converts the given world coordinate to the OpenGL coordinate.
	 *
	 * @param coord  as the given world coordinate.
	 * @param wWorld as the world width.
	 * @return the OpenGL coordinate.
	 */
	public static float convertToOpenGLYCoordinate(float coord, float wWorld) {
		return (2.0f * coord / wWorld) - 1.0f;
	}

	/**
	 * Gets the OpenGL dimension related to the given World dimension
	 * Note: it must not be used for coordinates.
	 *
	 * @param worldDimension as the given world dimension.
	 * @param wWorld         as the world width.
	 * @return the OpenGL dimension.
	 */
	public static float getOpenGLDimension(float worldDimension, float wWorld) {
		float deltaGL = 2.0f;
		return worldDimension * deltaGL / wWorld;
	}

	/**
	 * Checks to see if a GLES error has been raised.
	 */
	public static void checkGlError(String op) {
		int error = GLES20.glGetError();
		if (error != GLES20.GL_NO_ERROR) {
			String msg = op + ": glError 0x" + Integer.toHexString(error);
			Log.e(TAG, msg);
			throw new RuntimeException(msg);
		}
	}
}
