package ch.shibastudio.liquidwrapper.collision.shapes;

import ch.shibastudio.liquidwrapper.LiquidWrapperJNI;
import ch.shibastudio.liquidwrapper.common.Vec2;

/**
 * Created by shibakaneki on 27.11.17.
 */

public class PolygonShape extends Shape{
	public PolygonShape(){
		super(LiquidWrapperJNI.PolygonShape_new());
	}

	public PolygonShape(long ptr){
		super(ptr);
	}

	/**
	 * Sets the polygon points.
	 * @param points as the list of coordinates.
	 */
	public void set(Vec2[] points){
		long[] pts = new long[points.length];
		for(int i=0; i<points.length; i++){
			pts[i] = points[i].getPtr();
		}

		LiquidWrapperJNI.PolygonShape_set(super.getPtr(), pts, points.length);
	}

	/**
	 * Builds vertices to represent an axis-aligned box centered on the local origin.
	 * @param hx as the half width.
	 * @param hy as the half height.
	 */
	public void setAsBox(float hx, float hy){
		LiquidWrapperJNI.PolygonShape_setAsBox(super.getPtr(), hx, hy);
	}

	/**
	 * Builds vertices to represent an oriented box.
	 * @param hx as the half width.
	 * @param hy as the half height.
	 * @param center as the center coordinate.
	 * @param angle as the angle.
	 */
	public void setAsBox(float hx, float hy, Vec2 center, float angle){
		LiquidWrapperJNI.PolygonShape_setAsBox2(super.getPtr(), hx, hy, center.getPtr(), angle);
	}

	/**
	 * Gets the vertex count.
	 * @return the vertex count.
	 */
	public int getVertexCount(){
		return LiquidWrapperJNI.PolygonShape_getVertexCount(super.getPtr());
	}
}
