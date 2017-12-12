package ch.shibastudio.glcheckuptest.entities;

import ch.shibastudio.glcheckuptest.WorldDescriptor;
import ch.shibastudio.liquidwrapper.common.Vec2;
import ch.shibastudio.liquidwrapper.dynamics.Body;
import ch.shibastudio.liquidwrapper.dynamics.World;

/**
 * Created by shibakaneki on 23.06.17.
 */

public abstract class AbstractBodyEntity extends AbstractEntity {
	protected Body body;
	private boolean isActive;

	public AbstractBodyEntity(){
		super();
	}

	public AbstractBodyEntity(float[] color){
		super(color);
	}

	/**
	 * Gets the body.
	 * @return the body.
	 */
	public Body getBody(){
		return this.body;
	}

	/**
	 * Activates the ball.
	 */
	public void activate(){
		this.isActive = true;
	}

	/**
	 * Deactivate the ball.
	 */
	public void deactivate(){
		this.isActive = false;
	}

	/**
	 * Gets the information whether the ball is active.
	 * @return true if the ball is active; otherwise false.
	 */
	public boolean isActive(){
		return this.isActive;
	}

	/**
	 * Creates the body of this entity.
	 * @param world as the world.
	 * @param worldDescriptor as the world descriptor.
	 * @param pos as the initial position.
	 * @return the created body.
	 */
	public abstract Body createBody(World world, WorldDescriptor worldDescriptor, Vec2 pos);
}
