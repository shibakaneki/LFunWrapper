package ch.shibastudio.liquidwrapper.particle;

import ch.shibastudio.liquidwrapper.AbstractNativeObject;
import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;
import ch.shibastudio.liquidwrapper.common.Vec2;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class ParticleDef extends AbstractNativeObject{
	public ParticleDef(){
		super(LiquidWrapperJNI.ParticleDef_new());
	}

	/**
	 * Sets the flags
	 * @param flags as the given flags (a combination of ParticleFlags)
	 */
	public void setFlags(int flags){
		LiquidWrapperJNI.ParticleDef_setFlags(super.getPtr(), flags);
	}

	/**
	 * Gets the flags.
	 * @return the flags.
	 */
	public int getFlags(){
		return LiquidWrapperJNI.ParticleDef_getFlags(super.getPtr());
	}

	/**
	 * Sets the particle position.
	 * @param position as the position to set.
	 */
	public void setPosition(Vec2 position){
		LiquidWrapperJNI.ParticleDef_setPosition(super.getPtr(), position.getPtr());
	}

	/**
	 * Gets the position.
	 * @return the position.
	 */
	public Vec2 getPosition(){
		return new Vec2(LiquidWrapperJNI.ParticleDef_getPosition(super.getPtr()));
	}

	/**
	 * Sets the velocity.
	 * @param velocity as the velocity.
	 */
	public void setVelocity(Vec2 velocity){
		LiquidWrapperJNI.ParticleDef_setVelocity(super.getPtr(), velocity.getPtr());
	}

	/**
	 * Gets the velocity.
	 * @return the velocity.
	 */
	public Vec2 getVelocity(){
		return new Vec2(LiquidWrapperJNI.ParticleDef_getVelocity(super.getPtr()));
	}

	/**
	 * Sets the color.
	 * @param color as the color.
	 */
	public void setColor(ParticleColor color){
		LiquidWrapperJNI.ParticleDef_setColor(super.getPtr(), color.getPtr());
	}

	/**
	 * Gets the color.
	 * @return the color.
	 */
	public ParticleColor getcolor(){
		return new ParticleColor(LiquidWrapperJNI.ParticleDef_getColor(super.getPtr()));
	}

	/**
	 * Sets the particle lifetime.
	 * @param lifetime as the lifetime.
	 */
	public void setLifetime(float lifetime){
		LiquidWrapperJNI.ParticleDef_setLifetime(super.getPtr(), lifetime);
	}

	/**
	 * Gets the lifetime.
	 * @return the lifetime.
	 */
	public float getLifetime(){
		return LiquidWrapperJNI.ParticleDef_getLifetime(super.getPtr());
	}

	/**
	 * Sets the particle group.
	 * @param group as the particle group.
	 */
	public void setParticleGroup(ParticleGroup group){
		LiquidWrapperJNI.ParticleDef_setParticleGroup(super.getPtr(), group.getPtr());
	}

	/**
	 * Gets the particle group.
	 * @return the particle group.
	 */
	public ParticleGroup getParticleGroup(){
		return new ParticleGroup(LiquidWrapperJNI.ParticleDef_getParticleGroup(super.getPtr()));
	}
}
