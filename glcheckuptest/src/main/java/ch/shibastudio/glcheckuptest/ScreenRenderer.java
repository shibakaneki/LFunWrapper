package ch.shibastudio.glcheckuptest;

/**
 * Created by shibakaneki on 12.07.17.
 */

import android.content.Context;
import android.opengl.GLES20;

import org.json.JSONObject;

import ch.shibastudio.glcheckuptest.shaders.Material;
import ch.shibastudio.glcheckuptest.shaders.ShaderProgramOld;
import ch.shibastudio.glcheckuptest.shaders.Texture;
import ch.shibastudio.glcheckuptest.utils.RenderHelper;

/**
 * ScreenRenderer.
 * Blends a frame buffer as an input onto the final screen.
 */
public class ScreenRenderer {
	private static final String TAG = "ScreenRenderer";
	private Material mMaterial;
	private float mAlphaThreshold;

	public ScreenRenderer(Context context, JSONObject json, Texture fboTexture) {
		mMaterial = new Material(new ShaderProgramOld("texture.glslv", "screen.glslf"));

		mMaterial.addAttribute(
				"aPosition", 3, Material.AttrComponentType.FLOAT, 4, false,
				RenderHelper.SCREEN_QUAD_VERTEX_STRIDE);
		mMaterial.addAttribute(
				"aTexCoord", 2, Material.AttrComponentType.FLOAT, 4, false,
				RenderHelper.SCREEN_QUAD_VERTEX_STRIDE);

		// Add the diffuse texture: particle FBO
		mMaterial.addTexture("uDiffuseTexture", fboTexture);

		mMaterial.setBlendFunc(
				Material.BlendFactor.SRC_ALPHA,
				Material.BlendFactor.ONE_MINUS_SRC_ALPHA);

		// Read in values from the JSON file

		// Alpha threshold
		mAlphaThreshold = (float) (json.optDouble("alphaThreshold", 0.0));
	}

	/**
	 * Draw function for the geometry that this class owns.
	 */
	public void draw(float[] transformFromTexture) {RenderHelper.SCREEN_QUAD_VERTEX_BUFFER.rewind();

		mMaterial.beginRender();

		// Set attribute arrays
		mMaterial.setVertexAttributeBuffer(
				"aPosition", RenderHelper.SCREEN_QUAD_VERTEX_BUFFER, 0);
		mMaterial.setVertexAttributeBuffer(
				"aTexCoord", RenderHelper.SCREEN_QUAD_VERTEX_BUFFER, 3);

		// Set per draw uniforms
		GLES20.glUniformMatrix4fv(
				mMaterial.getUniformLocation("uMvpTransform"), 1, false,
				transformFromTexture, 0);
		GLES20.glUniform1f(
				mMaterial.getUniformLocation("uAlphaThreshold"),
				mAlphaThreshold);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

		mMaterial.endRender();
	}
}
