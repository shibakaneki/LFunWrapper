package ch.shibastudio.liquidwrapper.particle;

import java.nio.ByteBuffer;
import java.util.List;

import ch.shibastudio.liquidwrapper.AbstractNativeObject;
import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class ParticleSystem extends AbstractNativeObject{
	public ParticleSystem(long ptr){
		super(ptr);
	}

	/**
	 * Creates a particle.
	 * @param particleDef as the particle definition.
	 * @return the index of the created particle.
	 */
	public int createParticle(ParticleDef particleDef){
		return LiquidWrapperJNI.ParticleSystem_createParticle(super.getPtr(), particleDef.getPtr());
	}

	/**
	 * Destroy the particle at the given index.
	 * @param particleIndex as ther given particle index.
	 */
	public void destroyParticle(int particleIndex){
		LiquidWrapperJNI.ParticleSystem_destroyParticle(super.getPtr(), particleIndex);
	}

	/**
	 * Gets the particle count.
	 * @return the particle count.
	 */
	public int getParticleCount(){
		return LiquidWrapperJNI.ParticleSystem_getParticleCount(super.getPtr());
	}

	/**
	 * Copy the position buffer.
	 * @param startIndex as the start index of the copy.
	 * @param numParticles as the number of particle position to copy
	 * @param outBuffer as the destination buffer.
	 */
	public void copyPositionBuffer(int startIndex, int numParticles, ByteBuffer outBuffer){
		LiquidWrapperJNI.ParticleSystem_getPositionBuffer(super.getPtr(), startIndex, numParticles, outBuffer);
	}

	/**
	 * Copy the weight buffer.
	 * @param startIndex as the start index of the copy.
	 * @param numParticles as the number of particle position to copy
	 * @param outBuffer as the destination buffer.
	 */
	public void copyWeightBuffer(int startIndex, int numParticles, ByteBuffer outBuffer){
		LiquidWrapperJNI.ParticleSystem_getWeightBuffer(super.getPtr(), startIndex, numParticles, outBuffer);
	}

	/**
	 * Copy the velocity buffer.
	 * @param startIndex as the start index of the copy.
	 * @param numParticles as the number of particle position to copy
	 * @param outBuffer as the destination buffer.
	 */
	public void copyVelocityBuffer(int startIndex, int numParticles, ByteBuffer outBuffer){
		LiquidWrapperJNI.ParticleSystem_getVelocityBuffer(super.getPtr(), startIndex, numParticles, outBuffer);
	}

	/**
	 * Copy the color buffer.
	 * @param startIndex as the start index of the copy.
	 * @param numParticles as the number of particle position to copy
	 * @param outBuffer as the destination buffer.
	 */
	public void copyColorBuffer(int startIndex, int numParticles, ByteBuffer outBuffer){
		LiquidWrapperJNI.ParticleSystem_getColorBuffer(super.getPtr(), startIndex, numParticles, outBuffer);
	}

	/**
	 * Copy the position buffer.
	 * @param startIndex as the start index of the copy.
	 * @param numParticles as the number of particle position to copy
	 * @param outBuffer as the destination buffer.
	 */
	public void copyStuckCandidatesBuffer(int startIndex, int numParticles, ByteBuffer outBuffer){
		LiquidWrapperJNI.ParticleSystem_getStuckCandidates(super.getPtr(), startIndex, numParticles, outBuffer);
	}

	/**
	 * Gets the stuck candidates count.
	 * @return the stuck candidates count.
	 */
	public int getStuckCandidatesCount(){
		return LiquidWrapperJNI.ParticleSystem_getStuckCandidatesCount(super.getPtr());
	}

	/**
	 * Sets an indicator whether the particles should be destroyed by age.
	 * @param isDestructingByAge as the indicator whether the particles should be destroyed by age.
	 */
	public void setDestructionByAge(boolean isDestructingByAge){
		LiquidWrapperJNI.ParticleSystem_setDestructionByAge(super.getPtr(), isDestructingByAge);
	}

	/**
	 * Gets the particle group list.
	 * @return the first particle group of the list.
	 */
	public ParticleGroup getParticleGroupList(){
		return new ParticleGroup(LiquidWrapperJNI.ParticleSystem_getParticleGroupList(super.getPtr()));
	}

	/**
	 * Sets the max particles count.
	 * @param maxCount as the max particles count.
	 */
	public void setMaxParticleCount(int maxCount){
		LiquidWrapperJNI.ParticleSystem_setMaxParticleCount(super.getPtr(), maxCount);
	}

	/**
	 * Gets the max particles count.
	 * @return the max particles count.
	 */
	public int getMaxParticleCount(){
		return LiquidWrapperJNI.ParticleSystem_getMaxParticleCount(super.getPtr());
	}

	/**
	 * Creates a particle group.
	 * @param particleGroupDef as the particle group definition
	 * @return the created particle group.
	 */
	public ParticleGroup createParticleGroup(ParticleGroupDef particleGroupDef){
		return new ParticleGroup(LiquidWrapperJNI.ParticleSystem_createParticleGroup(super.getPtr(), particleGroupDef.getPtr()));
	}
}
