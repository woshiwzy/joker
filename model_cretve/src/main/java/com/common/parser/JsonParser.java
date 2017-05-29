package com.common.parser;

import com.common.bean.LoginBean;
import com.common.net.NetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonParser {

	public static final String MESSAGE_OK = "OK";
	public static final String STATUS_OK = "200";
	public static final String STATUS_LOGOUT = "502";

	// 解析登陆
	public static NetResult parseLogin(InputStream ins) throws Exception, JSONException {
		String rsultJson = getJsonFromInputStream(ins);
//		LogUtil.d(App.tag, "" + rsultJson);
		JSONObject jobj = new JSONObject(rsultJson);
		String status = getStringFroJso("status", jobj);
		String message = getStringFroJso("message", jobj);
		NetResult netResult = new NetResult();
		netResult.setCode(status);
		netResult.setMessage(message);
		if (STATUS_OK.equals(status)) {
			JSONObject response = jobj.getJSONObject("responseResult");
			LoginBean lb = new LoginBean();
			String userName = getStringFroJso("username", response);
			lb.setUsername(userName);
			String accessToken = getStringFroJso("accessToken", response);
			lb.setAccessToken(accessToken);
			String createTime = getStringFroJso("createTime", response);
			lb.setCreateTime(createTime);
			String expiredTime = getStringFroJso("expiredTime", response);
			lb.setExpiredTime(expiredTime);
			String appKey = getStringFroJso("appKey", response);
			lb.setAppKey(appKey);
			String tokenStatus = getStringFroJso("tokenStatus", response);
			lb.setTokenStatus(tokenStatus);
			lb.setEmail(getStringFroJso("mail", response));
			Object[] data = { lb };
			netResult.setData(data);
		}
		return netResult;
	}

	// 解析验证邮箱
	public static NetResult parseUpdateEmail(InputStream ins) throws Exception, JSONException {
		String rsultJson = getJsonFromInputStream(ins);
//		LogUtil.d(App.tag, "" + rsultJson);
		JSONObject jobj = new JSONObject(rsultJson);
		String status = getStringFroJso("status", jobj);
		String message = getStringFroJso("message", jobj);
		NetResult netResult = new NetResult();
		netResult.setCode(status);
		netResult.setMessage(message);
		if (STATUS_OK.equals(status)) {
			String content = getStringFroJso("responseResult", jobj);
			Object[] data = { content };
			netResult.setData(data);
		}
		return netResult;
	}

	// 解析验证码
	public static NetResult parseVerifyEmail(InputStream ins) throws Exception, JSONException {
		String rsultJson = getJsonFromInputStream(ins);
//		LogUtil.d(App.tag, "" + rsultJson);
		JSONObject jobj = new JSONObject(rsultJson);
		String status = getStringFroJso("status", jobj);
		String message = getStringFroJso("message", jobj);
		NetResult netResult = new NetResult();
		netResult.setCode(status);
		netResult.setMessage(message);
		if (STATUS_OK.equals(status)) {
			JSONObject response = jobj.getJSONObject("responseResult");
			String verifyCode = getStringFroJso("verifyCode", response);
			String verifyMessage = getStringFroJso("verifyMessage", response);
			Object[] data = { verifyCode, verifyMessage };
			netResult.setData(data);
		}
		return netResult;
	}

	// 软件版本
	public static NetResult parseVersion(InputStream ins) throws Exception, JSONException {
		String rsultJson = getJsonFromInputStream(ins);
//		LogUtil.d(App.tag, "" + rsultJson);
		JSONObject jobj = new JSONObject(rsultJson);
		String status = getStringFroJso("status", jobj);
		String message = getStringFroJso("message", jobj);
		NetResult netResult = new NetResult();
		netResult.setCode(status);
		netResult.setMessage(message);
		if (STATUS_OK.equals(status)) {
			// String[] datas = { version, forceUpdate, desc, downurl,
			// updateTime, packageSize };
			JSONObject response = jobj.getJSONObject("responseResult");
			String name = getStringFroJso("name", response);
			String version = getStringFroJso("version", response);
			String url = getStringFroJso("url", response);
			String desc = getStringFroJso("desc", response);
			String updateTime = getStringFroJso("updateTime", response);
			String packageSize = getStringFroJso("packageSize", response);
			String forceUpdate = getStringFroJso("forceUpdate", response);
			Object[] data = { version, forceUpdate, desc, url, updateTime, packageSize };
			netResult.setData(data);
		}
		return netResult;
	}

	// =====公共方法======================
	private static String getJsonFromInputStream(InputStream ins) {
		if (null != ins) {
			StringBuffer sbf = new StringBuffer();
			BufferedInputStream bfris = new BufferedInputStream(ins);
			byte[] buffer = new byte[512];
			int ret = -1;
			try {
				while ((ret = bfris.read(buffer)) != -1) {
					sbf.append(new String(buffer, 0, ret));
				}
				bfris.close();
				ins.close();
				return sbf.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String getStringFroJso(String key, JSONObject jobj) throws JSONException {
		if (jobj.has(key)) {
			return jobj.getString(key);
		} else {
			return "";
		}

	}
}
