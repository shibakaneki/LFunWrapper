package ch.shibastudio.glcheckuptest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.shibastudio.glcheckuptest.renderers.CheckupTextureRenderer;

/**
 * Created by shibakaneki on 06.10.17.
 */

public class TextureTestActivity extends AppCompatActivity{
	@BindView(R.id.texture) TextureView textureView;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_texture_test);
		ButterKnife.bind(this);

		System.loadLibrary("liquidfun");
		System.loadLibrary("liquidwrapper");

		CheckupTextureRenderer renderer = new CheckupTextureRenderer(this);
		renderer.setFps(30);
		this.textureView.setSurfaceTextureListener(renderer);
	}
}
