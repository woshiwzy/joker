package com.common.task;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.common.net.NetResult;
import com.common.util.LogUtil;
import com.cretve.model.R;

import java.util.HashMap;

/**
 * @author wangzy
 */
public class BaseTask extends AsyncTask<HashMap<String, String>, Void, NetResult> {

    NetCallBack mCallBack;
    public Dialog dialog;
    public Context activity;

    

    public static void resetTastk(BaseTask baseTask) {
        if (null != baseTask && !baseTask.isCancelled()) {
            baseTask.cancel(true);
        }
    }

    @Deprecated
    public BaseTask(NetCallBack callBack) {
        this.mCallBack = callBack;
    }

    public BaseTask(NetCallBack callBack, Context context) {
        this.mCallBack = callBack;
        this.activity = context;
    }


    public BaseTask(Context context, NetCallBack callBack) {
        this.activity = context;
        this.mCallBack = callBack;
    }

    public void showDialogForSelf(boolean cancelable) {
        if (null != activity) {

            if (null == dialog) {
                dialog = crateDialog(activity, this, cancelable);
            }

            if (null != dialog) {
                dialog.show();
            }

        }
    }

    public void forekill(){

        if(null!=mCallBack){
            mCallBack.foreKill(this);
        }

    }

    public void hideDialogSelf() {


        if (null != dialog && null != dialog.getWindow()) {
            dialog.dismiss();
        }
        dialog = null;
    }


    public Dialog crateDialog(Context context, final BaseTask baseTask, final boolean cancelAble) {
        if (null == context) {
            return null;
        }

        dialog = new Dialog(context, R.style.dialog);
        View dialogView = View.inflate(context, R.layout.dialog_progress, null);
        ProgressBar mProgressBar = (ProgressBar) dialogView.findViewById(R.id.progressBarMore);
        dialog.setContentView(dialogView);
        dialog.setCancelable(cancelAble);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtil.i("sonder","onCancel====");


                if (null != baseTask && baseTask.getStatus() == Status.RUNNING) {

                    baseTask.forekill();
                    baseTask.cancel(true);

                }

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                LogUtil.i("sonder","ondismis====");

                if (null != baseTask && baseTask.getStatus() == Status.RUNNING) {
                    baseTask.forekill();
                    baseTask.cancel(true);

                }

            }
        });

        return dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (null != mCallBack) {
            mCallBack.onPreCall();
            mCallBack.onPreCall(this);
        }
    }

    @Override
    protected NetResult doInBackground(HashMap<String, String>... params) {
        if (null != mCallBack && null != activity) {
            if (null == params) {
                return mCallBack.onDoInBack(null, this);
            } else {
                HashMap<String, String> paramMap = (HashMap<String, String>) params[0];
                NetResult result = mCallBack.onDoInBack(paramMap, this);
                return result;
            }
        }
        return null;
    }

    @Override
    protected void onCancelled() {

        if (null != dialog && null != dialog.getWindow() && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (null != mCallBack) {
            mCallBack.onCanCell();
            mCallBack.onCanCell(this);
        }

        super.onCancelled();
    }

    @Override
    protected void onPostExecute(NetResult result) {
        super.onPostExecute(result);
        if (null != mCallBack && null != activity) {
            mCallBack.onFinish(result);
            mCallBack.onFinish(result, this);
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
