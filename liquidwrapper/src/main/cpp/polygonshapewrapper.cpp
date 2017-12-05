#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Collision/Shapes/b2PolygonShape.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_PolygonShape_1new(
        JNIEnv* env,
        jobject obj){

    return (jlong)new b2PolygonShape();
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_PolygonShape_1set(
        JNIEnv* env,
        jobject obj,
        jlong psPtr,
        jlongArray points,
        jint count){

    // Note: We need to verify if this code is doing the right thing.
    b2PolygonShape* shape = (b2PolygonShape*)psPtr;

    jlong* elems = env->GetLongArrayElements(points, false);
    shape->Set((b2Vec2*)elems, count);

    env->ReleaseLongArrayElements(points, elems, 0);

}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_PolygonShape_1setAsBox(
        JNIEnv* env,
        jobject obj,
        jlong psPtr,
        jfloat x,
        jfloat y){

    b2PolygonShape* shape = (b2PolygonShape*)psPtr;
    shape->SetAsBox((float)x, (float)y);
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_PolygonShape_1setAsBox2(
        JNIEnv* env,
        jobject obj,
        jlong psPtr,
        jfloat x,
        jfloat y,
        jlong centerPtr,
        jfloat angle){

    b2PolygonShape* shape = (b2PolygonShape*)psPtr;
    shape->SetAsBox((float)x, (float)y, *((b2Vec2*)centerPtr), (float)angle);
}

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_PolygonShape_1getVertexCount(
        JNIEnv* env,
        jobject obj,
        jlong psPtr){

    b2PolygonShape* shape = (b2PolygonShape*)psPtr;
    return (int)shape->GetVertexCount();
}

#ifdef __cplusplus
}
#endif

