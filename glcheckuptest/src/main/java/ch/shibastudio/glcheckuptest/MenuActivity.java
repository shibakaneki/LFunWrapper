package ch.shibastudio.glcheckuptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shibakaneki on 05.10.17.
 */

public class MenuActivity extends AppCompatActivity {

	@BindView(R.id.glButton) Button glButton;
	@BindView(R.id.textureButton) Button textureButton;
	@BindView(R.id.textureTestButton) Button textureTestButton;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		ButterKnife.bind(this);

		this.glButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, GLActivity.class);
				startActivity(intent);
			}
		});

		this.textureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, TextureActivity.class);
				startActivity(intent);
			}
		});

		this.textureTestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, TextureTestActivity.class);
				startActivity(intent);
			}
		});
	}
}
