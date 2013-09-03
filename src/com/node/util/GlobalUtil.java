package com.node.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class GlobalUtil {
	static DisplayMetrics mDisplayMetric;

	/**
	 * 设置activity全屏显示
	 * 
	 * @param activity
	 */
	public static void setFullScreen(Activity activity) {
		UIUtil.setActivityFullScreen(activity);
	}

	/**
	 * 设置activity不全屏显示
	 * 
	 * @param activity
	 */
	public static void cancelFullScreen(Activity activity) {
		UIUtil.setActivityNotFullScreen(activity);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param view
	 */
	public static void hidenInputMethod(View view) {
		UIUtil.hidenInputMethod(view);
	}

	/**
	 * 得到设备高度
	 * 
	 * @param context
	 * @return
	 */
	public static float getDevPixHeight(Context context) {
		return initDisplayMetric(context).heightPixels;
	}

	/**
	 * 得到设备宽度
	 * 
	 * @param context
	 * @return
	 */
	public static float getDevPixWidth(Context context) {
		return initDisplayMetric(context).widthPixels;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private static DisplayMetrics initDisplayMetric(Context context) {
		if (mDisplayMetric == null) {
			mDisplayMetric = context.getResources().getDisplayMetrics();
		}
		return mDisplayMetric;
	}

	/**
	 * 把设备dip转化成px
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static float dip2px(Context context, int dip) {
		return initDisplayMetric(context).density * dip + 0.5f;
	}

	/**
	 * 判断是否是合法域名
	 * 
	 * @param inputStr
	 * @param trimInput
	 * @return
	 */
	public static boolean ifEndWithDomain(String inputStr, boolean trimInput) {
		return StrUtil.ifEndWithDomain(inputStr, trimInput);
	}
}
