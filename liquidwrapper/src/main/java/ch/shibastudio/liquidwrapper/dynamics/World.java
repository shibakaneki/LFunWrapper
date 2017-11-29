package ch.shibastudio.liquidwrapper.dynamics;

import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class World {
	private long worldPtr = 0;

	protected World(long worldPtr){
		this.worldPtr = worldPtr;
	}

	protected static long getCPtr(World obj) {
		return (obj == null) ? 0 : obj.worldPtr;
	}

	protected void finalize() {
		delete();
	}

	public synchronized void delete() {
		if (this.worldPtr != 0) {
			LiquidWrapperJNI.delete_World(this.worldPtr);
			this.worldPtr = 0;
		}
	}

	/**
	 * Constructor.
	 * @param gravityX as the X axis gravity.
	 * @param gravityY as the Y axis gravity.
	 */
	public World(float gravityX, float gravityY){
		this(LiquidWrapperJNI.new_World(gravityX, gravityY));
	}
}
