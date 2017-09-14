package com.pingxundata.pxcore.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;

/**
* @Title: PXWebView.java
* @Description: 平讯数据WebView控件
* @author Away
* @date 2017/9/12 14:49
* @copyright 重庆平讯数据
* @version V1.0
*/
public class PXWebView extends BaseWebView {


    public PXWebView(Context context) {
        super(context);
    }

    public PXWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
