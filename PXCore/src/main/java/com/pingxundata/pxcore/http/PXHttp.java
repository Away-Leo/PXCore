package com.pingxundata.pxcore.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pingxundata.pxcore.metadata.entity.RequestResult;
import com.pingxundata.pxcore.utils.JsonAnalyzeUtil;

import org.json.JSONObject;

import java.util.Map;

/**
* @Title: G_api.java
* @Description: 网络请求统一管理类(单例模式)
* @author Away
* @date 2017/10/20 10:46
* @copyright 重庆平讯数据
* @version V1.0
*/
public class PXHttp {
    private static PXHttp mInstance;

    private PXHttp() {
    }
    private OnResultHandler handler;

    public static PXHttp getInstance() {
        if (mInstance == null) {
            mInstance = new PXHttp();
        }
        return mInstance;
    }

    public interface OnResultHandler {
        void onResult(RequestResult requestResult,String jsonStr, int flag);
        void onError(int flag);
    }

    public PXHttp setHandleInterface(OnResultHandler handler) {
        this.handler = handler;
        return mInstance;
    }


    /**
     * 通用get请求 不带Dialog
     *
     * @param urlStr URL
     * @param map    参数
     * @param flag   标识
     */
    public void getJson(final String urlStr, Map<String, String> map, final int flag,final Class<?> clazz) {
        OkGo.<String>get(urlStr)
                .tag(this)
                .params(map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (handler != null) {
                            handler.onResult(JsonAnalyzeUtil.jsonStrToArray(response.body(),clazz),response.body(), flag);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (handler != null)
                            handler.onError(flag);
                    }
                });

    }




    /**
     * 通用的上传Json字符串post方法 不带Dialog
     * @param urlStr     URL
     * @param jsonObject jsonObject
     * @param flag       标识
     */
    public void upJson(final String urlStr, JSONObject jsonObject, final int flag,final Class<?> clazz) {
        OkGo.<String>post(urlStr)
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (handler != null) {
                            handler.onResult(JsonAnalyzeUtil.jsonStrToArray(response.body(),clazz),response.body(), flag);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (handler != null)
                            handler.onError(flag);
                    }
                });

    }






//    /**
//     * 通用get请求 带Dialog
//     *
//     * @param urlStr   URL
//     * @param activity activity
//     * @param map      参数
//     * @param flag     标识
//     */
//    public void getRequest(final String urlStr, BaseActivity activity, Map<String, String> map, final int flag) {
//        OkGo.<String>get(urlStr)
//                .tag(this)
//                .params(map)
//                .execute(new StringDialogCallback(activity) {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        if (handler != null) {
//                            handler.onResult(response.body(), flag);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        if (handler != null)
//                            handler.onError(flag);
//                    }
//                });
//    }




//    /**
//     * 通用的上传Json字符串post方法 带Dialog
//     *
//     * @param urlStr     URL
//     * @param activity   activity
//     * @param jsonObject jsonObject
//     * @param flag       标识
//     */
//    public void upJson(final String urlStr,JSONObject jsonObject, final int flag) {
//        OkGo.<String>post(urlStr)
//                .tag(this)
//                .upJson(jsonObject)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        if (handler != null) {
//                            handler.onResult(response.body(), flag);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        if (handler != null)
//                            handler.onError(flag);
//                    }
//                });
//
//
//    }




//    private OnResultHandlerJson handler2;
//
//    public interface OnResultHandlerJson {
//        void onResult(Response<BaseBean<List<ServerModel>>> jsonObject, int flag);
//
//        void onError(int flag);
//    }
//
//    public G_api setHandleInterface2(OnResultHandlerJson handler2) {
//        this.handler2 = handler2;
//        return mInstance;
//    }
//
//    public void getJsonRequest(final String urlStr, Map<String, String> map, final int flag) {
//        OkGo.<BaseBean<List<ServerModel>>>get(urlStr)
//                .tag(this)
//                .params(map)
//                .execute(new JsonCallback<BaseBean<List<ServerModel>>>() {
//
//                    @Override
//                    public void onSuccess(Response<BaseBean<List<ServerModel>>> response) {
//                        if (handler2 != null) {
//                            handler2.onResult(response, flag);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<BaseBean<List<ServerModel>>> response) {
//                        super.onError(response);
//                    }
//                });
//
//    }

}
