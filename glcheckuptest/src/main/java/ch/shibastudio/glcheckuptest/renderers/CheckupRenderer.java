package ch.shibastudio.glcheckuptest.renderers;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.shibastudio.glcheckuptest.CheckupConstants;
import ch.shibastudio.glcheckuptest.WorldDescriptor;
import ch.shibastudio.glcheckuptest.entities.AbstractEntity;
import ch.shibastudio.glcheckuptest.entities.CheckupBall;
import ch.shibastudio.glcheckuptest.entities.CoordinateGridEntity;
import ch.shibastudio.glcheckuptest.entities.GroundLeftEntity;
import ch.shibastudio.glcheckuptest.entities.GroundRightEntity;
import ch.shibastudio.glcheckuptest.entities.InputPipeEntity;
import ch.shibastudio.glcheckuptest.entities.LiquidEntity;
import ch.shibastudio.glcheckuptest.entities.LiquidParticleEntity;
import ch.shibastudio.glcheckuptest.entities.RectEntity;
import ch.shibastudio.glcheckuptest.entities.TargetGuideArrowEntity;
import ch.shibastudio.glcheckuptest.entities.TargetGuideRectEntity;
import ch.shibastudio.glcheckuptest.geometry.Point;
import ch.shibastudio.glcheckuptest.geometry.Vector;
import ch.shibastudio.glcheckuptest.programs.LiquidShaderProgram;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;
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

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by shibakaneki on 07.06.17.
 */

public class CheckupRenderer implements GLSurfaceView.Renderer {
	private enum EMode{
		BALL,
		LIQUID
	}

	private final static String LOG_TAG = "LIQUIDFUN";
	private final static int FPS = 30;
	private final static float MAX_DRAWING_TIME = 1000f / (float)FPS;
	private final static float TIME_STEP = 1.0f / (float)FPS;
	private final static int VELOCITY_ITERATION = 1;
	private final static int POSITION_ITERATION = 1;
	private final static int PARTICLE_ITERATION = 1;
	private final static int MAX_BALLS = 150;
	private final static int MAX_PARTICLES = 10000;
	private final static int NEW_PARTICLE_COUNT = 4;
	private final static long BALL_INTERVAL = 1l;
	private final static float LIQUID_PARTICLE_SIZE = 0.05f;
	private final static float RATIO_LEVEL_1_HOLE_TARGET = 0.26f;
	private final static float RATIO_LEVEL_2_HOLE_TARGET = 0.45f;
	private final int PARTICLE_COORD_COUNT = 2;
	private final int PARTICLE_COLOR_COUNT = 4;

	private final static float WORLD_TOP = 10f;
	private final static float WORLD_BOTTOM = 0f;
	private final static float WORLD_HEIGHT = WORLD_TOP - WORLD_BOTTOM;

	private final static float WALL1_X = 157f/360f;
	private final static float WALL1_Y = 208f/640f;
	private final static float WALL1_W = 176f/360f;
	private final static float WALL1_H = 20f/640f;

	private final static float WALL2_X = 300f/360f;//277f/360f;
	private final static float WALL2_Y = 375f/640f;
	private final static float WALL2_W = 200f/360f; //270f/360f;
	private final static float WALL2_H = 20f/640f;

	private final static float GROUND_Y = 538f/640f;
	private final static float GROUND_H = 20f/640f;
	private final static float HOLE_W = 50f/360f;
	private final static float GROUND_W = 310f/360f;

	private final static float TARGET_ARROW_W = 39f/360f;
	private final static float TARGET_ARROW_H = 53f/640f;

	private EMode mode = EMode.LIQUID;
	private boolean isSurfaceCreated = false;
	private int width;
	private int height;
	private float glLeft;
	private float glRight;

	private final float[] modelViewProjectionMatrix = new float[16];
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];

	private WorldDescriptor worldDescriptor;
	private float leftGroundW = 0.0f;
	private float rightGroundW = 0.0f;
	private float deltaXW = 0.0f;
	private float deltaYW = 0.0f;
	private long lastBallTime = 0l;
	private static float groundY = 0f;
	private static float groundYGL = 0f;
	private static float xWHole;
	private int level = 1;

	// Entities
	private CoordinateGridEntity coordinateGridEntity;
	private InputPipeEntity inputPipeEntity;
	private TargetGuideRectEntity targetGuideRectEntity;
	private TargetGuideArrowEntity targetGuideArrowEntity;

	private RectEntity wall1Entity;
	private RectEntity wall2Entity;

	// JAR Entities
	private RectEntity liquidJarLeftEntity;
	private RectEntity liquidJarRightEntity;
	private RectEntity liquidJarPipeLeftEntity;
	private RectEntity liquidJarPipeRightEntity;

	private GroundLeftEntity groundLeftEntity;
	private GroundRightEntity groundRightEntity;
	private CheckupBall balls[] = new CheckupBall[MAX_BALLS];
	private LiquidParticleEntity liquidParticles[];

	// LiquidFun
	private World world;
	private Body wall1Body;
	private Body wall2Body;
	private Body liquidJarLeftBody;
	private Body liquidJarRightBody;
	private Body liquidJarPipeLeftBody;
	private Body liquidJarPipeRightBody;



	// Particle related members
	private ParticleSystem liquidParticleSystem;
	private LiquidEntity liquidEntity;
	private LiquidShaderProgram liquidShaderProgram;
	private Context context;
	private final float[] projectionMatrix2 = new float[16];
	private final float[] viewProjectionMatrix2 = new float[16];

	private Random random = new Random();
	private ByteBuffer posBuffer = ByteBuffer.allocateDirect(MAX_PARTICLES * PARTICLE_COORD_COUNT  * OpenGLUtils.BYTE_PER_FLOAT).order(ByteOrder.nativeOrder());
	private ByteBuffer colorBuffer = ByteBuffer.allocateDirect(MAX_PARTICLES * PARTICLE_COLOR_COUNT * OpenGLUtils.BYTE_PER_FLOAT).order(ByteOrder.nativeOrder());

	private long lastRefreshTime = 0l;

	public CheckupRenderer(@NonNull World world, Context context){
		this.world = world;
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.isSurfaceCreated = true;
		this.width = 0;
		this.height = 0;

		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		if(this.mode == EMode.LIQUID){
			this.liquidShaderProgram = new LiquidShaderProgram(this.context);
			this.liquidEntity = new LiquidEntity(MAX_PARTICLES);

			// Add one particle for the test
			//this.liquidEntity.addParticle(
			//		new Point(0f, 5f, 0f),
			//		Color.rgb(255, 0, 0));
		}

		if(OpenGLUtils.IS_DEBUG){
			this.coordinateGridEntity = new CoordinateGridEntity();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;

		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
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

		this.createWalls();
		this.createGround();

		switch (this.mode){
			case BALL:
				this.createBalls();
				break;
			case LIQUID:
				this.createJar();
				this.createLiquid();
				break;
		}

		Matrix.setLookAtM(this.viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(this.modelViewProjectionMatrix, 0, this.projectionMatrix, 0, this.viewMatrix, 0);

		xWHole = -1.5f;


		orthoM(projectionMatrix2, 0, -ratio, ratio, -1f, 1f, -1f, 1f);

		float scaleFactor = 2f / WORLD_HEIGHT;  // OpenGL height (2f) / world height

		float[] transformationMatrix = new float[16];
		float[] translationMatrix = new float[16];
		float[] scaleMatrix = new float[16];

		setIdentityM(translationMatrix, 0);
		setIdentityM(scaleMatrix, 0);
		setIdentityM(transformationMatrix, 0);

		translateM(translationMatrix, 0, 0f, -5f, 0f);
		scaleM(scaleMatrix, 0, scaleFactor, scaleFactor, 1f);
		multiplyMM(transformationMatrix, 0, scaleMatrix, 0, translationMatrix, 0);

		multiplyMM(viewProjectionMatrix2, 0, projectionMatrix2, 0, transformationMatrix, 0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if(!this.isSurfaceCreated){
			return;
		}

		long startTime = System.currentTimeMillis();

		if(OpenGLUtils.IS_DEBUG){
			float dt = (startTime - this.lastRefreshTime) / 1000f;
			this.lastRefreshTime = startTime;
			float fps = 1.0f / dt;
			Log.d(LOG_TAG, "FPS = " + fps);
		}

		synchronized (this){
			// Read Inputs
			this.setTargetPos();
			this.setGroundHolePos();

			// Update Positions
			this.world.step(TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION, PARTICLE_ITERATION);
			this.updatePositions();

			// Add random drops
			this.addParticles();
		}

		// Render
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		this.wall1Entity.render(this.modelViewProjectionMatrix);
		this.wall2Entity.render(this.modelViewProjectionMatrix);
		this.groundLeftEntity.render(this.modelViewProjectionMatrix);
		this.groundRightEntity.render(this.modelViewProjectionMatrix);
		this.targetGuideRectEntity.render(this.modelViewProjectionMatrix);
		this.targetGuideArrowEntity.render(this.modelViewProjectionMatrix);

		switch(this.mode){
			case BALL:
				for(int i=0; i<this.balls.length; i++){
					CheckupBall ball = this.balls[i];
					if(ball.isActive()){
						ball.render(this.modelViewProjectionMatrix);
					}
				}
				break;
			case LIQUID:
				this.liquidJarLeftEntity.render(this.modelViewProjectionMatrix);
				this.liquidJarRightEntity.render(this.modelViewProjectionMatrix);
				//this.liquidJarPipeLeftEntity.render(this.modelViewProjectionMatrix);
				//this.liquidJarPipeRightEntity.render(this.modelViewProjectionMatrix);

				this.liquidShaderProgram.useProgram();
				this.liquidShaderProgram.setUniforms(this.viewProjectionMatrix2);
				this.liquidEntity.bindData(this.liquidShaderProgram);
				this.liquidEntity.draw();

				break;
		}

		this.inputPipeEntity.render(this.modelViewProjectionMatrix);

		if(OpenGLUtils.IS_DEBUG){
			this.coordinateGridEntity.render(this.modelViewProjectionMatrix);
		}

		// ----------------------------

		long endTime = System.currentTimeMillis();
		long deltaTime = endTime - startTime;

		if(deltaTime < MAX_DRAWING_TIME){
			long sleepTime = (long)MAX_DRAWING_TIME - deltaTime;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the X position of the ground hole, in world coordinates.
	 */
	public void setGroundHolePos(){
		if(null == this.groundLeftEntity || null == this.groundRightEntity){
			Log.w(LOG_TAG, "Setting the X position of the hole too early!");
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
	 * Adds a ball.
	 */
	public void addBall(){
		for(int i=0; i<this.balls.length; i++){
			CheckupBall ball = this.balls[i];
			if(!ball.isActive()){
				this.putBallInStartPosition(ball);
				break;
			}
		}
	}

	/**
	 * Updates the position of the entities.
	 */
	private void updatePositions(){
		this.groundLeftEntity.updateEntity(deltaYW);
		this.groundRightEntity.updateEntity(deltaYW);

		switch(this.mode){
			case BALL:
				long t = System.currentTimeMillis();
				if(t - this.lastBallTime >= BALL_INTERVAL){
					this.lastBallTime = t;
					this.addBall();
				}

				for(int i=0; i<this.balls.length; i++){
					CheckupBall ball = this.balls[i];
					if(null != ball  && ball.isActive()){
						if(ball.getBody().getPositionY() < this.worldDescriptor.getBottom()){
							this.putBallInStock(ball);
						}else{
							this.updateObjectPosition(ball, ball.getBody());
						}
					}
				}
				break;
			case LIQUID:
				this.liquidParticleSystem.copyPositionBuffer(0, this.liquidEntity.getParticleCount(), posBuffer);
				this.liquidParticleSystem.copyColorBuffer(0, this.liquidEntity.getParticleCount(), colorBuffer);

				this.liquidEntity.updateParticles(posBuffer, colorBuffer);

				break;
		}
	}

	/**
	 * Puts the given ball in stock, waiting to be used by the simulation.
	 * @param ball as the given ball.
	 */
	private void putBallInStock(@NonNull CheckupBall ball){
		ball.deactivate();
		ball.getBody().setLinearVelocity(new Vec2(0.0f, 0.0f));
		ball.getBody().setAngularVelocity(0.0f);
		ball.getBody().setTransform(-0.8f, this.worldDescriptor.getTop() + 10.0f, 0.0f);
	}

	/**
	 * Puts the given ball in the start position, ready to fall down.
	 * @param ball as the given ball.
	 */
	private void putBallInStartPosition(@NonNull CheckupBall ball){
		ball.getBody().setTransform(-0.8f, this.worldDescriptor.getTop() + 0.5f, 0.0f);
		ball.activate();
	}

	/**
	 * Creates the jar that will be used as the input for the liquid.
	 */
	private void createJar(){
		float deltaXW = this.worldDescriptor.getRight() - this.worldDescriptor.getLeft();
		float deltaYW = this.worldDescriptor.getTop() - this.worldDescriptor.getBottom();

		float widthW = deltaXW;
		float heightW = 0.01f;
		float xW = ((CheckupConstants.INPUT_PIPE_CENTER_X - 0.5f - CheckupConstants.INPUT_PIPE_WIDTH/2f) * deltaXW) - (float)(widthW * Math.cos(Math.toRadians(-45d)))/2f;
		float yW = this.worldDescriptor.getTop() + (float)(widthW * Math.sin(Math.toRadians(45d)))/2f - 0.5f;

		// Jar Left
		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.staticBody);
		bodyDef.setPosition(xW, yW);
		bodyDef.setAngle(CheckupConstants.DEG_TO_RAD_RATIO * -45.0f);
		this.liquidJarLeftBody = this.world.createBody(bodyDef);
		PolygonShape jarLeftShape = new PolygonShape();
		jarLeftShape.setAsBox(widthW / 2.0f, heightW / 2.0f);
		FixtureDef jarLeftFixtureDef = new FixtureDef();
		jarLeftFixtureDef.setShape(jarLeftShape);
		jarLeftFixtureDef.setDensity(1.0f);
		jarLeftFixtureDef.setFriction(1.0f);
		jarLeftFixtureDef.setRestitution(0.2f);
		this.liquidJarLeftBody.createFixture(jarLeftFixtureDef);

		this.liquidJarLeftEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(widthW),
				this.getOpenGLDimension(heightW),
				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
		);

		// Jar Right
		xW = ((CheckupConstants.INPUT_PIPE_CENTER_X - 0.5f + CheckupConstants.INPUT_PIPE_WIDTH/2f) * deltaXW) + (float)(widthW * Math.cos(Math.toRadians(45d)))/2f;
		bodyDef.setPosition(xW, yW);
		bodyDef.setAngle(CheckupConstants.DEG_TO_RAD_RATIO * 45.0f);
		this.liquidJarRightBody = this.world.createBody(bodyDef);
		PolygonShape jarRightShape = new PolygonShape();
		jarRightShape.setAsBox(widthW / 2.0f, heightW / 2.0f);
		FixtureDef jarRightFixtureDef = new FixtureDef();
		jarRightFixtureDef.setShape(jarRightShape);
		jarRightFixtureDef.setDensity(1.0f);
		jarRightFixtureDef.setFriction(1.0f);
		jarRightFixtureDef.setRestitution(0.2f);
		this.liquidJarRightBody.createFixture(jarRightFixtureDef);

		this.liquidJarRightEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(widthW),
				this.getOpenGLDimension(heightW),
				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
		);

		// Jar Pipe Left
		/*float xLeftPipe = (CheckupConstants.INPUT_PIPE_CENTER_X - CheckupConstants.INPUT_PIPE_WIDTH/2f - 0.5f) * deltaXW;
		bodyDef.setPosition(xLeftPipe, this.worldDescriptor.getTop() - 1.25f*CheckupConstants.INPUT_PIPE_HEIGHT * deltaYW);
		bodyDef.setAngle(CheckupConstants.DEG_TO_RAD_RATIO * 90.0f);
		this.liquidJarPipeLeftBody = this.world.createBody(bodyDef);
		PolygonShape jarPipeLeftShape = new PolygonShape();
		jarPipeLeftShape.setAsBox(CheckupConstants.INPUT_PIPE_HEIGHT * deltaYW / 2f, heightW);
		FixtureDef jarPipeLeftFixtureDef = new FixtureDef();
		jarPipeLeftFixtureDef.setShape(jarRightShape);
		jarPipeLeftFixtureDef.setDensity(1.0f);
		jarPipeLeftFixtureDef.setFriction(1.0f);
		jarPipeLeftFixtureDef.setRestitution(0.2f);
		this.liquidJarPipeLeftBody.createFixture(jarPipeLeftFixtureDef);

		this.liquidJarPipeLeftEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(CheckupConstants.INPUT_PIPE_HEIGHT * deltaYW/2f),
				this.getOpenGLDimension(heightW),
				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
		);

		// Jar Pipe Right
		float xRightPipe = (CheckupConstants.INPUT_PIPE_CENTER_X + CheckupConstants.INPUT_PIPE_WIDTH/2f - 0.5f) * deltaXW;
		bodyDef.setPosition(xRightPipe, this.worldDescriptor.getTop() - 1.25f*CheckupConstants.INPUT_PIPE_HEIGHT * deltaYW);
		bodyDef.setAngle(CheckupConstants.DEG_TO_RAD_RATIO * 90.0f);
		this.liquidJarPipeRightBody = this.world.createBody(bodyDef);
		PolygonShape jarPipeRightShape = new PolygonShape();
		jarPipeLeftShape.setAsBox(CheckupConstants.INPUT_PIPE_HEIGHT * deltaYW / 2f, heightW);
		FixtureDef jarPipeRightFixtureDef = new FixtureDef();
		jarPipeRightFixtureDef.setShape(jarPipeRightShape);
		jarPipeRightFixtureDef.setDensity(1.0f);
		jarPipeRightFixtureDef.setFriction(1.0f);
		jarPipeRightFixtureDef.setRestitution(0.2f);
		this.liquidJarPipeRightBody.createFixture(jarPipeRightFixtureDef);

		this.liquidJarPipeRightEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(CheckupConstants.INPUT_PIPE_HEIGHT * deltaYW/2f),
				this.getOpenGLDimension(heightW),
				new float[]{1.0f, 0.0f, 0.0f, 1.0f}
		);*/

		this.updateObjectPosition(this.liquidJarLeftEntity, this.liquidJarLeftBody);
		this.updateObjectPosition(this.liquidJarRightEntity, this.liquidJarRightBody);
		//this.updateObjectPosition(this.liquidJarPipeLeftEntity, this.liquidJarPipeLeftBody);
		//this.updateObjectPosition(this.liquidJarPipeRightEntity, this.liquidJarPipeRightBody);
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
		wall1FixtureDef.setDensity(1.0f);
		wall1FixtureDef.setFriction(1.0f);
		wall1FixtureDef.setRestitution(0.2f);
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
		wall2FixtureDef.setDensity(1.0f);
		wall2FixtureDef.setFriction(1.0f);
		wall2FixtureDef.setRestitution(0.2f);
		this.wall2Body.createFixture(wall2FixtureDef);

		this.wall2Entity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(wall2WidthW),
				this.getOpenGLDimension(wall2HeightW)
		);

		this.updateObjectPosition(this.wall1Entity, this.wall1Body);
		this.updateObjectPosition(this.wall2Entity, this.wall2Body);
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

		this.groundLeftEntity.createBody(this.world, this.worldDescriptor, new Vec2(this.worldDescriptor.getLeft(), groundY));

		// Right ground
		this.groundRightEntity = new GroundRightEntity(
				0.0f,
				0.0f,
				OpenGLUtils.getOpenGLDimension(groundW, deltaYW),
				OpenGLUtils.getOpenGLDimension(groundH, deltaYW)
		);

		this.groundRightEntity.createBody(this.world, this.worldDescriptor, new Vec2(this.worldDescriptor.getRight(), groundY));
	}

	/**
	 * Creates the balls.
	 */
	private void createBalls(){
		for(int i=0; i<this.balls.length; i++){
			CheckupBall ball = new CheckupBall(
					0.0f,
					0.0f,
					this.getOpenGLDimension(0.1f),
					10
			);

			ball.createBody(this.world, this.worldDescriptor, new Vec2(-0.8f, this.worldDescriptor.getTop() + 10.0f));
			ball.deactivate();
			this.balls[i] = ball;
		}
	}

	/**
	 * Creates the liquid.
	 */
	private void createLiquid(){
		ParticleSystemDef particleSystemDef =  new ParticleSystemDef();
		particleSystemDef.setRadius(LIQUID_PARTICLE_SIZE);
		particleSystemDef.setDensity(10.0f);
		particleSystemDef.setMaxCount(MAX_PARTICLES);
		this.liquidParticleSystem = this.world.createParticleSystem(particleSystemDef);
		//this.liquidParticleSystem.setRadius(0.1f);
		this.addParticles();
	}

	/**
	 * Updates the given object position.
	 * @param entity as the entity to be updated.
	 * @param body as the body related to the given entity.
	 */
	private void updateObjectPosition(AbstractEntity entity, Body body){
		Vec2 pos = body.getPosition();
		float angle = body.getAngle();

		float entityCoords[] = this.convertToOpenGLCoordinates(new float[]{pos.getX(), pos.getY(), 0.0f});
		entity.setPosition(entityCoords[0], entityCoords[1]);
		entity.setAngle((float)Math.toDegrees((angle)));

		pos.delete();
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
	private float convertToOpenGLXCoordinate(float coord){
		float deltaW = this.worldDescriptor.getRight() - this.worldDescriptor.getLeft();
		float deltaGL = 2.0f;
		return coord * deltaGL / deltaW;
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

	private void addParticles(){
		int particleColor = Color.argb(255, 0, 172, 231);
		ParticleDef particleDef = new ParticleDef();
		particleDef.setColor(Color.red(particleColor), Color.green(particleColor), Color.blue(particleColor), Color.alpha(particleColor));
		particleDef.setFlags(ParticleFlag.viscousParticle | ParticleFlag.staticPressureParticle);

		float minX = this.liquidJarLeftBody.getPositionX() + 0.01f;
		float maxX = this.liquidJarRightBody.getPositionX() - 0.01f;

		for(int i=0; i<NEW_PARTICLE_COUNT; i++){
			float x = minX + this.random.nextFloat() * (maxX - minX);
			float y = this.liquidJarLeftBody.getPositionY();
			particleDef.setPosition(x, y);
			//this.liquidParticleSystem.createParticle(particleDef);
			//this.liquidEntity.addParticle(x, y, particleColor, 38f);
		}
	}
}
