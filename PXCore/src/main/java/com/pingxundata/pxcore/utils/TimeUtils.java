package com.pingxundata.pxcore.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 与时间相关的工具类



 */
public class TimeUtils {

    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getTodayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }


    /*
     * 将时间转换为时间戳

     */
    public static Long dateToStamp(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long ts = date.getTime();
        return ts;
    }
    /*
    获取时间差

     */
    public static String getTimesToNow(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = format.format(new Date());
        String returnText = null;
        try {
            long from = format.parse(date).getTime();
            long to = format.parse(now).getTime();
            int days = (int) ((to - from)/(1000 * 60 * 60 * 24));
            if(days == 0){//一天以内，以分钟或者小时显示

                int hours = (int) ((to - from)/(1000 * 60 * 60));
                if(hours == 0){
                    int minutes = (int) ((to - from)/(1000 * 60));
                    if(minutes == 0){
                        returnText = "刚刚";
                    }else{
                        returnText = minutes + "分钟前";
                    }
                }else{
                    returnText = hours + "小时前";
                }
            } else if(days == 1){
                returnText = "昨天";
            }else{
                returnText = days + "天前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnText;
    }


    public static String showTimeCount(long time) {
        if (time >= 360000000) {
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time / 3600000;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = hour + ":" + minue + ":" + sec;
        return timeCount;
    }
    /**
     * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getTodayDateTimes() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日",
                Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime_Today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(new Date());
    }

//    public static String getHMS() {
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("HHmmss");
//        Date date = new Date(System.currentTimeMillis());
//        String time = sDateFormat.format(date);
//        return time;
//    }
//
//    public static String getTime(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        return format.format(date);
//    }
//
//
//
    /**
     * 将时间戳转化为时间精确到秒



     */
    public static String conversionStrictTime(String currentTime) {
        if (currentTime==null||currentTime.equals("null")||currentTime.equals(null)){
            return "";}
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
        long lcc = Long.valueOf(currentTime);
        int i = Integer.parseInt(currentTime);
        String times = sDateFormat.format(new Date(i * 1000L));
        return times;
    }
//    /**
//     * 将时间戳转换为时间



//     *
//     * @param currentTime
//     * @return
//     */
//    public static String conversionTime(String currentTime) {
//        if (currentTime==null||currentTime.equals("null")||currentTime.equals(null)){
//            return "";
//        }
//            SimpleDateFormat sDateFormat = new SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss E");
//            Date date = new Date(Long.parseLong(currentTime));
//            String time = sDateFormat.format(date);
//            return time;
//        }
//
//    /**
//     * 将字符串转化为时间戳
//     * @param time
//     * @return
//     */
//    public static long conversionStringToData(String time){
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
//        Date date = new Date();
//        try {
//            date = sDateFormat.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date.getTime();
//    }


}
