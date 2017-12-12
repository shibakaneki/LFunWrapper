package ch.shibastudio.glcheckuptest.entities;

/**
 * Created by shibakaneki on 12.06.17.
 */

public class RectEntity extends AbstractEntity {

	public RectEntity(float x, float y, float w, float h){
		this(x, y, w, h, new float[]{0.607f, 0.607f, 0.607f, 1.0f});
	}

	public RectEntity(float x, float y, float w, float h, float[] color){
		super(color);

		super.setX(x);
		super.setY(y);

		coords = new float[]{
				x-w/2f, y-h/2f, 0.0f,
				x-w/2f, y+h/2f, 0.0f,
				x+w/2f, y+h/2f, 0.0f,
				x+w/2f, y-h/2f, 0.0f
		};
		super.drawOrder = new short[]{0, 1, 2, 0, 2, 3};

		this.init();
	}
}
