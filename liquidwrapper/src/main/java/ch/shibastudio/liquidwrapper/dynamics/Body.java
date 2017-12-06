package ch.shibastudio.liquidwrapper.dynamics;

import ch.shibastudio.liquidwrapper.AbstractNativeObject;
import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;
import ch.shibastudio.liquidwrapper.collision.shapes.Shape;
import ch.shibastudio.liquidwrapper.common.Vec2;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class Body extends AbstractNativeObject{

	public Body(long ptr) {
		super(ptr);
	}

	/**
	 * Creates a fixture.
	 * @param fixtureDef as the fixture definition.
	 * @return the fixture.
	 */
	public Fixture createFixture(FixtureDef fixtureDef){
		return new Fixture(LiquidWrapperJNI.Body_createFixture(super.getPtr(), fixtureDef.getPtr()));
	}

	/**
	 * Creates a fixture.
	 * @param shape as the shape.
	 * @param density as the density.
	 * @return the fixture.
	 */
	public Fixture createFixture(Shape shape, float density){
		return new Fixture(LiquidWrapperJNI.Body_createFixture2(super.getPtr(), shape.getPtr(), density));
	}

	/**
	 * Destroy a fixture.
	 * @param fixture as the fixture to destroy.
	 */
	public void destroyFixture(Fixture fixture){
		LiquidWrapperJNI.Body_destroyFixture(super.getPtr(), fixture.getPtr());
	}

	/**
	 * Set the position of the body's origin and rotation.
	 * @param position as the position of the body origin.
	 * @param angle as the rotation angle.
	 */
	public void setTransform(Vec2 position, float angle){
		LiquidWrapperJNI.Body_setTransform(super.getPtr(), position.getPtr(), angle);
	}

	/**
	 * Gets the position.
	 * @return the position.
	 */
	public Vec2 getPosition(){
		return new Vec2(LiquidWrapperJNI.Body_getPosition(super.getPtr()));
	}
	/**
	 * Gets the angle.
	 * @return the angle.
	 */
	public float getAngle(){
		return LiquidWrapperJNI.Body_getAngle(super.getPtr());
	}

	/**
	 * Gets the position X coordinate.
	 * @return the X coordinate.
	 */
	public float getPositionX(){
		Vec2 pos = this.getPosition();
		return pos.getX();
	}

	/**
	 * Gets the position Y coordinate.
	 * @return the Y coordinate.
	 */
	public float getPositionY(){
		Vec2 pos = this.getPosition();
		return pos.getY();
	}

	// TODO: Implement me!
}
