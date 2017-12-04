#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_delete(
        JNIEnv* env,
        jobject obj,
        jlong ptr){

    void* cPtr = (void*)ptr;
    if(NULL != cPtr){
        delete cPtr;
    }
}

#ifdef __cplusplus
}
#endif