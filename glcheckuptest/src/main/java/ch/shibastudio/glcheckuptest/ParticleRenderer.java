package ch.shibastudio.glcheckuptest;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import ch.shibastudio.glcheckuptest.renderers.ParticleTestRenderer;
import ch.shibastudio.glcheckuptest.shaders.Material;
import ch.shibastudio.glcheckuptest.shaders.WaterParticleMaterial;
import ch.shibastudio.glcheckuptest.utils.FileHelper;
import ch.shibastudio.liquidwrapper.particle.ParticleGroup;
import ch.shibastudio.liquidwrapper.particle.ParticleSystem;

/**
 * Created by shibakaneki on 11.07.17.
 */

public class ParticleRenderer {
	// Framebuffer for the particles to render on.
	public static final int FB_SIZE = 256;
	private static final String JSON_FILE = "materials/particlerenderer.json";
	private final static String LOG_TAG = "LIQUIDFUN";

	private ByteBuffer particleColorBuffer;
	private ByteBuffer particlePositionBuffer;
	private ByteBuffer particleWeightBuffer;
	private List<ParticleGroup> mParticleRenderList = new ArrayList<>(256);

	private final float[] transformFromTexture = new float[16];
	private final float[] transformFromWorld = new float[16];
	private RenderSurface renderSurface;
	private WaterParticleMaterial waterParticleMaterial;
	private ScreenRenderer waterScreenRenderer;
	private BlurRenderer blurRenderer;

	public ParticleRenderer(){
		this.particlePositionBuffer = ByteBuffer
				.allocateDirect(2 * 4 * CheckupConstants.MAX_PARTICLE_COUNT)
				.order(ByteOrder.nativeOrder());
		this.particleColorBuffer = ByteBuffer
				.allocateDirect(4 * CheckupConstants.MAX_PARTICLE_COUNT)
				.order(ByteOrder.nativeOrder());
		this.particleWeightBuffer = ByteBuffer
				.allocateDirect(4 * CheckupConstants.MAX_PARTICLE_COUNT)
				.order(ByteOrder.nativeOrder());
	}

	public void onSurfaceCreated(Context context) {
		// Create the render surfaces
		this.renderSurface = new RenderSurface(FB_SIZE, FB_SIZE);
		this.renderSurface.setClearColor(Color.argb(0, 255, 255, 255));

		// Create the blur renderer
		this.blurRenderer = new BlurRenderer();

		// Read in our specific json file
		String materialFile = FileHelper.loadAsset(context.getAssets(), JSON_FILE);
		try {
			JSONObject json = new JSONObject(materialFile);

			// Water particle material. We are utilizing the position and color
			// buffers returned from LiquidFun directly.
			this.waterParticleMaterial = new WaterParticleMaterial(
					context, json.getJSONObject("waterParticlePointSprite"));

			// Initialize attributes specific to this material
			this.waterParticleMaterial.addAttribute(
					"aPosition", 2, Material.AttrComponentType.FLOAT,
					4, false, 0);
			this.waterParticleMaterial.addAttribute(
					"aColor", 4, Material.AttrComponentType.UNSIGNED_BYTE,
					1, true, 0);
			this.waterParticleMaterial.addAttribute(
					"aWeight", 1, Material.AttrComponentType.FLOAT,
					1, false, 0);
			this.waterParticleMaterial.setBlendFunc(
					Material.BlendFactor.ONE,
					Material.BlendFactor.ONE_MINUS_SRC_ALPHA);

			// Non-water particle material. We are utilizing the position and
			// color buffers returned from LiquidFun directly.
			/*mParticleMaterial = new ParticleMaterial(
					context, json.getJSONObject("otherParticlePointSprite"));

			// Initialize attributes specific to this material
			mParticleMaterial.addAttribute(
					"aPosition", 2, Material.AttrComponentType.FLOAT,
					4, false, 0);
			mParticleMaterial.addAttribute(
					"aColor", 4, Material.AttrComponentType.UNSIGNED_BYTE,
					1, true, 0);
			mParticleMaterial.setBlendFunc(
					Material.BlendFactor.ONE,
					Material.BlendFactor.ONE_MINUS_SRC_ALPHA);*/

			// Scrolling texture when we copy water particles from FBO to screen
			this.waterScreenRenderer = new ScreenRenderer(
					context, json.getJSONObject("waterParticleToScreen"),
					this.renderSurface.getTexture());

			// Scrolling texture when we copy water particles from FBO to screen
			//mScreenRenderer = new ScreenRenderer(
			//		context, json.getJSONObject("otherParticleToScreen"),
			//		mRenderSurface[1].getTexture());

			// Texture for paper
			//JSONObject materialData = json.getJSONObject(PAPER_MATERIAL_NAME);
			//String textureName = materialData.getString(DIFFUSE_TEXTURE_NAME);
			//mPaperTexture = new Texture(context, textureName);
		} catch (JSONException ex) {
			Log.e(LOG_TAG, "Cannot parse" + JSON_FILE + "\n" + ex.getMessage());
		}
	}

	public void onSurfaceChanged(int width, int height) {
		// Set up the transform
		float ratio = (float) height / width;
		Matrix.setIdentityM(this.transformFromTexture, 0);
		Matrix.scaleM(this.transformFromTexture, 0, 1, 1 / ratio, 1);

		Matrix.setIdentityM(this.transformFromWorld, 0);
		Matrix.translateM(this.transformFromWorld, 0, -1, -ratio, 0);
		Matrix.scaleM(this.transformFromWorld,
				0,
				2f * ratio / ParticleTestRenderer.getInstance().worldWidth,
				2f / ParticleTestRenderer.getInstance().worldHeight,
				1);
	}

	/**
	 * Updates the particle.
	 * @param dt as the elapsed delta time.
	 */
	public void update(float dt){ }

	/**
	 * Renders the particles.
	 */
	public void render(){
		this.particleColorBuffer.rewind();
		this.particlePositionBuffer.rewind();
		this.particleWeightBuffer.rewind();
		this.mParticleRenderList.clear();

		ParticleSystem particleSystem = ParticleTestRenderer.getInstance().getParticleSystem();
		try{
			int worldParticleCount = particleSystem.getParticleCount();
			// grab the most current particle buffers
			particleSystem.copyPositionBuffer(0, worldParticleCount, this.particlePositionBuffer);
			particleSystem.copyColorBuffer(0, worldParticleCount, this.particleColorBuffer);
			particleSystem.copyWeightBuffer(0, worldParticleCount, this.particleWeightBuffer);

			GLES20.glClearColor(0, 0, 0, 0);

			this.particlePositionBuffer.putFloat(0, 0);
			this.particlePositionBuffer.putFloat(4, 0.5f);
			Log.d("LIQUIDFUN", "particle[0] position: " +this.particlePositionBuffer.getFloat(0) +";" +this.particlePositionBuffer.getFloat(1));

			// Draw the particles
			drawParticles();

			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			GLES20.glViewport( 0, 0, ParticleTestRenderer.getInstance().screenWidth,ParticleTestRenderer.getInstance().screenHeight);

			// Draw the paper texture.
			//AbstractTextureRenderer.getInstance().drawTexture(mPaperTexture, Renderer.MAT4X4_IDENTITY, -1, -1, 1, 1);

			// Copy the water particles to screen
			this.waterScreenRenderer.draw(this.transformFromTexture);

			// Copy the other particles to screen
			//mScreenRenderer.draw(mTransformFromTexture);
		}finally{
			ParticleTestRenderer.getInstance().releaseParticleSystem();
		}
	}

	/**
	 * Draw the particles.
	 */
	private void drawParticles(){
		this.drawWaterParticles();
	}

	/**
	 * Draws the water particles.
	 */
	private void drawWaterParticles(){
		// Draw all water particles to temp render surface 0
		this.renderSurface.beginRender(GLES20.GL_COLOR_BUFFER_BIT);
		this.waterParticleMaterial.beginRender();

		// Set attribute arrays
		this.waterParticleMaterial.setVertexAttributeBuffer("aPosition", this.particlePositionBuffer, 0);
		this.waterParticleMaterial.setVertexAttributeBuffer("aColor", this.particleColorBuffer, 0);
		this.waterParticleMaterial.setVertexAttributeBuffer("aWeight", this.particleWeightBuffer, 0);

		// Set uniforms
		GLES20.glUniformMatrix4fv(
				this.waterParticleMaterial.getUniformLocation("uTransform"),1, false, this.transformFromWorld, 0);

		// Go through each particle group
		ParticleSystem ps = ParticleTestRenderer.getInstance().getParticleSystem();
		try {
			ParticleGroup currGroup = ps.getParticleGroupList();

			while (currGroup != null) {
				// Only draw water particles in this pass; queue other groups
				//if (currGroup.getGroupFlags() ==
				//		Tool.getTool(Tool.ToolType.WATER).getParticleGroupFlags()) {
					drawParticleGroup(currGroup);
				//} else {
				//	mParticleRenderList.add(currGroup);
				//}

				currGroup = currGroup.getNext();
			}
		} finally {
			ParticleTestRenderer.getInstance().releaseParticleSystem();
		}

		this.waterParticleMaterial.endRender();

		this.renderSurface.endRender();

		this.blurRenderer.draw(this.renderSurface.getTexture(), this.renderSurface);
	}

	/**
	 * Draws the given particle group.
	 * @param pg as the given particle group.
	 */
	private void drawParticleGroup(ParticleGroup pg) {
		int particleCount = pg.getParticleCount();
		int instanceOffset = pg.getBufferIndex();
		GLES20.glDrawArrays(GLES20.GL_POINTS, instanceOffset, particleCount);
	}

	/**
	 * Resets everything.
	 */
	public void reset() {
		this.particlePositionBuffer.clear();
		this.particleColorBuffer.clear();
		this.particleWeightBuffer.clear();
	}
}
