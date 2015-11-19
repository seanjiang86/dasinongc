package com.dasinong.app.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;
import android.util.Base64;

public class StringHelper {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static boolean isLoginUserName(String name) {
		Pattern p = Pattern.compile("(?!^\\d[A-Za-z0-9_-]*$)^[A-Za-z0-9\u4e00-\u9fa5_-]{2,25}$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

	public static boolean isPassword(String pwd) {
		Pattern p = Pattern.compile("^[A-Za-z0-9\\^\\$\\.\\+\\*_@!#%&~=-]{6,32}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	public static boolean isRegisterUserName(String pwd) {
		Pattern p = Pattern.compile("(?!^\\d[A-Za-z0-9_-]*$)^[A-Za-z0-9_-]{5,20}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	public static boolean isPhoneNumber(String phoneNum) {
		if (!TextUtils.isEmpty(phoneNum) && phoneNum.length() == 11) {
			Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(phoneNum);
			return m.matches();
		} else {
			return false;
		}
	}

	public static String toMD5(String plainText) {
		try {
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md.update(plainText.getBytes());
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			// 生成具体的md5密码到buf数组
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();// 32 buf.toString().substring(8, 24)
			// 16（32位截取）
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean invalidString(Object obj) {
		return obj == null || "".equals(obj.toString()) || "null".equals(obj.toString());
	}

	public static String formatDate(String time) {
		try {
			return format.format(Long.parseLong(time));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
}
