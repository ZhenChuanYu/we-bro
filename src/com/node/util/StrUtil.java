package com.node.util;

import com.node.browser.NodeConstants;

public class StrUtil {
	static String[] domains = NodeConstants.domains;

	/**
	 * 判断是否是合法域名,Test
	 * @param inputStr
	 * @param trimInput
	 * @return
	 */
	static boolean ifEndWithDomain(String inputStr, boolean trimInput) {
		boolean flag = false;
		String tmp = trimInput ? inputStr.trim() : inputStr;
		for (String dom : domains) {
			if (tmp.endsWith("." + dom)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
