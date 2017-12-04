package ch.shibastudio.liquidwrapper;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class LiquidWrapperJNI {
	// Global
	public final static native void delete(long cPtr);

	// World
	public final static native long World_new(float gravityX, float gravityY);
	public final static native void World_delete(long worldPtr);
	public final static native void World_step2(long worldPtr, float timeStep, int velocityIterations, int positionIterations);
	public final static native void World_step(long worldPtr, float timeStep, int velocityIterations, int positionIterations, int particleIterations);

	// BodyDef
	public final static native long BodyDef_new();
	public final static native void BodyDef_setType(long bodyDefPtr, int type);
	public final static native int BodyDef_getType(long bodyDefPtr);
	public final static native void BodyDef_setAngle(long bodyDefPtr, float angle);
	public final static native float BodyDef_getAngle(long bodyDefPtr);
	public final static native void BodyDef_setAngularVelocity(long bodyDefPtr, float angularVelocity);
	public final static native float BodyDef_getAngularVelocity(long bodyDefPtr);
	public final static native void BodyDef_setAngularDamping(long bodyDefPtr, float angularDamping);
	public final static native float BodyDef_getAngularDamping(long bodyDefPtr);
	public final static native void BodyDef_setLinearDamping(long bodyDefPtr, float linearDamping);
	public final static native float BodyDef_getLinearDamping(long bodyDefPtr);
	public final static native void BodyDef_setGravityScale(long bodyDefPtr, float gravityScale);
	public final static native float BodyDef_getGravityScale(long bodyDefPtr);

}
