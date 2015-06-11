package com.dasinong.app.ui.manager;

import com.dasinong.app.entity.AccountEntity;
import com.dasinong.app.entity.User;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;


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
	
	public static void saveAccount(Context context, User user) {
		SharedPreferencesHelper.setString(context, Field.USER_NAME, user.getUserName());
		SharedPreferencesHelper.setString(context, Field.USER_ID, user.getUserId());
		SharedPreferencesHelper.setString(context, Field.USER_PHONE, user.getCellPhone());
		SharedPreferencesHelper.setString(context, Field.USER_ADDRESS, user.getAddress());
		
		//save db | user fields
		
	}
	
	public static String getAuthToken(Context context){
		return SharedPreferencesHelper.getString(context, Field.USER_AUTH_TOKEN, "");
	}
	
	public static void logout(Context context){
		SharedPreferencesHelper.setString(context, Field.USER_NAME, "");
		SharedPreferencesHelper.setString(context, Field.USER_ID, "");
		SharedPreferencesHelper.setString(context, Field.USER_PHONE, "");
		SharedPreferencesHelper.setString(context, Field.USER_ADDRESS, "");
		SharedPreferencesHelper.setString(context, Field.USER_AUTH_TOKEN, "");
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
