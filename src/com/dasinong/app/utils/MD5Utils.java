package com.dasinong.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liuningning on 15/6/13.
 */
public class MD5Utils {


    /**
     * 对字符串进行md5加密
     *
     * @param plainText
     *            要加密的字符串
     * @return 加密后的密文
     */
    public static String md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte b[] = md.digest(plainText.getBytes());
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                int i = (b[offset] & 0xFF) | 0x100;
                buf.append(Integer.toHexString(i).substring(1));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
