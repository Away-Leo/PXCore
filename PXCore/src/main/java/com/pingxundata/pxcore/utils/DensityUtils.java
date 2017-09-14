package com.pingxundata.pxcore.utils;

import android.content.Context;
import android.util.TypedValue;


/**
 * 常用单位转换的辅助类
 */

public class DensityUtils {


    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
    /**
     * 设备像素(dip,dp)转屏幕像素(px)
     * px就是像素，如果用px,就会用实际像素画，比个如吧，用画一条长度为240px的横线，在480宽的模拟器上看就是一半的屏宽，而在320宽的模拟器上看就是2／3的屏宽了。
     　　  * 而dip，就是把屏幕的高分成480分，宽分成320分。比如你做一条160dip的横线，无论你在320还480的模拟器上，都是一半屏的长度。
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
