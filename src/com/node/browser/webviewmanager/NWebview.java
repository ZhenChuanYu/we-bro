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
		initSettings();
		// setWebViewClient();
		// setWebChromeClient();
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
		if(android.os.Build.VERSION.SDK_INT>=16){
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

	/**
	 * 更新下当前content的实际宽度 实际宽度=原始宽度*缩放比率
	 */
	private void updateContentWidthHeight(float scale) {
		contentWidth = (int) (contentSrcWidth * scale);
		contentHeight = (int) (contentSrcHeight * scale);
		maxRight = contentWidth - GlobalUtil.getDevPixWidth(getContext());
		maxBottom = contentHeight - getHeight();
	}
}
