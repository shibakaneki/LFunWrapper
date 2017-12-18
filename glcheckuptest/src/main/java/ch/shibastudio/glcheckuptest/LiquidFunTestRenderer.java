package ch.shibastudio.glcheckuptest;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.shibastudio.glcheckuptest.entities.AbstractEntity;
import ch.shibastudio.glcheckuptest.entities.BallEntity;
import ch.shibastudio.glcheckuptest.entities.CoordinateGridEntity;
import ch.shibastudio.glcheckuptest.entities.RectEntity;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;
import ch.shibastudio.liquidwrapper.collision.shapes.CircleShape;
import ch.shibastudio.liquidwrapper.collision.shapes.PolygonShape;
import ch.shibastudio.liquidwrapper.common.Vec2;
import ch.shibastudio.liquidwrapper.dynamics.Body;
import ch.shibastudio.liquidwrapper.dynamics.BodyDef;
import ch.shibastudio.liquidwrapper.dynamics.BodyType;
import ch.shibastudio.liquidwrapper.dynamics.FixtureDef;
import ch.shibastudio.liquidwrapper.dynamics.World;

/**
 * Created by shibakaneki on 10.06.17.
 */

public class LiquidFunTestRenderer implements GLSurfaceView.Renderer {
	private final static int FPS = 30;
	private final static float MAX_DRAWING_TIME = 1000f / (float)FPS;
	private final static float TIME_STEP = 1.0f / (float)FPS;
	private final static int VELOCITY_ITERATION = 6;
	private final static int POSITION_ITERATION = 2;

	private final static float WORLD_TOP = 10f;
	private final static float WORLD_BOTTOM = 0f;
	private final static float WORLD_HEIGHT = WORLD_TOP - WORLD_BOTTOM;

	private final float GROUND_W = 10.0f;
	private final float GROUND_H = 0.2f;
	private final float GROUND_X = 0.0f;
	private final float GROUND_Y = 0.0f + GROUND_H;

	private final float TEST_W = 0.25f;
	private final float TEST_H = 0.25f;
	private final float TEST_X = -1.0f;
	private final float TEST_Y = 10.0f;

	private final float OBSTACLE_W = 3.0f;
	private final float OBSTACLE_H = 0.2f;
	private final float OBSTACLE_X = -1.0f;
	private final float OBSTACLE_Y = 6.0f;

	private final float OBSTACLE2_W = 3.0f;
	private final float OBSTACLE2_H = 0.2f;
	private final float OBSTACLE2_X = 1.5f;
	private final float OBSTACLE2_Y = 3.0f;

	private boolean isSurfaceCreated = false;
	private int width;
	private int height;

	private final float[] modelViewProjectionMatrix = new float[16];
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];

	private CoordinateGridEntity coordinateGridEntity;

	private World world;
	private float worldStep = 0;
	private WorldDescriptor worldDescriptor;

	// Ground
	private RectEntity groundRectEntity;
	private Body groundBody;

	// Test object
	private RectEntity testRectEntity;
	private Body dynamicBody;

	// Ball
	private BallEntity ballEntity;
	private Body ballBody;

	// Obstacle
	private RectEntity obstacleEntity;
	private Body obstacleBody;

	private RectEntity obstacle2Entity;
	private Body obstacle2Body;

	public LiquidFunTestRenderer(@NonNull World world){
		this.world = world;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.isSurfaceCreated = true;
		this.width = 0;
		this.height = 0;

		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

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
		Matrix.frustumM(this.projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

		float worldHalfWidth = (WORLD_HEIGHT * ratio)/2f;
		this.worldDescriptor = new WorldDescriptor(0 - worldHalfWidth, WORLD_TOP, worldHalfWidth, WORLD_BOTTOM);

		// Ground
		this.createGround();

		// Obstacle
		this.createObstacle();

		// Test Object
		this.createTestObject();
		this.createBallObject();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if(!this.isSurfaceCreated){
			return;
		}

		long startTime = System.currentTimeMillis();

		this.worldStep+=0.05f;
		this.world.step(this.worldStep * TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION, 0);
		this.updatePositions();

		Matrix.setLookAtM(this.viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(this.modelViewProjectionMatrix, 0, this.projectionMatrix, 0, this.viewMatrix, 0);

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		if(OpenGLUtils.IS_DEBUG){
			this.coordinateGridEntity.render(this.modelViewProjectionMatrix);
		}

		this.groundRectEntity.render(this.modelViewProjectionMatrix);
		this.obstacleEntity.render(this.modelViewProjectionMatrix);
		this.obstacle2Entity.render(this.modelViewProjectionMatrix);

		this.testRectEntity.render(this.modelViewProjectionMatrix);
		this.ballEntity.render(this.modelViewProjectionMatrix);

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
	 * Creates the ground.
	 */
	private void createGround(){
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.setType(BodyType.staticBody);
		groundBodyDef.setPosition(GROUND_X, GROUND_H);
		this.groundBody = this.world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(GROUND_W/2f, GROUND_H/2f);
		this.groundBody.createFixture(groundBox, 0.0f);

		this.groundRectEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(GROUND_W),
				this.getOpenGLDimension(GROUND_H));

		this.groundRectEntity.setPosition(
				this.convertToOpenGLXCoordinate(GROUND_X),
				this.convertToOpenGLYCoordinate(GROUND_Y));
	}

	/**
	 * Creates the test object.
	 */
	private void createTestObject(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.dynamicBody);
		bodyDef.setPosition(TEST_X , TEST_Y);
		bodyDef.setGravityScale(0.7f);
		bodyDef.setLinearDamping(0.1f);

		this.dynamicBody = this.world.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(TEST_W/2f, TEST_H/2f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(dynamicBox);
		fixtureDef.setDensity(50.0f);
		fixtureDef.setFriction(1.0f);
		this.dynamicBody.createFixture(fixtureDef);

		this.testRectEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(TEST_W),
				this.getOpenGLDimension(TEST_H),
				new float[]{0.0f, 0.678f, 0.909f, 1.0f});

		this.testRectEntity.setPosition(
				this.convertToOpenGLXCoordinate(TEST_X),
				this.convertToOpenGLYCoordinate(TEST_Y));
	}

	/**
	 * Creates the ball object.
	 */
	private void createBallObject(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.dynamicBody);
		bodyDef.setPosition(TEST_X , 1.5f * TEST_Y);
		bodyDef.setGravityScale(0.7f);
		bodyDef.setAngularDamping(0.1f);

		this.ballBody = this.world.createBody(bodyDef);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(TEST_W/2f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(circleShape);
		fixtureDef.setDensity(50.0f);
		fixtureDef.setFriction(1.0f);
		this.ballBody.createFixture(fixtureDef);

		this.ballEntity = new BallEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(TEST_W/2f),
				10,
				new float[]{0.96f, 0.65f, 0.137f, 1.0f});

		this.ballEntity.setPosition(
				this.convertToOpenGLXCoordinate(TEST_X),
				this.convertToOpenGLYCoordinate(TEST_Y));
	}

	/**
	 * Creates the obstacle.
	 */
	private void createObstacle(){
		// Obstacle 1
		BodyDef obstacleBodyDef = new BodyDef();
		obstacleBodyDef.setType(BodyType.staticBody);
		obstacleBodyDef.setPosition(OBSTACLE_X, OBSTACLE_Y);
		obstacleBodyDef.setAngle((float)Math.toRadians(-20.0));
		this.obstacleBody = this.world.createBody(obstacleBodyDef);
		PolygonShape obstacleBox = new PolygonShape();
		obstacleBox.setAsBox(OBSTACLE_W/2f, OBSTACLE_H/2f);
		FixtureDef obstacleFixtureDef = new FixtureDef();
		obstacleFixtureDef.setShape(obstacleBox);
		obstacleFixtureDef.setDensity(1.0f);
		obstacleFixtureDef.setFriction(1.0f);
		this.obstacleBody.createFixture(obstacleFixtureDef);


		this.obstacleEntity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(OBSTACLE_W),
				this.getOpenGLDimension(OBSTACLE_H));

		this.obstacleEntity.setPosition(
				this.convertToOpenGLXCoordinate(OBSTACLE_X),
				this.convertToOpenGLYCoordinate(OBSTACLE_Y));

		// Obstacle 2
		BodyDef obstacle2BodyDef = new BodyDef();
		obstacle2BodyDef.setType(BodyType.staticBody);
		obstacle2BodyDef.setPosition(OBSTACLE2_X, OBSTACLE2_Y);
		obstacle2BodyDef.setAngle((float)Math.toRadians(30.0));
		this.obstacle2Body = this.world.createBody(obstacle2BodyDef);
		PolygonShape obstacle2Box = new PolygonShape();
		obstacle2Box.setAsBox(OBSTACLE2_W/2f, OBSTACLE2_H/2f);
		FixtureDef obstacle2FixtureDef = new FixtureDef();
		obstacle2FixtureDef.setShape(obstacle2Box);
		obstacle2FixtureDef.setDensity(100.0f);
		obstacle2FixtureDef.setFriction(1.0f);
		this.obstacle2Body.createFixture(obstacle2FixtureDef);

		this.obstacle2Entity = new RectEntity(
				0.0f,
				0.0f,
				this.getOpenGLDimension(OBSTACLE2_W),
				this.getOpenGLDimension(OBSTACLE2_H));

		this.obstacle2Entity.setPosition(
				this.convertToOpenGLXCoordinate(OBSTACLE2_X),
				this.convertToOpenGLYCoordinate(OBSTACLE2_Y));
	}

	/**
	 * Updates the position of the entities.
	 */
	private void updatePositions(){
		this.updateObjectPosition(this.testRectEntity, this.dynamicBody);
		this.updateObjectPosition(this.ballEntity, this.ballBody);
		this.updateObjectPosition(this.obstacleEntity, this.obstacleBody);
		this.updateObjectPosition(this.obstacle2Entity, this.obstacle2Body);
	}

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

		openglCoord[0] = this.convertToOpenGLXCoordinate(worldCoords[0]);
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
}
