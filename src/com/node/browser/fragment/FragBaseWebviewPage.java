package com.node.browser.fragment;

import com.node.browser.R;
import com.node.browser.webviewmanager.NWebview;
import com.node.browser.webviewmanager.WebViewManager;
import com.node.log.NLog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragBaseWebviewPage extends NFragment {
	private final static String TAG = FragBaseWebviewPage.class.getName();

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
		webView = (NWebview) view.findViewById(R.id.webview);
		initWebview(webView);

		if (invoker != null) {
			invoker.loadWithMessage(webView);
			invoker.loadWithUrl(webView);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	private void initWebview(NWebview webView) {
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				//必须return false
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
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
				WebViewManager.instance().loadingUrlInNewWindow(resultMsg);
				return true;
			}
		});
		
	}
}
