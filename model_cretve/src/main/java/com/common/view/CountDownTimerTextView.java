package com.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CountDownTimerTextView extends TextView implements Runnable {

	public CountDownTimerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private long mday, mhour, mmin, msecond;
	private boolean run = false; //

	public void setTimes(long[] times) {
		mday = times[0];
		mhour = times[1];
		mmin = times[2];
		msecond = times[3];

	}

	private void ComputeTime() {
		msecond--;
		if (msecond < 0) {
			mmin--;
			msecond = 59;
			if (mmin < 0) {
				mmin = 59;
				mhour--;
				if (mhour < 0) {
					mhour = 23;
					mday--;
				}
			}
		}
	}

	public boolean isRun() {
		return run;
	}

	public void beginRun() {
		this.run = true;
		run();
	}

	public void stopRun() {
		this.run = false;
	}

	@Override
	public void run() {
		if (run) {
			ComputeTime();
			String day = formatNumber(mday);
			day = "00".equals(day) ? "" : day + ":";
			String strTime = day + formatNumber(mhour) + ":" + formatNumber(mmin) + ":" + formatNumber(msecond);
			this.setText(strTime);

			if ((mday + mhour + mmin + msecond) == 0) {
				removeCallbacks(this);
			} else {
				postDelayed(this, 1000);
			}
		} else {
			removeCallbacks(this);
		}
	}

	private String formatNumber(long number) {
		String ret = String.valueOf(number);
		if (ret.length() == 2) {
			return ret;
		} else {
			return "0" + ret;
		}
	}
}
