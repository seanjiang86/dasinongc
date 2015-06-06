package com.dasinong.app.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuningning on 15/6/6.
 */
public class FieldUtils {

    /**
     * field must be public otherwise return empty hash map
     *
     * @param obj domain
     * @return hashMap
     */
    public HashMap<String, String> convertToHashMap(Object obj) {

        HashMap<String, String> map = new HashMap<>(16, 0.75F);

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {



            field.setAccessible(true);
            try {
                String key = field.getName();
                Object value = field.get(obj);
                if (value.getClass() == String.class) {
                    map.put(key, (String) value);
                }else{
                    map.put(key,String.valueOf(value));
                }

            } catch (IllegalAccessException e) {

            } catch (IllegalArgumentException ex) {

            }
        }

        return map;

    }


}
