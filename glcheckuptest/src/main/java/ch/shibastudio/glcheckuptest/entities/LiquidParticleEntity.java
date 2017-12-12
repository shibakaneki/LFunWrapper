package ch.shibastudio.glcheckuptest.entities;

import android.opengl.GLES20;

import static ch.shibastudio.glcheckuptest.CheckupConstants.DEG_TO_RAD_RATIO;

/**
 * Created by shibakaneki on 28.06.17.
 */

public class LiquidParticleEntity extends AbstractEntity {
	private int index;

	public LiquidParticleEntity(float x, float y, float r, int index){
		this(x, y, r, new float[]{0.0f, 0.678f, 0.909f, 1.0f}, index);
	}

	public LiquidParticleEntity(float x, float y, float r, float[] color, int index){
		super(color);

		super.setX(x);
		super.setY(y);

		this.index = index;

		coords = new float[]{
				x + r/((float)(Math.sin(Math.toRadians(30)))), y, 0.0f,
				x - r, y + r/((float)(Math.tan(Math.toRadians(30)))), 0.0f,
				x - r, y - r/((float)(Math.tan(Math.toRadians(30)))), 0.0f
		};

		super.drawOrder = new short[]{0, 1, 2};

		this.init();
	}

	/**
	 * Gets the particle index.
	 * @return the index.
	 */
	public int getIndex(){return this.index;}
}
