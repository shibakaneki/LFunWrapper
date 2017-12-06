package ch.shibastudio.liquidwrapper.particle;

/**
 * Created by shibakaneki on 27.11.17.
 */

public enum ParticleFlag {
	b2_waterParticle(0),
	b2_zombieParticle(1 << 1),
	b2_wallParticle(1 << 2),
	b2_springParticle(1 << 3),
	b2_elasticParticle(1 << 4),
	b2_viscousParticle(1 << 5),
	b2_powderParticle(1 << 6),
	b2_tensileParticle(1 << 7),
	b2_colorMixingParticle(1 << 8),
	b2_destructionListenerParticle(1 << 9),
	b2_barrierParticle(1 << 10),
	b2_staticPressureParticle(1 << 11),
	b2_reactiveParticle(1 << 12),
	b2_repulsiveParticle(1 << 13),
	b2_fixtureContactListenerParticle(1 << 14),
	b2_particleContactListenerParticle(1 << 15),
	b2_fixtureContactFilterParticle(1 << 16),
	b2_particleContactFilterParticle(1 << 17);

	private final int value;

	ParticleFlag(){
		this.value = 0;
	}

	ParticleFlag(int value){
		this.value = value;
	}
}
