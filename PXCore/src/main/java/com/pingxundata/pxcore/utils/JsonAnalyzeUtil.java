package com.pingxundata.pxcore.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pingxundata.pxcore.metadata.entity.RequestResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * json解析工具类
 */
public class JsonAnalyzeUtil {

    public static JSONObject toJSONObject(String source) {
        if (ObjectHelper.isNotEmpty(source)) {
            Log.v("toJSONObject======",source);
            return JSONObject.parseObject(source);
        } else {
            return null;
        }
    }

    /**
     * 将返回jsonstr转换为集合
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static RequestResult jsonStrToArray(String jsonStr, Class<?> clazz) {
        try{
            if (ObjectHelper.isNotEmpty(jsonStr) && ObjectHelper.isNotEmpty(clazz)) {
                RequestResult requestResult = new RequestResult();
                JSONObject sourceJsonObject = toJSONObject(jsonStr);
                if (ObjectHelper.isNotEmpty(sourceJsonObject.getBoolean("success"))) {
                    requestResult.setSuccess(sourceJsonObject.getBoolean("success"));
                }
                if (ObjectHelper.isNotEmpty(sourceJsonObject.getString("code"))) {
                    requestResult.setCode(sourceJsonObject.getString("code"));
                }
                if (ObjectHelper.isNotEmpty(sourceJsonObject.getString("message"))) {
                    requestResult.setMessage(sourceJsonObject.getString("message"));
                }
                try {
                    if (ObjectHelper.isNotEmpty(sourceJsonObject.getJSONArray("data"))) {
                        try {
                            requestResult.setResultList(parseJsonToBeanList(sourceJsonObject.getJSONArray("data").toJSONString(), clazz));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("将json字符串转换为集合出错", e.getMessage());
                        }
                    }
                } catch (ClassCastException e) {
                    //如果为此结构则为集合类型
                    if (ObjectHelper.isNotEmpty(sourceJsonObject.get("data"))) {
                        try {
                            JSONObject jsonObject = sourceJsonObject.getJSONObject("data");
                            if(jsonObject.containsKey("content")){
                                if (ObjectHelper.isNotEmpty(jsonObject.getJSONArray("content"))) {
                                    try {
                                        requestResult.setResultList(parseJsonToBeanList(jsonObject.getJSONArray("content").toJSONString(), clazz));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        Log.e("将json字符串转换为集合出错", e1.getMessage());
                                    }
                                    if (ObjectHelper.isNotEmpty(jsonObject.getIntValue("totalElements"))) {
                                        requestResult.setTotalElements(jsonObject.getIntValue("totalElements"));
                                    }
                                    if (ObjectHelper.isNotEmpty(jsonObject.getIntValue("totalPages"))) {
                                        requestResult.setTotalPages(jsonObject.getIntValue("totalPages"));
                                    }
                                    if (ObjectHelper.isNotEmpty(jsonObject.getIntValue("number"))) {
                                        requestResult.setNumber(jsonObject.getIntValue("number"));
                                    }
                                    if (ObjectHelper.isNotEmpty(jsonObject.getIntValue("size"))) {
                                        requestResult.setSize(jsonObject.getIntValue("size"));
                                    }
                                }
                            }else{
                                //此则为实体类型
                                Object jsonObject1 = JSONObject.parseObject(sourceJsonObject.getJSONObject("data").toJSONString(), clazz);
                                requestResult.setEntityResult(jsonObject1);
                            }
                        } catch (ClassCastException e1) {
                            try{
                                //此则为实体类型
                                Object jsonObject1 = JSONObject.parseObject(sourceJsonObject.getJSONObject("data").toJSONString(), clazz);
                                requestResult.setEntityResult(jsonObject1);
                            }catch (ClassCastException e2){
                                try{
                                    //此则为实体类型
                                    Object jsonObject1=sourceJsonObject.get("data");
                                    requestResult.setEntityResult(jsonObject1);
                                }catch (ClassCastException e3){
                                    RequestResult requestResult1=new RequestResult();
                                    requestResult1.setMessage(e3.getMessage());
                                    return requestResult1;
                                }
                            }
                        }
                    }
                }
                return requestResult;
            } else {
                return new RequestResult();
            }
        }catch (Exception e){
            return new RequestResult();
        }
    }

    /**
     * 将jsonArray格式数据转换为集合
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseJsonToBeanList(String jsonStr, Class<T> clazz) throws Exception {
        Collection collection = JSONArray.parseArray(jsonStr, clazz);
        List<T> userList = new ArrayList<T>();
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            T user = (T) it.next();
            userList.add(user);
        }
        return userList;
    }
}
