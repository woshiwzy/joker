package com.common.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class AssertTool {

    private static String tag = "AssertTool";
    public static String DATA_PATH = "data/";

    public static String getAssertStringContent(Context context, String srcPath) {
        StringBuffer sbf = new StringBuffer();
        InputStream is = getInputStreamFromAssertFile(context, srcPath);
        String line = null;
        if (null != is) {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            try {
                while ((line = bfr.readLine()) != null) {
                    sbf.append(line.trim());
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sbf.toString();
    }

    /**
     * 列出base_data下得文件资源
     *
     * @param context
     * @return
     */
    public static HashSet<String> listAssertFiles(Context context) {
        HashSet<String> hset = null;
        try {
            AssetManager assertManager = context.getAssets();
            String[] files = assertManager.list(DATA_PATH);
            if (null != files && files.length > 0) {
                hset = new HashSet<String>();
                for (String uni : files) {
                    hset.add(uni);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(tag, GlobalTool.getLogPrffix() + ":" + e.getLocalizedMessage());
        }
        return hset;
    }

    public static ArrayList<String> listAssertFiles(Context context, String root) {
        ArrayList<String> hset = null;
        try {
            AssetManager assertManager = context.getAssets();
            String[] files = assertManager.list(root);
            if (null != files && files.length > 0) {
                hset = new ArrayList<String>();
                for (String uni : files) {
                    hset.add(uni);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(tag, GlobalTool.getLogPrffix() + ":" + e.getLocalizedMessage());
        }
        return hset;
    }


    /**
     * 打开assert的流
     *
     * @param context
     * @param srcPath
     * @return
     */
    public static InputStream getInputStreamFromAssertFile(Context contexr, String srcPath) {
        AssetManager assertManager = contexr.getAssets();
        InputStream is = null;
        try {
            is = assertManager.open(srcPath);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(tag, GlobalTool.getLogPrffix() + ":" + e.getLocalizedMessage());
        }
        return is;
    }

    /**
     * 按行读取文件
     *
     * @param path
     * @return
     */
    public static ArrayList<String> readLinesFromAssertsFiles(Context context, String srcPath) {
        InputStream is = getInputStreamFromAssertFile(context, srcPath);
        String line = null;
        ArrayList<String> list = null;

        if (null != is) {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            list = new ArrayList<String>();
            try {
                while ((line = bfr.readLine()) != null) {
                    list.add(line.trim());
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(tag, GlobalTool.getLogPrffix() + " message:" + e.getLocalizedMessage());
            }
        }
        return list;
    }
}
