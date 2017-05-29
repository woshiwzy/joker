package com.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateTool {

	/**
	 * 检测中文姓名
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkChineseName(String name) {
		if (StringUtils.isEmpty(name) || !name.matches("[\u4e00-\u9fa5]{2,4}")) {
			return false;
		} else
			return true;
	}

	public static boolean checkEmail(String emailStr) {
		if (StringUtils.isEmpty(emailStr)) {
			return false;
		}

		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(emailStr);
		boolean isMatched = matcher.matches();

		return  isMatched;

//		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//		Pattern regex = Pattern.compile(check);
//		Matcher matcher = regex.matcher(emailStr);
//		boolean isMatched = matcher.matches();
//		if (isMatched) {
//			return true;
//		}
//		return false;
	}

	public static boolean isMobilePhone(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isUsereName(String uname) {
		if (StringUtils.isEmpty(uname)) {
			return false;
		}
		Pattern p = Pattern.compile("^[a-z0-9_-]{3,15}$");
		Matcher m = p.matcher(uname);
		return m.matches();
	}

	public static boolean isPasswd(String passswd) {
		// [a-zA-Z][a-zA-Z0-9]{5,15}
		if (StringUtils.isEmpty(passswd)) {
			return false;
		}
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,15}");
		Matcher m = p.matcher(passswd);
		return m.matches();
	}

}
