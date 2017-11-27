#include <jni.h>

extern "C"

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_new_World(
        JNIEnv *env,
        jobject obj,
        jfloat gravityX,
        jfloat gravityY){

    // TODO: Implement me! (create the World using LiquidFun and return the pointer of the World)
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_delete_World(
        JNIEnv* env,
        jobject obj,
        jlong worldPtr){
    // TODO: Implement me!
}