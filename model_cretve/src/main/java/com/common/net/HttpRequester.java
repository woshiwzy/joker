package com.common.net;

import android.content.Context;
import android.os.Build;

import com.common.util.Constant;
import com.common.util.LogUtil;
import com.common.util.NetTool;
import com.common.util.StringUtils;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http请求工具，不直接调用
 *
 * @author:wangzhengyun 2012-9-11
 */
public class HttpRequester {

    final static String ENCODE = "UTF-8";
    public final static int TIME_OUT = 1000 * 30;
    final static String tag = "HttpRequester";

    final static String exception_url_empty = "URL 为空";
    final static String exception_param_empty = "参数 为空";
    final static String exception_request_time_out = "请求超时";
    final static String exception_getway_time_out = "网关超时";
    final static String exception_inner_error = "内部错误";
    final static String exception_getway_error = "网关错误";

    public static final int APN_NOTSET = 0;
    public static final int APN_CMWAP = 1; // 移动2G的wap接入10.0.0.172
    public static final int APN_CMNET = 2; // 移动2G的net接入
    public static final int APN_UNIWAP = 3; // 联通2G的wap接入
    public static final int APN_UNINET = 4; // 联通2G的net接入
    public static final int APN_CTWAP = 5; // 电信2G的wap接入10.0.0.200
    public static final int APN_CTNET = 6; // 电信2G的net接入
    public static final int APN_3GWAP = 7; // 电信3G的wap接入

    private static String mApnName; // 接入点类型名称
    public static int mApn; // 接入点类型


    public final static String REQUEST_POST = "POST";
    public final static String REQUEST_GET = "GET";
    public final static String REQUEST_PUT = "PUT";
    public final static String REQUEST_DELETE = "DELETE";


    public static void setApn(String apnName) {
        mApnName = apnName;
        if (apnName == null || apnName.length() == 0) {
            mApn = APN_NOTSET;
            return;
        }
        apnName = apnName.toLowerCase();
        if ("cmwap".equals(apnName)) {
            mApn = APN_CMWAP;
        } else if ("uniwap".equals(apnName)) {
            mApn = APN_UNIWAP;
        } else if ("ctwap".equals(apnName)) {
            mApn = APN_CTWAP;
        } else if ("3gwap".equals(apnName)) {
            mApn = APN_3GWAP;
        } else {
            mApn = APN_NOTSET;
        }
    }

    // 简单的header请求
    public static InputStream httphttpHeader(Context context, String path, HashMap<String, String> paramMap, String format)
            throws NetException, Exception {
        if (null == paramMap) {
            throw new NetException(-1, exception_param_empty);
        }
        // 组装url
        if (StringUtils.isEmpty(path)) {
            throw new NetException(-1, exception_url_empty);
        } else {
            // 添加请求参数
            URL url = new URL(path);
            HttpURLConnection conn = null;
            // 查看apn类型
            String typeName = NetTool.getNetType(context);
            setApn(typeName);
            LogUtil.i(Constant.TAG, "header apn:" + typeName + " url:" + path);
            conn = createProxyConnection(mApn, url);
            conn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            String acceptType = "xml";
            if (!StringUtils.isEmpty(format)) {
                acceptType = format;
            }
            conn.setRequestProperty("accept", "application/" + acceptType);
            if (null != paramMap && !paramMap.isEmpty()) {
                for (Entry<String, String> entry : paramMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtils.isEmpty(value)) {
                        conn.setRequestProperty(key, value);
                    }
                }
            }
            LogUtil.i(Constant.TAG_TRACETRIP, path + " " + paramMap.toString());
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 更具不通的apn类型，设置不通代理
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", ENCODE);
            conn.connect();
            if (null != conn) {
                int responseCode = conn.getResponseCode();
                if (200 == responseCode) {
                    return createNetInputStream(conn);
                } else {
                    return null;
                }
            }
            return null;
        }
    }

    /**
     * get方法取得参数
     *
     * @param path
     * @param paramMap
     * @throws UnsupportedEncodingException void 下午03:39:30 2011
     * @deprecated 本应用全部使用 post请求,如果需要使用get请求,请修改下面的方法后使用
     */
    public static String httpGet(Context context, String path, Map<String, String> paramMap) throws NetException, Exception {

        StringBuffer result = new StringBuffer();
        StringBuffer paramSbf = new StringBuffer();

        if (null != paramMap) {
            paramMap.putAll(NetTool.getCommonParam(context));
            // 组装参数
            for (Entry<String, String> entry : paramMap.entrySet()) {

                String name = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "";
                }
                paramSbf.append(name + "=" + URLEncoder.encode(value, ENCODE) + "&");
            }
        } else {
            throw new NetException(-1, exception_param_empty);
        }
        // 组装url
        if (StringUtils.isEmpty(path)) {
            throw new NetException(-1, exception_url_empty);
        } else {
            // 检测url
            String paramString = paramSbf.toString();
            if (path.endsWith("?")) {
                path = path + paramSbf.toString().substring(0, paramString.length());
            } else {
                path = path + "?" + paramSbf.toString().substring(0, paramString.length());
            }
            // 网络请求

            result = doHttpRequest(context, path, null);
        }
        return result.toString();
    }

    /**
     * httpPost参数,适用tralveksk相关接口
     *
     * @param path
     * @param paramMap
     * @return
     * @throws Exception String 下午05:53:46 2011
     */
    public static InputStream httpPost(Context context, String path, Map<String, String> paramMap) throws NetException, Exception {
        InputStream netInputStream = null;
        if (null == paramMap) {
            throw new NetException(-1, exception_param_empty);
        }

        // 组装url
        if (StringUtils.isEmpty(path)) {
            throw new NetException(-1, exception_url_empty);
        } else {
            StringBuffer xmlBuffer = new StringBuffer();
            xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xmlBuffer.append("<request>");
            xmlBuffer.append("<common>");
            // 添加公共参数
            for (Entry<String, String> entry : NetTool.getCommonParam(context).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "";
                }
                xmlBuffer.append("<" + key.trim() + ">" + value + "</" + key.trim() + ">");
            }
            xmlBuffer.append("</common>");

            xmlBuffer.append("<customized>");
            // 添加接口的参数参数

            for (Entry<String, String> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "";
                }
                xmlBuffer.append("<" + key.trim() + ">" + value + "</" + key + ">");
            }

            xmlBuffer.append("</customized>");
            xmlBuffer.append("</request>");
            // 添加请求参数
            LogUtil.i(Constant.TAG_TRACETRIP, path + " " + xmlBuffer.toString());
            netInputStream = doHttpRequestXML(context, path, xmlBuffer.toString(), null);
        }
        return netInputStream;
    }


    /**
     * httpPost参数,适用tt相关接口
     *
     * @param path
     * @param paramMap
     * @throws Exception
     */
    public static InputStream httpPostWithData(Context context, String path, Map<String, String> paramMap, String xmlData) throws NetException, Exception {
        InputStream netInputStream = null;
        if (null == paramMap) {
            throw new NetException(-1, exception_param_empty);
        }
        // 组装url
        if (StringUtils.isEmpty(path)) {
            throw new NetException(-1, exception_url_empty);
        } else {
            StringBuffer xmlBuffer = new StringBuffer();
            xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xmlBuffer.append("<request>");
            xmlBuffer.append("<common>");
            // 添加公共参数
            for (Entry<String, String> entry : NetTool.getCommonParam(context).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "";
                }
                xmlBuffer.append("<" + key.trim() + ">" + value + "</" + key.trim() + ">");
            }
            xmlBuffer.append("</common>");
            xmlBuffer.append("<customized>");
            // 添加接口的参数参数
            for (Entry<String, String> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "";
                }
                xmlBuffer.append("<" + key.trim() + ">" + value + "</" + key + ">");
            }
            xmlBuffer.append(xmlData);
            xmlBuffer.append("</customized>");
            xmlBuffer.append("</request>");
            // 添加请求参数
            LogUtil.i(Constant.TAG_TRACETRIP, path + " " + xmlBuffer.toString());
            netInputStream = doHttpRequestXML(context, path, xmlBuffer.toString(), null);
        }
        return netInputStream;
    }


    /**
     * @param context
     * @param path
     * @param paramStr
     * @return
     * @throws NetException
     * @throws Exception
     */
    public static InputStream httpPost(Context context, String path, String paramStr) throws NetException, Exception {
        InputStream netInputStream = null;
        if (null == paramStr) {
            throw new NetException(-1, exception_param_empty);
        }

        // 组装url
        if (StringUtils.isEmpty(path)) {
            throw new NetException(-1, exception_url_empty);
        } else {
            StringBuffer xmlBuffer = new StringBuffer();
            xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xmlBuffer.append("<request>");
            xmlBuffer.append("<common>");
            // 添加公共参数
            for (Entry<String, String> entry : NetTool.getCommonParam(context).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value)) {
                    value = "";
                }
                xmlBuffer.append("<" + key.trim() + ">" + value + "</" + key.trim() + ">");
            }
            xmlBuffer.append("</common>");

            xmlBuffer.append("<customized>");

            // 添加接口的参数参数
            xmlBuffer.append(paramStr);

            xmlBuffer.append("</customized>");
            xmlBuffer.append("</request>");
            // 添加请求参数

            netInputStream = doHttpRequestXML(context, path, xmlBuffer.toString(), null);
        }
        return netInputStream;
    }


    /**
     * @param context
     * @param uri
     * @return
     */
    public static InputStream downLoadCityData(Context context, String uri) {
        InputStream in = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = null;
            // 查看apn类型
            String typeName = NetTool.getNetType(context);
            setApn(typeName);
            switch (mApn) {
                case APN_CMWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "apn_wamp fail");
                        e.printStackTrace();
                    }
                }
                break;
                case APN_3GWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                        e.printStackTrace();
                    }
                }
                break;
                case APN_UNIWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                        e.printStackTrace();
                    }
                }
                break;
                case APN_CTWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "APN_CTWAP fail");
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    conn = (HttpURLConnection) url.openConnection();
                    break;
            }
            // 更具不通的apn类型，设置不通代理
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 更具不通的apn类型，设置不通代理
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", ENCODE);
            conn.connect();
            if (null != conn) {
                StringBuffer xmlBuffer = new StringBuffer();
                xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                xmlBuffer.append("<request>");
                xmlBuffer.append("<common>");
                // 添加公共参数
                for (Entry<String, String> entry : NetTool.getCommonParam(context).entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    xmlBuffer.append("<" + key + ">" + value + "</" + key + ">");
                }
                xmlBuffer.append("</common>");
                xmlBuffer.append("<customized>");
                xmlBuffer.append("</customized>");
                xmlBuffer.append("</request>");
                conn.getOutputStream().write(xmlBuffer.toString().getBytes());
            }
            int responseCode = conn.getResponseCode();
            if (200 == responseCode) {
                in = conn.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * 下载新的app
     *
     * @param downloadUrl
     * @return
     */
    public static InputStream doHttpDownLoad(Context context, String downloadUrl) {

        InputStream in = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = null;
            String typeName = NetTool.getNetType(context);
            setApn(typeName);
            switch (mApn) {
                case APN_CMWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "apn_wamp fail");
                        e.printStackTrace();
                    }
                }
                break;
                case APN_3GWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                        e.printStackTrace();
                    }
                }
                break;
                case APN_UNIWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                        e.printStackTrace();
                    }
                }
                break;
                case APN_CTWAP: {
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));
                        conn = (HttpURLConnection) url.openConnection(proxy);
                    } catch (Exception e) {
                        LogUtil.e(Constant.TAG, "APN_CTWAP fail");
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    conn = (HttpURLConnection) url.openConnection();
                    break;
            }
            // 更具不通的apn类型，设置不通代理
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 更具不通的apn类型，设置不通代理
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", ENCODE);
            conn.connect();
            if (null != conn) {
                StringBuffer xmlBuffer = new StringBuffer();
                xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                xmlBuffer.append("<request>");
                xmlBuffer.append("<common>");
                // 添加公共参数
                for (Entry<String, String> entry : NetTool.getCommonParam(context).entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    xmlBuffer.append("<" + key + ">" + value + "</" + key + ">");
                }
                xmlBuffer.append("</common>");
                xmlBuffer.append("<customized>");
                xmlBuffer.append("</customized>");
                xmlBuffer.append("</request>");
                conn.getOutputStream().write(xmlBuffer.toString().getBytes(ENCODE));
            }
            int responseCode = conn.getResponseCode();
            if (200 == responseCode) {
                in = conn.getInputStream();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;

    }

    /**
     * @param context
     * @param path
     * @param data
     * @return
     * @throws IOException
     * @throws NetException
     * @throws Exception
     */
    public static InputStream doHttpRequestPost(Context context, String path, byte[] data) throws IOException, NetException, Exception {
        URL url = new URL(path);
        HttpURLConnection conn = null;
        // 查看apn类型
        String typeName = NetTool.getNetType(context);
        setApn(typeName);
        LogUtil.i(Constant.TAG, "apn:" + typeName + " url:" + path);
        conn = createProxyConnection(mApn, url);
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 更具不通的apn类型，设置不通代理
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Charset", ENCODE);
        conn.connect();
        if (null != data && null != conn) {
            OutputStream os = conn.getOutputStream();
            os.write(data);
        }
//        return createNetInputStream(conn);
        return conn.getInputStream();
    }

    /**
     * 使用 postXML形式，该方法适用于travelsky的网络接口
     *
     * @param path
     * @param xml
     */
    public static InputStream doHttpRequestXML(Context context, String path, String xml, String token) throws NetException, Exception {
        URL url = new URL(path);
        HttpURLConnection conn = null;
        // 查看apn类型
        String typeName = NetTool.getNetType(context);
        setApn(typeName);
        LogUtil.i(Constant.TAG, "apn:" + typeName + " url:" + path);
        conn = createProxyConnection(mApn, url);
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("Charset", ENCODE);

        if (!StringUtils.isEmpty(token)) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 更具不通的apn类型，设置不通代理
        conn.setRequestMethod("POST");
        conn.connect();
        if (!StringUtils.isEmpty(xml)) {
            OutputStream os = conn.getOutputStream();
            os.write(xml.getBytes());
        }
        return createNetInputStream(conn);
    }


    public static InputStream doHttpRequestText(Context context, String path, String text, String token, String requestType) throws NetException, Exception {
        URL url = new URL(path);
        HttpURLConnection conn = null;
        // 查看apn类型
        String typeName = NetTool.getNetType(context);
        setApn(typeName);
//        LogUtil.i(Constant.TAG, "apn:" + typeName + " url:" + path);
        conn = createProxyConnection(mApn, url);

        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("Charset", ENCODE);

        if (!StringUtils.isEmpty(token)) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        if (requestType.equals(HttpRequester.REQUEST_POST)) {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
        }

        if (requestType.equals(HttpRequester.REQUEST_DELETE)) {
            conn.setDoOutput(false);
        }


        // 更具不通的apn类型，设置不通代理
        conn.setRequestMethod(requestType);
        conn.connect();

        if (!StringUtils.isEmpty(text) && (!"{}".equals(text)) && !HttpRequester.REQUEST_GET.equals(requestType) && !HttpRequester.REQUEST_DELETE.equals(requestType)) {
            OutputStream os = conn.getOutputStream();
            os.write(text.getBytes());
        }
        return createNetInputStream(conn);
    }


    public static InputStream doHttpRequestJson(Context context, String path, String json) throws NetException, Exception {
        return doHttpRequestXML(context, path, json, null);
    }

    public static InputStream doHttpRequestJson(Context context, String path, String json, String token, String requestType) throws NetException, Exception {
        return doHttpRequestText(context, path, json, token, requestType);
    }


    public static InputStream doHttpRequestInHeader(Context context, String path, HashMap<String, String> hmap) throws NetException,
            Exception {
        URL url = new URL(path);
        HttpURLConnection conn = null;
        // 查看apn类型
        String typeName = NetTool.getNetType(context);
        setApn(typeName);
        LogUtil.i(Constant.TAG, "apn:" + typeName + " url:" + path);
        conn = createProxyConnection(mApn, url);
        conn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 更具不通的apn类型，设置不通代理
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Charset", ENCODE);
        conn.connect();
        return createNetInputStream(conn);
    }


    public static InputStream doUploadImage(Context context, String path, byte data[]) throws NetException, Exception {
        URL url = new URL(path);
        HttpURLConnection conn = null;
        // 查看apn类型
        String typeName = NetTool.getNetType(context);
        setApn(typeName);
        LogUtil.i(Constant.TAG, "apn:" + typeName + " url:" + path);
        conn = createProxyConnection(mApn, url);
//        conn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
//        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("Content-Type", "multipart/form-data");

        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 更具不通的apn类型，设置不通代理
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Charset", ENCODE);
        conn.connect();
        if (null != data) {
            OutputStream os = conn.getOutputStream();
            os.write(data);
        }
        return createNetInputStream(conn);
    }


    /**
     * 返回数据流
     *
     * @param conn
     * @return
     * @throws IOException
     * @throws NetException
     */
    public static InputStream createNetInputStream(HttpURLConnection conn) throws IOException, NetException {
        int responseCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();
        LogUtil.e("HTTPREQUESTER", "response code:" + responseCode);
        LogUtil.e("HTTPREQUESTER", "response code:" + responseMessage);
        InputStream ins = null;
        try {
            ins = conn.getInputStream();
        } catch (Exception e) {
            ins = conn.getErrorStream();
        }

        return ins;


//        switch (responseCode) {
//            case 200: {
//                // 保存session信息
//                InputStream ins = conn.getInputStream();
//                return ins;
//            }
//            case 408: {
//                // 请求超时
//                throw new NetException(408, exception_request_time_out);
//            }
//            case 504: {
//                // 网关超时
//                throw new NetException(504, exception_getway_time_out);
//            }
//            case 500: {
//                // 内部错误
//                throw new NetException(500, exception_inner_error);
//            }
//            case 502: {
//                // 网关错误
//                throw new NetException(502, exception_getway_error);
//            }
//            default:
//                throw new NetException(responseCode, "网络异常 代码:" + responseCode);
//        }
    }

    /**
     * 生成带有代理的Connection
     *
     * @param mApn
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpURLConnection createProxyConnection(int mApn, URL url) throws IOException {
        HttpURLConnection conn = null;
        switch (mApn) {
            case APN_CMWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "apn_wamp fail");
                    e.printStackTrace();
                }
            }
            break;
            case APN_3GWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                    e.printStackTrace();
                }
            }
            break;
            case APN_UNIWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                    e.printStackTrace();
                }
            }
            break;
            case APN_CTWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "APN_CTWAP fail");
                    e.printStackTrace();
                }
            }
            break;
            default:
                conn = (HttpURLConnection) url.openConnection();
                break;

        }
        return conn;
    }

    /**
     * data为null就是get请求
     *
     * @param path
     * @param data
     * @return String 上午09:27:54 2011
     */
    public static StringBuffer doHttpRequest(Context context, String path, byte[] data) throws NetException, Exception {
        StringBuffer result = new StringBuffer();
        URL url = new URL(path);
        HttpURLConnection conn = null;
        // 查看apn类型
        String typeName = NetTool.getNetType(context);
        setApn(typeName);
        LogUtil.i(Constant.TAG, "apn:" + typeName + " url:" + path);
        switch (mApn) {
            case APN_CMWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "apn_wamp fail");
                    e.printStackTrace();
                }
            }
            break;
            case APN_3GWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                    e.printStackTrace();
                }
            }
            break;
            case APN_UNIWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "APN_3GWAP fail");
                    e.printStackTrace();
                }
            }
            break;
            case APN_CTWAP: {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    LogUtil.e(Constant.TAG, "APN_CTWAP fail");
                    e.printStackTrace();
                }
            }
            break;
            default:
                conn = (HttpURLConnection) url.openConnection();
                break;
        }
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoOutput(true);
        conn.setUseCaches(false);

        // 更具不通的apn类型，设置不通代理

        if (null == data) {
            conn.setRequestMethod("GET");
        } else {
            conn.setRequestMethod("POST");
        }
        conn.setRequestProperty("Charset", ENCODE);

        if (null != data) {
            OutputStream os = conn.getOutputStream();
            os.write(data);
        }

        int responseCode = conn.getResponseCode();
        switch (responseCode) {
            case 200: {
                // 正常
                InputStream ins = conn.getInputStream();
                BufferedInputStream fin = new BufferedInputStream(ins);
                int b = -1;
                byte buf[] = new byte[512];

                String tempStr = null;
                while ((b = fin.read(buf)) != -1) {
                    tempStr = new String(buf, 0, buf.length);
                    result.append(tempStr);
                }
                fin.close();
                ins.close();
                return result;
            }
            case 408: {
                // 请求超时
                throw new NetException(408, exception_request_time_out);
            }
            case 504: {
                // 网关超时
                throw new NetException(504, exception_getway_time_out);
            }
            case 500: {
                // 内部错误
                throw new NetException(500, exception_inner_error);
            }
            case 502: {
                // 网关错误
                throw new NetException(502, exception_getway_error);
            }
            default:
                throw new NetException(responseCode, "网络异常 代码:" + responseCode);
        }
    }

    /**
     * 文件上传
     *
     * @param actionUrl
     * @param params
     * @param files
     * @return String 上午11:24:20 2011
     */
    public static String postUpload(String actionUrl, Map<String, String> params, FormFile[] files, String token) throws Exception {

        String BOUNDARY = "---------7d4a6d158c9"; // 数据分隔线

        String MULTIPART_FORM_DATA = "multipart/form-data";

        URL url = new URL(actionUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(TIME_OUT);

        conn.setDoInput(true);// 允许输入

        conn.setDoOutput(true);// 允许输出


        conn.setUseCaches(false);// 不使用Cache

        conn.setRequestMethod("POST");

//        long datas = 0;
//
//        for (Map.Entry<String, String> m : params.entrySet()) {
//            datas += m.getValue().getBytes().length;
//        }
//
//        for (FormFile f : files) {
//            datas += f.getData().length;
//        }
//        conn.setFixedLengthStreamingMode(datas);

        conn.setRequestProperty("Connection", "Keep-Alive");

        conn.setRequestProperty("Charset", ENCODE);

        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);

        if (Build.VERSION.SDK_INT > 13) {
            conn.setRequestProperty("Connection", "close");
        }

        if (!StringUtils.isEmpty(token)) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> entry : params.entrySet()) {// 构建表单字段内容

            sb.append("--");

            sb.append(BOUNDARY);

            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");

            sb.append(entry.getValue());

            sb.append("\r\n");

        }


        ByteArrayOutputStream byo = new ByteArrayOutputStream();

//        outStream.write(sb.toString().getBytes());// 发送表单字段数据
        byo.write(sb.toString().getBytes());

        for (FormFile file : files) {// 发送文件数据

            StringBuilder split = new StringBuilder();

            split.append("--");

            split.append(BOUNDARY);

            split.append("\r\n");

            split.append("Content-Disposition: form-data;name=\"" + file.getParameterName() + "\";filename=\"" + file.getFilname() + "\"\r\n");

            split.append("Content-Type: " + file.getContentType() + "\r\n\r\n");

//            outStream.write(split.toString().getBytes());

            byo.write(split.toString().getBytes());

//            outStream.write(file.getData(), 0, file.getData().length);
            byo.write(file.getData());

//            outStream.write("\r\n".getBytes());

            byo.write("\r\n".getBytes());

        }

        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志

        byo.write(end_data);

//        outStream.write(end_data);

        byte[] alldate = byo.toByteArray();

        conn.setFixedLengthStreamingMode(alldate.length);

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());

        outStream.write(alldate);
        outStream.flush();


        int responseCode = conn.getResponseCode();

        if (200 == responseCode) {

            InputStream is = conn.getInputStream();

            return readInputStreamText(is);
        } else {

            String errorMesg = readInputStreamText(conn.getErrorStream());

            return errorMesg;
        }

    }


    private static String readInputStreamText(InputStream ins) throws IOException {

        int ret = -1;

        byte[] buffter = new byte[512];

        StringBuilder b = new StringBuilder();
        while ((ret = ins.read(buffter)) != -1) {
            b.append(new String(buffter, 0, ret));
        }
        ins.close();

        return b.toString();
    }


//    public static String postUpload(String actionUrl, FormFile file) throws Exception {
//
//        return postUpload(actionUrl, new HashMap<String, String>(), new FormFile[]{file});
//
//    }

}
