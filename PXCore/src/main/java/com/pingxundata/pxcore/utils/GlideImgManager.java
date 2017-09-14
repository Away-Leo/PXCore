package com.pingxundata.pxcore.utils;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by LH on 2016/6/22.
 * Glide操作工具类
 */
public class GlideImgManager {

    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }
    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag 0或者1
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            //圆形图片
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            //圆角图片，10 是设置圆角度
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context,10)).into(iv);
        }else if (2==tag){
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context,20)).into(iv);
        }else if(tag==3){
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
        }
    }
}
