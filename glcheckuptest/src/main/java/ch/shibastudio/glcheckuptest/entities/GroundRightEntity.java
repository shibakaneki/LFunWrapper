package ch.shibastudio.glcheckuptest.entities;

import ch.shibastudio.glcheckuptest.WorldDescriptor;
import ch.shibastudio.glcheckuptest.utils.OpenGLUtils;
import ch.shibastudio.liquidwrapper.collision.shapes.PolygonShape;
import ch.shibastudio.liquidwrapper.common.Vec2;
import ch.shibastudio.liquidwrapper.dynamics.Body;
import ch.shibastudio.liquidwrapper.dynamics.BodyDef;
import ch.shibastudio.liquidwrapper.dynamics.BodyType;
import ch.shibastudio.liquidwrapper.dynamics.FixtureDef;
import ch.shibastudio.liquidwrapper.dynamics.World;

/**
 * Created by shibakaneki on 26.06.17.
 */
public class GroundRightEntity extends AbstractBodyEntity {
	private final static float SLOPE_RATIO = 0.95f;
	private final static float DENSITY = 50.0f;
	private final static float FRICTION = 1.0f;
	private final static float BODY_HEIGHT = 0.001f;
	private final static float ANGLE = (float)Math.toRadians(63d);

	private float width;
	private float height;
	private float xW;
	private float yW;
	private float hW;
	private float wW;

	private Body topBody;
	private Body angleBody;

	public GroundRightEntity(float x, float y, float w, float h){
		this(x, y, w, h, new float[]{0.607f, 0.607f, 0.607f, 1.0f});
	}

	public GroundRightEntity(float x, float y, float w, float h, float[] color){
		super(color);

		super.setX(x);
		super.setY(y);

		this.width = w;
		this.height = h;

		coords = new float[]{
				x+w/2f, y-h/2f, 0.0f,
				x+w/2f, y+h/2f, 0.0f,
				x-SLOPE_RATIO*(w/2f), y+h/2f, 0.0f,
				x-w/2f, y-h/2f, 0.0f,
				x-SLOPE_RATIO*(w/2f), y-h/2f, 0.0f
		};
		super.drawOrder = new short[]{0, 1, 4, 1, 2, 4, 4, 2, 3};

		this.init();
	}

	@Override
	public Body createBody(World world, WorldDescriptor worldDescriptor, Vec2 pos) {
		float hw = worldDescriptor.getTop() - worldDescriptor.getBottom();
		float hgl = 2.0f;

		this.wW = this.width * hw/hgl;
		this.hW = this.height * hw/hgl;

		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.staticBody);

		// -- Top body --
		bodyDef.setPosition(pos.getX(), pos.getY() + (this.hW - BODY_HEIGHT)/2f);
		this.topBody = world.createBody(bodyDef);
		PolygonShape topShape = new PolygonShape();
		topShape.setAsBox(SLOPE_RATIO*this.wW/2f, BODY_HEIGHT/2f);
		FixtureDef topFixtureDef = new FixtureDef();
		topFixtureDef.setShape(topShape);
		topFixtureDef.setDensity(DENSITY);
		topFixtureDef.setFriction(FRICTION);
		this.topBody.createFixture(topFixtureDef);

		// -- Angle body --
		bodyDef.setPosition(pos.getX() - SLOPE_RATIO *this.wW/2f, pos.getY() - this.hW/2f);
		float angleLength = (float)Math.sqrt(Math.pow(this.hW, 2) + Math.pow((1f-SLOPE_RATIO)*this.wW/2f,2));
		bodyDef.setAngle(ANGLE);
		this.angleBody = world.createBody(bodyDef);
		PolygonShape angleShape = new PolygonShape();
		angleShape.setAsBox(angleLength/2f, BODY_HEIGHT/2f);
		FixtureDef angleFixtureDef = new FixtureDef();
		angleFixtureDef.setShape(angleShape);
		angleFixtureDef.setDensity(DENSITY);
		angleFixtureDef.setFriction(FRICTION);
		this.angleBody.createFixture(angleFixtureDef);

		return this.topBody;
	}

	/**
	 * Sets the position of the body.
	 * @param x as the given X position of the center of the whole entity.
	 * @param y as the given Y position of the center of the whole entity.
	 */
	public void setBodyPosition(float x, float y){
		this.xW = x;
		this.yW = y;

		this.topBody.setTransform(x, y + (this.hW - BODY_HEIGHT)/2f, 0f);
		this.angleBody.setTransform(x - (SLOPE_RATIO - 0.5f)*this.wW - (1.0f - SLOPE_RATIO)/2f * this.wW/2f, y, ANGLE);
	}

	/**
	 * Updates the entity.
	 * @param worldWidth as the world width.
	 */
	public void updateEntity(float worldWidth){
		float entityCoords[] = OpenGLUtils.convertToOpenGLCoordinates(new float[]{this.xW, this.yW, 0.0f}, worldWidth);
		super.setPosition(entityCoords[0], entityCoords[1]);
	}
}
