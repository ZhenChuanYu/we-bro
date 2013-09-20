package com.node.browser.webviewmanager;

import java.util.ArrayList;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.webkit.WebView;

import com.node.browser.ActivityMain.FragAdapter;
import com.node.browser.fragment.FragBaseWebviewPage;
import com.node.browser.fragment.NFragment;
import com.node.browser.webviewmanager.NWebview.UrlStatusObserver;

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
	private ViewPager mViewPager;

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
	public void initFields(FragAdapter adapter,
			ArrayList<NFragment> dataSource, ViewPager viewPager) {
		this.mFragAdapter = adapter;
		this.mFrags = dataSource;
		this.mViewPager = viewPager;
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
	public void loadingUrlInNewWindow(final String url,
			FragBaseWebviewPage parent, UrlStatusObserver urlStatusObserver) {
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
		newPage.setUrlStatusObserver(urlStatusObserver);
		mFrags.add(newPage);
		mFragAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(mFrags.size() - 1, false);
		// if (parent != null) {
		// removeFragmentChild(parent);
		// parent.setChild(newPage);
		// }
		// mFragAdapter.notifyDataSetChanged();
	}

	/**
	 * 删除一个Fragment，用于当前Webview加载新url时需删除原来的子webview
	 * 
	 * @param nFragment
	 */

	@Deprecated
	public void removeFragmentChild(NFragment nFragment) {
		if (nFragment instanceof FragBaseWebviewPage) {
			FragBaseWebviewPage basePage = (FragBaseWebviewPage) nFragment;
			if (basePage.hasChild()) {
				basePage.stopChildWebview();
				basePage.removeChild(mFrags);
				mFragAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 获得当前webview
	 * 
	 * @return
	 */
	public NWebview currentWebview() {
		NFragment curr = (NFragment) mFragAdapter.getItem(mViewPager
				.getCurrentItem());
		if (curr instanceof FragBaseWebviewPage) {
			return curr.getWebview();
		} else {
			return null;
		}
	}

	/**
	 * 根据index获得webview
	 * 
	 * @param index
	 * @return
	 */
	public NWebview getWebview(int index) {
		return ((NFragment) mFragAdapter.getItem(index)).getWebview();
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
