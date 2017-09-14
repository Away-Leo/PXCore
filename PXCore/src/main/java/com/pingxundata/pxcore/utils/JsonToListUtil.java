package com.pingxundata.pxcore.utils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class JsonToListUtil {
    /**
     * jsonArray 转换成 javaBean list
     *
     * @param jsonStr json格式的String数据
     * @param clazz 需要转成的bean的.class对象
     * @param <T> 转化成的bean类型
     * @return 集合
     * @throws Exception
     */
    public static <T> List<T> parseJsonToBeanList(String jsonStr, Class<T> clazz) throws Exception
    {
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
