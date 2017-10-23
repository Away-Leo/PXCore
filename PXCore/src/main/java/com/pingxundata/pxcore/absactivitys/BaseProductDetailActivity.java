package com.pingxundata.pxcore.absactivitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.utils.ActivityUtil;
import com.pingxundata.pxcore.utils.ObjectHelper;

/**
* @Title: BaseProductDetailActivity.java
* @Description: 产品详情基础activity
* @author Away
* @date 2017/10/20 14:18
* @copyright 重庆平讯数据
* @version V1.0
*/
public abstract class BaseProductDetailActivity extends AppCompatActivity {

    /**
     * webview回调信号值
     */
    public static final int WEBVIEW_RESULT=2005;

    public String productName;

    public String productId;

    public String appName;

    public String channelNo;

    public String applyArea;

    int backImg;

    int titleColor;

    int topBack;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ObjectHelper.isNotEmpty(requestCode)&&requestCode==WEBVIEW_RESULT){
            //用Bundle携带数据
            Bundle bundleJump = new Bundle();
            //传递name参数为tinyphp
            bundleJump.putString("productId", productId);
            bundleJump.putString("productName", productName);
            bundleJump.putString("appName", appName);
            bundleJump.putString("channelNo",channelNo);
            bundleJump.putString("applyArea",applyArea );
            bundleJump.putInt("backImg",this.backImg);
            bundleJump.putInt("titleColor",this.titleColor);
            bundleJump.putInt("topBack",this.topBack);
            ActivityUtil.goForward(this,PXRecommendActivity.class,bundleJump,false);
        }
    }

    /**
     * @Author: Away
     * @Description: 启动webview（通过推荐参数值决定是否激活推荐功能）
     * @Param: recommendCd
     * @Param: dataBundle
     * @Return void
     * @Date 2017/10/20 15:11
     * @Copyright 重庆平讯数据
     */
    public void startWebForRecommend(int recommendCd,Bundle dataBundle,int backImg,int titleColor,int topBack){
        this.productName = dataBundle.getString("productName");
        this.productId = dataBundle.getString("productId");
        this.appName=dataBundle.getString("appName");
        this.backImg=backImg;
        this.titleColor=titleColor;
        this.topBack=topBack;
        dataBundle.putInt("backImg",this.backImg);
        dataBundle.putInt("titleColor",this.titleColor);
        dataBundle.putInt("topBack",this.topBack);
        //如果开关为推荐
        if(ObjectHelper.isNotEmpty(recommendCd)&&recommendCd==0){
            ActivityUtil.goForward(this, PXSimpleWebViewActivity.class, WEBVIEW_RESULT, dataBundle);
        }else{//一般流程
            ActivityUtil.goForward(this,PXSimpleWebViewActivity.class,dataBundle,true);
        }
    }

}
