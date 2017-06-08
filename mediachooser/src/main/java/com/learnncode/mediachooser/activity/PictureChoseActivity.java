package com.learnncode.mediachooser.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.learnncode.mediachooser.MediaModel;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.adapter.GridViewAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PictureChoseActivity extends Activity {


    private Uri fileUri;
    private ArrayList<MediaModel> mGalleryModelList;
    private GridView mImageGridView;
    private GridViewAdapter mImageAdapter;
    private Cursor mImageCursor;

    private final Handler handler = new Handler();

    public static String keyIsTake = "isTaked";
    public static String keyCount = "count";
    public static String keyCountErrorNotify = "erroTip";
    public static String keyResult = "datas";

    public static String keyOutputX = "ox";
    public static String keyOutputY = "oy";


    private boolean isTaked = false;
    private boolean isTakeFromThis = true;
    private int MAX_IMG_LIMIT = 1;


    public static final int MEDIA_TYPE_IMAGE = 1;

    public static final String folderName = "cretve";
    private String countErrorNotify = "";

    public static final short REQUEST_PERMISSION = 101;
    public static final short CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 102;
    public static final short SHORT_REQUEST_MEDIAS = 103;
    public static final short SHORT_REQUEST_CROP = 104;
    public static final short REQUEST_TAKE_PHOTO_70 = 105;
    public static final short SHORT_REQUEST_CROP70 = 106;

    private int outPutX = -1;
    private int outputY = -1;

    private File fileAndroid70CameraTake;
    private File fileAndroid70Crop;

    private String tag = "picchoose";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_chose);

        mImageGridView = (GridView) findViewById(R.id.gridViewImages);
        isTaked = getIntent().getBooleanExtra(keyIsTake, false);
        MAX_IMG_LIMIT = getIntent().getIntExtra(keyCount, 1);
        countErrorNotify = getIntent().getStringExtra(keyCountErrorNotify);


        outPutX = getIntent().getIntExtra(keyOutputX, -1);
        outputY = getIntent().getIntExtra(keyOutputY, -1);


        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        findViewById(R.id.cameraImageViewFromMediaChooserHeaderBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isTaked = true;
                isTakeFromThis = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(PictureChoseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(PictureChoseActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            ) {
                        doInitOrTake();
                    } else {
                        ActivityCompat.requestPermissions(PictureChoseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
                    }
                } else {
                    doInitOrTake();
                }

            }
        });

        findViewById(R.id.doneTextViewViewFromMediaChooserHeaderView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mImageAdapter && mImageAdapter.getSelectList().size() != 0) {
                    ArrayList<String> list = mImageAdapter.getSelectList();
                    onPictureSelected(list);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                doInitOrTake();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        } else {
            doInitOrTake();
        }

    }

    private void doInitOrTake() {

        if (isTaked) {
            isTakeFromThis = false;
            gotoTake();
        } else {
            initPhoneImages();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            doInitOrTake();
        }
    }

    private void gotoTake() {
        if (Build.VERSION.SDK_INT >= 24) {

            fileAndroid70CameraTake = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
            if (!fileAndroid70CameraTake.getParentFile().exists())
                fileAndroid70CameraTake.getParentFile().mkdirs();
            Uri imageUri = FileProvider.getUriForFile(this, getResources().getString(R.string.provider_name), fileAndroid70CameraTake);//通过FileProvider创建一个content类型的Uri
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            startActivityForResult(intent, REQUEST_TAKE_PHOTO_70);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    private void initPhoneImages() {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            mImageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
            setAdapter(mImageCursor);

            if (null != mImageCursor && !mImageCursor.isClosed()) {
                mImageCursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setAdapter(Cursor imagecursor) {

        if (imagecursor.getCount() > 0) {
            mGalleryModelList = new ArrayList<MediaModel>();

            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String file = imagecursor.getString(dataColumnIndex).toString();
                if (null != file && (new File(file)).exists()) {

                    MediaModel galleryModel = new MediaModel(file, false);
                    mGalleryModelList.add(galleryModel);

                }
            }

            mImageAdapter = new GridViewAdapter(this, 0, mGalleryModelList, false);
            mImageGridView.setAdapter(mImageAdapter);
        } else {
            Toast.makeText(this, this.getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
        }


        mImageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = (MediaModel) adapter.getItem(position);

                if (MAX_IMG_LIMIT == 1) {
                    adapter.singleSelect(galleryModel);

                } else {
                    if (adapter.isSelect(galleryModel)) {
                        adapter.selectModel(galleryModel);
                    } else {

                        if (adapter.getSlectMode().size() == MAX_IMG_LIMIT) {

                            if (null != countErrorNotify && countErrorNotify.length() > 0) {
                                Toast.makeText(PictureChoseActivity.this, countErrorNotify, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(PictureChoseActivity.this, "Max image files is " + MAX_IMG_LIMIT, Toast.LENGTH_LONG).show();
                            }
                            return;
                        }
                        adapter.selectModel(galleryModel);

                    }
                }
            }
        });


    }


    public void addItem(String item) {
        if (mImageAdapter != null) {
            MediaModel model = new MediaModel(item, false);
            mGalleryModelList.add(0, model);
            mImageAdapter.notifyDataSetChanged();
        } else {
            initPhoneImages();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
                String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
                Log.i(tag, fileUriString);
                if (isTakeFromThis) {
                    addItem(fileUriString);
                } else {
                    ArrayList<String> files = new ArrayList<>();
                    files.add(fileUriString);
                    onPictureSelected(files);
                }


//                final AlertDialog alertDialog = MediaChooserConstants.getDialog(PictureChoseActivity.this).create();
//                alertDialog.show();
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
//                        addItem(fileUriString);
//                        alertDialog.cancel();
//                    }
//                }, 5000);


            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == SHORT_REQUEST_CROP && resultCode == RESULT_OK) {

            try {
                String path = data.getStringExtra("data");

                Uri uri = data.getData();

                String decodepath=Uri.decode(uri.getEncodedPath());

//                String dataString = data.getDataString();
//                Log.i(tag, "x" + data.getScheme() + " data:" + dataString);

                ArrayList<String> fiels = new ArrayList<>();
//                String file = dataString.replace("file:///", "/");

                fiels.add(decodepath);

//                fiels.add(file);

                putDataBack(fiels);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (requestCode == REQUEST_TAKE_PHOTO_70 && resultCode == RESULT_OK) {

            Log.i(tag, "picture:" + fileAndroid70CameraTake.getPath());
            Log.e(tag, "get pciture from camera 70:" + fileAndroid70CameraTake.exists());

            if (null != fileAndroid70CameraTake && fileAndroid70CameraTake.exists()) {

                ArrayList<String> files = new ArrayList<>();
                files.add(fileAndroid70CameraTake.getAbsolutePath());
                if (!isTakeFromThis) {
                    onPictureSelected(files);
                }
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }

        } else if (requestCode == SHORT_REQUEST_CROP70 && resultCode == RESULT_OK) {
            Uri uri = FileProvider.getUriForFile(this, getResources().getString(R.string.provider_name), fileAndroid70Crop);
            Log.e(tag, "uri 70:" + uri.getPath());
            if (null != fileAndroid70Crop && fileAndroid70Crop.exists()) {
                ArrayList<String> files = new ArrayList<>();
                files.add(fileAndroid70Crop.getAbsolutePath());
                putDataBack(files);
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }

            Log.e(tag, "android 7.0 crop sucess");
        }

    }


    /**
     * @param activity
     * @param isTake           是否是拍照请求
     * @param picCount         需要选择的照片张数
     * @param countErrorNotify 超过最大张数的错误提示
     */
    private static void gotoSelectPicture(Activity activity, boolean isTake, int picCount, String countErrorNotify, int outputX, int outputY) {

        Intent intent = new Intent(activity, PictureChoseActivity.class);
        intent.putExtra(PictureChoseActivity.keyIsTake, isTake);
        intent.putExtra(PictureChoseActivity.keyCount, picCount);
        intent.putExtra(PictureChoseActivity.keyCountErrorNotify, countErrorNotify);
        intent.putExtra(PictureChoseActivity.keyOutputX, outputX);
        intent.putExtra(PictureChoseActivity.keyOutputY, outputY);
        activity.startActivityForResult(intent, SHORT_REQUEST_MEDIAS);
    }


    /**
     * @param activity
     * @param outputX  照片输出宽度，设置为-1 不会被裁剪，否则会调用系统裁剪
     * @param outputY  照片输出的高度，设置为-1 不会被裁剪，否则会调用系统裁剪
     */
    public static void gotoTakePicture(Activity activity, int outputX, int outputY) {
        gotoSelectPicture(activity, true, 1, "", outputX, outputY);
    }


    /**
     * @param activity
     * @param picCount         要选择的照片数量
     * @param countErrorNotify 数量超过时的提示
     * @param outputX          照片输出宽度，设置为-1 不会被裁剪，否则会调用系统裁剪
     * @param outputY          照片输出的高度，设置为-1 不会被裁剪，否则会调用系统裁剪
     */
    public static void gotoSelectPicture(Activity activity, int picCount, String countErrorNotify, int outputX, int outputY) {

        gotoSelectPicture(activity, false, picCount, countErrorNotify, outputX, outputY);

    }




    public void cropImageUri(Uri uriIn, Uri uriOut, int outputX, int outputY, int requestCode, File filePath) {

        Log.e(tag, "crop with code:" + requestCode);

        if (Build.VERSION.SDK_INT >= 24) {

//            fileAndroid70Crop = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
//            if (!fileAndroid70Crop.getParentFile().exists()) {
//                fileAndroid70Crop.getParentFile().mkdirs();
//                try {
//                    fileAndroid70Crop.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            fileAndroid70Crop = filePath;

            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri inputUri = FileProvider.getUriForFile(this, getResources().getString(R.string.provider_name), filePath);
//            Uri outputUri = FileProvider.getUriForFile(this, getResources().getString(R.string.provider_name), fileAndroid70Crop);

            Log.i(tag, "crop output:" + fileAndroid70Crop.getAbsolutePath());

//            this.grantUriPermission("com.android.camera", outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            this.grantUriPermission("com.android.camera", inputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(inputUri, "image/*");

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//请求URI授权读取
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//请求URI授权写入

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, inputUri);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent, requestCode);
        } else {
            //======use local api=====
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uriIn, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOut);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent, requestCode);
        }

    }


    private void onPictureSelected(ArrayList<String> selectedList) {
        if (null != selectedList && selectedList.size() == 1 && (-1 != outPutX && -1 != outputY)) {

            String oldF = selectedList.get(0);
            File of = new File(oldF);


//            String dcimpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

            String nf = oldF.replace(of.getName(), "r" + of.getName());

            if (Build.VERSION.SDK_INT >= 24) {
                cropImageUri(Uri.fromFile(of), Uri.fromFile(new File(nf)), outPutX, outputY, SHORT_REQUEST_CROP70, of);
            } else {
                cropImageUri(Uri.fromFile(of), Uri.fromFile(new File(nf)), outPutX, outputY, SHORT_REQUEST_CROP, null);
            }

        } else {
            putDataBack(selectedList);
        }
    }


    private void putDataBack(ArrayList<String> selectedList) {
        Intent intent = new Intent();
        intent.putExtra(keyResult, selectedList);
        setResult(RESULT_OK, intent);
        finish();
    }


    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
