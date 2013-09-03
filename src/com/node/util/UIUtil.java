package com.node.util;

import java.lang.reflect.Method;

import com.node.log.NLog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {

	/**
	 * 设置activity全屏幕显示
	 * 
	 * @param activity
	 */
	static void setActivityFullScreen(Activity activity) {
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		setFlagForActivity(activity, flag, flag);
	}

	/**
	 * 设置activity不全屏幕现实，留出顶部任务栏
	 * 
	 * @param activity
	 */
	static void setActivityNotFullScreen(Activity activity) {
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		setFlagForActivity(activity, 0, flag);
	}

	/**
	 * 为activity设置flag use only for method
	 * {@link #setActivityFullScreen(Activity)} and
	 * {@link #setActivityNotFullScreen(Activity)}
	 * 
	 * @param activity
	 * @param flags
	 * @param mask
	 * @see {@link Window#setFlags(int, int)} is core api method
	 * @see {@link #setActivityNotFullScreen(Activity)}
	 * @see {@link #setActivityFullScreen(Activity)}
	 */
	private static void setFlagForActivity(Activity activity, int flags,
			int mask) {
		if (activity == null) {
			NLog.e(null, "activity is null in method setFlagForActivity");
			return;
		}
		Window window = activity.getWindow();
		if (window == null) {
			NLog.e(null, "window is null in method setFlagForActivity");
			return;
		}
		window.setFlags(flags, mask);
	}

	static void requestLandscape(Activity activity) {
		setOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	static void requestPortrait(Activity activity) {
		setOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private static void setOrientation(Activity activity, int orientation) {
		activity.setRequestedOrientation(orientation);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param view
	 */
	static void hidenInputMethod(View view) {
		if (view == null) {
			return;
		}
		InputMethodManager manager = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static int measureViewWidth( View target, View parent ) {
	    try {
	        Method m = parent.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
	        m.setAccessible(true);
	        m.invoke(parent,
	                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	    } catch (Exception e) {
	        return -1;
	    }

	    return parent.getMeasuredWidth();
	}
}
