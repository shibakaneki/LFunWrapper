package ch.shibastudio.glcheckuptest;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.shibastudio.glcheckuptest.renderers.CheckupRenderer;
import ch.shibastudio.glcheckuptest.renderers.LParticleRenderer;
import ch.shibastudio.liquidwrapper.dynamics.World;

public class GLActivity extends AppCompatActivity {
	private final static String LOG_TAG = "LIQUIDFUN";
	private final static boolean DEBUG_LIQUIDFUN = false;

	@BindView(R.id.gameSurface) CheckupGLSurfaceView surface;

	private GLSurfaceView.Renderer renderer;
	private World world;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_gl);

		ButterKnife.bind(this);

		System.loadLibrary("liquidfun");
		System.loadLibrary("liquidwrapper");

		if(this.hasGL20()){
			this.world = new World(0.0f, -10.0f);


			this.renderer = DEBUG_LIQUIDFUN ? new LParticleRenderer(this) : new CheckupRenderer(this.world, this);
			//if(DEBUG_LIQUIDFUN){
			//	((ParticleTestRenderer)this.renderer).init(this);
			//}
			this.surface.setRenderer(this.renderer);
			this.surface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		}else{
			Log.e(LOG_TAG, "Time to get a new phone! OpenGL ES 2.0 is not supported by this phone");
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		this.surface.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();
		this.surface.onPause();
	}

	/**
	 * Verifies that the phone can run OpenGL ES 2.0
	 * @return true if OpenGL ES 2.0 is available; otherwise false.
	 */
	private boolean hasGL20(){
		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
	}
}
