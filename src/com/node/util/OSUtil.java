package com.node.util;

import android.os.Build;
import android.os.Build.VERSION_CODES;

public class OSUtil {
	// API Version
	public static final int SDK_VERSION_1 = VERSION_CODES.BASE;// 1

	public static final int SDK_VERSION_1_1 = VERSION_CODES.BASE_1_1;// 2

	public static final int SDK_VERSION_1_5 = VERSION_CODES.CUPCAKE;// 3

	public static final int SDK_VERSION_1_6 = VERSION_CODES.DONUT;// 4

	public static final int SDK_VERSION_2_0 = VERSION_CODES.ECLAIR;// 5

	public static final int SDK_VERSION_2_0_1 = VERSION_CODES.ECLAIR_0_1;// 6

	public static final int SDK_VERSION_2_1 = VERSION_CODES.ECLAIR_MR1;// 7

	public static final int SDK_VERSION_2_2 = VERSION_CODES.FROYO;// 8

	public static final int SDK_VERSION_2_3 = VERSION_CODES.GINGERBREAD;// 9

	public static final int SDK_VERSION_2_3_3 = VERSION_CODES.GINGERBREAD_MR1;// 10

	public static final int SDK_VERSION_3_0 = VERSION_CODES.HONEYCOMB;// 11

	public static final int sdk_version_3_1 = VERSION_CODES.HONEYCOMB_MR1;// 12

	public static final int SDK_VERSION_3_2 = VERSION_CODES.HONEYCOMB_MR2;// 13

	public static final int SDK_VERSION_4_0 = VERSION_CODES.ICE_CREAM_SANDWICH;// 14

	public static final int SDK_VERSION_4_0_3 = VERSION_CODES.ICE_CREAM_SANDWICH_MR1;// 15

	public static final int SDK_VERSION_4_1 = VERSION_CODES.JELLY_BEAN;// 16

	public static final int SDK_VERSION_4_2 = VERSION_CODES.JELLY_BEAN_MR1;// 17
	
	/**
	 * 得到手机端Android版本
	 * @return 手机端版本
	 */
	public static int getLocalApiVersion() {
		int version = Build.VERSION.SDK_INT;
		return version;
	}
}
