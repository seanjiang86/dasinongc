package com.dasinong.app.ui.manager;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class SharedPreferencesHelper {
	public final static String TAG = "SettingHelper";
    private final static String DATA_NAME = "dasinong_data";
    private final static String SPLITCHAR = ",";

    public enum Field {
    	USER_NAME,
    	USER_ID,
    	USER_AUTH_TOKEN,
    	USER_ADDRESS,
    	USER_PHONE,
//    	SESSIONID,
    	VILLAGE_ID,
    	LATITUDE,
    	LONGITUDE,
    	FIELD_SIZE,
    	VARIETY_ID,
    	SUBSTAGE_ID,
    };
    
    public static int getInt(Context context, Field field, int defaultValue) {
        return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getInt(field.name(), defaultValue);
    }

    public static void setInt(Context context, Field field, int value) {
        Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(field.name(), value);
        editor.commit();
    }

    public static String getString(Context context, Field field, String defaultValue) {
        return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(field.name(), defaultValue);
    }

    public static void setString(Context context, Field field, String value) {
        Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(field.name(), value);
        editor.commit();
    }
    public static String getString(Context context, String field, String defaultValue) {
    	return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(field, defaultValue);
    }
    
    public static void setString(Context context, String field, String value) {
    	Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
    	editor.putString(field, value);
    	editor.commit();
    }

    public static long getLong(Context context, Field field, long defaultValue) {
        return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getLong(field.name(), defaultValue);
    }

    public static void setLong(Context context, Field field, long value) {
        Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(field.name(), value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, Field field, boolean defaultValue) {
        return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getBoolean(field.name(), defaultValue);
    }

    public static void setBoolean(Context context, Field field, boolean value) {
        Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(field.name(), value);
        editor.commit();
    }
    
    public static void setArraylong(Context context, Field field, ArrayList<Long> arrayLong) {
        if (arrayLong != null && arrayLong.size() > 0) {
            String str = "";
            for (int index = 0, count = arrayLong.size(); index < count; ++index) {
                str = str + arrayLong.get(index).toString() + SPLITCHAR;
            }
            Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(field.name(), str);
            editor.commit();
        }
    }
    
    public static void removeKey(Context context,Field field){
    	Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
    	editor.remove(field.name());
    	editor.commit();
    }

    public static ArrayList<Long> getArrayLong(Context context, Field field, String defaultValue) {
        ArrayList<Long> arrayLong = new ArrayList<Long>();
        String str = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(field.name(), defaultValue);
        if (str != null) {
            String[] ids = str.split(SPLITCHAR);
            for (int index = 0; index < ids.length; ++index) {
                long id = Long.parseLong(ids[index]);
                arrayLong.add(id);
            }
        }
        return arrayLong;
    }

    public static void setArrayString(Context context, Field field, ArrayList<String> displayList) {
        String str = "";
        Integer num = 0;
        for (int index = 0, count = displayList.size(); index < count; ++index) {
            str = str + displayList.get(index) + SPLITCHAR;
            num++;
        }
        Editor editor = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(field.name(), str);
        editor.commit();
    }

    public static ArrayList<String> getArrayString(Context context, Field field, String defaultValue) {
        ArrayList<String> displayList = new ArrayList<String>();
        String str = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(field.name(), defaultValue);
        if (str != null) {
            String[] ids = str.split(SPLITCHAR);
            for (int index = 0; index < ids.length; index++) {
                if (!ids[index].equals(""))
                    displayList.add(ids[index]);
            }
        }
        return displayList;
    }
    
}
