package com.node.browser.fragment;

import com.node.browser.webviewmanager.NWebview;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

public class NFragment extends Fragment {

	protected NWebview webView;

	protected InvokerAfterInit invoker;// 初始化webview之后的回调

	public NWebview getWebview() {
		return this.webView;
	}

	public void setInvokerAfterInit(InvokerAfterInit invoker) {
		this.invoker = invoker;
	}

	/**
	 * 在WebView初始化后执行
	 * 
	 * @author zhenchuan
	 * 
	 */
	public interface InvokerAfterInit {
		void loadWithUrl(WebView webview);

		void loadWithMessage(WebView webview);
	}

}
