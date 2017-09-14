package com.pingxundata.pxcore.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 我的工具
 */
public class MyTools {
    /**
     * 将edittext转换为string
     *
     * @param editText
     * @return String
     */


    public static String getEdittextStr(EditText editText) {
        String str = editText.getText().toString();
        if (TextUtils.isEmpty(str)){
            return "";
        }else{
            return str.trim().replace(" ","");
        }
    }

    /**
     * 将TextView转换为string
     *
     * @param textView
     * @return
     */
    public static String getTextViewStr(TextView textView) {
        String str = textView.getText().toString().trim().replaceAll(" ", "");
        return str;
    }
    /**
     * 初始化额度范围
     * @param start
     * @param end
     * @return
     */
    public static  String initTvQuota(Double start, Double end) {
        Double dStart=0.0;
        Double dEnd=0.0;
        String newStr="";
        if (start>=10000.0){
            dStart=start/10000;
            dEnd=end/10000;
            newStr=String.valueOf((dStart).intValue())+"-"+String.valueOf((dEnd).intValue())+"万元";
        }else {
            dStart=start;
            dEnd=end;
            newStr=String.valueOf((dStart).intValue())+"-"+String.valueOf((dEnd).intValue())+"元";
        }
        return newStr;
    }
    /**
     * URI转图片地址
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 将edittext转换为string
     * 并转换为GBK编码过后的String
     *
     * @param editText
     * @return
     */


    public static String getEdittextGBKStr(EditText editText) {
        String str = editText.getText().toString().trim().replaceAll(" ", "");
        String strGBK = null;
        try {
            strGBK = URLEncoder.encode(str, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strGBK;
    }

    /**
     * 将edittext转换为string
     * 并转换为双重GBK编码过后的String
     *
     * @param editText
     * @return
     */
    public static String getEdittextGBK2Str(EditText editText) {
        String str = editText.getText().toString().trim().replaceAll(" ", "");
        String strGBK = null;
        try {
            strGBK = URLEncoder.encode(URLEncoder.encode(str, "GBK"), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strGBK;
    }

    /**
     * 将String转换为双重编码过后的GBKstring
     *
     * @param
     * @return
     */
    public static String getGBK2Str(String str) {
        String strGBK = null;
        try {
            strGBK = URLEncoder.encode(URLEncoder.encode(str, "GBK"), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strGBK;
    }

    public static String getGBKStr(String str) {
        String strGBK = null;
        try {
            strGBK = URLEncoder.encode(str, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strGBK;
    }

    /**
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|17[0-9]|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches() && mobiles.length() == 11;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为中文



     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }


    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
//    public static int dayForWeek(String pTime) throws Exception {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c = Calendar.getInstance();
//        c.setTime(format.parse(pTime));
//        int dayForWeek = 0;
//        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
//            dayForWeek = 7;
//        } else {
//            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
//        }
//        return dayForWeek;
//    }

    /**
     * 打开、关闭输入法（当输入法为打开时，则关闭输入法，当输入法为关闭时，则打开）



     *
     * @param context
     */
    public static void offInput(Context context) {
        InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //计算childe高度获取ListView总的高度,解决嵌套冲突
    public static void setListViewHeightBasedChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams param = listView.getLayoutParams();
        param.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        param.height += 5;//if without this statement,the listview will be a little short
        listView.setLayoutParams(param);
    }

    /**
     * 软键盘自动弹出



     * @param editText
     */
    public static void showSoftInput(final EditText editText){
        try {
            editText.requestFocus();
            editText.setSelection(editText.getText().length());//将光标移至文字末尾



            Timer timer = new Timer();
            timer.schedule(new TimerTask()
                           {
                               public void run()
                               {
                                   InputMethodManager inputManager =
                                           (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(editText, 0);//显示软键盘，控件必须获取到了焦点才起作用
//                                   inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//切换软键盘状态，可以无焦点



                               }
                           },
                    100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * 获取某个控件的高度
     *
     * @param view
     * @return
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();

    }

    /**
     * 默认为空
     *
     * @param msg
     * @return
     */
    public static Boolean isEmpty(String msg) {
        if (!msg.equals("") && !msg.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    public static void removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
    }

    /**
     * rxJava实现计时器功能


     * @param time 传入的时间


     * @return
     */
//    public static Observable<Integer> timer(int time) {
//        if (time < 0) time = 0;
//
//        final int countTime = time;
//        return Observable.interval(0, 1, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Func1<Long, Integer>() {
//                    @Override
//                    public Integer call(Long increaseTime) {
//                        return countTime + increaseTime.intValue();
//                    }
//                })
//                .take(countTime + 1);
//
//    }

}
