package com.node.browser.webviewmanager;

import com.node.browser.NodeConstants;
import com.node.util.GlobalUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;

public class NWebview extends WebView {

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

	private int titleHeight;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// determine height of title bar
		View title = getChildAt(0);
		titleHeight = title == null ? 0 : title.getMeasuredHeight();
	}

	private boolean touchInTitleBar;

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {

		boolean wasInTitle = false;
		switch (me.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			touchInTitleBar = (me.getY() <= visibleTitleHeight());
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			wasInTitle = touchInTitleBar;
			touchInTitleBar = false;
			break;
		}
		if (touchInTitleBar || wasInTitle) {
			View title = getChildAt(0);
			if (title != null) {
				// this touch belongs to title bar, dispatch it here
				me.offsetLocation(0, getScrollY());
				return false;
			}
		}
		// this is our touch, offset and process
		me.offsetLocation(0, -titleHeight);
		return super.dispatchTouchEvent(me);
	}

	/**
	 * @return visible height of title (may return negative values)
	 */
	private int visibleTitleHeight() {
		return titleHeight - getScrollY();
	}

	@Override
	protected void onDraw(Canvas c) {
		c.save();
		int tH = visibleTitleHeight();
		if (tH > 0) {
			// clip so that it doesn't clear background under title bar
			int sx = getScrollX(), sy = getScrollY();
			c.clipRect(sx, sy + tH, sx + getWidth(), sy + getHeight());
		}
		c.translate(0, titleHeight);
		super.onDraw(c);
		c.restore();
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

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		View title = getChildAt(0);
		if (title != null) // undo horizontal scroll, so that title scrolls only
							// vertically
			title.offsetLeftAndRight(l - title.getLeft());
		boolean needUpdateBound = false;
		if (l <= 0) {
			setScrollX(0);
		}
		if (t <= 0) {
			setScrollY(0);
		}
		/* 此处判断显示或隐藏顶部Url输入框 */
		hidenOrShowHeaderUrlArea(l, t, oldl, oldt);
	}

	/**
	 * 判断是否显示或隐藏顶部的组件
	 * 
	 * @see method {@link NWebview#hidenOrShowHeaderUrlArea(int, int, int, int)}<br>
	 *      used in lifestyle method
	 *      {@link #onScrollChanged(int, int, int, int)}<br>
	 */
	private long lastMillis = 0;
	private int lastTop = 0;
	private int headerHeight = (int) GlobalUtil.dip2px(getContext(), 40) + 1;

	private void hidenOrShowHeaderUrlArea(int l, int t, int oldl, int oldt) {
		long currentMillis = System.currentTimeMillis();
		int currentTop = t;
		if (lastMillis == 0 || currentMillis - lastMillis > 300) {
			lastMillis = currentMillis;
			lastTop = currentTop;
		}
		boolean isDown = t - oldt > 0;// 判断用户是否意图向上滑动
		if (isDown) {
			if (currentTop - lastTop > headerHeight
					&& currentTop > 1.5 * headerHeight) {
				NWebview webview = WebViewManager.instance().currentWebview();
				if (webview.equals(this)) {
					urlAreaHidenOrShowDeletegate.onHidenUrlArea();
				}
				lastTop = currentTop;
			}
		} else {
			if (currentTop <= 6 * headerHeight) {
				NWebview webview = WebViewManager.instance().currentWebview();
				if (webview.equals(this)) {
					urlAreaHidenOrShowDeletegate.onShowUrlArea();
				}
			}
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

	/*
	 * webview通知 显示或隐藏Url输入区域接口
	 */
	public interface UrlAreaHidenOrShowDelegate {

		void onHidenUrlArea();

		void onShowUrlArea();
	}

	private UrlAreaHidenOrShowDelegate urlAreaHidenOrShowDeletegate;

	public void setUrlAreaHidenOrShowDelegate(
			UrlAreaHidenOrShowDelegate areaHidenOrShowDelegate) {
		this.urlAreaHidenOrShowDeletegate = areaHidenOrShowDelegate;
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
