package com.common.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.common.util.DateTool;
import com.common.util.NumberHelper;
import com.common.util.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangzy on 16/6/6.
 */
public class TimerTextView extends TextView {


    private String timeformat = "mm:ss";
    private boolean isTimeout;
    private Timer timer;
    private long timeEnd;
    private int timeout;
    private TimerTask timerTask;
    private OnTimeOutListener onTimeOutListener;


    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        timer = new Timer();
    }

    @Deprecated
    public void setTimerEnd(long endTime) {

        if (-1 == endTime) {
            if (null != onTimeOutListener) {
                onTimeOutListener.onTimeout(TimerTextView.this);
            }
            return;
        }

        this.isTimeout = false;
        this.timeEnd = endTime;

        if (null != timerTask) {
            timerTask.cancel();
        }


        long now = DateTool.getNow().getTime() / 1000;

        if (endTime < now) {
            this.isTimeout = true;
            if (null != onTimeOutListener) {
                onTimeOutListener.onTimeout(TimerTextView.this);
            }
            return;
        }


        timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0);

            }
        };

        timer.schedule(timerTask, 0, 1 * 1000);

    }


    public void setTimerOut(final int tiemOutTime) {

        if (-1 == tiemOutTime) {
            if (null != onTimeOutListener) {
                onTimeOutListener.onTimeout(TimerTextView.this);
            }
            return;
        }

        this.isTimeout = false;
        this.timeout = tiemOutTime;

        if (null != timerTask) {
            timerTask.cancel();
        }


        if (timeout <= 0) {
            this.isTimeout = true;
            if (null != onTimeOutListener) {
                onTimeOutListener.onTimeout(TimerTextView.this);
            }
            return;
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeout--;
                handler.sendEmptyMessage(0);
            }
        };

        timer.schedule(timerTask, 0, 1 * 1000);

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {


            if(timeout<=0){
                isTimeout = true;
                if (null != onTimeOutListener) {
                    onTimeOutListener.onTimeout(TimerTextView.this);
                }

                setText("00:00");

                timerTask.cancel();
                timerTask = null;

                return;

            }else{

                StringBuffer stringBuffer = new StringBuffer();
                int remain=timeout;

                int day = (int) (remain / 60 / 60 / 24);
                if (day > 0) {
                    stringBuffer.append(day + " day ");
                }

                int hour = (int) (remain / 60 / 60 % 24);
                if (hour == 0) {
                    if (day > 0) {
                        stringBuffer.append(":00");
                    }
                } else if (hour > 0) {
                    if (day > 0) {
                        stringBuffer.append(":" + NumberHelper.LeftPad_Tow_Zero(hour));
                    }
                }

                int munite = (int) (remain / 60 % 60);
                int second = (int) (remain % 60);

                String mmText = NumberHelper.LeftPad_Tow_Zero(munite) + ":" + NumberHelper.LeftPad_Tow_Zero(second);

                String resultText = StringUtils.isEmpty(stringBuffer.toString()) ? mmText : stringBuffer.toString() + ":" + mmText;

                setText(resultText);
            }


//            long now = DateTool.getNow().getTime() / 1000;
//            if (timeEnd < now) {
//                isTimeout = true;
//                if (null != onTimeOutListener) {
//                    onTimeOutListener.onTimeout(TimerTextView.this);
//                }
//
//                setText("00:00");
//
//                timerTask.cancel();
//                timerTask = null;
//
//                return;
//            } else {
//
//                long remain = timeEnd - now;
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                int day = (int) (remain / 60 / 60 / 24);
//                if (day > 0) {
//                    stringBuffer.append(day + " day ");
//                }
//
//
//                int hour = (int) (remain / 60 / 60 % 24);
//                if (hour == 0) {
//                    if (day > 0) {
//                        stringBuffer.append(":00");
//                    }
//                } else if (hour > 0) {
//                    if (day > 0) {
//                        stringBuffer.append(":" + NumberHelper.LeftPad_Tow_Zero(hour));
//                    }
//                }
//
//                int munite = (int) (remain / 60 % 60);
//                int second = (int) (remain % 60);
//
//                String mmText = NumberHelper.LeftPad_Tow_Zero(munite) + ":" + NumberHelper.LeftPad_Tow_Zero(second);
//
//                String resultText = StringUtils.isEmpty(stringBuffer.toString()) ? mmText : stringBuffer.toString() + ":" + mmText;
//
//                setText(resultText);
//
//            }


        }
    };


    public static interface OnTimeOutListener {
        public void onTimeout(TimerTextView timerTextView);
    }


    public OnTimeOutListener getOnTimeOutListener() {
        return onTimeOutListener;
    }

    public void setOnTimeOutListener(OnTimeOutListener onTimeOutListener) {
        this.onTimeOutListener = onTimeOutListener;
    }
}
