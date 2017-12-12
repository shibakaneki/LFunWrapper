package ch.shibastudio.glcheckuptest.entities;

import ch.shibastudio.glcheckuptest.WorldDescriptor;
import ch.shibastudio.liquidwrapper.collision.shapes.CircleShape;
import ch.shibastudio.liquidwrapper.common.Vec2;
import ch.shibastudio.liquidwrapper.dynamics.Body;
import ch.shibastudio.liquidwrapper.dynamics.BodyDef;
import ch.shibastudio.liquidwrapper.dynamics.BodyType;
import ch.shibastudio.liquidwrapper.dynamics.FixtureDef;
import ch.shibastudio.liquidwrapper.dynamics.World;

/**
 * Created by shibakaneki on 19.06.17.
 */

public class CheckupBall extends AbstractBodyEntity {
	private final static float GRAVITY_SCALE = 1.0f;
	private final int numSegment;
	private float radius;

	public CheckupBall(float cx, float cy, float r, int numSegment){
		this(cx, cy, r, numSegment, new float[]{0.0f, 0.678f, 0.909f, 1.0f});
	}

	public CheckupBall(float cx, float cy, float r, int numSegment, float[] color){
		super(color);

		this.numSegment = numSegment;
		coords = new float[COORDS_PER_VERTEX * (numSegment+1)]; // We add 1 for the center coordinate
		drawOrder = new short[COORDS_PER_VERTEX * numSegment];

		this.generateCoordinates(cx, cy, r);

		this.init();
	}

	/**
	 * Gets the radius.
	 * @return the radius.
	 */
	public float getRadius(){
		return this.radius;
	}

	/**
	 * Generates the coordinates of the circle points.
	 * @param cx as the X center coordinate.
	 * @param cy as the Y center coordinate.
	 * @param r as the radius.
	 */
	private void generateCoordinates(float cx, float cy, float r){
		coords[0] = cx;
		coords[1] = cy;
		coords[2] = 0.0f;
		this.radius = r;

		int startIndex = 3;
		float angleStep = 360f / (float)this.numSegment;
		int coordCount = 1;
		for(int i=0; i<this.numSegment; i++){
			float angle = (float)i * angleStep;
			float angleRad = angle * (float)Math.PI / 180f;

			float x = cx + r * (float)Math.cos(angleRad);
			float y = cy + r * (float)Math.sin(angleRad);

			coords[startIndex + i * COORDS_PER_VERTEX] = x;
			coords[startIndex + i * COORDS_PER_VERTEX +  1] = y;
			coords[startIndex + i * COORDS_PER_VERTEX + 2] = 0.0f;
			coordCount++;
			if(i > 0){
				drawOrder[(i-1) * COORDS_PER_VERTEX] = 0;
				drawOrder[((i-1) * COORDS_PER_VERTEX)+1] = (short)i;
				drawOrder[((i-1) * COORDS_PER_VERTEX)+2] = (short)(i+1);
			}
		}

		// Close the path
		drawOrder[drawOrder.length-COORDS_PER_VERTEX] = 0;
		drawOrder[drawOrder.length-COORDS_PER_VERTEX+1] = (short)this.numSegment;
		drawOrder[drawOrder.length-COORDS_PER_VERTEX+2] = 1;
	}


	@Override
	public Body createBody(World world, WorldDescriptor worldDescriptor, Vec2 pos){
		float hw = worldDescriptor.getTop() - worldDescriptor.getBottom();
		float hgl = 2.0f;

		BodyDef bodyDef = new BodyDef();
		bodyDef.setType(BodyType.dynamicBody);
		bodyDef.setPosition(pos.getX() , pos.getY());
		bodyDef.setGravityScale(GRAVITY_SCALE);
		bodyDef.setLinearDamping(0.0f);
		bodyDef.setAngularDamping(0.1f);

		this.body = world.createBody(bodyDef);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(this.getRadius() * hw / hgl);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(circleShape);
		fixtureDef.setDensity(10.0f);
		//fixtureDef.setRestitution(0.42f);
		this.body.createFixture(fixtureDef);

		return this.body;
	}

	@Override
	public void activate(){
		super.activate();
		this.body.setGravityScale(GRAVITY_SCALE);
	}

	@Override
	public void deactivate(){
		super.deactivate();
		this.body.setGravityScale(0.0f);
	}
}
