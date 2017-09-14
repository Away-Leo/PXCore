package com.pingxundata.pxcore.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Map工具类
 *
 * @author jqlin
 */
public class MapUtils {

    /**
     * 将实体类转换成请求参数,以map<k,v>形式返回
     *
     * @return
     */
    public static Map<String, String> getMapParams(Object object) {


        Field[] fields = object.getClass().getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<String, String>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                params.put(field.getName(), String.valueOf(field.get(object)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return params;
    }

}
