package ch.shibastudio.glcheckuptest.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ch.shibastudio.glcheckuptest.ParticleRenderer;
import ch.shibastudio.glcheckuptest.renderers.ParticleTestRenderer;

/**
 * Created by shibakaneki on 12.07.17.
 */

public class WaterParticleMaterial extends Material {
	private static final String TAG = "WaterParticleMaterial";
	private static final String DIFFUSE_TEXTURE_NAME = "uDiffuseTexture";

	private float mParticleSizeScale;
	// Parameters for adding in particle weight.
	// 0: Scale - decreases the range of values
	// 1: Range shift - shift the range from [0.0, inf) to [value, inf) so we
	//    get a less abrupt dropoff.
	// 2: Cutoff - values above this will affect color
	private final float[] mWeightParams = new float[3];

	public WaterParticleMaterial(Context context, JSONObject json) {
		super(new ShaderProgramOld("water_particle.glslv", "particle.glslf"));

		// Read in values from the JSON file
		mParticleSizeScale =
				(float) json.optDouble("particleSizeScale", 1.0);

		// Scale of weight. This changes values from [0.0, max) to
		// [0.0, max*scale).
		mWeightParams[0] = (float) json.optDouble("weightScale", 1.0);

		// Range shift. This shifts values from [0.0, max) to
		// [range shift, max + range shift), so we take into account particles
		// with a small weight for a smoother curve.
		mWeightParams[1] = (float) json.optDouble("weightRangeShift", 0.0);

		// Cutoff. This means particles with a weight less than the cutoff
		// will not have any weight applied.
		mWeightParams[2] = (float) json.optDouble("weightCutoff", 1.0);

		// Add the water texture that is scrolling
		try {
			String textureName = json.getString(DIFFUSE_TEXTURE_NAME);
			addTexture(DIFFUSE_TEXTURE_NAME, new Texture(context, textureName));
		} catch (JSONException ex) {
			Log.e(TAG, "Missing point sprite texture!\n" + ex.getMessage());
		}
	}

	@Override
	public void beginRender() {
		super.beginRender();

		// Specific uniforms to this material
		GLES20.glUniform1f(
				getUniformLocation("uPointSize"),
				Math.max(1.0f, mParticleSizeScale * ParticleRenderer.FB_SIZE *
						(ParticleTestRenderer.PARTICLE_RADIUS / ParticleTestRenderer.getInstance().worldHeight)));
		GLES20.glUniform3fv(getUniformLocation("uWeightParams"), 1, mWeightParams, 0);
	}
}
