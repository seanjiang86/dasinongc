package com.dasinong.app.components.net;

/**
 * Created by Administrator on 2015/6/25.
 */
public class FilterUtils {

    /**
     * "{
     * }"
     * @param src
     * @return
     */
    public static String filter(String src) {
        if (src == null) {
            return null;
        }
        src = src.replaceAll("\"\\{", "{").replaceAll("\\}\"", "}").replaceAll("\\\\","");

        return src;
    }


}
