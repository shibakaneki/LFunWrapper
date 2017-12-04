package ch.shibastudio.liquidwrapper.dynamics;

import ch.shibastudio.liquidwrapper.AbstractNativeObject;
import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class World extends AbstractNativeObject{
	/**
	 * Constructor.
	 * @param gravityX as the X axis gravity.
	 * @param gravityY as the Y axis gravity.
	 */
	public World(float gravityX, float gravityY){
		super(LiquidWrapperJNI.World_new(gravityX, gravityY));
	}

	/**
	 * Take a time step.
	 * @param timeStep as the time step.
	 * @param velocityIterations as the velocity iteration count.
	 * @param positionIterations as the position iteration count.
	 */
	public void step(float timeStep, int velocityIterations, int positionIterations){
		LiquidWrapperJNI.World_step2(super.getPtr(), timeStep, velocityIterations, positionIterations);
	}

	/**
	 * Take a time step.
	 * @param timeStep as the time step.
	 * @param velocityIterations as the velocity iteration count.
	 * @param positionIterations as the position iteration count.
	 * @param particleIterations as the particle iteration count.
	 */
	public void step(float timeStep, int velocityIterations, int positionIterations, int particleIterations){
		LiquidWrapperJNI.World_step(super.getPtr(), timeStep, velocityIterations, positionIterations, particleIterations);
	}
}
