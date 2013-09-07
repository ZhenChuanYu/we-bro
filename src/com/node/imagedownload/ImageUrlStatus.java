package com.node.imagedownload;

import java.util.HashMap;

import android.widget.ImageView;

public class ImageUrlStatus {

	public static final int STATUS_REQUESTING = 100;// 正在请求
	public static final int STATUS_REQUESTED_ERROR = 101;// 请求失败
	public static final int STATUS_REQUESTED_SUCCESS = 102;// url请求成功
	public static final int STATUS_EMPTY_REQUEST = 103;// 此Url未请求过


	/*
	 * 保存Url的请求状态
	 */
	static final HashMap<String, Integer> mRequeststatus = new HashMap<String, Integer>(
			50);
	/*
	 * 保存ImageView － Url的映射
	 */
	static final HashMap<ImageView, String> mImageViewUrlPair = new HashMap<ImageView, String>(
			50);

	/**
	 * 判断url可否请求
	 * 
	 * @param url
	 * @return
	 */
	public static boolean canRequestUrl(String url) {
		int status = getUrlRequeststatus(url);
		return (status != STATUS_REQUESTING);
	}

	public static int getUrlRequeststatus(String url) {
		if (mRequeststatus.containsKey(url)) {
			return mRequeststatus.get(url);
		} else {
			mRequeststatus.put(url, STATUS_EMPTY_REQUEST);
			return STATUS_EMPTY_REQUEST;
		}
	}

	/**
	 * 设置url请求的状态
	 * 
	 * @param url
	 * @param status
	 */
	public static void setUrlRequeststatus(String url, int status) {
		mRequeststatus.put(url, status);
	}

	public static void setImageViewUrlPair(ImageView imgView, String url) {
		mImageViewUrlPair.put(imgView, url);
	}

	public static String getImageViewUrl(ImageView imgView) {
		if (mImageViewUrlPair.containsKey(imgView)) {
			return mImageViewUrlPair.get(imgView);
		} else {
			return "";
		}
	}
}
