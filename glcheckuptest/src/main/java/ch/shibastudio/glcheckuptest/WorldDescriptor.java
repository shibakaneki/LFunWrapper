package ch.shibastudio.glcheckuptest;

/**
 * Created by shibakaneki on 14.06.17.
 */

public class WorldDescriptor {
	private float left = 0f;
	private float right = 0f;
	private float top = 0f;
	private float bottom = 0f;

	public WorldDescriptor(float l, float t, float r, float b){
		this.left = l;
		this.top = t;
		this.right = r;
		this.bottom = b;
	}

	/**
	 * Gets the left coordinate.
	 * @return the left coordinate.
	 */
	public float getLeft(){ return this.left; }

	/**
	 * Gets the right coordinate.
	 * @return the right coordinate.
	 */
	public float getRight(){ return this.right; }

	/**
	 * Gets the top coordinate.
	 * @return the top coordinate.
	 */
	public float getTop(){ return this.top; }

	/**
	 * Gets the bottom coordinate.
	 * @return the bottom coordinate.
	 */
	public float getBottom(){ return this.bottom; }
}
