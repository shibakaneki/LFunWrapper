#include <jni.h>
#include "../../../libs/liquidfun/include/Box2D/Particle/b2ParticleSystem.h"
#include "../../../libs/liquidfun/include/Box2D/Particle/b2Particle.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1createParticle(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jlong particleDefPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    b2ParticleDef* pPartDef = (b2ParticleDef*)particleDefPtr;
    return (jint)pPartSys->CreateParticle(*pPartDef);
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1destroyParticle(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint particleIndex){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    pPartSys->DestroyParticle((int32)particleIndex);
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getPositionBuffer(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint startIndex,
        jint particleCount,
        jobject outBuffer){

    /*const int PARTICLE_ELEMENT_SIZE = sizeof(float32);
    const int PARTICLE_STRIDE = 2*PARTICLE_ELEMENT_SIZE;
    const int VEC2_SIZE = sizeof(b2Vec2);

    void *outBuf = env->GetDirectBufferAddress(outBuffer);
    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    b2Vec2 *positions = pPartSys->GetPositionBuffer();


    outBuf += (startIndex * PARTICLE_STRIDE);
    positions += (startIndex * VEC2_SIZE);

    for(int i=0; i<particleCount; i++){
        outBuf += (i*PARTICLE_STRIDE);
        positions += (i*VEC2_SIZE);

        memcpy(outBuf, &positions->x, PARTICLE_ELEMENT_SIZE);
        memcpy(outBuf + PARTICLE_ELEMENT_SIZE, &positions->y, PARTICLE_ELEMENT_SIZE);
    }*/
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getWeightBuffer(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint startIndex,
        jint particleCount,
        jbyte* outBuffer){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    // TODO: Implement me!



}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getVelocityBuffer(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint startIndex,
        jint particleCount,
        jbyte* outBuffer){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    // TODO: Implement me!



}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getColorBuffer(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint startIndex,
        jint particleCount,
        jbyte* outBuffer){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    // TODO: Implement me!



}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getStuckCandidates(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint startIndex,
        jint particleCount,
        jbyte* outBuffer){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    // TODO: Implement me!



}

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getStuckCandidatesCount(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    return (jint)pPartSys->GetStuckCandidateCount();
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1setDestructionByAge(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jboolean isDestroyingByAge){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    pPartSys->SetDestructionByAge(isDestroyingByAge);
}

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getParticleCount(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    return (jint)pPartSys->GetParticleCount();
}

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getParticleGroupCount(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    return (jint)pPartSys->GetParticleGroupCount();
}

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getParticleGroupList(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    return (jlong)pPartSys->GetParticleGroupList();
}

JNIEXPORT void JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1setMaxParticleCount(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jint maxCount){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    pPartSys->SetMaxParticleCount((int32)maxCount);
}

JNIEXPORT jint JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1getMaxParticleCount(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    return (jint)pPartSys->GetMaxParticleCount();
}

JNIEXPORT jlong JNICALL Java_ch_shibastudio_liquidwrapper_LiquidWrapperJNI_ParticleSystem_1createParticleGroup(
        JNIEnv *env,
        jobject obj,
        jlong particleSystemPtr,
        jlong particleGroupDefPtr){

    b2ParticleSystem* pPartSys = (b2ParticleSystem*)particleSystemPtr;
    b2ParticleGroupDef* pParticleGroupDef = (b2ParticleGroupDef*) particleGroupDefPtr;
    return (jlong)pPartSys->CreateParticleGroup(*pParticleGroupDef);
}


#ifdef __cplusplus
}
#endif