package com.datepicker;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.common.util.NumberHelper;
import com.cretve.model.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimePicker extends FrameLayout {


	private NumberPicker mDateSpinner;
	private NumberPicker mHourSpinner;
	private NumberPicker mMinuteSpinner;
	private NumberPicker mAmpmSpinner;

	private Calendar mDate;
	private Context mContext;
	private int mHour, mMinute;
	private int ampm;
	private String[] mDateDisplayValues = new String[7];
	private OnDateTimeChangedListener mOnDateTimeChangedListener;

	public DateTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public DateTimePicker(Context context) {
		super(context);
		mContext = context;
		init();

	}

	private void init() {

		if(null==mDate){
			mDate = Calendar.getInstance();
		}
		mHour = mDate.get(Calendar.HOUR_OF_DAY);
		mMinute = mDate.get(Calendar.MINUTE);
		LayoutInflater.from(mContext).inflate(R.layout.dialog_date, this, true);
		/**
		 * 初始化控件
		 */
		mDateSpinner = (NumberPicker) this.findViewById(R.id.np_date);
		mDateSpinner.setMinValue(0);
		mDateSpinner.setMaxValue(6);

		updateDateControl();
		mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);

		mHourSpinner = (NumberPicker) this.findViewById(R.id.np_hour);
		mHourSpinner.setMaxValue(12);
		mHourSpinner.setMinValue(1);
		mHourSpinner.setValue(mHour);
		mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

		mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_minute);
		mMinuteSpinner.setMaxValue(59);
		mMinuteSpinner.setMinValue(0);
		mMinuteSpinner.setValue(mMinute);

		String[] disPlay=new String[60];

		for(int i=0;i<60;i++){
			disPlay[i]= NumberHelper.LeftPad_Tow_Zero(i);
		}
		mMinuteSpinner.setDisplayedValues(disPlay);
		mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);

		mAmpmSpinner = (NumberPicker) this.findViewById(R.id.np_ampm);

		mAmpmSpinner.setMaxValue(1);
		mAmpmSpinner.setMinValue(0);
		mAmpmSpinner.setValue(mDate.get(GregorianCalendar.AM_PM));

		mAmpmSpinner.setDisplayedValues(new String[] { "AM", "PM" });
		mAmpmSpinner.setOnValueChangedListener(mOnAMPMChangedListener);


	}

	private OnValueChangeListener mOnDateChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
			updateDateControl();
			onDateTimeChanged();
		}
	};

	private OnValueChangeListener mOnHourChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mHour = mHourSpinner.getValue();
			onDateTimeChanged();

		}
	};

	private OnValueChangeListener mOnMinuteChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mMinute = mMinuteSpinner.getValue();
			onDateTimeChanged();
		}
	};

	private OnValueChangeListener mOnAMPMChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			ampm = mAmpmSpinner.getValue();
			onDateTimeChanged();
		}
	};

	private void updateDateControl() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(mDate.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
		mDateSpinner.setDisplayedValues(null);
		for (int i = 0; i < 7; ++i) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			mDateDisplayValues[i] = (String) DateFormat.format("EEE MMM dd ", cal);
		}
		mDateSpinner.setDisplayedValues(mDateDisplayValues);
		mDateSpinner.setValue(7 / 2);
		mDateSpinner.invalidate();
	}




	public interface OnDateTimeChangedListener {
		void onDateTimeChanged(DateTimePicker view, int year, int month, int day, int hour, int minute,int ampm);
	}

	public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
		mOnDateTimeChangedListener = callback;
	}

	private void onDateTimeChanged() {
		if (mOnDateTimeChangedListener != null) {
			mOnDateTimeChangedListener.onDateTimeChanged(this, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
					mDate.get(Calendar.DAY_OF_MONTH), mHour, mMinute,ampm);
		}
	}

	public Calendar getmDate() {
		return mDate;
	}

	public void setmDate(Calendar mDate) {
		this.mDate = mDate;
	}

	public int getAmpm() {
		return ampm;
	}

	public void setAmpm(int ampm) {
		this.ampm = ampm;
	}
}