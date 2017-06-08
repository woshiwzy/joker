package com.bigkoo.pickerview.adapter;

/**
 * Created by wangzy on 16/3/24.
 */
public class MonthTextAdapter implements WheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;


    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    /**
     * Default constructor
     */
    public MonthTextAdapter() {
        this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public MonthTextAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Object getItem(int index) {
        int ret = 0;
        if (index >= 0 && index < getItemsCount()) {
            ret = minValue + index;
        }
        if (ret == 0) {
            return month[0];
        }
        return month[ret - 1];
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    @Override
    public int indexOf(Object o) {
        String monthText = (String) o;

        int index = 0;
        for (int i = 0, isize = month.length; i < isize; i++) {
            if (monthText.equals(month[i])) {
                index = i;
                break;
            }
        }
        return index - minValue;
    }
}
