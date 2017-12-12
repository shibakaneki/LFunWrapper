package ch.shibastudio.glcheckuptest.entities;

import ch.shibastudio.glcheckuptest.CheckupConstants;

/**
 * Created by shibakaneki on 07.06.17.
 */

public class InputPipeEntity extends AbstractEntity{
	public InputPipeEntity(float left, float right){
		super();
		super.color = new float[]{0.607f, 0.607f, 0.607f, 1.0f};
		coords = new float[]{
				left, 1.0f, 0.0f,
				left, 1.0f - 2*CheckupConstants.INPUT_PIPE_HEIGHT, 0.0f,
				right, 1.0f - 2*CheckupConstants.INPUT_PIPE_HEIGHT, 0.0f,
				right, 1.0f, 0.0f,
				left + (CheckupConstants.INPUT_PIPE_CENTER_X - CheckupConstants.INPUT_PIPE_WIDTH/2f) * (right - left), 1.0f - 2*CheckupConstants.INPUT_PIPE_HEIGHT, 0.0f,
				left + (CheckupConstants.INPUT_PIPE_CENTER_X - CheckupConstants.INPUT_PIPE_WIDTH/2f) * (right - left), 1.0f - 3*CheckupConstants.INPUT_PIPE_HEIGHT, 0.0f,
				left + (CheckupConstants.INPUT_PIPE_CENTER_X + CheckupConstants.INPUT_PIPE_WIDTH/2f) * (right - left), 1.0f - 3*CheckupConstants.INPUT_PIPE_HEIGHT, 0.0f,
				left + (CheckupConstants.INPUT_PIPE_CENTER_X + CheckupConstants.INPUT_PIPE_WIDTH/2f) * (right - left), 1.0f - 2*CheckupConstants.INPUT_PIPE_HEIGHT, 0.0f
		};
		super.drawOrder = new short[]{0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7};
		super.init();
	}
}
