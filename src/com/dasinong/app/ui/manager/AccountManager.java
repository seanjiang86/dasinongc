package com.dasinong.app.ui.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dasinong.app.entity.LoginRegEntity;
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
		if (!TextUtils.isEmpty(getAuthToken(context))) {
			result = true;
		}
		return result;
	}
	
	public static void saveAccount(Context context, LoginRegEntity entity) {
		SharedPreferencesHelper.setString(context, Field.USER_NAME, entity.getData().getUserName());
		SharedPreferencesHelper.setString(context, Field.USER_ID, entity.getData().getUserId());
		SharedPreferencesHelper.setString(context, Field.USER_PHONE, entity.getData().getCellPhone());
		SharedPreferencesHelper.setString(context, Field.USER_ADDRESS, entity.getData().getAddress());
		SharedPreferencesHelper.setString(context, Field.QQ_TOKEN, entity.getData().getQqtoken());
		SharedPreferencesHelper.setString(context, Field.WEIXIN_TOKEN, entity.getData().getWeixintoken());
		SharedPreferencesHelper.setString(context, Field.REFCODE, entity.getData().getRefcode());
		SharedPreferencesHelper.setInt(context, Field.REFUID, entity.getData().getRefuid());
		SharedPreferencesHelper.setInt(context, Field.INSTITUTIONID, entity.getData().getInstitutionId());
		SharedPreferencesHelper.setString(context, Field.CHANNEL, entity.getData().getChannel());
		SharedPreferencesHelper.setString(context, Field.USER_AUTH_TOKEN, entity.getAccessToken());
		
		SharedPreferencesHelper.setArrayString(context, Field.USER_FIELDS, entity.getData().getFields());
		SharedPreferencesHelper.setArrayString(context, Field.MONITOR_LOCATION_ID, entity.getData().getMonitorLocationId());
		
	
	}
	
	public static String getAuthToken(Context context){
		return SharedPreferencesHelper.getString(context, Field.USER_AUTH_TOKEN, "");
	}
	
	public static void setAuthToken(Context context, String authToken) {
		SharedPreferencesHelper.setString(context, Field.USER_AUTH_TOKEN, authToken);
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
		SharedPreferencesHelper.setInt(context, Field.INSTITUTIONID, 0);
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
