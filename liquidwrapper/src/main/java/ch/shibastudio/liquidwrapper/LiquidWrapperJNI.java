package ch.shibastudio.liquidwrapper;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class LiquidWrapperJNI {
	// World
	public final static native long new_World(float jarg1, float jarg2);
	public final static native void delete_World(long jarg1);
}
