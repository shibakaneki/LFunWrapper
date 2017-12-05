package ch.shibastudio.liquidwrapper.dynamics;

import ch.shibastudio.liquidwrapper.AbstractNativeObject;
import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;
import ch.shibastudio.liquidwrapper.collision.shapes.ChainShape;
import ch.shibastudio.liquidwrapper.collision.shapes.CircleShape;
import ch.shibastudio.liquidwrapper.collision.shapes.EdgeShape;
import ch.shibastudio.liquidwrapper.collision.shapes.PolygonShape;
import ch.shibastudio.liquidwrapper.collision.shapes.Shape;
import ch.shibastudio.liquidwrapper.collision.shapes.ShapeType;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class FixtureDef extends AbstractNativeObject{
	public FixtureDef(){
		super(LiquidWrapperJNI.FixtureDef_new());
	}

	/**
	 * Sets the shape.
	 * @param shape as the shape.
	 */
	public void setShape(Shape shape){
		LiquidWrapperJNI.FixtureDef_setShape(super.getPtr(), shape.getPtr());
	}

	/**
	 * Gets the shape.
	 * @return the shape.
	 */
	public Shape getShape(){
		long shapePtr = LiquidWrapperJNI.FixtureDef_getShape(super.getPtr());

		switch(ShapeType.values()[LiquidWrapperJNI.Shape_getType(shapePtr)]){
			case edge: return new EdgeShape(shapePtr);
			case polygon: return new PolygonShape(shapePtr);
			case chain: return new ChainShape(shapePtr);
			case circle: return new CircleShape(shapePtr);
		}

		return null;
	}

	/**
	 * Sets the friction.
	 * @param friction as the friction.
	 */
	public void setFriction(float friction){
		LiquidWrapperJNI.FixtureDef_setFriction(super.getPtr(), friction);
	}

	/**
	 * Gets the friction.
	 * @return the friction.
	 */
	public float getFriction(){
		return LiquidWrapperJNI.FixtureDef_getFriction(super.getPtr());
	}

	/**
	 * Sets the restitution.
	 * @param restitution as the restitution.
	 */
	public void setRestitution(float restitution){
		LiquidWrapperJNI.FixtureDef_setRestitution(super.getPtr(), restitution);
	}

	/**
	 * Gets the restitution.
	 * @return the restitution.
	 */
	public float getRestitution(){
		return LiquidWrapperJNI.FixtureDef_getRestitution(super.getPtr());
	}

	/**
	 * Sets the density
	 * @param density as the density.
	 */
	public void setDensity(float density){
		LiquidWrapperJNI.FixtureDef_setDensity(super.getPtr(), density);
	}

	/**
	 * Gets the density.
	 * @return the density.
	 */
	public float getDensity(){
		return LiquidWrapperJNI.FixtureDef_getDensity(super.getPtr());
	}

	/**
	 * Set the fixture as a sensor.
	 * A sensor shape collects contact information but never generates a collision response.
	 * @param isSensor as an indication whether the fixture is a sensor.
	 */
	public void setSensor(boolean isSensor){
		LiquidWrapperJNI.FixtureDef_setSensor(super.getPtr(), isSensor);
	}

	/**
	 * Gets an indication whether the fixture is a sensor.
	 * @return true if it is a sensor; otherwise false.
	 */
	public boolean isSensor(){
		return LiquidWrapperJNI.FixtureDef_isSensor(super.getPtr());
	}
}
