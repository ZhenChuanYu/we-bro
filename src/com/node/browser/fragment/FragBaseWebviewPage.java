package com.node.browser.fragment;

import java.util.ArrayList;
import java.util.Iterator;

import com.node.browser.R;
import com.node.browser.cookie.NodeCookieManager;
import com.node.browser.webviewmanager.NWebview;
import com.node.browser.webviewmanager.NWebview.UrlStatusObserver;
import com.node.browser.webviewmanager.WebViewManager;
import com.node.downloadprovider.DownloadManager;
import com.node.downloadprovider.DownloadManager.Request;
import com.node.log.NLog;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.HitTestResult;
import android.widget.LinearLayout;

public class FragBaseWebviewPage extends NFragment {
	private final static String TAG = FragBaseWebviewPage.class.getName();

	private NWebview.UrlStatusObserver mUrlStatusObserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.node_frag_basepage, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		NLog.e(TAG, "NFragment onViewCreated is in");
		webView = (NWebview) view.findViewById(R.id.webview);
		mWebviewContainer = (LinearLayout) view
				.findViewById(R.id.webview_container);

		initWebview(webView);
		webView.setUrlStatusObserver(mUrlStatusObserver);

		if (invoker != null) {
			invoker.loadWithMessage(webView);
			invoker.loadWithUrl(webView);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	private void initWebview(final NWebview webView) {
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("newwindow:")) {
					WebViewManager.instance().loadingUrlInNewWindow(
							url.substring(10), FragBaseWebviewPage.this,
							mUrlStatusObserver);
				} else {
					view.loadUrl(url); // load url in current WebView
				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				webView.updateUrlStatus(true, false);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.setAttribute('target','_self');link.href = 'newwindow:'+link.href;}}}");
				webView.updateUrlStatus(false, false);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onCreateWindow(WebView view, boolean isDialog,
					boolean isUserGesture, Message resultMsg) {
				if (isDialog) {
					NLog.e(TAG, "new window is dialog");
				} else {
					NLog.e(TAG, "new window is not a dialog");
				}
				if (isUserGesture) {
					NLog.e(TAG, "new window 是用户点击链接");
				} else {
					NLog.e(TAG, "new window 不是用户点击链接");
				}
				// WebViewManager.instance().loadingUrlInNewWindow(resultMsg);
				return true;
			}
		});

		webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				NLog.e("download", "url is:" + url + "\n userAgent is "
						+ userAgent + "\n contentDis " + contentDisposition
						+ "\n mimetype is " + mimetype + "\n contentLength is "
						+ contentLength);
				// begin to download
				DownloadManager manager = new DownloadManager(getActivity()
						.getContentResolver(), getActivity().getPackageName());
				DownloadManager.Request request = new Request(Uri.parse(url));
				request.setDestinationInExternalPublicDir(
						Environment.DIRECTORY_DOWNLOADS, "/");
				request.setDescription("");
				String cookie = NodeCookieManager.instance().getCookie(url);
				NLog.e("download cookie is ", cookie);
				request.addRequestHeader("Cookie", cookie);
				// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
				request.setShowRunningNotification(true);
				manager.enqueue(request);
			}
		});
	}

	private FragBaseWebviewPage child = null;

	public FragBaseWebviewPage getChild() {
		return child;
	}

	public void setChild(FragBaseWebviewPage child) {
		this.child = child;
	}

	/**
	 * 是否有子儿子
	 * 
	 * @return
	 */
	public boolean hasChild() {
		return child != null;
	}

	/**
	 * 停止子节点的Webview
	 */
	@Deprecated
	public void stopChildWebview() {
		if (child == null) {
			return;
		} else {
			child.stopChildWebview();
			NLog.e("null", child.webView == null ? "webview is null"
					: "webview is not null");
			NLog.e("null", child.mWebviewContainer == null ? "null"
					: "mWebviewContainer is not null");
			child.mWebviewContainer.removeView(child.webView);
			child.webView.removeAllViews();
			child.webView.clearHistory();
			child.webView.destroy();
		}
	}

	@Override
	public void onDetach() {
		NLog.e(TAG, "onDetach is in");
		super.onDetach();
	}

	/**
	 * 移除子节点
	 */
	@Deprecated
	public void removeChild(ArrayList<NFragment> dataSouce) {
		if (this.child == null) {
			return;
		} else {
			this.child.removeChild(dataSouce);
			Iterator<NFragment> it = dataSouce.iterator();
			while (it.hasNext()) {
				NFragment nFrag = it.next();
				if (nFrag.equals(this.child)) {
					NLog.i(TAG, "NFragment: UUID is " + this.child.hashCode()
							+ " removed");
					it.remove();
					this.child = null;
					break;
				}
			}
		}
	}

	public void setUrlStatusObserver(UrlStatusObserver observer) {
		mUrlStatusObserver = observer;
	}

}
