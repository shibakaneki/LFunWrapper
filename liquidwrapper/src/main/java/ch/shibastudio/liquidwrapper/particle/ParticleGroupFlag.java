package ch.shibastudio.liquidwrapper.particle;

/**
 * Created by shibakaneki on 07.12.17.
 */

public enum ParticleGroupFlag {
	solidParticleGroup(1 << 0),
	rigidParticleGroup(1 << 1),
	particleGroupCanBeEmpty(1 << 2),
	particleGroupWillBeDestroyed(1 << 3),
	particleGroupNeedsUpdateDepth(1 << 4),
	particleGroupInternalMask(particleGroupWillBeDestroyed.value | particleGroupNeedsUpdateDepth.value);

	private final int value;

	ParticleGroupFlag(){
		this.value = 0;
	}

	ParticleGroupFlag(int value){
		this.value = value;
	}
}
