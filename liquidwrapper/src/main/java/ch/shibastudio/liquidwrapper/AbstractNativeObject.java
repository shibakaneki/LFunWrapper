package ch.shibastudio.liquidwrapper;

/**
 * Created by shibakaneki on 04.12.17.
 */

public abstract class AbstractNativeObject {
	private long cPtr = 0;

	protected AbstractNativeObject(long ptr){
		this.cPtr = ptr;
	}

	/**
	 * Finalizes the object.
	 */
	protected void finalize(){
		this.delete();
	}

	/**
	 * Deletes the object.
	 */
	public synchronized void delete() {
		if (this.cPtr != 0) {
			LiquidWrapperJNI.delete(this.cPtr);
			this.cPtr = 0;
		}
	}

	/**
	 * Gets the pointer to the object.
	 * @return the pointer.
	 */
	protected long getPtr(){
		return this.cPtr;
	}
}
