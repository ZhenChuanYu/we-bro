package com.node.browser.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.node.browser.webviewmanager.NWebview;
import com.node.log.NLog;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class NFragment extends Fragment {

	protected NWebview webView;
	protected LinearLayout mWebviewContainer;

	protected InvokerAfterInit invoker;// 初始化webview之后的回调

	public NFragment() {
		super();
		NLog.e("zhenchuan", "the new NFragment UUID is " + hashCode());
	}

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
