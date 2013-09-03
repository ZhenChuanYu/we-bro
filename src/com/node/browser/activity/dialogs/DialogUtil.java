package com.node.browser.activity.dialogs;

import android.app.Activity;
import android.content.Intent;

public class DialogUtil {

	public static void showSplashDialog(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, ActivitySplash.class);
		activity.startActivity(intent);
	}

}
