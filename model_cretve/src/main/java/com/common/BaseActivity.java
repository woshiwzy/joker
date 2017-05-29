package com.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.callback.OnProgressDialogCallback;
import com.common.task.BaseTask;
import com.common.util.CustomToast;
import com.common.util.StringUtils;
import com.cretve.model.R;

import java.util.HashMap;

/**
 * 基础activity
 *
 * @author:wangzhengyun 2012-8-31
 */
public class BaseActivity extends AppCompatActivity implements OnProgressDialogCallback {

    public Dialog mProgressDialog = null;

    private OnProgressDialogCallback mProgressDialogCallback;
    private ProgressBar mProgressBar = null;

    public BaseTask baseTask;
    private AsyncTask mTaskRunning;
    private HashMap<BaseTask, Dialog> taskMap;//= new HashMap<BaseTask, Dialog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        taskMap = new HashMap<BaseTask, Dialog>();
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        crateDialog();
    }

    public void reSetTask() {
        if (null != baseTask && baseTask.getStatus() == AsyncTask.Status.RUNNING) {
            baseTask.cancel(true);
        }
    }


    @Override
    public void startActivity(Intent intent) {

        try {
            super.startActivity(intent);
        } catch (Exception e) {
            onStartActivityException(e);
        }

    }

    public void onStartActivityException(Exception e) {

    }


    public void crateDialog() {
        mProgressDialog = new Dialog(this, R.style.dialog);
        View dialogView = View.inflate(this, R.layout.dialog_progress, null);
        mProgressBar = (ProgressBar) dialogView.findViewById(R.id.progressBarMore);
        mProgressDialog.setContentView(dialogView);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (null != mProgressDialogCallback) {
                    mProgressDialogCallback.onProgressDialogcancel();
                }
            }
        });
    }

    public Dialog getmProgressDialog() {
        return mProgressDialog;
    }


    public void setmProgressDialog(Dialog mProgressDialog) {
        this.mProgressDialog = mProgressDialog;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // hideProgressDialog();
        super.onNewIntent(intent);
    }

    public void setDialogCancellAable(boolean isCancellAable) {
        mProgressDialog.setCancelable(isCancellAable);
    }

    /**
     * 初始化视图
     */
    public void initView() {

    }


    public void showProgressDialogWithTask(AsyncTask runingTask) {
        mTaskRunning = runingTask;
        showProgressDialog();
    }


    public void hideProgressDialogWithTask() {
        if (null != mTaskRunning && !mTaskRunning.isCancelled()) {
            mTaskRunning.cancel(true);
        }
        hideProgressDialog();
    }

    public String getInputFromId(int id) {

        TextView textView = (TextView) findViewById(id);

        String input = textView.getText().toString();

        if (StringUtils.isEmpty(input)) {

            return "";
        } else {
            return input.trim();
        }
    }

    public String getInput(EditText input) {

        String text = input.getText().toString();
        if (StringUtils.isEmpty(text)) {

            return "";
        } else {
            return text.trim();
        }

    }

    public String getInput(TextView input) {

        String text = input.getText().toString();
        if (StringUtils.isEmpty(text)) {

            return "";
        } else {
            return text.trim();
        }

    }

    public TextView findTextViewById(int id) {
        return (TextView) findViewById(id);
    }

    public EditText findEditTextById(int id) {
        return (EditText) findViewById(id);
    }

    public Button findButtonById(int id) {
        return (Button) findViewById(id);
    }

    public ImageView findImageViewById(int id) {
        return (ImageView) findViewById(id);
    }

    public ImageButton findImageButtonById(int id) {
        return (ImageButton) findViewById(id);
    }

    public ListView findListViewById(int id) {
        return (ListView) findViewById(id);
    }

    public RelativeLayout findRelativeLayout(int id) {
        return (RelativeLayout) findViewById(id);
    }

    public LinearLayout findLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    public AutoCompleteTextView findAutoCompleteTextById(int id) {
        return (AutoCompleteTextView) findViewById(id);
    }

    @Override
    public void onBackPressed() {


        backFinish();
        return;
    }


    public void showToast(String message, int times) {
        if (null != customToast) {
            customToast.hide();
        }
        customToast = CustomToast.makeText(getApplicationContext(), message, times);
        customToast.show();
    }


    // public void showProgressDialog(String title, String message,
    // boolean cancelable) {
    //
    // }

    public void showProgressDialog(boolean isCancellAble) {
        if (!isFinishing()) {
            mProgressDialog.setCancelable(isCancellAble);
            mProgressDialog.show();
        }
    }

    public void showProgressDialog() {
        if (!isFinishing()) {
            showProgressDialog(true);
        }
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }


    public void showNewDialogWithNewTask(final BaseTask baseTask) {
        if (null == baseTask) {
            return;
        }

        final Dialog progressDialog = crateNewDialog(this);
        progressDialog.getWindow().getDecorView().setTag(baseTask);
        progressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();

                BaseTask runningTask = (BaseTask) progressDialog.getWindow().getDecorView().getTag();
                if (null != runningTask && baseTask.getStatus() == AsyncTask.Status.RUNNING) {
                    runningTask.cancel(true);
                }

                if (null != baseTask) {
                    taskMap.remove(baseTask);
                }

            }
        });
        progressDialog.show();
        taskMap.put(baseTask, progressDialog);
    }


    public void hideNewDialogWithTask(final BaseTask baseTask) {
        if (null == baseTask) {
            return;
        }
        Dialog dlg = taskMap.get(baseTask);
        if (null != dlg) {
            dlg.dismiss();
        }
        taskMap.remove(baseTask);
    }


    public Dialog crateNewDialog(Activity activity) {

        Dialog mProgressDialog = new Dialog(activity, R.style.dialog);
        View dialogView = View.inflate(activity, R.layout.dialog_progress, null);
        ProgressBar nProgressBar = (ProgressBar) dialogView.findViewById(R.id.progressBarMore);
        mProgressDialog.setContentView(dialogView);
        mProgressDialog.setCancelable(true);
        return mProgressDialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



        mProgressDialog.dismiss();
        if (null != customToast) {
            customToast.hide();
        }
    }

    public void backFinish() {
        finish();
    }

    @Override
    public void onProgressDialogcancel() {

    }


    public void setCancelAble(boolean isAble) {
        mProgressDialog.setCancelable(isAble);
    }


    protected CustomToast customToast;


    public void showNotifyTextIn5Seconds(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//        showToast(text, 5 * 1000);
    }

    public void showNotifyTextIn5Seconds(int text) {

        showToast(getResources().getString(text), 5 * 1000);
    }


    public void showToastWithTime(String message, int second) {
        if (null != customToast) {
            customToast.hide();
        }

        customToast = CustomToast.makeText(getApplicationContext(), message, second * 1000);
        customToast.show();
    }


    public void closeKeyBorad() {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().peekDecorView().getWindowToken(), 0);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}

