#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Dynamics/b2Body.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1new(
        JNIEnv* env,
        jobject obj){

    return (jlong)new b2BodyDef*;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1setType(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr,
        jint type){

    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    pBodyDef->type = (b2BodyType)type;
}

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1getType(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr){
    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    return (NULL != pBodyDef) ? (jint)pBodyDef->type : 0;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1setAngle(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr,
        jfloat angle){

    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    pBodyDef->angle = (float)angle;
}

JNIEXPORT jfloat JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1getAngle(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr){
    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    return (NULL != pBodyDef) ? (jfloat)pBodyDef->angle : 0;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1setAngularVelocity(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr,
        jfloat angularVelocity){

    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    pBodyDef->angularVelocity = (float)angularVelocity;
}

JNIEXPORT jfloat JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1getAngularVelocity(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr){
    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    return (NULL != pBodyDef) ? (jfloat)pBodyDef->angularVelocity : 0;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1setAngularDamping(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr,
        jfloat angularDamping){

    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    pBodyDef->angularDamping = (float)angularDamping;
}

JNIEXPORT jfloat JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1getAngularDamping(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr){
    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    return (NULL != pBodyDef) ? (jfloat)pBodyDef->angularDamping : 0;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1setLinearDamping(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr,
        jfloat linearDamping){

    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    pBodyDef->linearDamping = (float)linearDamping;
}

JNIEXPORT jfloat JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1getLinearDamping(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr){
    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    return (NULL != pBodyDef) ? (jfloat)pBodyDef->linearDamping : 0;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1setGravityScale(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr,
        jfloat gravityScale){

    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    pBodyDef->gravityScale = (float)gravityScale;
}

JNIEXPORT jfloat JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_BodyDef_1getGravityScale(
        JNIEnv* env,
        jobject obj,
        jlong bodyDefPtr){
    b2BodyDef* pBodyDef = (b2BodyDef*)bodyDefPtr;
    return (NULL != pBodyDef) ? (jfloat)pBodyDef->gravityScale : 0;
}

#ifdef __cplusplus
}
#endif