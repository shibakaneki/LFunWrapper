package ch.shibastudio.glcheckuptest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by shibakaneki on 07.06.17.
 */

public class CheckupGLSurfaceView extends GLSurfaceView {

	public CheckupGLSurfaceView(Context context) {
		super(context);
		this.init();
	}

	public CheckupGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	@Override
	public void setRenderer(Renderer renderer){
		super.setRenderer(renderer);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}

	/**
	 * Initializes the view.
	 */
	private void init(){
		setEGLContextClientVersion(2);
		setPreserveEGLContextOnPause(true);
	}
}
