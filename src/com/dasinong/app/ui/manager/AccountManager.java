package com.dasinong.app.ui.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dasinong.app.entity.User;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;


/**
 * @ClassName AccountManager
 * @author linmu
 * @Decription 账户管理
 */
public class AccountManager {

	public static final String CHECK_LOGIN = "check_login";
	
	/**
	 * isLogin
	 * @Description 是否登录
	 * @return boolean
	 */
	public static final boolean isLogin(Context context) {
		boolean result = false;
		if (!TextUtils.isEmpty(getUserId(context))) {
			result = true;
		}
		return result;
	}
	
	public static void saveAccount(Context context, User user) {
		SharedPreferencesHelper.setString(context, Field.USER_NAME, user.getUserName());
		SharedPreferencesHelper.setString(context, Field.USER_ID, user.getUserId());
		SharedPreferencesHelper.setString(context, Field.USER_PHONE, user.getCellPhone());
		SharedPreferencesHelper.setString(context, Field.USER_ADDRESS, user.getAddress());
		SharedPreferencesHelper.setString(context, Field.QQ_TOKEN, user.getQqtoken());
		SharedPreferencesHelper.setString(context, Field.WEIXIN_TOKEN, user.getWeixintoken());
		SharedPreferencesHelper.setString(context, Field.REFCODE, user.getRefcode());
		SharedPreferencesHelper.setInt(context, Field.REFUID, user.getRefuid());
		
		SharedPreferencesHelper.setString(context, Field.CHANNEL, user.getChannel());
		
		SharedPreferencesHelper.setArrayString(context, Field.USER_FIELDS, user.getFields());
		SharedPreferencesHelper.setArrayString(context, Field.MONITOR_LOCATION_ID, user.getMonitorLocationId());
	}
	
	public static String getAuthToken(Context context){
		return SharedPreferencesHelper.getString(context, Field.USER_AUTH_TOKEN, "");
	}
	
	public static String getUserId(Context context){
		return SharedPreferencesHelper.getString(context, Field.USER_ID, "");
	}
	
	public static String[] getUserFields(Context context){
		return SharedPreferencesHelper.getArrayString(context, Field.USER_FIELDS, null);
	}
	
	public static String[] getLocations(Context context){
		return SharedPreferencesHelper.getArrayString(context, Field.MONITOR_LOCATION_ID, null);
	}
	
	public static String getQQToken(Context context){
		return SharedPreferencesHelper.getString(context, Field.QQ_TOKEN, "");
	}
	
	public static String getWXToken(Context context){
		return SharedPreferencesHelper.getString(context, Field.WEIXIN_TOKEN, "");
	}
	
	public static void logout(Context context){
		SharedPreferencesHelper.setString(context, Field.USER_NAME, "");
		SharedPreferencesHelper.setString(context, Field.USER_ID, "");
		SharedPreferencesHelper.setString(context, Field.USER_PHONE, "");
		SharedPreferencesHelper.setString(context, Field.USER_ADDRESS, "");
		SharedPreferencesHelper.setString(context, Field.USER_AUTH_TOKEN, "");
		SharedPreferencesHelper.setLong(context, Field.FIELDID, -1);
		SharedPreferencesHelper.setString(context, Field.REFCODE, "");
		SharedPreferencesHelper.setString(context, Field.QQ_TOKEN, "");
		SharedPreferencesHelper.setString(context, Field.WEIXIN_TOKEN, "");
		SharedPreferencesHelper.setInt(context, Field.REFUID, -1);
		SharedPreferencesHelper.setString(context, Field.CHANNEL, "");
	}
	
	public static boolean checkLogin(Context context){
		if(isLogin(context)){
			return true;
		}else{
			try {
				Intent intent = new Intent(context ,RegisterPhoneActivity.class);
				context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}

}
