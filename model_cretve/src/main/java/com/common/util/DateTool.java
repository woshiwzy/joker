package com.common.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author:wangzhengyun 2012-9-11
 */
public class DateTool {


    static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    public static DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");


    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {

        try {


            SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gpsUTCDate = null;
            try {
                gpsUTCDate = utcFormater.parse(utcTime);
            } catch (ParseException e) {
//                e.printStackTrace();
            }
            SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
            localFormater.setTimeZone(TimeZone.getDefault());
            String localTime = localFormater.format(gpsUTCDate.getTime());
            return localTime;
        } catch (Exception e) {

        }

        return utcTime;
    }


    /**
     * 全球统一时间转换为设备时区时间
     *
     * @param date
     * @return
     */
    public static Date getGenlinTime2Location(Date date) {

        TimeZone timeZoneLondon = TimeZone.getTimeZone("Europe/London");//格林威治

//        TimeZone timeZoneUTC= TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(timeZoneLondon);
        cal.setTime(date);

        TimeZone timeZoneshanghai = TimeZone.getDefault();
        cal.setTimeZone(timeZoneshanghai);

        return cal.getTime();
    }

    public static Date getMTTime2Location(Date date) {

        TimeZone timeZoneLondon = TimeZone.getTimeZone("UTC-7");

        Calendar cal = Calendar.getInstance(timeZoneLondon);
        cal.setTime(date);

        TimeZone timeZoneshanghai = TimeZone.getDefault();
        cal.setTimeZone(timeZoneshanghai);

        return cal.getTime();
    }

    public static String getLocalFullTime(Date date) {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy, hh:mm");
            int ampm = calendar.get(Calendar.AM_PM);
            String timelabel = simpleDateFormat.format(date) + " " + (ampm == 1 ? "PM" : "AM");

            return timelabel;
        } catch (Exception e) {

        }

        return "";
    }


    public static String getLocalFullTime2(Date date) {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yyyy\nhh:mm");
            int ampm = calendar.get(Calendar.AM_PM);
            String timelabel = simpleDateFormat.format(date) + " " + (ampm == 1 ? "pm" : "am");

            return timelabel;
        } catch (Exception e) {

        }

        return "";
    }

    public static String getLocalFullTime3(Date date) {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm");
            int ampm = calendar.get(Calendar.AM_PM);
            String timelabel = simpleDateFormat.format(date) + " " + (ampm == 1 ? "pm" : "am");

            return timelabel;
        } catch (Exception e) {

        }

        return "";
    }

    public static String getLocalFullTime4(Date date) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            String timelabel = simpleDateFormat.format(date);
            return timelabel;
        } catch (Exception e) {

        }

        return "";
    }

    public static String getLocalFullTime(String text) {

        try {

            Date date = formatter.parse(text.replace("T", " "));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm");
            int ampm = calendar.get(Calendar.AM_PM);
            String timelabel = simpleDateFormat.format(date) + " " + (ampm == 1 ? "PM" : "AM");

            return timelabel;
        } catch (Exception e) {

        }

        return text;
    }

    public static String timeTranseAustraliaBrisbane2Local(String time) {
        try {
            TimeZone destTimeZone = TimeZone.getDefault();
            TimeZone srcTimeZone = TimeZone.getTimeZone("Australia/Brisbane");
            String timeLabel = DateTransformer.dateTransformBetweenTimeZone(formatter.parse(time), formatter, srcTimeZone, destTimeZone);
            return timeLabel;
        } catch (Exception e) {
        }
        return time;
    }

    public static String timeTranseAustraliaBrisbane(String time) {
        try {
            TimeZone srcTimeZone = TimeZone.getDefault();
            TimeZone destTimeZone = TimeZone.getTimeZone("Australia/Brisbane");
            String timeLabel = DateTransformer.dateTransformBetweenTimeZone(formatter.parse(time), formatter, srcTimeZone, destTimeZone);
            return timeLabel;
        } catch (Exception e) {
        }
        return time;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String timeTrans2LocalTimeFromMST(String time) {
        try {
            TimeZone destTimeZone = TimeZone.getDefault();
            TimeZone srcTimeZone = TimeZone.getTimeZone("MST");
            String timeLabel = DateTransformer.dateTransformBetweenTimeZone(formatter.parse(time), formatter, srcTimeZone, destTimeZone);
            return timeLabel;
        } catch (Exception e) {
        }
        return time;
    }

    public static boolean isSameDay(Date d1, Date d2) {

        return (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDay() == d2.getDay());
    }

    public static String formatDateLocal(Context context, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM y");
        String s = dateFormat.format(date);
        return s;
    }

    public static String formatDateLocal3(Context context, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String s = dateFormat.format(date);
        return s;
    }

    public static String formatDateLocal4(Context context, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");
        String s = dateFormat.format(date);
        return s;
    }

    public static String formatDateLocal2(Context context, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy\n HH:mm");
        String s = dateFormat.format(date);
        return s;
    }


    // 获取现在时间
    public static String getNowTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static Date getNow() {

        return new Date();
    }

    public static boolean isTimeBetwwn(String startYYYYmmDD,
                                       String centerYYYYmmDD, String endYYYYmmDD) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date dStart = sdf.parse(startYYYYmmDD);
            Date dCenter = sdf.parse(centerYYYYmmDD);
            Date dEnd = sdf.parse(endYYYYmmDD);

            long timeStart = dStart.getTime();
            long timeCenter = dCenter.getTime();
            long timeEnd = dEnd.getTime();

            if ((timeCenter >= timeStart) && (timeCenter <= timeEnd)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 获取此前3个月数组
    public static String[] getLasttMonth(int m) {
        String[] months = new String[m];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        months[2] = sdf.format(calendar.getTime());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        months[1] = sdf.format(calendar.getTime());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        months[0] = sdf.format(calendar.getTime());
        return months;
    }

    public static String[] getYears2Now(int startYear) {
        int thisYear = DateTool.getThisYear();
        int deltaYear = thisYear - startYear;
        if (deltaYear > 0) {
            String[] data = new String[1 + deltaYear];
            for (int i = 0; i <= deltaYear; i++) {
                data[i] = String.valueOf((startYear + i));
            }
            return data;
        } else {
            String[] empty = new String[0];
            return empty;
        }
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String datestr = sdf.format(date);
        return datestr;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static String getNowMonth() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date d = new Date();
            return sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取年份中的月份
     *
     * @param yyyymm
     * @return
     */
    public static String getMonthFrom(String yyyymm) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date = sdf.parse(yyyymm);
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            return String.valueOf(ca.get(Calendar.MONTH) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yyyymm;
    }


    public static int getThisDay() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        return ca.get(Calendar.DAY_OF_MONTH);
    }

    public static int getThisMonth() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        return ca.get(Calendar.MONTH);
    }

    /**
     * 返回年eg：2013
     *
     * @return
     */
    public static int getThisYear() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        return ca.get(Calendar.YEAR);
    }

    /**
     * 格式化年月，返回 eg：2012年10月
     *
     * @param yyyymm
     * @return
     */
    public static String formatYearMonth(String yyyymm) {
        String format = "";
        try {
            String year = yyyymm.substring(0, 4) + "年";
            String month = String.valueOf(Integer.parseInt(yyyymm.substring(4,
                    yyyymm.length()))) + "月";
            format = year + month;
        } catch (Exception e) {
            e.printStackTrace();
            return yyyymm;
        }
        return format;
    }

    /**
     * 月份加0
     *
     * @param monthIn
     * @return
     */
    public static String monthFormat(String monthIn) {
        if (StringUtils.isEmpty(monthIn)) {
            return "01";
        } else {
            if (monthIn.length() == 1) {
                return "0" + monthIn;
            } else {
                return monthIn;
            }
        }
    }

    /**
     * 获取某年某月的天数
     *
     * @param yyyyMM
     * @return
     * @throws ParseException
     */
    public static int getDayCountforMoneth(String yyyyMM) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date = sdf.parse(yyyyMM);
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            int days = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
            return days;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 28;
    }

    /**
     * 返回日期的天
     *
     * @param yyyyMMdd
     * @return
     */
    public static String getDayForDate(String yyyyMMdd) {
        if (!StringUtils.isEmpty(yyyyMMdd) && yyyyMMdd.length() == 8) {
            return Integer.parseInt(yyyyMMdd.substring(yyyyMMdd.length() - 2,
                    yyyyMMdd.length())) + "";
        }
        return "";
    }

    /**
     * 返回日期中的天数
     *
     * @param yyyyMM
     * @return
     */
    public static String getYearMonth(String yyyyMM) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date = sdf.parse(yyyyMM.trim());
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            int month = ca.get(Calendar.MONTH) + 1;
            return String.valueOf(month);
        } catch (Exception e) {
        }
        return yyyyMM;
    }

    /**
     * 在输入的日期上加deltaDate天，返回之后的Date
     *
     * @param date
     * @param deltaDate
     * @return
     */
    public static Date addDayOnDate(Date date, int deltaDate) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DATE, (ca.get(Calendar.DATE) + deltaDate));
        return ca.getTime();
    }

    /**
     * 判断起飞事件是否在某个事件段,输入格式 2012-10-04 12:11,5:00-12:00
     *
     * @param inputs
     * @param time_betwwen
     * @return
     */
    public static boolean isTimeBetwwen(String inputs, String time_betwwen) {
        if (!time_betwwen.contains("-")) {
            return true;
        } else {
            String[] times = time_betwwen.split("-");
            int startHour = Integer.parseInt(times[0].split(":")[0]);
            int endHour = Integer.parseInt(times[1].split(":")[0]);
            try {
                Date d = Constant.SDFD5.parse(inputs);
                Calendar ca = Calendar.getInstance();
                ca.setTime(d);
                int endMinus = ca.get(Calendar.MINUTE);

                int departHour = ca.get(Calendar.HOUR_OF_DAY);
                if (departHour >= startHour /* && departHour <= endHour */) {
                    if (departHour < endHour) {// 包含整点
                        return true;
                    } else if ((departHour == endHour) && (endMinus == 0)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 获取时间的字符串 yyyy-MM-dd HH:mm
     *
     * @param inputs
     * @return
     * @throws ParseException
     */
    public static Date getDateFormStr(String inputs) throws ParseException {
        Date d = Constant.SDFD5.parse(inputs);
        return d;
    }

    /**
     * 将分钟变成小时
     *
     * @param inputMinues
     * @return
     */
    public static String formatMinutes(int inputMinues) {
        String time = "";
        int hour = inputMinues / 60;
        if (hour != 0) {
            time += hour + "小时";
        }
        if (inputMinues % 60 != 0) {
            time += String.valueOf(inputMinues % 60) + "分钟";
        }
        return time;
    }

    /**
     * 返回时间字符串小时分钟 10:44,仅限于yyyy-MM-dd HH:mm格式
     *
     * @param inputs
     * @return
     */
    public static String getTimeHourAndMinute(String inputs) {
        String returnStr = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date time = sdf.parse(inputs);
            Calendar ca = Calendar.getInstance();
            ca.setTime(time);
            int hour = ca.get(Calendar.HOUR_OF_DAY);
            int minute = ca.get(Calendar.MINUTE);
            returnStr = String.valueOf(formatNumber(hour) + ":"
                    + formatNumber(minute));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStr;
    }

    public static String formatNumber(int inputs) {
        DecimalFormat dcf = new DecimalFormat("00");
        String ret = dcf.format(inputs);
        return ret;
    }

    /**
     * 获取月和日
     *
     * @param d
     * @return
     */
    public static String getDateMonthAndDay(Date d) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DATE);
        String m = String.valueOf((month + 1) + "月" + day);
        return m;
    }

    /**
     * 返回 对应日期所在星期，比如2011/11/15返回"四"
     *
     * @param d
     * @return
     */
    public static String getDateInWeekDay(Date d) {
        String weekday[] = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        int dweek = ca.get(Calendar.DAY_OF_WEEK);
        return weekday[dweek - 1];
    }

    /**
     * 获取小时和分钟仅仅限于 "yyyy-MM-dd HH:mm";格式
     *
     * @param input
     * @return
     */
    public static String parseDateForHAndM(String input) {
        String result = "";
        try {
            String pattern = "yyyy-MM-dd HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date d = sdf.parse(input);
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            result = sdf2.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 把201210变成2012年10月
    public static String spliteYearMonth4Number(String inputyyyyMM) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(inputyyyyMM.substring(0, 4) + "年");
        sbf.append(Integer.parseInt(inputyyyyMM.substring(4,
                inputyyyyMM.length()))
                + "月");
        return sbf.toString();
    }

    // 把20120101变成2012年1月日
    public static String spliteYearMonthDay4Number(String inputyyyyMMdd) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(inputyyyyMMdd.substring(0, 4) + "年");
        sbf.append(Integer.parseInt(inputyyyyMMdd.substring(4, 6)) + "月");
        sbf.append(Integer.parseInt(inputyyyyMMdd.substring(6, 8)) + "日");
        return sbf.toString();
    }

    public static String splitMonthDay4Number(String inputyyyyMMdd) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(Integer.parseInt(inputyyyyMMdd.substring(4, 6)) + "月");
        sbf.append(Integer.parseInt(inputyyyyMMdd.substring(6, 8)) + "日");
        return sbf.toString();
    }

    public static String getDayFromDate(String date) {
        int index = date.lastIndexOf("-");
        if (index > 0)
            return date.substring(index + 1);
        return date;
    }

    public static String getDayByDate(Date d) {
        if (null == d) {
            return "";
        }
        String daystr = "";
        int day = d.getDay();
        switch (day) {
            case 0:
                daystr = "星期日";
                break;
            case 1:
                daystr = "星期一";
                break;
            case 2:
                daystr = "星期二";
                break;
            case 3:
                daystr = "星期三";
                break;
            case 4:
                daystr = "星期四";
                break;
            case 5:
                daystr = "星期五";
                break;
            case 6:
                daystr = "星期六";
                break;

            default:
                break;
        }
        return daystr;
    }

    /**
     * 当前日期加一天返回字符串
     */
    public static Date addOneDay2String4Day(Date d) {
        try {
            Calendar ca = Calendar.getInstance();// .setTime(d);
            ca.setTime(d);
            ca.add(Calendar.DATE, 1);
            Date nd = ca.getTime();
            return nd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 当前日期加一天返回字符串
     *
     * @param input
     */
    public static String addOneDay2String(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(input);
            Calendar ca = Calendar.getInstance();// .setTime(d);
            ca.setTime(d);
            ca.add(Calendar.DATE, 1);
            Date nd = ca.getTime();
            input = sdf.format(nd);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
