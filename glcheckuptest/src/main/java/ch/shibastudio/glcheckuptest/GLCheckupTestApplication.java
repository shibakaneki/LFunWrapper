package ch.shibastudio.glcheckuptest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by shibakaneki on 14.12.17.
 */

public class GLCheckupTestApplication extends Application {
	@Override
	public void onCreate(){
		super.onCreate();
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);
	}
}
