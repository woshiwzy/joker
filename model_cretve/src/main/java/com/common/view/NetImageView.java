package com.common.view;import android.content.Context;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.util.AttributeSet;import android.widget.ImageView;import com.common.net.NetResult;import com.common.task.BaseTask;import com.common.task.NetCallBack;import com.common.util.LogUtil;import com.common.util.StringUtils;import java.io.BufferedInputStream;import java.io.ByteArrayOutputStream;import java.io.FileInputStream;import java.io.InputStream;import java.net.URL;import java.util.HashMap;/** * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。 * * @author Alan */public class NetImageView extends ImageView {    private int mBorderThickness = 0;    private Context mContext;    private int defaultColor = 0xFFFFFFFF;    // 如果只有其中一个有值，则只画一个圆形边框    private int mBorderOutsideColor = 0;    private int mBorderInsideColor = 0;    // 控件默认长、宽    private int defaultWidth = 0;    private int defaultHeight = 0;    private boolean lastUploadSuccess = false;    private boolean isDownload = false;    private int holder = -1;    private String url = "";    public NetImageView(Context context, AttributeSet attrs) {        super(context, attrs);    }////    public NetImageView(Context context) {//        super(context);//        mContext = context;//    }    public void setUrlWithHolder(final String urlText, int drablwHolder) {        if (lastUploadSuccess == true) {        } else {            setImageResource(drablwHolder);        }        lastUploadSuccess = false;        this.holder = drablwHolder;        this.url = urlText;        if (!StringUtils.isEmpty(urlText)) {            BaseTask baseTask = new BaseTask(new NetCallBack() {                @Override                public void onPreCall() {                    isDownload = true;                }                @Override                public NetResult onDoInBack(HashMap<String, String> paramMap,BaseTask btask) {                    try {                        InputStream inputStream=null;                        if(urlText.startsWith("http")){                            URL url1 = new URL(urlText);                            inputStream= url1.openStream();                        }else{                            inputStream=new FileInputStream(urlText);                        }                        BufferedInputStream bfi = new BufferedInputStream(inputStream);                        ByteArrayOutputStream byo = new ByteArrayOutputStream();                        byte[] buffer = new byte[512];                        int rest = -1;                        while ((rest = bfi.read(buffer)) != -1) {                            byo.write(buffer, 0, rest);                        }                        NetResult netRest = new NetResult();                        netRest.setTag(byo.toByteArray());                        return netRest;                    } catch (Exception e) {                    }                    return null;                }                @Override                public void onFinish(NetResult result) {                    isDownload = false;                    if (null != result) {                        try {                            byte[] imgData = (byte[]) result.getTag();                            Bitmap bmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);                            setImageBitmap(bmap);                            lastUploadSuccess = true;                        } catch (Exception e) {                        }                    }                }            });            baseTask.execute(new HashMap<String, String>());            LogUtil.i("wzy", "refresh header: " + this.url);        }    }    public void onResume() {        if (lastUploadSuccess == false && isDownload == false) {            setUrlWithHolder(this.url, this.holder);        }    }    public void forceRefresh() {        setUrlWithHolder(this.url, this.holder);    }}