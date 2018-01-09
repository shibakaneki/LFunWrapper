package ch.shibastudio.lfunwrapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ch.shibastudio.liquidwrapper.HelloWrapper;

public class MainActivity extends AppCompatActivity {

	// Used to load the 'native-lib' library on application startup.
	static {
		System.loadLibrary("liquidfun");
		System.loadLibrary("liquidwrapper");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		HelloWrapper wrapper = new HelloWrapper();

		// Example of a call to a native method
		TextView tv = (TextView) findViewById(R.id.sample_text);
		tv.setText(wrapper.stringFromJNI());
	}
}
