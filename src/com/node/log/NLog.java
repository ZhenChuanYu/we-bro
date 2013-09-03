package com.node.log;

public class NLog {
	private static String TAG = "NodeBrowser.log";
	private static boolean LOG_SWITCH = true;

	public static void i(String tag, String msg) {
		if (LOG_SWITCH) {
			android.util.Log.i(tag == null ? TAG : tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_SWITCH) {
			android.util.Log.e(tag == null ? TAG : tag, msg);
		}
	}
}
