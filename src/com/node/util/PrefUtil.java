package com.node.util;

import com.node.log.NLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preference Settings Persistence Class Writing settings to preference xml file
 * Method {@code PreferenceUtil.initPreferenceUtil} must be pre-invoked in
 * /data/data/[PackageName]/shared_prefs directory path Use {@link PrefFactory}
 * to create {@link PrefUtil} instance
 * 
 * @see {@link PrefFactory}
 * @author yuzhenchuan
 */
public class PrefUtil {
	protected static Context mContext;
	protected static SharedPreferences mSharedPref;
	public static PrefUtil preUtil = null;

	private static PrefUtil getInstance() {
		if (preUtil == null)
			throw new IllegalArgumentException("PreferenceUtil not inited");
		return preUtil;
	}

	public static void initPreferenceUtil(Context context) {
		mContext = context;
		if (preUtil == null)
			preUtil = new PrefUtil();
	}

	private PrefUtil() {
	};

	/* put & set methods */
	/**
	 * put String to preference.xml
	 * 
	 * @param key
	 *            {@link ElementConstants}
	 * @param value
	 *            setting value
	 */
	public static void putString(String key, String value) {
		mSharedPref.edit().putString(key, value).commit();
	}

	public static void putInt(String key, int value) {
		mSharedPref.edit().putInt(key, value).commit();
	}

	public static void putLong(String key, long value) {
		mSharedPref.edit().putLong(key, value).commit();
	}

	public static void putBoolean(String key, boolean value) {
		mSharedPref.edit().putBoolean(key, value).commit();
	}

	public static void putFloat(String key, float value) {
		mSharedPref.edit().putFloat(key, value).commit();
	}

	public static String getString(String key, String defValue) {
		return mSharedPref.getString(key, defValue);
	}

	public static int getInt(String key, int defValue) {
		return mSharedPref.getInt(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return mSharedPref.getBoolean(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		return mSharedPref.getFloat(key, defValue);
	}

	public static float getLong(String key, long defValue) {
		return mSharedPref.getLong(key, defValue);
	}

	public static SharedPreferences getmSharedPref() {
		return mSharedPref;
	}

	public static class PrefFactory {
		static String ATT_NAME_DEFAULT = "default";
		static String ATT_NAME_ = "others";

		/**
		 * 创建默认的偏好设置文件
		 * 
		 * @return
		 */
		public static PrefUtil createDefault() {
			createForAttributeName(ATT_NAME_DEFAULT);
			return getInstance();
		}

		/**
		 * 创建特定属性名的偏好设置文件
		 * 
		 * @param att_name
		 *            {@link ElementConstants}
		 * @return
		 */
		private static PrefUtil createForAttributeName(String att_name) {
			if (mContext == null) {
				NLog.e(null, "mContext is null");
				throw new IllegalArgumentException(" mContext is null");
			}
			if (ATT_NAME_DEFAULT.equals(att_name)) {
				mSharedPref = PreferenceManager
						.getDefaultSharedPreferences(mContext);
			} else {
				mSharedPref = mContext.getSharedPreferences(att_name,
						mContext.MODE_PRIVATE);
			}
			return getInstance();
		}
	}
}
