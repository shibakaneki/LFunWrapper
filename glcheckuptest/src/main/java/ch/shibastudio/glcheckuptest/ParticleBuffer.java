package ch.shibastudio.glcheckuptest;

import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ch.shibastudio.liquidwrapper.common.Vec2;

/**
 * Created by shibakaneki on 12.07.17.
 */

public class ParticleBuffer {
	private ByteBuffer buffer;
	private int ptr;
	private int maxParticle = 0;
	private int particleCount = 0;

	public ParticleBuffer(int maxParticle){
		this.maxParticle = maxParticle;
		this.buffer = ByteBuffer
				.allocateDirect(maxParticle * 4 * 4) // 4 bytes per X and 4 per Y
				.order(ByteOrder.nativeOrder());
		this.buffer.position(0);
		this.ptr = 0;
	}

	/**
	 * Adds a particle to the buffer.
	 * @param pos as a 2 dimensional vector holding the X & Y position of the particle to add.
	 */
	public void addParticle(@NonNull Vec2 pos){
		if(this.ptr > (this.maxParticle * 4 * 4) - 8){
			// Go back to the beginning of the buffer.
			this.ptr = 0;
			this.particleCount--;
		}

		Log.d("LIQUIDFUN", "Adding drop at " +pos.getX() +";" +pos.getY());

		this.buffer.putFloat(this.ptr, pos.getX());
		this.ptr +=4;
		this.buffer.putFloat(this.ptr, pos.getY());
		this.ptr += 4;
		this.particleCount++;
	}

	/**
	 * Resets the buffer.
	 */
	public void reset(){
		ptr = 0;
		particleCount = 0;
	}

	/**
	 * Gets the particle count.
	 * @return the particle count.
	 */
	public int count(){
		return this.particleCount;
	}

	/**
	 * Gets the buffer.
	 * @return the buffer.
	 */
	public ByteBuffer getBuffer(){
		return this.buffer;
	}
}
