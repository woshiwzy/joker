package com.common.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


/**
 * Created by wangzy on 15/9/16.
 */
public abstract class BasePage {



    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;


    protected View rootView;
    protected int showCount = 0;
    protected int hideCount = 0;

    private String title;

    public boolean needRefresh = false;

    //==============
    protected Activity activity;

    public abstract void initView();

    public LayoutInflater layoutInflater;

    public BasePage(Activity activity) {
        this.activity=activity;
        this.layoutInflater=LayoutInflater.from(activity);

    }

    public void onShow() {

        showCount++;
    }

    public void onHide() {
        hideCount++;

    }

    public void onResume(){

    }

    public void onPause(){

    }

    public void onDestory(){}

    public View findViewById(int id) {

        return rootView.findViewById(id);
    }

    public String getInput(EditText input) {
        return input.getText().toString();
    }

    public TextView findTextViewById(int id) {
        return (TextView) rootView.findViewById(id);
    }

    public EditText findEditTextById(int id) {
        return (EditText) rootView.findViewById(id);
    }

    public Button findButtonById(int id) {
        return (Button) rootView.findViewById(id);
    }

    public ImageView findImageViewById(int id) {
        return (ImageView) rootView.findViewById(id);
    }

    public ImageButton findImageButtonById(int id) {
        return (ImageButton) rootView.findViewById(id);
    }

    public ListView findListViewById(int id) {
        return (ListView) rootView.findViewById(id);
    }

    public RelativeLayout findRelativeLayout(int id) {
        return (RelativeLayout) rootView.findViewById(id);
    }

    public LinearLayout findLinearLayout(int id) {
        return (LinearLayout) rootView.findViewById(id);
    }

    public AutoCompleteTextView findAutoCompleteTextById(int id) {
        return (AutoCompleteTextView) rootView.findViewById(id);
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }



    public void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    public void showError() {
        Toast.makeText(activity, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }


    public void openFilePicker() {

    }

}
