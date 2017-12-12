package ch.shibastudio.glcheckuptest.utils;

/**
 * Created by shibakaneki on 12.07.17.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * RenderHelper
 * Data and functions to help with rendering.
 */
public class RenderHelper {
	// Vertex data
	public static final float[] SCREEN_QUAD_VERTEX_DATA = {
			-1.0f, -1.0f, 0.0f, // Position 0
			0.0f, 0.0f, // TexCoord 0
			-1.0f, 1.0f, 0.0f, // Position 1
			0.0f, 1.0f, // TexCoord 1
			1.0f, 1.0f, 0.0f, // Position 2
			1.0f, 1.0f, // TexCoord 2
			1.0f, -1.0f, 0.0f, // Position 3
			1.0f, 0.0f // TexCoord 3
	};
	public static final FloatBuffer SCREEN_QUAD_VERTEX_BUFFER;
	public static final int SCREEN_QUAD_NUM_VERTICES = 4;
	// We get the size of the vertex data in floats, and multiply with
	// sizeof(float) which is 4 bytes.
	public static final int SCREEN_QUAD_VERTEX_STRIDE =
			SCREEN_QUAD_VERTEX_DATA.length / SCREEN_QUAD_NUM_VERTICES * 4;

	static {
		SCREEN_QUAD_VERTEX_BUFFER =
				ByteBuffer.allocateDirect(SCREEN_QUAD_VERTEX_DATA.length * 4)
						.order(ByteOrder.nativeOrder()).asFloatBuffer();
		SCREEN_QUAD_VERTEX_BUFFER.put(SCREEN_QUAD_VERTEX_DATA).position(0);
	}
}
