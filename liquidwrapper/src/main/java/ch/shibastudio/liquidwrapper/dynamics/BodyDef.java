package ch.shibastudio.liquidwrapper.dynamics;

import ch.shibastudio.liquidwrapper.AbstractNativeObject;
import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;
import ch.shibastudio.liquidwrapper.common.Vec2;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class BodyDef extends AbstractNativeObject {
	private Vec2 position;
	private Vec2 linearVelocity;

	private boolean isAllowingSleep;
	private boolean isAwake;
	private boolean isFixedRotation;
	private boolean isBullet;
	private boolean isActive;

	public BodyDef(){
		super(LiquidWrapperJNI.BodyDef_new());
	}

	/**
	 * Sets the body type.
	 * @param type as the body type.
	 */
	public void setType(BodyType type){
		LiquidWrapperJNI.BodyDef_setType(super.getPtr(), type.ordinal());
	}

	/**
	 * Gets the body type.
	 * @return the body type.
	 */
	public BodyType getType(){
		return BodyType.values()[LiquidWrapperJNI.BodyDef_getType(super.getPtr())];
	}

	/**
	 * Sets the position.
	 * @param position as the position.
	 */
	public void setPosition(Vec2 position){
		this.position = position;
	}

	/**
	 * Gets the position.
	 * @return the position.
	 */
	public Vec2 getPosition(){
		return this.position;
	}

	/**
	 * Sets the angle.
	 * @param angle as the angle.
	 */
	public void setAngle(float angle){
		LiquidWrapperJNI.BodyDef_setAngle(super.getPtr(), angle);
	}

	/**
	 * Gets the angle.
	 * @return the angle.
	 */
	public float getAngle(){
		return LiquidWrapperJNI.BodyDef_getAngle(super.getPtr());
	}

	/**
	 * Sets the linear velocity.
	 * @param linearVelocity as the linear velocity.
	 */
	public void setLinearVelocity(Vec2 linearVelocity){
		this.linearVelocity = linearVelocity;
	}

	/**
	 * Gets the linear velocity.
	 * @return the linear velocity.
	 */
	public Vec2 getLinearVelocity(){
		return this.linearVelocity;
	}

	/**
	 * Sets the angular velocity.
	 * @param angularVelocity as the angular velocity.
	 */
	public void setAngularVelocity(float angularVelocity){
		LiquidWrapperJNI.BodyDef_setAngularVelocity(super.getPtr(), angularVelocity);
	}

	/**
	 * Gets the angular velocity.
	 * @return the angular velocity.
	 */
	public float getAngularVelocity(){
		return LiquidWrapperJNI.BodyDef_getAngularVelocity(super.getPtr());
	}

	/**
	 * Sets the linear damping.
	 * @param linearDamping as the linear damping.
	 */
	public void setLinearDamping(float linearDamping){
		LiquidWrapperJNI.BodyDef_setLinearDamping(super.getPtr(), linearDamping);
	}

	/**
	 * Gets the linear damping.
	 * @return the linear damping.
	 */
	public float getLinearDamping(){
		return LiquidWrapperJNI.BodyDef_getLinearDamping(super.getPtr());
	}

	/**
	 * Sets the angular damping.
	 * @param angularDamping as the angular damping.
	 */
	public void setAngularDamping(float angularDamping){
		LiquidWrapperJNI.BodyDef_setAngularDamping(super.getPtr(), angularDamping);
	}

	/**
	 * Gets the angular dampoing.
	 * @return the angular damping.
	 */
	public float getAngularDamping(){
		return LiquidWrapperJNI.BodyDef_getAngularDamping(super.getPtr());
	}

	/**
	 * Sets the gravity scale.
	 * @param gravityScale as the gravity scale.
	 */
	public void setGravityScale(float gravityScale){
		LiquidWrapperJNI.BodyDef_setGravityScale(super.getPtr(), gravityScale);
	}

	/**
	 * Gets the gravity scale.
	 * @return the gravity scale.
	 */
	public float getGravityScale(){
		return LiquidWrapperJNI.BodyDef_getGravityScale(super.getPtr());
	}

	/**
	 * Sets an indicator of sleep allowance.
	 * @param allowSleep as the sleep allowance.
	 */
	public void setAllowSleep(boolean allowSleep){
		this.isAllowingSleep = allowSleep;
	}

	/**
	 * Gets an indicator whether the body allow sleep.
	 * @return true if allowing sleep; otherwise false.
	 */
	public boolean isAllowingSleep(){
		return this.isAllowingSleep;
	}

	/**
	 * Sets the awake state.
	 * @param awake as the awake state.
	 */
	public void setAwake(boolean awake){
		this.isAwake = awake;
	}

	/**
	 * Gets an indication whether the body is awake.
	 * @return true if awake; otherwise false.
	 */
	public boolean isAwake(){
		return this.isAwake;
	}

	/**
	 * Sets the rotation state.
	 * @param isFixed as the rotation state.
	 */
	public void setFixedRotation(boolean isFixed){
		this.isFixedRotation = isFixed;
	}

	/**
	 * Gets an indication whether the rotation is fixed.
	 * @return true if the rotation is fixed; otherwise false.
	 */
	public boolean isFixedRotation(){
		return this.isFixedRotation;
	}

	/**
	 * Sets the bullet state.
	 * @param isBullet as the bullet state.
	 */
	public void setBullet(boolean isBullet){
		this.isBullet = isBullet;
	}

	/**
	 * Gets an indication whether the body is a bullet.
	 * @return true if the body is a bullet; otherwise false.
	 */
	public boolean isBullet(){
		return this.isBullet;
	}

	/**
	 * Sets the active state.
	 * @param isActive as the active state.
	 */
	public void setActive(boolean isActive){
		this.isActive = isActive;
	}

	/**
	 * Gets an indication whether the body is active.
	 * @return true if the body is active; otherwise false.
	 */
	public boolean isActive(){
		return this.isActive;
	}
}
