#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Collision/Shapes/b2EdgeShape.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_EdgeShape_1new(
        JNIEnv* env,
        jobject obj){

    return (jlong)new b2EdgeShape();
}

#ifdef __cplusplus
}
#endif

