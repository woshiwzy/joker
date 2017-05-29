package com.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

public class DialogUtils {


    public static void showRetryDialog(Activity activity, String title, String msg, final String postiveText, final String negatve, final DialogCallBackListener dialogCallBackListener) {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton(postiveText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != dialogCallBackListener) {
                    dialogCallBackListener.onDone(true);
                }
            }
        });
        builder.setNegativeButton(negatve, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != dialogCallBackListener) {
                    dialogCallBackListener.onDone(false);
                }
            }
        });
        builder.create().show();

    }


    public static void showMessageDialog(String title, String message, String postive, Activity ativity, final DialogCallBackListener dialogCallBackListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ativity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(postive, new OnClickListener() {
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

    /**
     * @param activity
     * @param title
     * @param yesText
     * @param noText
     * @param errTip
     * @param callBackListener
     */
    public static void showInputDialog(final Activity activity, String title, String yesText, String noText, final String errTip,
                                       final DialogCallBackListener callBackListener) {

        final EditText editText = new EditText(activity);
        editText.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(activity).setTitle(title).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(yesText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != callBackListener) {
                            String alias = editText.getText().toString();
                            if (!StringUtils.isEmpty(alias)) {
                                dialog.dismiss();
                                callBackListener.onCallBack(true, alias);
                            } else {
                                Tool.ToastShow(activity, errTip);
                            }
                        }
                    }
                }).setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onCallBack(false, "");
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void showInputPWDDialog(final Activity activity, String title, String yesText, String noText, final String errTip,
                                          final DialogCallBackListener callBackListener) {

        final EditText editText = new EditText(activity);
        editText.setGravity(Gravity.CENTER);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        new AlertDialog.Builder(activity).setTitle(title).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(yesText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != callBackListener) {
                            String alias = editText.getText().toString();
                            if (!StringUtils.isEmpty(alias)) {
                                dialog.dismiss();
                                callBackListener.onCallBack(true, alias);
                            } else {
                                Tool.ToastShow(activity, errTip);
                            }
                        }
                    }
                }).setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onCallBack(false, "");
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void showInputDialog2(final Activity activity, String title, String yesText, String noText, final String errTip, final String msg,
                                        final DialogCallBackListener callBackListener) {

        final EditText editText = new EditText(activity);
        editText.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(activity).setTitle(title).setMessage(msg).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(yesText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != callBackListener) {
                            String alias = editText.getText().toString();
                            callBackListener.onCallBack(true, alias);
                            callBackListener.onCallBack(true, alias, dialog);

//                            if (!StringUtils.isEmpty(alias)) {
//
//                            } else {
//                                Tool.ToastShow(activity, errTip);
//                            }
                        }
                    }
                }).setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onCallBack(false, "");
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void showConfirmDialog(final Activity activity, String title, String message, String yesText, String noText, final DialogCallBackListener callBackListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(yesText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(true);
                }
            }
        });

        builder.setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(false);
                }
            }
        });
        builder.create().show();
    }


    static Dialog dlg;
    public static void showConfirmDialog2(final Activity activity, String title, String message, String yesText, String noText, final DialogCallBackListener callBackListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(yesText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(true);
                }
            }
        });

        builder.setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(false);
                }
            }
        });

        if(null!=dlg){
            dlg.dismiss();
            dlg=null;
        }

        dlg = builder.create();
        dlg.show();
    }


    public static void showConfirmDialog3(final Context activity, String title, String message, String yesText, String noText, final DialogCallBackListener callBackListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(yesText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(true);
                }
            }
        });

        builder.setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(false);
                }
            }
        });

        if(null!=dlg){
            dlg.dismiss();
            dlg=null;
        }

        dlg = builder.create();
        dlg.show();
    }


    public static void showConfirmDialog(final Activity activity, int title, int message, int yesText, int noText, final DialogCallBackListener callBackListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(yesText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(true);
                }

            }
        });

        builder.setNegativeButton(noText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (null != callBackListener) {
                    callBackListener.onDone(false);
                }

            }
        });
        builder.create().show();

    }

}
