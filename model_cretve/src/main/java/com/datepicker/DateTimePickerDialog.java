package com.datepicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import java.util.Calendar;
import java.util.Date;

public class DateTimePickerDialog extends AlertDialog implements  OnClickListener {
	private DateTimePicker mDateTimePicker;
	public static Calendar mDate = Calendar.getInstance();
	private OnDateTimeSetListener mOnDateTimeSetListener;
	public static  int ampm;

	public DateTimePickerDialog(Context context, long date) {
		super(context);

		mDateTimePicker = new DateTimePicker(context);
		if(null!=mDate){
			mDateTimePicker.setmDate(mDate);
			mDateTimePicker.setAmpm(ampm);
		}


		setView(mDateTimePicker);

		mDateTimePicker.setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener() {
					@Override
					public void onDateTimeChanged(DateTimePicker view,
							int year, int month, int day, int hour, int minute,int ampm) {
						mDate.set(Calendar.YEAR, year);
						mDate.set(Calendar.MONTH, month);
						mDate.set(Calendar.DAY_OF_MONTH, day);
						mDate.set(Calendar.HOUR_OF_DAY, hour);
						mDate.set(Calendar.MINUTE, minute);

						Date nowDate=new Date();
						Calendar tempCalendar=Calendar.getInstance();
						tempCalendar.setTime(nowDate);
						mDate.set(Calendar.SECOND, tempCalendar.get(Calendar.SECOND));

						DateTimePickerDialog.this.ampm=ampm;

						/**
						 * 更新日期
						 */
						updateTitle(mDate.getTimeInMillis());
					}
				});

		setButton("Confirm", this);
		setButton2("Cancel", (OnClickListener) null);
		mDate.setTimeInMillis(date);
		updateTitle(mDate.getTimeInMillis());
	}

	/*
	 * 接口回調控件 秒数
	 */
	public interface OnDateTimeSetListener {
		void OnDateTimeSet(AlertDialog dialog, long date,int ampm);
	}

//	/**
//	 * 更新对话框日期
//	 * 
//	 * @param date
//	 */
	private void updateTitle(long date) {
//		int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE
//				| DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;
//		
//		setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
	}

	/*
	 * 对外公开方法让Activity实现
	 */
	public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
		mOnDateTimeSetListener = callBack;
	}

	public void onClick(DialogInterface arg0, int arg1) {
		if (mOnDateTimeSetListener != null) {
			mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis(),ampm);
		}
	}

	public Calendar getmDate() {
		return mDate;
	}

	public void setmDate(Calendar mDate,int ampm) {
		this.mDate = mDate;
		mDateTimePicker.setmDate(mDate);
		mDateTimePicker.setAmpm(ampm);
	}

	public int getAmpm() {
		return ampm;
	}

	public void setAmpm(int ampm) {
		mDateTimePicker.setAmpm(ampm);
		this.ampm = ampm;
	}
}
