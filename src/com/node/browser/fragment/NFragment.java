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

	public String UUID;// 唯一标示

	public NFragment() {
		super();
		UUID = String.valueOf(this.hashCode());
		NLog.e("zhenchuan", "the new NFragment UUID is " + UUID);
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

	/**
	 * 判断是否为新UUID
	 * 
	 * @param addUUID
	 * @return
	 */
	public boolean isNewUUID(boolean addUUID) {
		boolean has = hasUUID(UUID);
		if (addUUID && !has) {
			addUUID(UUID);
		}
		return !has;
	}

	/**
	 * 从UUID组中删除UUID
	 */
	public void deleteUUID() {
		deleteUUID(UUID);
	}

	/**
	 * UUID管理
	 */
	private static List<String> uuids = new ArrayList<String>(50);

	/**
	 * 删除一个UUID
	 * 
	 * @param uuid
	 */
	public static synchronized void deleteUUID(String uuid) {
		Iterator<String> it = uuids.iterator();
		while (it.hasNext()) {
			String temp = it.next();
			if (temp.equals(uuid)) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * 判断是否含有UUID
	 * 
	 * @param uuid
	 * @return
	 */
	public static synchronized boolean hasUUID(String uuid) {
		boolean has = false;
		for (String id : uuids) {
			if (id.equals(uuid)) {
				has = true;
				break;
			}
		}
		return has;
	}

	/**
	 * 增加加一个UUID
	 * 
	 * @param uuid
	 */
	public static synchronized void addUUID(String uuid) {
		uuids.add(uuid);
	}

}
