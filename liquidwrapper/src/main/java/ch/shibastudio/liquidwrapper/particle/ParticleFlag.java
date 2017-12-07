package ch.shibastudio.liquidwrapper.particle;

/**
 * Created by shibakaneki on 27.11.17.
 */

public enum ParticleFlag {
	waterParticle(0),
	zombieParticle(1 << 1),
	wallParticle(1 << 2),
	springParticle(1 << 3),
	elasticParticle(1 << 4),
	viscousParticle(1 << 5),
	powderParticle(1 << 6),
	tensileParticle(1 << 7),
	colorMixingParticle(1 << 8),
	destructionListenerParticle(1 << 9),
	barrierParticle(1 << 10),
	staticPressureParticle(1 << 11),
	reactiveParticle(1 << 12),
	repulsiveParticle(1 << 13),
	fixtureContactListenerParticle(1 << 14),
	particleContactListenerParticle(1 << 15),
	fixtureContactFilterParticle(1 << 16),
	particleContactFilterParticle(1 << 17);

	private final int value;

	ParticleFlag(){
		this.value = 0;
	}

	ParticleFlag(int value){
		this.value = value;
	}
}
