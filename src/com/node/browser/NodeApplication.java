package com.node.browser;

import com.node.log.NLog;

import android.app.Application;
import android.widget.Toast;

public class NodeApplication extends Application {
	private static final String TAG="NodeApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		NLog.i(TAG, "browser start");
		
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
