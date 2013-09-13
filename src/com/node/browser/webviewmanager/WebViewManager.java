package com.node.browser.webviewmanager;

import java.util.ArrayList;

import android.os.Message;
import android.webkit.WebView;

import com.node.browser.ActivityMain.FragAdapter;
import com.node.browser.fragment.FragBaseWebviewPage;
import com.node.browser.fragment.NFragment;

/**
 * 管理增删改webview的组件
 * 
 * @author zhenchuan
 * 
 */
public class WebViewManager {

	private static WebViewManager mManager;
	private FragAdapter mFragAdapter;
	private ArrayList<NFragment> mFrags;

	private WebViewManager() {
	};

	public static WebViewManager instance() {
		if (mManager == null) {
			mManager = new WebViewManager();
		}
		return mManager;
	}

	/**
	 * 此方法必须在应用创建时优先调用且只调用一次
	 * 
	 * @param adapter
	 * @param dataSource
	 */
	public void initFields(FragAdapter adapter, ArrayList<NFragment> dataSource) {
		this.mFragAdapter = adapter;
		this.mFrags = dataSource;
	}

	/**
	 * 在一个新窗口中打开链接 用于处理Href链接
	 * 
	 * @param url
	 * @param resultMsg
	 */
	public void loadingUrlInNewWindow(final Message resultMsg) {
		FragBaseWebviewPage newPage = new FragBaseWebviewPage();
		newPage.setInvokerAfterInit(new NFragment.InvokerAfterInit() {
			@Override
			public void loadWithUrl(WebView webview) {
				// do nothing
			}

			@Override
			public void loadWithMessage(WebView webview) {
				WebView.WebViewTransport transfer = (WebView.WebViewTransport) resultMsg.obj;
				transfer.setWebView(webview);
				resultMsg.sendToTarget();
			}
		});
		mFrags.add(newPage);
		mFragAdapter.notifyDataSetChanged();
	}

	/**
	 * 在一个新窗口中打开url
	 * 
	 * @param url
	 */
	public void loadingUrlInNewWindow(final String url) {
		FragBaseWebviewPage newPage = new FragBaseWebviewPage();
		newPage.setInvokerAfterInit(new NFragment.InvokerAfterInit() {
			@Override
			public void loadWithUrl(WebView webview) {
				webview.loadUrl(url);
			}

			@Override
			public void loadWithMessage(WebView webview) {
				// do nothing
			}
		});
		mFrags.add(newPage);
		mFragAdapter.notifyDataSetChanged();
	}

	/**
	 * 记录打开历史
	 */
	private ArrayList<TimeWebViewPair<Long, NWebview>> history;

	private void addTimeWebViewPair(NWebview web) {
		if (history == null) {
			history = new ArrayList<WebViewManager.TimeWebViewPair<Long, NWebview>>();
		}
		long now = System.currentTimeMillis();
		history.add(new TimeWebViewPair<Long, NWebview>(now, web));
	}

	class TimeWebViewPair<T, V> {
		T t;
		V v;

		public TimeWebViewPair(T t, V v) {
			this.t = t;
			this.v = v;
		}

		T getFirst() {
			return t;
		}

		V getSecond() {
			return v;
		}
	}
}
