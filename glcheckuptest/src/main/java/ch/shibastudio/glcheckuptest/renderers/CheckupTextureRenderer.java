package ch.shibastudio.glcheckuptest.renderers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.shibastudio.glcheckuptest.CheckupConstants;
import ch.shibastudio.glcheckuptest.R;
import ch.shibastudio.glcheckuptest.WorldDescriptor;
import ch.shibastudio.glcheckuptest.entities.AbstractEntity;
import ch.shibastudio.glcheckuptest.entities.GroundLeftEntity;
import ch.shibastudio.glcheckuptest.entities.GroundRightEntity;
import ch.shibastudio.glcheckuptest.entities.InputPipeEntity;
import ch.shibastudio.glcheckuptest.entities.LiquidEntity;
import ch.shibastudio.glcheckuptest.entities.RectEntity;
import ch.shibastudio.glcheckuptest.entities.TargetGuideArrowEntity;
import ch.shibastudio.glcheckuptest.entities.TargetGuideRectEntity;
import ch.shibastudio.glcheckuptest.programs.IShaderProgram;
import ch.shibastudio.glcheckuptest.programs.LiquidShaderProgram;
import ch.shibastudio.glcheckuptest.programs.LiquidTextureShaderProgram;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;
import ch.shibastudio.glcheckuptest.utils.TextureHelper;
import ch.shibastudio.liquidwrapper.collision.shapes.PolygonShape;
import ch.shibastudio.liquidwrapper.common.Vec2;
import ch.shibastudio.liquidwrapper.dynamics.Body;
import ch.shibastudio.liquidwrapper.dynamics.BodyDef;
import ch.shibastudio.liquidwrapper.dynamics.BodyType;
import ch.shibastudio.liquidwrapper.dynamics.FixtureDef;
import ch.shibastudio.liquidwrapper.dynamics.World;
import ch.shibastudio.liquidwrapper.particle.ParticleDef;
import ch.shibastudio.liquidwrapper.particle.ParticleFlag;
import ch.shibastudio.liquidwrapper.particle.ParticleSystem;
import ch.shibastudio.liquidwrapper.particle.ParticleSystemDef;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static ch.shibastudio.glcheckuptest.CheckupConstants.INPUT_PIPE_CENTER_X;
import static ch.shibastudio.glcheckuptest.CheckupConstants.INPUT_PIPE_HEIGHT;
import static ch.shibastudio.glcheckuptest.CheckupConstants.INPUT_PIPE_WIDTH;
import static ch.shibastudio.glcheckuptest.CheckupConstants.PARTICLE_SIZE_WORLD;

/**
 * Created by shibakaneki on 06.10.17.
 */

public class CheckupTextureRenderer extends AbstractTextureRenderer {
	private final static boolean USES_TEXTURE = false;

	private final static long TARGET_BLINK = 500;

	private final static boolean USE_LIQUID = true;
	private final static float EARTH_GRAVITY = 20f;//9.81f;
	private final static float WATER_DENSITY = 998.2071f; //  20°C
	private final static float WALL_DENSITY = 1.0f;
	private final static float WALL_FRICTION = 1.0f;
	private final static float WALL_RESTITUTION = 0.2f;

	private final static float WORLD_TOP = 100f;
	private final static float WORLD_BOTTOM = 0f;
	private final static float WORLD_HEIGHT = WORLD_TOP - WORLD_BOTTOM;
	private final static int MAX_PARTICLES = 100000;
	private final static int NEW_PARTICLE_COUNT = 40;
	private final static int PARTICLE_LIFETIME = 15;
	private final static float GROUND_Y = 538f/640f;
	private final static float GROUND_H = 20f/640f;
	private final static float HOLE_W = 50f/360f;
	private final static float GROUND_W = 310f/360f;
	private final static float TARGET_ARROW_W = 39f/360f;
	private final static float TARGET_ARROW_H = 53f/640f;
	private final static float WALL1_X = 140f/360f;
	private final static float WALL1_Y = 228f/640f;
	private final static float WALL1_W = 176f/360f;
	private final static float WALL1_H = 20f/640f;
	private final static float WALL2_X = 340f/360f;
	private final static float WALL2_Y = 375f/640f;
	private final static float WALL2_W = 200f/360f; //270f/360f;
	private final static float WALL2_H = 20f/640f;
	private final static int FPS = 30;
	private final static float TIME_STEP = 1.0f / (float)FPS;
	private final static int VELOCITY_ITERATION = 5;
	private final static int POSITION_ITERATION = 2;
	private final static int PARTICLE_ITERATION = 3;
	private final int PARTICLE_COORD_COUNT = 2;
	private final int PARTICLE_VELOCITY_COUNT = 2;
	private final int PARTICLE_COLOR_COUNT = 4;
	private final static float RATIO_LEVEL_1_HOLE_TARGET = 0.26f;
	private final static float RATIO_LEVEL_2_HOLE_TARGET = 0.45f;

	private final float[] modelViewProjectionMatrix = new float[16];
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];
	private final float[] projectionMatrix2 = new float[16];
	private final float[] viewProjectionMatrix2 = new float[16];

	private static float xWHole;
	private static float groundY = 0f;
	private static float groundYGL = 0f;

	private Context context;
	private World world;
	private WorldDescriptor worldDescriptor;

	private float glLeft;
	private float glRight;
	private float deltaXW = 0.0f;
	private float deltaYW = 0.0f;
	private int level = 1;
	private float particleSize = 0f;

	// Liquid
	private LiquidEntity liquidEntity;
	private LiquidShaderProgram liquidShaderProgram;
	private ParticleDef particleDef;
	int particleColor = Color.argb(50, 0, 172, 231);

	private ParticleSystem liquidParticleSystem;
	private InputPipeEntity inputPipeEntity;
	private TargetGuideRectEntity targetGuideRectEntity;
	private TargetGuideArrowEntity targetGuideArrowEntity;
	private RectEntity wall1Entity;
	private RectEntity wall2Entity;
	private Body wall1Body;
	private Body wall2Body;
	private Body liquidJarLeftBody;
	private Body liquidJarRightBody;
	private Body liquidJarTopBody;
	private GroundLeftEntity groundLeftEntity;
	private GroundRightEntity groundRightEntity;
//	private RectEntity liquidJarLeftEntity;
//	private RectEntity liquidJarRightEntity;
//	private RectEntity liquidJarTopEntity;

	private Random random = new Random();
	private ByteBuffer posBuffer = ByteBuffer.allocateDirect(MAX_PARTICLES * PARTICLE_COORD_COUNT  * OpenGLUtils.BYTE_PER_FLOAT).order(ByteOrder.nativeOrder());
	private ByteBuffer colorBuffer = ByteBuffer.allocateDirect(MAX_PARTICLES * PARTICLE_COLOR_COUNT).order(ByteOrder.nativeOrder());

	private long lastTargetBlinkTime;
	private boolean isInTarget = false;

	private float inputLeft;
	private float inputRight;

	private int liquidTexture;
	private IShaderProgram liquidTextureProgram;

	public CheckupTextureRenderer(@NonNull Context context){
		this.context = context;
	}

	@Override
	public void initRendering() {
		this.world = new World(0f, -EARTH_GRAVITY);

		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glViewport(0, 0, super.getWidth(), super.getHeight());

		float ratio = (float) super.getWidth() / super.getHeight();
		this.glLeft = -ratio; // ratio * 2.0f /2.0f because delta y is 2.0f and we want half of it to get the left
		this.glRight = ratio; // same for left but with the reverse sign.
		Matrix.frustumM(this.projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

		float worldHalfWidth = (WORLD_HEIGHT * ratio)/2f;
		this.worldDescriptor = new WorldDescriptor(0 - worldHalfWidth, WORLD_TOP, worldHalfWidth, WORLD_BOTTOM);

		this.deltaXW = this.worldDescriptor.getRight() - this.worldDescriptor.getLeft();
		this.deltaYW = this.worldDescriptor.getTop() - this.worldDescriptor.getBottom();

		this.inputPipeEntity = new InputPipeEntity(-1.0f * ratio, 1.0f*ratio);
		this.targetGuideRectEntity = new TargetGuideRectEntity(0.0f, 0.0f, this.getOpenGLDimension(HOLE_W*deltaXW), this.getOpenGLDimension(1.2f*GROUND_H * deltaYW));
		this.targetGuideArrowEntity = new TargetGuideArrowEntity(0.0f, 0.0f, this.getOpenGLDimension(TARGET_ARROW_W * deltaXW), this.getOpenGLDimension(TARGET_ARROW_H * deltaYW));

		this.inputLeft = this.worldDescriptor.getLeft() + deltaXW * (INPUT_PIPE_CENTER_X - CheckupConstants.INPUT_PIPE_WIDTH/4f);
		this.inputRight = this.worldDescriptor.getLeft() + deltaXW * (INPUT_PIPE_CENTER_X + CheckupConstants.INPUT_PIPE_WIDTH/4f);

		// 2 x (glDeltaH) x particlesRadius / worldH x pixelH
		this.particleSize = 2f * 2f * PARTICLE_SIZE_WORLD / WORLD_HEIGHT * super.getHeight();

		this.createWalls();
		this.createGround();
		this.createJar();

		if(USE_LIQUID){
			this.createLiquid();
		}

		Matrix.setLookAtM(this.viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(this.modelViewProjectionMatrix, 0, this.projectionMatrix, 0, this.viewMatrix, 0);

		xWHole = -1.8f;

		orthoM(projectionMatrix2, 0, -ratio, ratio, -1f, 1f, -1f, 1f);

		float scaleFactor = 2f / WORLD_HEIGHT;  // OpenGL height (2f) / world height

		float[] transformationMatrix = new float[16];
		float[] translationMatrix = new float[16];
		float[] scaleMatrix = new float[16];

		setIdentityM(translationMatrix, 0);
		setIdentityM(scaleMatrix, 0);
		setIdentityM(transformationMatrix, 0);

		//translateM(translationMatrix, 0, 0f, -5f, 0f);
		translateM(translationMatrix, 0, 0f, -WORLD_HEIGHT/2f, 0f);
		scaleM(scaleMatrix, 0, scaleFactor, scaleFactor, 1f);
		multiplyMM(transformationMatrix, 0, scaleMatrix, 0, translationMatrix, 0);

		multiplyMM(viewProjectionMatrix2, 0, projectionMatrix2, 0, transformationMatrix, 0);
	}

	@Override
	public void render() {
		synchronized (this){
			// Read Inputs
			this.setTargetPos();
			this.setGroundHolePos();

			// Update Positions
			this.world.step(TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION, PARTICLE_ITERATION);
			this.updatePositions();

			if(USE_LIQUID){
				// Add random drops
				this.addParticles();
			}
		}

		// Render
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		long t = System.currentTimeMillis();
		if(t - this.lastTargetBlinkTime >= TARGET_BLINK){
			this.lastTargetBlinkTime = t;
			this.isInTarget = !this.isInTarget;
			this.targetGuideRectEntity.setState(this.isInTarget);
		}

		if(USE_LIQUID){
			this.liquidTextureProgram.useProgram();

			if(USES_TEXTURE){
				this.liquidTextureProgram.setTexture(this.liquidTexture);
			}

			this.liquidTextureProgram.setUniforms(this.viewProjectionMatrix2);
			this.liquidEntity.bindData(this.liquidTextureProgram, this.posBuffer, this.colorBuffer);
			this.liquidEntity.draw(this.liquidParticleSystem.getParticleCount());
		}

		this.wall1Entity.render(this.modelViewProjectionMatrix);
		this.wall2Entity.render(this.modelViewProjectionMatrix);
		this.groundLeftEntity.render(this.modelViewProjectionMatrix);
		this.groundRightEntity.render(this.modelViewProjectionMatrix);
		this.targetGuideRectEntity.render(this.modelViewProjectionMatrix);
		this.targetGuideArrowEntity.render(this.modelViewProjectionMatrix);

		this.inputPipeEntity.render(this.modelViewProjectionMatrix);

//		this.liquidJarTopEntity.render(this.modelViewProjectionMatrix);
//		this.liquidJarLeftEntity.render(this.modelViewProjectionMatrix);
//		this.liquidJarRightEntity.render(this.modelViewProjectionMatrix);
	}

	/**
	 * Sets the X position of the ground hole, in world coordinates.
	 */
	public void setGroundHolePos(){
		if(null == this.groundLeftEntity || null == this.groundRightEntity){
			return;
		}

		synchronized (this){
			this.groundLeftEntity.setBodyPosition(xWHole - deltaXW * (HOLE_W/2f + GROUND_W/2f), groundY);
			this.groundRightEntity.setBodyPosition(xWHole + deltaXW * (HOLE_W/2f + GROUND_W/2f), groundY);
		}
	}

	/**
	 * Sets the target position, in world coordinates.
	 */
	public void setTargetPos(){
		float x = this.glLeft + this.getTargetRatioForCurrentLevel() * (this.glRight - this.glLeft);
		float y = this.groundYGL;
		float arrowY = y + OpenGLUtils.getOpenGLDimension((GROUND_H + TARGET_ARROW_H/2f) * deltaYW, deltaYW);

		this.targetGuideRectEntity.setPosition(x, y);
		this.targetGuideArrowEntity.setPosition(x, arrowY);
	}

	/**
	 * Creates the walls.
	 */
	private void createWalls(){
		float deltaXW = this.worldDescriptor.getRight() - this.worldDescriptor.getLeft();
		float deltaYW = this.worldDescriptor.getTop() - this.worldDescriptor.getBottom();

		float wall1WidthW = WALL1_W * deltaXW;
		float wall1HeightW = WALL1_H * deltaYW;
		float wall1XW = this.worldDescriptor.getLeft() + WALL1_X * deltaXW;
		float wall1YW = this.worldDescriptor.getTop() - WALL1_Y * deltaYW;

		float wall2WidthW = WALL2_W * deltaXW;
		float wall2HeightW = WALL2_H * deltaYW;
		float wall2XW = this.worldDescriptor.getLeft() + WALL2_X * deltaXW;
		float wall2YW = this.worldDescriptor.getTop() - WALL2_Y * deltaYW;

		// Wall 1
		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.staticBody);
		bodyDef.setPosition(wall1XW, wall1YW);
		bodyDef.setAngle(CheckupConstants.DEG_TO_RAD_RATIO * -30.0f);
		this.wall1Body = this.world.createBody(bodyDef);
		PolygonShape wall1Shape = new PolygonShape();
		wall1Shape.setAsBox(wall1WidthW / 2.0f, wall1HeightW / 2.0f);
		FixtureDef wall1FixtureDef = new FixtureDef();
		wall1FixtureDef.setShape(wall1Shape);
		wall1FixtureDef.setDensity(WALL_DENSITY);
		wall1FixtureDef.setFriction(WALL_FRICTION);
		wall1FixtureDef.setRestitution(WALL_RESTITUTION);
		this.wall1Body.createFixture(wall1FixtureDef);

		this.wall1Entity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(wall1WidthW),
				this.getOpenGLDimension(wall1HeightW)
		);

		// Wall 2
		bodyDef.setPosition(wall2XW, wall2YW);
		bodyDef.setAngle(CheckupConstants.DEG_TO_RAD_RATIO * 30.0f);
		this.wall2Body = this.world.createBody(bodyDef);
		PolygonShape wall2Shape = new PolygonShape();
		wall2Shape.setAsBox(wall2WidthW / 2.0f, wall2HeightW / 2.0f);
		FixtureDef wall2FixtureDef = new FixtureDef();
		wall2FixtureDef.setShape(wall2Shape);
		wall2FixtureDef.setDensity(WALL_DENSITY);
		wall2FixtureDef.setFriction(WALL_FRICTION);
		wall2FixtureDef.setRestitution(WALL_RESTITUTION);
		this.wall2Body.createFixture(wall2FixtureDef);

		this.wall2Entity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(wall2WidthW),
				this.getOpenGLDimension(wall2HeightW)
		);

		this.updateObjectPosition(this.wall1Entity, this.wall1Body);
		this.updateObjectPosition(this.wall2Entity, this.wall2Body);

		wall1FixtureDef.delete();
		wall1Shape.delete();
		wall2FixtureDef.delete();
		wall2Shape.delete();
		bodyDef.delete();
	}

	/**
	 * Creates the ground.
	 */
	private void createGround(){
		groundY = this.worldDescriptor.getTop() - GROUND_Y * deltaYW;
		groundYGL = OpenGLUtils.convertToOpenGLYCoordinate(groundY, deltaYW);

		float groundH = GROUND_H * deltaYW;
		float groundW = GROUND_W * deltaXW;

		// Left ground
		this.groundLeftEntity = new GroundLeftEntity(
				0.0f,
				0.0f,
				OpenGLUtils.getOpenGLDimension(groundW, deltaYW),
				OpenGLUtils.getOpenGLDimension(groundH, deltaYW)
		);

		Vec2 leftPos = new Vec2(this.worldDescriptor.getLeft(), groundY);
		this.groundLeftEntity.createBody(this.world, this.worldDescriptor, leftPos);
		leftPos.delete();

		// Right ground
		this.groundRightEntity = new GroundRightEntity(
				0.0f,
				0.0f,
				OpenGLUtils.getOpenGLDimension(groundW, deltaYW),
				OpenGLUtils.getOpenGLDimension(groundH, deltaYW)
		);

		Vec2 rightPos = new Vec2(this.worldDescriptor.getRight(), groundY);
		this.groundRightEntity.createBody(this.world, this.worldDescriptor, rightPos);
		rightPos.delete();
	}

	/**
	 * Creates the jar that will be used as the input for the liquid.
	 */
	private void createJar(){

		float deltaXW = this.worldDescriptor.getRight() - this.worldDescriptor.getLeft();
		float deltaYW = this.worldDescriptor.getTop() - this.worldDescriptor.getBottom();

		float angle = 90f + 30f;
		float angleRight = 90f - 30f;
		float angleRad = (float)Math.toRadians(angle - 90f);
		float angleCos = (float)Math.cos(angleRad);
		float angleSin = (float)Math.sin(angleRad);
		float length = 2f * INPUT_PIPE_HEIGHT / angleCos;
		float y = WORLD_TOP;
		float yTop = y + INPUT_PIPE_HEIGHT * deltaXW;

		// Left
		float xLeft = this.worldDescriptor.getLeft() + (INPUT_PIPE_CENTER_X - 0.5f*INPUT_PIPE_WIDTH - (INPUT_PIPE_HEIGHT * (float)Math.tan(angleRad))) * deltaXW;

		// Right
		float xRight = this.worldDescriptor.getLeft() + (INPUT_PIPE_CENTER_X + 0.5f*INPUT_PIPE_WIDTH + INPUT_PIPE_HEIGHT * (float)Math.tan(angleRad)) * deltaXW;

		// Top
		float xTop = this.worldDescriptor.getLeft() + INPUT_PIPE_CENTER_X * deltaXW;

		float widthW = deltaXW;
		float heightW = 0.001f;

		// Jar Top
		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.staticBody);
		bodyDef.setPosition(xTop, yTop);
		this.liquidJarTopBody = this.world.createBody(bodyDef);
		PolygonShape jarTopShape = new PolygonShape();
		jarTopShape.setAsBox(WORLD_HEIGHT*length, WORLD_HEIGHT*heightW / 2.0f);
		FixtureDef jarTopFixtureDef = new FixtureDef();
		jarTopFixtureDef.setShape(jarTopShape);
		jarTopFixtureDef.setDensity(WALL_DENSITY);
		jarTopFixtureDef.setFriction(WALL_FRICTION);
		jarTopFixtureDef.setRestitution(WALL_RESTITUTION);
		this.liquidJarTopBody.createFixture(jarTopFixtureDef);

//		this.liquidJarTopEntity = new RectEntity(
//				0.0f,
//				0.0f,
//				4*length,
//				2*heightW,
//				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
//		);

		// Jar Left
		bodyDef.setType(BodyType.staticBody);
		bodyDef.setPosition(xLeft, y);
		float rad = (float)Math.toRadians(angle);
		bodyDef.setAngle((float)Math.toRadians(angle));
		this.liquidJarLeftBody = this.world.createBody(bodyDef);
		PolygonShape jarLeftShape = new PolygonShape();
		jarLeftShape.setAsBox(WORLD_HEIGHT*length/2f, WORLD_HEIGHT*heightW / 2.0f);
		FixtureDef jarLeftFixtureDef = new FixtureDef();
		jarLeftFixtureDef.setShape(jarLeftShape);
		jarLeftFixtureDef.setDensity(WALL_DENSITY);
		jarLeftFixtureDef.setFriction(WALL_FRICTION);
		jarLeftFixtureDef.setRestitution(WALL_RESTITUTION);
		this.liquidJarLeftBody.createFixture(jarLeftFixtureDef);

//		this.liquidJarLeftEntity = new RectEntity(
//				0.0f,
//				0.0f,
//				2*length,
//				2*heightW,
//				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
//		);

		// Jar Right
		bodyDef.setPosition(xRight, y);
		bodyDef.setAngle((float)Math.toRadians(angleRight));
		this.liquidJarRightBody = this.world.createBody(bodyDef);
		PolygonShape jarRightShape = new PolygonShape();
		jarRightShape.setAsBox(WORLD_HEIGHT*length / 2.0f, WORLD_HEIGHT*heightW / 2.0f);
		FixtureDef jarRightFixtureDef = new FixtureDef();
		jarRightFixtureDef.setShape(jarRightShape);
		jarRightFixtureDef.setDensity(WALL_DENSITY);
		jarRightFixtureDef.setFriction(WALL_FRICTION);
		jarRightFixtureDef.setRestitution(WALL_RESTITUTION);
		this.liquidJarRightBody.createFixture(jarRightFixtureDef);

//		this.liquidJarRightEntity = new RectEntity(
//				0.0f,
//				0.0f,
//				2*length,
//				2*heightW,
//				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
//		);

//		this.updateObjectPosition(this.liquidJarTopEntity, this.liquidJarTopBody);
// 		this.updateObjectPosition(this.liquidJarLeftEntity, this.liquidJarLeftBody);
//		this.updateObjectPosition(this.liquidJarRightEntity, this.liquidJarRightBody);

		jarTopShape.delete();
		jarTopFixtureDef.delete();
		jarLeftShape.delete();
		jarLeftFixtureDef.delete();
		jarRightShape.delete();
		jarRightFixtureDef.delete();

	}

	/**
	 * Creates the liquid.
	 */
	private void createLiquid(){
		this.liquidEntity = new LiquidEntity();

		ParticleSystemDef particleSystemDef =  new ParticleSystemDef();
		particleSystemDef.setRadius(PARTICLE_SIZE_WORLD);
		particleSystemDef.setDensity(WATER_DENSITY);
		particleSystemDef.setMaxCount(MAX_PARTICLES);


		this.liquidParticleSystem = this.world.createParticleSystem(particleSystemDef);
		this.liquidParticleSystem.setDestructionByAge(true);

		this.particleDef = new ParticleDef();

		this.particleDef.setColor(Color.red(this.particleColor), Color.green(this.particleColor), Color.blue(this.particleColor), Color.alpha(this.particleColor));
		this.particleDef.setFlags(ParticleFlag.waterParticle | ParticleFlag.colorMixingParticle);

		this.addParticles();

		particleSystemDef.delete();

		this.liquidTextureProgram = USES_TEXTURE ? new LiquidTextureShaderProgram(this.context) : new LiquidShaderProgram(this.context);

		if(USES_TEXTURE){
			this.liquidTexture = TextureHelper.loadTexture(this.context, R.drawable.liquid_tex);
		}
	}

	/**
	 * Updates the given object position.
	 * @param entity as the entity to be updated.
	 * @param body as the body related to the given entity.
	 */
	private void updateObjectPosition(AbstractEntity entity, Body body){
		float entityCoords[] = this.convertToOpenGLCoordinates(new float[]{body.getPositionX(), body.getPositionY(), 0.0f});
		entity.setPosition(entityCoords[0], entityCoords[1]);
		entity.setAngle((float)Math.toDegrees((body.getAngle())));

	}

	/**
	 * Converts the given world coordinates to OpenGL coordinates.
	 * @param worldCoords as the given world coordinates (x, y, z).
	 * @return the opengl coordinates as an array of [x, y, z]
	 */
	private float[] convertToOpenGLCoordinates(float[] worldCoords){
		float openglCoord[] = new float[3];

		openglCoord[0] = this.getOpenGLDimension(worldCoords[0]); //this.convertToOpenGLXCoordinate(worldCoords[0]);
		openglCoord[1] = this.convertToOpenGLYCoordinate(worldCoords[1]);
		openglCoord[2] = worldCoords[2];

		return openglCoord;
	}

	/**
	 * Converts the given world coordinate to the OpenGL coordinate.
	 * @param coord as the given world coordinate.
	 * @return the OpenGL coordinate.
	 */
	private float convertToOpenGLYCoordinate(float coord){
		float deltaW = this.worldDescriptor.getTop() - this.worldDescriptor.getBottom();
		return (2.0f * coord / deltaW) -1.0f;
	}

	/**
	 * Gets the OpenGL dimension related to the given World dimension
	 * Note: it must not be used for coordinates.
	 * @param worldDimension as the given world dimension.
	 * @return the OpenGL dimension.
	 */
	private float getOpenGLDimension(float worldDimension){
		float deltaW = this.worldDescriptor.getTop() - this.worldDescriptor.getBottom();
		float deltaGL = 2.0f;
		return worldDimension * deltaGL / deltaW;
	}

	/**
	 * Updates the position of the entities.
	 */
	private void updatePositions(){
		this.groundLeftEntity.updateEntity(deltaYW);
		this.groundRightEntity.updateEntity(deltaYW);

		if(USE_LIQUID){
			// Update the particles to display.
			int particleCount = this.liquidParticleSystem.getParticleCount();
			this.liquidParticleSystem.copyPositionBuffer(0, particleCount, this.posBuffer);
			this.liquidParticleSystem.copyColorBuffer(0, particleCount, this.colorBuffer);
		}
	}

	/**
	 * Gets the target ratio for the current level.
	 *
	 * @return the target ratio for the current level.
	 */
	private float getTargetRatioForCurrentLevel() {
		switch (this.level) {
			case 1:
				return RATIO_LEVEL_1_HOLE_TARGET;
			case 2:
				return RATIO_LEVEL_2_HOLE_TARGET;
		}
		return -1f;
	}

	/**
	 * Adds some particles.
	 */
	private void addParticles(){
		synchronized (this){

			if(null == this.particleDef || this.particleDef.getPtr() == 0)
				return;

			float minX = this.inputLeft;
			float maxX = this.inputRight;

			int freeParticlecount = this.liquidParticleSystem.getMaxParticleCount() - this.liquidParticleSystem.getParticleCount();
			int particlesToCreate = Math.min(freeParticlecount, NEW_PARTICLE_COUNT);

			for(int i=0; i<particlesToCreate; i++){
				float x = minX + this.random.nextFloat() * (maxX - minX);
				float y = WORLD_TOP;
				this.particleDef.setPosition(x, y);
				int particleIndex = this.liquidParticleSystem.createParticle(particleDef);
				this.liquidParticleSystem.setParticleLifetime(particleIndex, PARTICLE_LIFETIME);
			}
		}
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		super.onSurfaceTextureDestroyed(surface);

		synchronized (this){
			this.particleDef.delete();
			this.world.destroyParticleSystem(this.liquidParticleSystem);
			this.world.delete();
		}

		return true;
	}
}
