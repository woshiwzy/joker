package com.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author wangzy
 */
public class Tool {



    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public static int getTotalRam(Context context){//GB
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0 ;
        try{
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader,8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(firstLine != null){
            totalRam = (int)Math.ceil((new Float(Float.valueOf(firstLine) / (1024 * 1024)).doubleValue()));
        }

        return totalRam ;//返回1GB/2GB/3GB/4GB
    }


    public static void openGpsSettignWithResultCode(Activity activity, int resultCode) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, resultCode); //

    }

    public static boolean isOpenGPS(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getScreenOrientation(Activity activity) {
        return activity.getResources().getConfiguration().orientation;
    }

    public static int getSigedHash(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            int hashcode = sign.hashCode();

            return hashcode;

//            return hashcode == -82892576 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static double getDistance(LatLng start, LatLng end) {
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;

//      double Lat1r = (Math.PI/180)*(gp1.getLatitudeE6()/1E6);
//      double Lat2r = (Math.PI/180)*(gp2.getLatitudeE6()/1E6);
//      double Lon1r = (Math.PI/180)*(gp1.getLongitudeE6()/1E6);
//      double Lon2r = (Math.PI/180)*(gp2.getLongitudeE6()/1E6);

        //地球半径
        double R = 6371;

        //两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }


    private static final double EARTH_RADIUS = 6378137.0;

    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static void openAppOnStore(Context context, String packageName) {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName));
        context.startActivity(viewIntent);
    }

    /**
     * center the markers
     *
     * @param markes
     * @param padding
     * @return
     */
    public static CameraUpdate getMarkerCenter(List<Marker> markes, int padding) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markes) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        return cu;
    }

    /**
     * Get google map center
     *
     * @param mMap
     * @return
     */
    public static LatLng getCenterLocation(GoogleMap mMap) {

        VisibleRegion visibleRegion = mMap.getProjection()
                .getVisibleRegion();

        Point x = mMap.getProjection().toScreenLocation(
                visibleRegion.farRight);

        Point y = mMap.getProjection().toScreenLocation(
                visibleRegion.nearLeft);

        Point centerPoint = new Point(x.x / 2, y.y / 2);

        LatLng centerFromPoint = mMap.getProjection().fromScreenLocation(
                centerPoint);

        return centerFromPoint;
    }

    /**
     * Get address list from text
     *
     * @param context
     * @param strAddress
     * @param size
     * @return
     * @throws IOException
     */
    public static List<Address> getLocationFromAddress(Context context, String strAddress, int size) throws IOException {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        address = coder.getFromLocationName(strAddress, size);


        if (address == null) {
            return null;
        }
        return address;

    }


    /**
     * Get address list from text
     *
     * @param context
     * @param strAddress
     * @param size
     * @return
     * @throws IOException
     */
    public static List<Address> getLocationFromAddress(Context context, String strAddress, int size, Locale locale) throws IOException {
        Geocoder coder = new Geocoder(context, locale);
        List<Address> address;
        address = coder.getFromLocationName(strAddress, size);


        if (address == null) {
            return null;
        }
        return address;

    }

    public static String getResultStress(Address addres) {


        String l1 = addres.getAddressLine(0);
        String l2 = addres.getAddressLine(1);
        String l3 = addres.getAddressLine(2);

        String addressLine = StringUtils.isEmpty(l1) ? "" : l1 + " " + (StringUtils.isEmpty(l2) ? "" : l2) + " " + (StringUtils.isEmpty(l3) ? "" : l3);

        return addressLine;
    }

    public static String getFileName(String path) {

        try {
            String fname = path.substring(path.lastIndexOf("/") + 1, path.length());
            return fname;
        } catch (Exception e) {

        }

        return null;
    }


    public static float getInputNumber(String input) {
        if (!StringUtils.isEmpty(input)) {
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(input);
            String all = matcher.replaceAll("");
            if (StringUtils.isEmpty(all)) {
                return 0.f;
            }
            float result = Float.parseFloat(all);
            return result;
        }

        return 0;
    }

    public static float getInputNumber(TextView textView) {

        String txt = textView.getText().toString();
        if (!StringUtils.isEmpty(txt)) {
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(txt);
            String all = matcher.replaceAll("");
            if (StringUtils.isEmpty(all)) {
                return 0.f;
            }
            float result = Float.parseFloat(all);
            return result;
        }

        return 0;
    }

    public static boolean isInstallGMS(Context context) {

        if (Tool.isAppInstalled(context, "com.google.android.gms") /*&& Tool.isAppInstalled(context, "com.google.android.gms")*/) {
            return true;
        }

        return false;
    }


    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }

    public static String getSignature(String pkgname, Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            StringBuilder builder = new StringBuilder();
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            String signature = builder.toString();
            return signature;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static String getDisplayMetricsText(Context cx) {

        StringBuffer sbf = new StringBuffer();
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        sbf.append(android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "\n");

        sbf.append("\nThe absolute width:" + String.valueOf(screenWidth) + " pixels\n");
        sbf.append("The absolute heightin:" + String.valueOf(screenHeight)
                + " pixels\n");
        sbf.append("The logical density of the display.:" + String.valueOf(density)
                + "\n");
        sbf.append("X dimension :" + String.valueOf(xdpi) + "pixels per inch\n");
        sbf.append("Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n");
        return sbf.toString();
    }

    public static void sendShareFile(Context context, File file) {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("application/octet-stream");
//        String[] emailReciver = new String[] {  };
//
//        String emailTitle = "微信聊天记录";
//        String emailContent = "内容";
//        email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
//        email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);
//        email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
        // 附件
        email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        // 调用系统的邮件系统
        context.startActivity(Intent.createChooser(email, "Send with"));

    }


    public static void sendShare(Context context, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(shareIntent, "Share to"));
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * getListView realChild View
     *
     * @param listView
     * @param wantedPosition
     * @return
     */
    public static View getChildViwFromList(ListView listView, int wantedPosition) {

        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0

        int wantedChild = wantedPosition - firstPosition;
        if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
            return null;
        }
        View wantedView = listView.getChildAt(wantedChild);

        return wantedView;
    }


    /**
     * 读取RAW
     *
     * @param activity
     * @param id
     * @return
     */
    public static String readStringFromStream(Activity activity, int id) {
        try {
            InputStream is = activity.getResources().openRawResource(id);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = -1;
            byte[] buffer = new byte[512];
            while ((i = is.read(buffer)) != -1) {
                bo.write(buffer, 0, i);
            }
            bo.flush();
            byte[] bytes = bo.toByteArray();
            String ret = new String(bytes);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("wzy", "err:" + e.getLocalizedMessage());
            return "";
        }

    }

    public static float getFontHeight(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.descent - fm.ascent);
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.leading - fm.ascent);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] getFileContent(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedInputStream bfi = new BufferedInputStream(new FileInputStream(file));
                ByteArrayOutputStream bro = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                int ret = -1;
                while ((ret = bfi.read(buffer)) != -1) {
                    bro.write(buffer, 0, ret);
                }
                bro.flush();
                return bro.toByteArray();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String getFileType(String fileUri) {
        File file = new File(fileUri);
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return fileType;
    }

    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

    public static long getCacheSize(Context context) {
        File cacheDir = context.getCacheDir();
        return cacheDir.getTotalSpace() - cacheDir.getUsableSpace();
    }

    public static void clearCahce(Context context) {
        File cacheDir = context.getCacheDir();
        searchDirs(cacheDir);
    }

    private static void searchDirs(File fdir) {
        File[] lsfirs = fdir.listFiles();
        for (File f : lsfirs) {
            if (f.isFile()) {
                f.delete();
                LogUtil.i("wzy", "delte:" + f.getAbsolutePath());
            } else {
                searchDirs(f);
            }
        }
    }

    public static void startActivityForResult(Activity activity, Class claz, int requestCode) {
        Intent intent = new Intent(activity, claz);
        activity.startActivityForResult(intent, requestCode);
    }

    // 选择图片与裁剪＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
    // 裁剪意图，用于相册
    public static void startPhotoZoom(Activity activity, Uri uri, File outputFile, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    // 裁剪意图，用于拍照
    public static void startCropImg(Activity activity, Bitmap data, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void chosePicFromAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void chosePicFromCamera(Activity activity, int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 选择图片与裁剪＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝end=====

    public static void showMessageDialog(int string, Context ativity) {
        if (null == ativity || ((Activity) ativity).isFinishing()) {
            return;
        }
        Builder builder = new Builder(ativity);


        builder.setMessage(ativity.getResources().getString(string));
//        builder.setTitle("Notification");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    public static void showMessageDialog(String message, Context ativity) {


        if (null == ativity || ((Activity) ativity).isFinishing()) {
            return;
        }


        Builder builder = new Builder(ativity);
        builder.setMessage(message);
//        builder.setTitle("Notification");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }


    public static void showMessageDialog(String message, Activity ativity, final DialogCallBackListener dialogCallBackListener) {

        if (null == ativity || ativity.isFinishing()) {
            return;
        }


        Builder builder = new Builder(ativity);
        builder.setMessage(message);
//        builder.setTitle("Notification");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != dialogCallBackListener) {
                    dialogCallBackListener.onDone(true);
                }

            }
        });

        builder.create().show();

    }


    public static void showMessageDialog(int message, Activity ativity, final DialogCallBackListener dialogCallBackListener) {

        if (null == ativity || ativity.isFinishing()) {
            return;
        }


        Builder builder = new Builder(ativity);
        builder.setMessage(message);
//        builder.setTitle("Notification");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != dialogCallBackListener) {
                    dialogCallBackListener.onDone(true);
                }

            }
        });

        builder.create().show();

    }

    public static byte[] int2bytearray(int i) {

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        buf.write(i);
        byte[] b = buf.toByteArray();
        return b;

        // byte[] result = new byte[4];
        // result[3] = (byte) ((i >> 24) & 0xFF);
        // result[2] = (byte) ((i >> 16) & 0xFF);
        // result[1] = (byte) ((i >> 8) & 0xFF);
        // result[0] = (byte) (i & 0xFF);
        // return result;
    }

    public static void testCrash() {
        int x = 0;
        int y = 1 / x;
    }

    /**
     * 验证是身份证
     *
     * @param idCard
     * @return
     */
    public static boolean checkIdCard(String idCard) {
        IdcardValidator idca = new IdcardValidator();
        return idca.isValidatedAllIdcard(idCard);
    }

    /**
     * 判断sd卡是否可用
     *
     * @return
     */
    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 直接跳转到另一个activity
     *
     * @param fromContext
     */
    public static void startActivity(Context fromContext, Class claz) {
        Intent i = new Intent();
        i.setClass(fromContext, claz);
        fromContext.startActivity(i);
    }


    public static void startActivity(Context fromContext, Class claz, Intent i) {
        i.setClass(fromContext, claz);
        fromContext.startActivity(i);
    }

    /**
     * 安装apk
     *
     * @param dir
     */
    public static void installApk(Context context, String dir) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(dir)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 读取apk安装包的信息
     *
     * @param apkDir
     * @return{packageName,version
     */
    public static String[] getAppApkInfo(Context context, String apkDir) {

        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkDir, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packageName = appInfo.packageName; // 得到安装包名称
            String version = info.versionName; // 得到版本信息
            String infos[] = {packageName, version};
            return infos;
        }
        return null;
    }

    /**
     * 文件拷贝
     *
     * @param finsrc
     * @param dst
     * @return
     */
    public static boolean fileCopy(InputStream finsrc, File dst) {
        try {
            if (null == dst) {
                return false;
            } else {
                dst.delete();
                dst.createNewFile();
                FileOutputStream fout = new FileOutputStream(dst);
                byte[] buffer = new byte[512];
                int readsize = 0;
                while ((readsize = finsrc.read(buffer)) != -1) {
                    fout.write(buffer, 0, readsize);
                }
                // fout.flush();
                finsrc.close();
                fout.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(Constant.TAG, "copy file error:" + e.getMessage());
            return false;
        }
    }

    public static float getScreenDensity(Context context) {

        return context.getResources().getDisplayMetrics().density;
    }

    // dip转像素
    public int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;
    }

    // 像素转dip
    public float PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float dips = Pixels / SCALE;

        return dips;

    }

    /**
     * 获取渠道号
     *
     * @return
     */
    public static String getChannel(Context context) {
        String chnnel = "";
        ArrayList<String> lines = AssertTool.readLinesFromAssertsFiles(context, "channel.txt");
        chnnel = lines.get(0).trim();
        return chnnel;
    }

    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        String version = "-1";
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        int version = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 启动一个新的Activity
     *
     * @param ctx
     * @param clasz
     */
    public static void startOtherActivity(Activity ctx, Class clasz) {
        Intent intent = new Intent();
        intent.setClass(ctx, clasz);
        ctx.startActivity(intent);
        // ctx.overridePendingTransition(R.anim.activity_anim_in,
        // R.anim.activity_anim_out);
    }

    /**
     * 获取屏幕高度宽度
     *
     * @param ctx
     * @return
     */
    public static Point getDisplayMetrics(Context ctx) {
        DisplayMetrics metrcis = ctx.getResources().getDisplayMetrics();
        Point metricsPoint = new Point();
        metricsPoint.x = metrcis.widthPixels;
        metricsPoint.y = metrcis.heightPixels;
        return metricsPoint;
    }

    /**
     * 参数必须为：getDisplayMetrics返回值
     *
     * @param point
     * @return
     */
    public static Point getRandomPoint(Point point) {
        Random rand = new Random();
        int rx = rand.nextInt(point.x);
        int ry = rand.nextInt(point.y);
        return new Point(rx, ry);
    }

    /**
     * 根据设定后的宽度和高度设置按钮的出事随机值
     *
     * @param width
     * @param height
     * @return
     */
    public static Point getDimensionsByDimens(int width, int height) {
        Random rand = new Random();
        int rx = rand.nextInt(width);
        int ry = rand.nextInt(height);
        return new Point(rx, ry);
    }

    /**
     * 创建一个dlgbuilder
     *
     * @param context
     * @param view
     * @return
     */
    public static Builder createADialig(Context context, View view) {
        Builder alertDialog = new Builder(context);
        alertDialog.setView(view);
        return alertDialog;
    }

    /**
     * 发送广播
     *
     * @param context
     * @param intent
     */
    public static void sendBoardCast(Context context, Intent intent) {
        context.sendBroadcast(intent);
    }

    /**
     * 请求url
     *
     * @param url
     */
    public static void startUrl(Context mContext, String url) {

        if (!url.startsWith("http://") && !url.startsWith("https://")) {

            url = "http://" + url;
        }

        // DownloadManager.Request down=new DownloadManager.Request
        // (Uri.parse(url));
        // DownloadManager manager =
        // (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        // //设置允许使用的网络类型，这里是移动网络和wifi都可以
        // down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        // //禁止发出通知，既后台下载
        // down.setShowRunningNotification(true);
        // //不显示下载界面
        // down.setVisibleInDownloadsUi(false);
        // //设置下载后文件存放的位置
        // down.setDestinationInExternalFilesDir(mContext, null,
        // "skyworth.apk");
        // //将下载请求放入队列
        // manager.enqueue(down);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
    }

    /**
     * 获取状态栏和标题栏的高度和宽度
     *
     * @param context
     * @return
     */
    public static int getAppRect(Activity context) {
        /**
         * decorView是window中的最顶层view，可以从window中获取到decorView，
         * 然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏
         */
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// 状态栏

        /**
         * getWindow().findViewById(Window.ID_ANDROID_CONTENT)
         * 这个方法获取到的view就是程序不包括标题栏的部分，然后就可以知道标题栏的高度了。
         */
        int contentTop = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;// 标题栏
        return titleBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int geteAppUnVisibleHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// 状态栏

        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;// 标题栏
        return statusBarHeight + titleBarHeight;
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void mkdir(String dir) {
        File file = new File(dir);
        file.mkdir();
    }

    /**
     * 把asset下面的资源拷贝到sdcard下面
     *
     * @param context
     * @param destFile
     * @throws IOException
     */
    public static void copyAssetFile2Sdcard(Context context, String fname, String destFile) throws IOException {
        AssetManager asm = context.getAssets();
        File df = new File(destFile);
        if (df.exists() == false) {
            df.mkdir();
        } else {
            InputStream os = asm.open(fname);
            byte buf[] = new byte[256];
            int b = -1;
            FileOutputStream fout = new FileOutputStream(destFile + fname);
            while ((b = os.read(buf)) != -1) {
                fout.write(buf, 0, buf.length);
            }
            fout.flush();
            os.close();
            fout.close();
        }

    }

    public static void ToastShow(Activity activity, int id) {
        if (!activity.isFinishing()) {
            Toast.makeText(activity, activity.getResources().getString(id), Toast.LENGTH_SHORT).show();
        }
    }

    public static void ToastShow(Activity activity, String message) {
        if (!activity.isFinishing()) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * m 获取wifi型号强度
     *
     * @return
     */
    public static void showWifiStrenth(Activity context) {
        // 2.得到的值是一个0到-100的区间值，是一个int型数据，
        // 其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，
        // 有可能连接不上或者掉线
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int rssi = wifiInfo.getRssi();
        if (rssi < 0 && rssi >= -50) {
            // Tool.ToastShow(context, "亲，你的wifi信号不错！");
        } else {
            Tool.ToastShow(context, "亲，你的wifi信号较差,使用起来不够流畅！");
        }
    }

    /**
     * 获取imei,如果获取不到，则生成一个15位号码
     *
     * @return
     */
    public static String getImei(final Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei;
        try {
            imei = tm.getDeviceId();// imei
        } catch (Exception e) {
            imei = null;
        }

        if (StringUtils.isEmpty(imei) || "0".equals(imei)) {
            // 如果imei号为空或0，取mac地址作为imei号传递
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            imei = info.getMacAddress();

            // 如果mac地址为空或0，则通过uuid生成的imei号来传递
            if (StringUtils.isEmpty(imei) || "0".equals(imei)) {

                imei = SharePersistent.getPerference(context, Constant.STRING_IMEI);

                if (StringUtils.isEmpty(imei)) {
                    imei = UUIDGenerator.getUUID(15);
                    if (StringUtils.isEmpty(imei)) {
                        return "0";
                    }
                    SharePersistent.savePreference(context, Constant.STRING_IMEI, imei);
                }
            }
        }

        return imei;
    }

    /**
     * 比较版本号，确定是否升级
     *
     * @param locationVersion
     * @param newVersion
     * @return
     */
    public static boolean compareAppVersionName(String locationVersion, String newVersion) {

        try {
            String[] locationVersions = locationVersion.split("\\.");
            String[] newVersions = newVersion.split("\\.");
            StringBuffer sbfOld = new StringBuffer();
            StringBuffer sbfNew = new StringBuffer();
            for (int i = 0, isize = locationVersions.length; i < isize; i++) {
                int old = Integer.parseInt(locationVersions[i]);
                int now = Integer.parseInt(newVersions[i]);
                sbfOld.append(old);
                sbfNew.append(now);
            }
            int old = Integer.parseInt(sbfOld.toString());
            int now = Integer.parseInt(sbfNew.toString());
            if (now > old) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拨号
     *
     * @param number
     * @param context
     */
    private static void dial(String number, Context context) {
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Object iTelephony;
            iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
            Method dial = iTelephony.getClass().getDeclaredMethod("dial", String.class);
            dial.invoke(iTelephony, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打电话
     *
     * @param number
     * @param context
     */
    private static void call(String number, Context context) {
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Object iTelephony;
            iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
            Method dial = iTelephony.getClass().getDeclaredMethod("call", String.class);
            dial.invoke(iTelephony, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠
     *
     * @param times
     */
    public static void delay(long times) {
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRightLocation(String lat, String lng) {
        try {
            float lt = Math.abs(Float.parseFloat(lat));
            float lg = Math.abs(Float.parseFloat(lng));
            if ((lg > 0 && lg <= 180) && (lt > 0 && lt <= 90)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void exitApp() {

        android.os.Process.killProcess(android.os.Process.myPid());

    }
}
