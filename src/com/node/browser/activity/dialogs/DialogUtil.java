package com.node.browser.activity.dialogs;

import android.app.Activity;
import android.content.Intent;

public class DialogUtil {

	/**
	 * 打开splash对话框
	 * 
	 * @param activity
	 */
	public static void showSplashDialog(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, ActivitySplash.class);
		activity.startActivity(intent);
	}

	/**
	 * 打开确认和取消对话框<br>
	 * 只包含标题、内容、取消按钮和确认按钮<br>
	 * 
	 * @param activity
	 * @param title
	 * @param content
	 * @param ensureCallback
	 * @param cancelCallback
	 */
	public static void showEnsureCancelDialog(Activity activity, String title,
			String content, ActivityECDialog.ItfEnsureCallback ensureCallback,
			ActivityECDialog.ItfCancelCallback cancelCallback) {
		Intent intent = new Intent();
		intent.putExtra(ActivityECDialog.EXTRA_KEY_TITLE, title);
		intent.putExtra(ActivityECDialog.EXTRA_KEY_CONTENT, content);
		intent.setClass(activity, ActivityECDialog.class);
		ActivityECDialog.setEnsureCallback(ensureCallback);
		ActivityECDialog.setCancelCallback(cancelCallback);
		activity.startActivity(intent);
	}

	/**
	 * 打开确认、取消对话框<br>
	 * 包含标题、内容、取消按钮和确认按钮<br>
	 * 包含checkbox复选框<br>
	 * 
	 * @param activity
	 * @param title
	 * @param content
	 * @param checkboxText
	 * @param prefKey
	 * @param cancelCallback
	 * @param ensureCallback
	 */
	public static void showECCheckBoxDialog(Activity activity, String title,
			String content, String checkboxText, String prefKey,
			ActivityECCheckBoxDialog.ItfCancelCallback cancelCallback,
			ActivityECCheckBoxDialog.ItfEnsureCallback ensureCallback) {
		Intent intent = new Intent();
		intent.setClass(activity, ActivityECCheckBoxDialog.class);
		intent.putExtra(ActivityECCheckBoxDialog.EXTRA_KEY_TITLE, title);
		intent.putExtra(ActivityECCheckBoxDialog.EXTRA_KEY_CONTENT, content);
		intent.putExtra(ActivityECCheckBoxDialog.EXTRA_KEY_CHECKBOXTEXT,
				checkboxText);
		intent.putExtra(ActivityECCheckBoxDialog.EXTRA_KEY_PREFERENCE_KEY,
				prefKey);
		ActivityECCheckBoxDialog.setEnsureCallback(ensureCallback);
		ActivityECCheckBoxDialog.setCancelCallback(cancelCallback);
		activity.startActivity(intent);
	}
}
