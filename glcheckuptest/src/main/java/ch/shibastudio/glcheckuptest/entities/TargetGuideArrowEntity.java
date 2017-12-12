package ch.shibastudio.glcheckuptest.entities;

/**
 * Created by shibakaneki on 23.06.17.
 */

public class TargetGuideArrowEntity extends AbstractEntity {
	public TargetGuideArrowEntity(float x, float y, float w, float h) {
		this(x, y, w, h, new float[]{0.313f, 0.823f, 0.145f, 1.0f});
	}

	public TargetGuideArrowEntity(float x, float y, float w, float h, float[] color) {
		super(color);

		super.setX(x);
		super.setY(y);

		coords = new float[]{
				x, y-0.5f*h, 0.0f,
				x+0.5f*w, y-0.1f*h, 0.0f,
				x+0.25f*w, y-0.1f*h, 0.0f,
				x+0.25f*w, y+0.5f*h, 0.0f,
				x-0.25f*w, y+0.5f*h, 0.0f,
				x-0.25f*w, y-0.1f*h, 0.0f,
				x-0.5f*w, y-0.1f*h, 0.0f
		};
		super.drawOrder = new short[]{0, 1, 6, 2, 3, 5, 3, 4, 5};

		this.init();
	}
}
