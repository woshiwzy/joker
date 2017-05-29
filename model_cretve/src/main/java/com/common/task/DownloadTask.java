package com.common.task;

import android.app.Activity;
import android.content.Context;

import com.common.net.NetResult;
import com.common.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by wangzy on 2016/11/28.
 */

public class DownloadTask extends BaseTask {


    private String urlText;
    private String saveDir;
    private NetCallBack netCallBack;
    private Context activity;

    public DownloadTask(Context context, String url, String saveDir,NetCallBack callBack ) {
        super(callBack, context);

        this.activity=context;
        this.netCallBack = callBack;
        this.urlText = url;
        this.saveDir = saveDir;
    }


    @Override
    protected void onPreExecute() {
        if (null != netCallBack) {
            netCallBack.onPreCall(this);
        }

    }


    @Override
    protected NetResult doInBackground(HashMap<String, String>... params) {


        NetResult netResult = null;

        try {


            FileOutputStream fileOutputStream = new FileOutputStream(saveDir);


            URL url = new URL(urlText);
            InputStream ins = url.openStream();


            BufferedInputStream bfi = new BufferedInputStream(ins);

            BufferedOutputStream bfo=new BufferedOutputStream(fileOutputStream);



            byte[] buffer = new byte[1024];
            int ret = -1;

            while ((ret = bfi.read(buffer)) != -1) {
                bfo.write(buffer, 0, ret);
            }


            fileOutputStream.flush();
            bfo.close();

            fileOutputStream.close();

            bfi.close();

            netResult=new NetResult();
            netResult.setCode(NetResult.CODE_OK);
            netResult.setTag(saveDir);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("sonder","downloaer error:"+e.getLocalizedMessage());
        }

        return netResult;


    }


    @Override
    protected void onPostExecute(NetResult result) {

        if (null != netCallBack) {
            netCallBack.onFinish(result, this);
        }
    }


}
