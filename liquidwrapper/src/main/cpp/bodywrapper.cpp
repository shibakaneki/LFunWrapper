#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Dynamics/b2Body.h"
#include "../../../libs/liquidfun/include/Box2D/Dynamics/b2Fixture.h"
#include "../../../libs/liquidfun/include/Box2D/Collision/Shapes/b2Shape.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_Body_1createFixture(
        JNIEnv* env,
        jobject obj,
        jlong bodyPtr,
        jlong fixtureDefPtr){

    b2Body* pBody = (b2Body*)bodyPtr;
    b2FixtureDef* pFixtureDef = (b2FixtureDef*)fixtureDefPtr;
    return (jlong)pBody->CreateFixture(pFixtureDef);
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_Body_1destroyFixture(
        JNIEnv* env,
        jobject obj,
        jlong bodyPtr,
        jlong fixturePtr){

    b2Body* pBody = (b2Body*)bodyPtr;
    b2Fixture* pFixture = (b2Fixture*)fixturePtr;
    pBody->DestroyFixture(pFixture);
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_Body_1setTransform(
        JNIEnv* env,
        jobject obj,
        jlong bodyPtr,
        jlong posPtr,
        jfloat angle){

    b2Body* pBody = (b2Body*)bodyPtr;
    b2Vec2* pPos = (b2Vec2*)posPtr;
    pBody->SetTransform(*pPos, angle);
}

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_Body_1createFixture2(
        JNIEnv* env,
        jobject obj,
        jlong bodyPtr,
        jlong shapePtr,
        jfloat density){

    b2Body* pBody = (b2Body*)bodyPtr;
    b2Shape* pShape = (b2Shape*)shapePtr;
    return (jlong)pBody->CreateFixture(pShape, (float)density);
}

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_Body_1getPosition(
        JNIEnv* env,
        jobject obj,
        jlong bodyPtr){

    b2Body* pBody = (b2Body*)bodyPtr;
    return (jlong)&pBody->GetPosition();
}

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_Body_1getAngle(
        JNIEnv* env,
        jobject obj,
        jlong bodyPtr){

    b2Body* pBody = (b2Body*)bodyPtr;
    return (jfloat)pBody->GetAngle();
}

#ifdef __cplusplus
}
#endif