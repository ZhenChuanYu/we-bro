package com.node.browser.cookie;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * cookie manager component<br>
 * use {@link CookieManager} and {@link CookieSyncManager}
 * 
 * @author zhenchuan
 * 
 */

public class NodeCookieManager {

	private static CookieManager mCookieManager;
	private static CookieSyncManager mCookieSyncManager;

	private NodeCookieManager() {
	};

	private static NodeCookieManager manager;

	/**
	 * 须在程序开始时初始化 {@link #init(Context)}
	 */
	public static NodeCookieManager instance() {
		if (manager == null) {
			manager = new NodeCookieManager();
		}
		return manager;
	}

	public static NodeCookieManager init(Context context) {
		mCookieManager = CookieManager.getInstance();
		mCookieSyncManager = CookieSyncManager.createInstance(context);
		return manager;
	}

	public void startSync() {
		mCookieSyncManager.startSync();
	}

	public void stopSync() {
		mCookieSyncManager.stopSync();
	}

	public NodeCookieManager setAcceptCookie(boolean accept) {
		mCookieManager.setAcceptCookie(accept);
		return this;
	}

	public NodeCookieManager setAcceptFileSchemeCookies(boolean accept) {
		CookieManager.setAcceptFileSchemeCookies(accept);
		return this;
	}

	public String getCookie(String url) {
		return mCookieManager.getCookie(url);
	}

	public boolean hasCookies() {
		return mCookieManager.hasCookies();
	}

	public void removeAllCookie() {
		mCookieManager.removeAllCookie();
	}

	public void removeExpiredCookie() {
		mCookieManager.removeExpiredCookie();
	}

	public void removeSessionCookie() {
		mCookieManager.removeSessionCookie();
	}

	public void setCookie(String url, String value) {
		mCookieManager.setCookie(url, value);
	}

}