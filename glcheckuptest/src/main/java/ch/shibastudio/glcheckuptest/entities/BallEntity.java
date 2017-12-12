package ch.shibastudio.glcheckuptest.entities;

/**
 * Created by shibakaneki on 09.06.17.
 */

public class BallEntity extends AbstractEntity{
	private final int numSegment;
	private float radius;

	public BallEntity(float cx, float cy, float r, int numSegment){
		this(cx, cy, r, numSegment, new float[]{0.0f, 0.678f, 0.909f, 1.0f});
	}

	public BallEntity(float cx, float cy, float r, int numSegment, float[] color){
		super(color);

		this.numSegment = numSegment;
		coords = new float[COORDS_PER_VERTEX * (numSegment+1)]; // We add 1 for the center coordinate
		drawOrder = new short[COORDS_PER_VERTEX * numSegment];

		this.generateCoordinates(cx, cy, r);

		this.init();
	}

	/**
	 * Gets the radius.
	 * @return the radius.
	 */
	public float getRadius(){
		return this.radius;
	}

	/**
	 * Generates the coordinates of the circle points.
	 * @param cx as the X center coordinate.
	 * @param cy as the Y center coordinate.
	 * @param r as the radius.
	 */
	private void generateCoordinates(float cx, float cy, float r){
		coords[0] = cx;
		coords[1] = cy;
		coords[2] = 0.0f;
		this.radius = r;

		int startIndex = 3;
		float angleStep = 360f / (float)this.numSegment;
		int coordCount = 1;
		for(int i=0; i<this.numSegment; i++){
			float angle = (float)i * angleStep;
			float angleRad = angle * (float)Math.PI / 180f;

			float x = cx + r * (float)Math.cos(angleRad);
			float y = cy + r * (float)Math.sin(angleRad);

			coords[startIndex + i * COORDS_PER_VERTEX] = x;
			coords[startIndex + i * COORDS_PER_VERTEX +  1] = y;
			coords[startIndex + i * COORDS_PER_VERTEX + 2] = 0.0f;
			coordCount++;
			if(i > 0){
				drawOrder[(i-1) * COORDS_PER_VERTEX] = 0;
				drawOrder[((i-1) * COORDS_PER_VERTEX)+1] = (short)i;
				drawOrder[((i-1) * COORDS_PER_VERTEX)+2] = (short)(i+1);
			}
		}

		// Close the path
		drawOrder[drawOrder.length-COORDS_PER_VERTEX] = 0;
		drawOrder[drawOrder.length-COORDS_PER_VERTEX+1] = (short)this.numSegment;
		drawOrder[drawOrder.length-COORDS_PER_VERTEX+2] = 1;
	}
}
