package com.node.browser.webviewmanager;

import com.node.browser.NodeConstants;
import com.node.util.GlobalUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;

public class NWebview extends WebView {

	int contentSrcWidth;
	int contentWidth;
	int contentSrcHeight;
	int contentHeight;

	public NWebview(Context context) {
		super(context);
		initWebview();
	}

	public NWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWebview();
	}

	private void initWebview() {
		// 增加JS本地调用接口
		addJavascriptInterfaces();
		// 初始化设置
		initSettings();
		// 通知观察者
		notifyWebviewInited();
	}

	@SuppressLint("NewApi")
	private void initSettings() {
		WebSettings settings = getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setSupportMultipleWindows(true);
		settings.setDomStorageEnabled(true);
		settings.setDisplayZoomControls(true);
		settings.setUseWideViewPort(true);
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			settings.setAllowUniversalAccessFromFileURLs(true);
			settings.setAllowFileAccessFromFileURLs(true);
		}
		settings.setPluginState(PluginState.ON);
		settings.setUserAgentString(NodeConstants.UA_CHROME);
		/*
		 * settings.setDefaultZoom(ZoomDensity.FAR);
		 */
	}

	private void addJavascriptInterfaces() {
		// 使用js获得页面宽度的接口
		// addJavascriptInterface(new JavaScriptInterface(), "GETWIDTH");
	}

	/*
	 * protected void getContentWidthThrowJS() { loadUrl(
	 * "javascript:window.GETWIDTH.getContentWidth(document.getElementsByTagName('html')[0].scrollWidth);"
	 * ); }
	 */
	protected void getContentHeightThrowNative() {
		contentSrcHeight = getContentHeight();
	}

	/*
	 * class JavaScriptInterface { public void getContentWidth(String value) {
	 * if (value != null) { contentSrcWidth = Integer.parseInt(value);
	 * //以下需要在主线程执行 post(new Runnable() {
	 * 
	 * @Override public void run() { Toast.makeText( getContext(), "width is " +
	 * contentSrcWidth + " height is " + contentSrcHeight, Toast.LENGTH_SHORT)
	 * .show(); contentSrcHeight = getContentHeight();
	 * updateContentWidthHeight(getScale()); } }); } } }
	 */

	float maxRight = 0f;
	float maxBottom = 0f;
	float headerHeight = 0f;

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		boolean needUpdateBound = false;
		if (l <= 0) {
			setScrollX(0);
		}
		if (t <= 0) {
			setScrollY(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	public boolean zoomIn() {
		return super.zoomIn();
	}

	@Override
	public boolean zoomOut() {
		return super.zoomOut();
	}

	@Override
	public void reload() {
		super.reload();
		// 更新状态 isLoading为false，isWaiting为true
		updateUrlStatus(false, true);
	}

	/**
	 * 更新下当前content的实际宽度 实际宽度=原始宽度*缩放比率
	 */
	private void updateContentWidthHeight(float scale) {
		contentWidth = (int) (contentSrcWidth * scale);
		contentHeight = (int) (contentSrcHeight * scale);
		maxRight = contentWidth - GlobalUtil.getDevPixWidth(getContext());
		maxBottom = contentHeight - getHeight();
	}

	/*
	 * webview状态管理 通过status[INDEX_CAN_GO_BACK]获取是否可以回退的状态
	 * 通过status[INDEX_CAN_GO_FORWARD]获取是否可以前进的状态
	 * 通过status[INDEX_URL_IS_LOADING]获取是否有url正在加载
	 */
	public boolean[] status = new boolean[4];
	public static final int INDEX_CAN_GO_BACK = 0;
	public static final int INDEX_CAN_GO_FORWARD = 1;
	public static final int INDEX_URL_IS_LOADING = 2;
	public static final int INDEX_URL_WAITINGFOR_LOAD = 3;

	{
		status[INDEX_CAN_GO_BACK] = false;
		status[INDEX_CAN_GO_FORWARD] = false;
		status[INDEX_URL_IS_LOADING] = false;
		status[INDEX_URL_WAITINGFOR_LOAD] = true;
	}

	/**
	 * url状态观察者接口
	 * 
	 * @author zhenchuan
	 * 
	 */
	public interface UrlStatusObserver {

		void onInitNWebview(boolean[] status, NWebview webview);

		void onUrlStatusChanged(boolean[] status, NWebview webview);

	}

	/**
	 * webview的前进、后退、加载中状态发生变化的观察者
	 */
	private UrlStatusObserver mObserver;

	/**
	 * 设置状态变化的观察者
	 * 
	 * @param observer
	 */
	public void setUrlStatusObserver(UrlStatusObserver observer) {
		mObserver = observer;
	}

	/**
	 * 通知观察者，此时状态已经发生变化<br>
	 * 
	 * @see #updateUrlStatus(boolean)<br>
	 * @see #updateUrlStatus(boolean, boolean, boolean)<br>
	 */
	private void notifyObserverStatusChanged() {
		WebView current = WebViewManager.instance().currentWebview();
		if (current != null && current.equals(this) && mObserver != null) {
			mObserver.onUrlStatusChanged(status, this);
		}
	}

	/**
	 * 通知观察者，webview已初始化完成
	 */
	private void notifyWebviewInited() {
		WebView current = WebViewManager.instance().currentWebview();
		if (current != null && current.equals(this) && mObserver != null) {
			mObserver.onInitNWebview(status, this);
		}
	}

	/**
	 * 更新状态信息
	 * 
	 * @param canGoBack
	 * @param canGoForward
	 * @param isLoading
	 * @param isWaiting
	 */
	public void updateUrlStatus(boolean canGoBack, boolean canGoForward,
			boolean isLoading, boolean isWaiting) {
		status[INDEX_CAN_GO_BACK] = canGoBack;
		status[INDEX_CAN_GO_FORWARD] = canGoForward;
		status[INDEX_URL_IS_LOADING] = isLoading;
		status[INDEX_URL_WAITINGFOR_LOAD] = isWaiting;
		notifyObserverStatusChanged();
	}

	/**
	 * 更新状态<br>
	 * 通常使用此方法而非 {@link #updateUrlStatus(boolean)}
	 * 
	 * @param isLoading
	 */
	public void updateUrlStatus(boolean isLoading, boolean isWaiting) {
		updateUrlStatus(this.canGoBack(), this.canGoForward(), isLoading,
				isWaiting);
	}
}
