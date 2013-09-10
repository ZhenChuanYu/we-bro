package com.node.browser.fragment;

import com.node.browser.R;
import com.node.browser.webviewmanager.NWebview;
import com.node.log.NLog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class FragFirstPage extends NFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		webView.loadUrl("http://www.appchina.com");
		super.onViewCreated(view, savedInstanceState);
	}
	
	private void initWebview(NWebview webView2) {
//		webView2.setNWebChromeClient(new NWebChromeClient());
//		webView2.setNWebViewClient(new NWebViewClient());
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		NLog.e("zhenchuan", "onSaveInstance is in");
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public View getView() {
		NLog.e("zhenchuan", "getView is in");
		return super.getView();
	}
}
