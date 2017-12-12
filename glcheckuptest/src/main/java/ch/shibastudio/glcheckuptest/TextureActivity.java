package ch.shibastudio.glcheckuptest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.shibastudio.glcheckuptest.renderers.AbstractTextureRenderer;

/**
 * Created by shibakaneki on 05.10.17.
 */

public class TextureActivity extends AppCompatActivity {
	@BindView(R.id.texture) TextureView textureView;

	private AbstractTextureRenderer renderer;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_texture);
		ButterKnife.bind(this);

	}
}
