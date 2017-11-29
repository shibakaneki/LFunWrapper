#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Dynamics/b2World.h"
#include "../../../libs/liquidfun/include/Box2D/Common/b2Math.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_new_1World(
        JNIEnv *env,
        jobject obj,
        jfloat gravityX,
        jfloat gravityY){

    b2Vec2 gravityVector;
    gravityVector.x = (float)gravityX;
    gravityVector.y = (float)gravityY;
    b2World* world = new b2World(gravityVector);

    return (jlong)world;
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_delete_1World(
        JNIEnv* env,
        jobject obj,
        jlong worldPtr){

    b2World* world = (b2World*)worldPtr;
    delete world;
}

#ifdef __cplusplus
}
#endif