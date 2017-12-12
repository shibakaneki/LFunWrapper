package ch.shibastudio.liquidwrapper;

import android.util.Log;

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
	public abstract void delete();
/*	public synchronized void delete() {
		if (this.cPtr != 0) {
			Log.d("LIQUIDWRAPPER", "Deleting the pointer of " +this.getClass().getSimpleName());
			LiquidWrapperJNI.delete(this.cPtr);
			this.cPtr = 0;
		}
	}
*/

	/**
	 * Gets the pointer to the object.
	 * @return the pointer.
	 */
	public long getPtr(){
		return this.cPtr;
	}

	/**
	 * Deletes the C pointer.
	 */
	protected void deletePtr(){
		this.cPtr = 0;
	}
}
