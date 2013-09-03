package com.node.browser;

import android.app.Application;
import android.widget.Toast;

public class NodeApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		Toast.makeText(getApplicationContext(), "low memory",
				Toast.LENGTH_SHORT).show();
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
