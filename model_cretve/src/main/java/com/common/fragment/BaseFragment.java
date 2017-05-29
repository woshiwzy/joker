package com.common.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

/**
 * Created by wangzy on 16/9/1.
 */
public class BaseFragment extends Fragment {

    public final short SHORT_ACCESS_LOCATION = 1003;

    public boolean needRefresh = true;
    public boolean onBackPressed() {


        return false;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
