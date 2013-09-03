package com.node.browser.customviews;

import com.node.browser.customcomponents.NWebChromeClient;
import com.node.browser.customcomponents.NWebViewClient;
import com.node.log.NLog;
import com.node.util.GlobalUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebStorage.QuotaUpdater;
import android.widget.Toast;

public class NWebview extends WebView {

	NWebChromeClient mWebChromeClient;
	NWebViewClient mWebViewClient;
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
		setWebViewClient();
		setWebChromeClient();
	}

	private void initSettings() {
		WebSettings settings = getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		settings.setDisplayZoomControls(true);
		/*
		 * settings.setDefaultZoom(ZoomDensity.FAR);
		 */}

	private void addJavascriptInterfaces() {
		// 使用js获得页面宽度的接口
		// addJavascriptInterface(new JavaScriptInterface(), "GETWIDTH");
	}

	private void setWebChromeClient() {
		setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedIcon(WebView view, Bitmap icon) {
				super.onReceivedIcon(view, icon);
			}

			@Override
			public void onCloseWindow(WebView window) {
				super.onCloseWindow(window);
			}

			@Override
			public boolean onCreateWindow(WebView view, boolean isDialog,
					boolean isUserGesture, Message resultMsg) {
				return super.onCreateWindow(view, isDialog, isUserGesture,
						resultMsg);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				super.onShowCustomView(view, callback);
			}

			@Override
			public void onShowCustomView(View view, int requestedOrientation,
					CustomViewCallback callback) {
				super.onShowCustomView(view, requestedOrientation, callback);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}

			@Override
			public void onRequestFocus(WebView view) {
				super.onRequestFocus(view);
			}

			@Override
			public void onReceivedTouchIconUrl(WebView view, String url,
					boolean precomposed) {
				super.onReceivedTouchIconUrl(view, url, precomposed);
			}

			@Override
			public void onReachedMaxAppCacheSize(long requiredStorage,
					long quota, QuotaUpdater quotaUpdater) {
				super.onReachedMaxAppCacheSize(requiredStorage, quota,
						quotaUpdater);
			}

			@Override
			public void onHideCustomView() {
				super.onHideCustomView();
			}

			@Override
			public boolean onJsTimeout() {
				return super.onJsTimeout();
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				return super.onJsPrompt(view, url, message, defaultValue,
						result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				return super.onJsBeforeUnload(view, url, message, result);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onGeolocationPermissionsHidePrompt() {
				super.onGeolocationPermissionsHidePrompt();
			}

			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			@Override
			public void onExceededDatabaseQuota(String url,
					String databaseIdentifier, long quota,
					long estimatedDatabaseSize, long totalQuota,
					QuotaUpdater quotaUpdater) {
				super.onExceededDatabaseQuota(url, databaseIdentifier, quota,
						estimatedDatabaseSize, totalQuota, quotaUpdater);
			}

			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				return super.onConsoleMessage(consoleMessage);
			}

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				super.onConsoleMessage(message, lineNumber, sourceID);
			}
		});
	}

	private void setWebViewClient() {
		setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 获得网页宽度
				mWebViewClient.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mWebViewClient.onPageStarted(view, url, favicon);
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend,
					Message resend) {
				mWebViewClient.onFormResubmission(view, dontResend, resend);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				mWebViewClient.onLoadResource(view, url);
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view,
					HttpAuthHandler handler, String host, String realm) {
				mWebViewClient.onReceivedHttpAuthRequest(view, handler, host,
						realm);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				mWebViewClient.onReceivedError(view, errorCode, description,
						failingUrl);
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				mWebViewClient.onReceivedSslError(view, handler, error);
			}

			@Override
			public void onReceivedLoginRequest(WebView view, String realm,
					String account, String args) {
				mWebViewClient.onReceivedLoginRequest(view, realm, account,
						args);
			}

			@Override
			public void onScaleChanged(WebView view, float oldScale,
					float newScale) {
				mWebViewClient.onScaleChanged(view, oldScale, newScale);
			}

			@Override
			public void onTooManyRedirects(WebView view, Message cancelMsg,
					Message continueMsg) {
				mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
			}

			@Override
			public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
				mWebViewClient.onUnhandledKeyEvent(view, event);
			}

		});
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

	public void setNWebChromeClient(NWebChromeClient mWebChromeClient) {
		this.mWebChromeClient = mWebChromeClient;
	}

	public void setNWebViewClient(NWebViewClient mWebViewClient) {
		this.mWebViewClient = mWebViewClient;
	}
}
