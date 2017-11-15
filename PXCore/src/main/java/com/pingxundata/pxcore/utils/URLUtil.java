package com.pingxundata.pxcore.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class URLUtil {

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String truncateUrlPage(String strURL)
    {
        if(TextUtils.isEmpty(strURL)){
            return null;
        }
        String strAllParam=null;
        strURL=strURL.trim().toLowerCase();
        String[] arrSplit = strURL.split("[?]");

        //有参数
        if(arrSplit.length>1)
        {
            if(arrSplit[1]!=null)
            {
                strAllParam=arrSplit[1];
            }
        }

        return strAllParam;
    }
    /**
     * 解析出url参数中的键值对
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> getRequestParamMap(String URL)
    {
        if(TextUtils.isEmpty(URL)){
            return null;
        }

        String strUrlParam=truncateUrlPage(URL);//得到参数
        if(TextUtils.isEmpty(strUrlParam))
        {
            return null;
        }

        Map<String, String> mapRequest = new HashMap<String, String>();
        //每个键值为一组
        String[] arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit)
        {
            String[] arrSplitEqual= strSplit.split("[=]");

            //解析出键值
            if(arrSplitEqual.length>1)
            {
                if(!TextUtils.isEmpty(arrSplitEqual[1]))
                {
                    mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);//正确解析
                }else{
                    mapRequest.put(arrSplitEqual[0], "");//无value
                }
            }
        }
        return mapRequest;
    }
}