#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Collision/Shapes/b2CircleShape.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_CircleShape_1new(
        JNIEnv* env,
        jobject obj){

    return (jlong)new b2CircleShape();
}

#ifdef __cplusplus
}
#endif

