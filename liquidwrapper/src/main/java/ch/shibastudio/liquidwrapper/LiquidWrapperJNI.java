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
	public final static native long World_createBody(long worldPtr, long bodyDefPtr);

	// Vec2
	public final static native long Vec2_new();
	public final static native long Vec2_new2(float x, float y);
	public final static native void Vec2_setZero(long vec2Ptr);
	public final static native void Vec2_set(long vec2Ptr, float x, float y);
	public final static native float Vec2_length(long vec2Ptr);
	public final static native float Vec2_lengthSquared(long vec2Ptr);
	public final static native float Vec2_normalize(long vec2Ptr);
	public final static native boolean Vec2_isValid(long vec2Ptr);
	public final static native long Vec2_skew(long vec2Ptr);
	public final static native void Vec2_setX(long vec2Ptr, float x);
	public final static native float Vec2_getX(long vec2Ptr);
	public final static native void Vec2_setY(long vec2Ptr, float y);
	public final static native float Vec2_getY(long vec2Ptr);

	// Color
	public final static native long Color_new(float r, float g, float b);
	public final static native void Color_set(long colorPtr, float r, float g, float b);
	public final static native void Color_setRed(long colorPtr, float r);
	public final static native float Color_getRed(long colorPtr);
	public final static native void Color_setGreen(long colorPtr, float g);
	public final static native float Color_getGreen(long colorPtr);
	public final static native void Color_setBlue(long colorPtr, float b);
	public final static native float Color_getBlue(long colorPtr);

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
	public final static native void BodyDef_setAllowSleep(long bodyDefPtr, boolean allowSleep);
	public final static native boolean BodyDef_getAllowSleep(long bodyDefPtr);
	public final static native void BodyDef_setAwake(long bodyDefPtr, boolean isAwake);
	public final static native boolean BodyDef_getAwake(long bodyDefPtr);
	public final static native void BodyDef_setFixedRotation(long bodyDefPtr, boolean isFixed);
	public final static native boolean BodyDef_getFixedRotation(long bodyDefPtr);
	public final static native void BodyDef_setBullet(long bodyDefPtr, boolean isBullet);
	public final static native boolean BodyDef_getBullet(long bodyDefPtr);
	public final static native void BodyDef_setActive(long bodyDefPtr, boolean isActive);
	public final static native boolean BodyDef_getActive(long bodyDefPtr);
	public final static native void BodyDef_setPosition(long bodyDefPtr, long vec2PosPtr);
	public final static native long BodyDef_getPosition(long bodyDefPtr);
	public final static native void BodyDef_setLinearVelocity(long bodyDefPtr, long linearVelocityPtr);
	public final static native long BodyDef_getLinearVelocity(long bodyDefPtr);

	// Body
	public final static native long Body_createFixture(long bodyPtr, long fixtureDefPtr);
	public final static native long Body_createFixture2(long bodyPtr, long shapePtr, float density);
	public final static native void Body_destroyFixture(long bodyPtr, long fixturePtr);
	public final static native long Body_getPosition(long bodyPtr);
	public final static native float Body_getAngle(long bodyPtr);
	public final static native void Body_setTransform(long bodyPtr, long posPtr, float angle);

	// FixtureDef
	public final static native long FixtureDef_new();
	public final static native void FixtureDef_setShape(long fixtureDefPtr, long shapePtr);
	public final static native long FixtureDef_getShape(long fixtureDefPtr);
	public final static native void FixtureDef_setFriction(long fixtureDefPtr, float friction);
	public final static native float FixtureDef_getFriction(long fixtureDefPtr);
	public final static native void FixtureDef_setRestitution(long fixtureDefPtr, float restitution);
	public final static native float FixtureDef_getRestitution(long fixtureDefPtr);
	public final static native void FixtureDef_setDensity(long fixtureDefPtr, float density);
	public final static native float FixtureDef_getDensity(long fixtureDefPtr);
	public final static native void FixtureDef_setSensor(long fixtureDefPtr, boolean isSensor);
	public final static native boolean FixtureDef_isSensor(long fixtureDefPtr);

	// Fixture
	public final static native int Fixture_getType(long fixturePtr);
	public final static native long Fixture_getShape(long fixturePtr);
	public final static native void Fixture_setSensor(long fixturePtr, boolean isSensor);
	public final static native boolean Fixture_isSensor(long fixturePtr);
	public final static native long Fixture_getBody(long fixturePtr);
	public final static native long Fixture_getNext(long fixturePtr);
	public final static native void Fixture_setDensity(long fixturePtr, float density);
	public final static native float Fixture_getDensity(long fixturePtr);
	public final static native void Fixture_setFriction(long fixturePtr, float density);
	public final static native float Fixture_getFriction(long fixturePtr);
	public final static native void Fixture_setRestitution(long fixturePtr, float density);
	public final static native float Fixture_getRestitution(long fixturePtr);

	// Shape
	public final static native void Shape_setType(long shapePtr, int type);
	public final static native int Shape_getType(long shaptPtr);
	public final static native int Shape_getChildCount(long shaptPtr);
	public final static native float Shape_getRadius(long shapePtr);
	public final static native void Shape_setRadius(long shapePtr, float radius);

	// PolygonShape
	public final static native long PolygonShape_new();
	public final static native void PolygonShape_set(long pshapePtr, long[] points, int count);
	public final static native void PolygonShape_setAsBox(long pshapePtr, float x, float y);
	public final static native void PolygonShape_setAsBox2(long pshapePtr, float x, float y, long centerPtr, float angle);
	public final static native int PolygonShape_getVertexCount(long pshapePtr);

	// ParticleDef
	public final static native long ParticleDef_new();
	public final static native void ParticleDef_setFlags(long particleDefPtr, int flag);
	public final static native int ParticleDef_getFlags(long particleDefPtr);
	public final static native void ParticleDef_setPosition(long particleDefPtr, long positionPtr);
	public final static native long ParticleDef_getPosition(long particleDefPtr);
	public final static native void ParticleDef_setVelocity(long particleDefPtr, long velocityPtr);
	public final static native long ParticleDef_getVelocity(long particleDefPtr);
	public final static native void ParticleDef_setColor(long particleDefPtr, long colorPtr);
	public final static native long ParticleDef_getColor(long particleDefPtr);
	public final static native void ParticleDef_setLifetime(long particleDefPtr, float lifetime);
	public final static native float ParticleDef_getLifetime(long particleDefPtr);
	public final static native void ParticleDef_setParticleGroup(long particleDefPtr, long particleGroupPtr);
	public final static native long ParticleDef_getParticleGroup(long particleDefPtr);

	// ParticleColor
	public final static native long ParticleColor_new();
	public final static native long ParticleColor_new2(int r, int g, int b, int a);
	public final static native boolean ParticleColor_isZero(long particleColorPtr);
	public final static native void ParticleColor_set(long particleColorPtr, int r, int g, int b, int a);


}
