package ch.shibastudio.glcheckuptest.renderers;

import android.app.Activity;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.shibastudio.glcheckuptest.CheckupConstants;
import ch.shibastudio.glcheckuptest.ParticleBuffer;
import ch.shibastudio.glcheckuptest.ParticleRenderer;
import ch.shibastudio.glcheckuptest.entities.AbstractEntity;
import ch.shibastudio.glcheckuptest.entities.RectEntity;
import ch.shibastudio.glcheckuptest.shaders.ShaderProgramOld;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;
import ch.shibastudio.liquidwrapper.collision.shapes.PolygonShape;
import ch.shibastudio.liquidwrapper.common.Vec2;
import ch.shibastudio.liquidwrapper.dynamics.Body;
import ch.shibastudio.liquidwrapper.dynamics.BodyDef;
import ch.shibastudio.liquidwrapper.dynamics.BodyType;
import ch.shibastudio.liquidwrapper.dynamics.FixtureDef;
import ch.shibastudio.liquidwrapper.dynamics.World;
import ch.shibastudio.liquidwrapper.particle.ParticleColor;
import ch.shibastudio.liquidwrapper.particle.ParticleFlag;
import ch.shibastudio.liquidwrapper.particle.ParticleGroup;
import ch.shibastudio.liquidwrapper.particle.ParticleGroupDef;
import ch.shibastudio.liquidwrapper.particle.ParticleGroupFlag;
import ch.shibastudio.liquidwrapper.particle.ParticleSystem;
import ch.shibastudio.liquidwrapper.particle.ParticleSystemDef;

/**
 * Created by shibakaneki on 11.07.17.
 */

public class ParticleTestRenderer implements GLSurfaceView.Renderer {
	private final static float FPS = 60f;
	private final static float TIME_STEP = 1f / FPS;
	private final static float WORLD_HEIGHT = 10f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final int PARTICLE_ITERATIONS = 5;
	public static final float PARTICLE_RADIUS = 0.06f;
	public static final float PARTICLE_REPULSIVE_STRENGTH = 0.5f;

	private static ParticleTestRenderer instance;

	private final float[] modelViewProjectionMatrix = new float[16];
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];

	public float worldHeight;
	public float worldWidth;
	public int screenWidth;
	public int screenHeight;

	private World world;
	private ParticleSystem particleSystem;
	private Lock worldLock = new ReentrantLock();
	private volatile boolean isRunning = false;
	private ParticleRenderer particleRenderer;
	private Activity activity;

	private ParticleBuffer particleBuffer;

	private RectEntity squareEntity;
	private Body squareBody;

	private ParticleColor particleColor = new ParticleColor();
	public static ParticleTestRenderer getInstance(){
		if(null == instance){
			instance = new ParticleTestRenderer();
		}
		return instance;
	}

	public void init(Activity activity) {
		this.activity = activity;
		this.particleRenderer = new ParticleRenderer();
		reset();
	}

	private ParticleTestRenderer(){
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if(null == this.world){
			throw new IllegalStateException("Init world before rendering");
		}

		// Load all shaders
		ShaderProgramOld.loadAllShaders(this.activity.getAssets());

		this.particleRenderer.onSurfaceCreated(this.activity);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;

		this.worldHeight = WORLD_HEIGHT;
		this.worldWidth = WORLD_HEIGHT * ratio;
		this.screenWidth = width;
		this.screenHeight = height;

		Matrix.frustumM(this.projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		Matrix.setLookAtM(this.viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(this.modelViewProjectionMatrix, 0, this.projectionMatrix, 0, this.viewMatrix, 0);

		this.particleRenderer.onSurfaceChanged(width, height);

		// --------------------------------------------------------------------------------------
		// Creating the square
		BodyDef bd = new BodyDef();
		bd.setType(BodyType.staticBody);
		bd.setPosition(0, this.worldHeight/2f);
		bd.setAngle((float)Math.toRadians(45d));
		this.squareBody = this.world.createBody(bd);
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(0.5f, 0.5f);
		FixtureDef wall1FixtureDef = new FixtureDef();
		wall1FixtureDef.setShape(polygonShape);
		wall1FixtureDef.setDensity(1.0f);
		wall1FixtureDef.setFriction(1.0f);
		wall1FixtureDef.setRestitution(0.2f);
		this.squareBody.createFixture(wall1FixtureDef);
		this.squareEntity = new RectEntity(
				0.0f,
				0.0f,
				OpenGLUtils.getOpenGLDimension(1.0f, this.worldWidth),
				OpenGLUtils.getOpenGLDimension(1.0f, this.worldWidth)
		);
		// --------------------------------------------------------------------------------------

		// TODO: Add particles

		// Create the particles.
		int particleCount = 1;
		float radius = 0.4f;
		this.particleBuffer = new ParticleBuffer(particleCount);
		for(int i=0; i<particleCount; i++){
			float x = (i * 5) % this.screenWidth;
			float y = 0f;
			Vec2 worldPoint = new Vec2(
					this.worldWidth * x / this.screenWidth,
					this.worldHeight * (this.screenHeight - y) / this.screenHeight);
			this.particleBuffer.addParticle(worldPoint);
		}

		// Create the particle group
		this.setColor(Color.RED);
		ParticleGroupDef pgd = new ParticleGroupDef();
		pgd.setFlags(ParticleFlag.waterParticle | ParticleFlag.colorMixingParticle);
		pgd.setGroupFlags(ParticleGroupFlag.particleGroupCanBeEmpty);
		pgd.setLinearVelocity(new Vec2(0f, 0f));
		pgd.setColor(this.particleColor);
		this.particleBuffer.getBuffer().position(0);
//		pgd.setCircleShapesFromVertexList(
//				this.particleBuffer.getBuffer().slice(), this.particleBuffer.count(),
//				radius);

		ParticleSystem ps = this.getParticleSystem();
		try{
			ParticleGroup pg = ps.createParticleGroup(pgd);
			pgd.delete();
		}finally {
			this.releaseParticleSystem();
		}
	}

	public void setColor (int color) {
		// Convert ABGR back into ParticleColor
		// Box2D doesn't have this functionality,
		// check why color is stored as an int to begin with.
		short a = (short) (color >> 24 & 0xFF);
		short b = (short) (color >> 16 & 0xFF);
		short g = (short) (color >> 8 & 0xFF);
		short r = (short) (color & 0xFF);
		this.particleColor.set(r, g, b, a);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		this.update(TIME_STEP);
		this.updateObjectPosition(this.squareEntity, this.squareBody);
		this.render();
	}

	/**
	 * Starts the simulation.
	 */
	public void start(){
		this.isRunning = true;
	}

	/**
	 * Pauses the simulation.
	 */
	public void pause(){
		this.isRunning = false;
	}

	/**
	 * Resets everything.
	 */
	public void reset(){
		World world = this.getWorld();
		try {
			this.deleteWorld();
			this.world = new World(0, -10.0f);
			this.initParticleSystem();
		}finally {
			this.releaseWorld();
		}
	}

	@Override
	protected void finalize(){
		this.deleteWorld();
	}

	/**
	 * Updates the simulation.
	 * @param dt as the elapsed delta time.
	 */
	private void update(float dt){
		if(this.isRunning){
			this.particleRenderer.update(dt);
			World world = this.getWorld();
			try{
				world.step(dt, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
			}finally {
				this.releaseWorld();
			}
		}
	}

	/**
	 * Renders the elements.
	 */
	private void render(){
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// Draw square
		this.squareEntity.render(this.modelViewProjectionMatrix);

		// Draw particles
		this.particleRenderer.render();
	}

	private void updateObjectPosition(AbstractEntity entity, Body body){
		Vec2 pos = body.getPosition();
		float angle = body.getAngle();

		float entityCoords[] = OpenGLUtils.convertToOpenGLCoordinates(new float[]{pos.getX(), pos.getY(), 0.0f}, this.worldHeight);
		entity.setPosition(entityCoords[0], entityCoords[1]);
		entity.setAngle((float)Math.toDegrees((angle)));
	}

	/**
	 * Initializes the particle system.
	 */
	private void initParticleSystem(){
		World world = this.getWorld();
		try{
			ParticleSystemDef psDef = new ParticleSystemDef();
			psDef.setRadius(PARTICLE_RADIUS);
			psDef.setRepulsiveStrength(PARTICLE_REPULSIVE_STRENGTH);
			this.particleSystem = world.createParticleSystem(psDef);
			this.particleSystem.setMaxParticleCount(CheckupConstants.MAX_PARTICLE_COUNT);
			psDef.delete();
		}finally{
			this.releaseWorld();
		}
	}

	/**
	 * Gets the world.
	 * @return the world.
	 */
	public World getWorld(){
		this.worldLock.lock();
		return this.world;
	}

	/**
	 * Releases the world lock.
	 */
	public void releaseWorld(){
		this.worldLock.unlock();
	}

	/**
	 * Gets the particle system.
	 * @return the particle system.
	 */
	public ParticleSystem getParticleSystem(){
		this.worldLock.lock();
		return this.particleSystem;
	}

	/**
	 * Releases the particle system.
	 */
	public void releaseParticleSystem(){
		this.worldLock.unlock();
	}

	/**
	 * Deletes the world.
	 */
	private void deleteWorld(){
		World world = this.getWorld();
		try{
			if (world != null) {
				world.delete();
				this.world = null;
				this.particleSystem = null;
			}
		}finally{
			this.releaseWorld();
		}
	}
}
