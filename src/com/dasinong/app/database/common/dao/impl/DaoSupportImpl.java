package com.dasinong.app.database.common.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.dasinong.app.BuildConfig;
import com.dasinong.app.database.common.LocalDataBaseHelper;
import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.utils.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public class DaoSupportImpl<T> implements DaoSupport<T> {

	private static final String TAG = "DaoSupportImpl";
	private LocalDataBaseHelper sqLiteDatabase;
	private String mTableName;
	protected Class<T> mClass;

	public DaoSupportImpl(Context context) {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		mClass = (Class<T>) type.getActualTypeArguments()[0];

		mTableName = mClass.getSimpleName().toLowerCase();

		sqLiteDatabase = new LocalDataBaseHelper(context.getApplicationContext());

	}

	public List<T> query(String sql) {
		List<T> result = new ArrayList<T>();
		Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql.toString(), null);
		convertVo(result, cursor);
		return result;

	}

	@Override
	public List<String> querySingleColumn(String sql) {

		List<String> result = new ArrayList<String>();

		Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				result.add(cursor.getString(0));
			}

			cursor.close();

		}

		return result;
	}

	public List<T> query(String selection, String[] selectionArgs, String orderBy) {

		List<T> result = new ArrayList<>();
		String sql = SQLiteQueryBuilder.buildQueryString(false, mTableName, null, selection, null, null, orderBy, null);
		System.out.println(sql);
		if (BuildConfig.DEBUG) {
			Log.d("SQL", sql);
		}
		Cursor cursor = sqLiteDatabase.getWritableDatabase().query(mTableName, null, selection, selectionArgs, null, null, orderBy);
		convertVo(result, cursor);
		if (cursor != null) {
			cursor.close();
		}

		return result;

	}

	public List<T> query(String selection, String[] selectionArgs) {

		return query(selection, selectionArgs, null);

	}

	private void convertVo(List<T> result, Cursor cursor) {

		if (cursor != null) {
			T item;
			while (cursor.moveToNext()) {
				try {
					item = mClass.newInstance();

					Field[] fields = mClass.getDeclaredFields();
					String fieldName = "";
					String methodName = "";

					String typeName = "";
					int index = -1;

					for (Field field : fields) {
						fieldName = field.getName();
						index = cursor.getColumnIndex(fieldName);
						typeName = field.getType().getSimpleName();
						methodName = getMethodName(typeName);
						if (index >= 0) {
							setFieldValue(cursor, item, methodName, index, field);
						}

					}
					result.add(item);

				} catch (Exception e) {
					Logger.d(TAG, e.toString());

				}

			}

		}
	}

	/**
	 * @param cursor
	 *            cursor
	 * @param item
	 *            instance
	 * @param methodName
	 *            methodName
	 * @param index
	 *            cursor index
	 * @param field
	 *            field
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setFieldValue(Cursor cursor, T item, String methodName, int index, Field field) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Method method = cursor.getClass().getMethod(methodName, int.class);
		Object retValue = method.invoke(cursor, index);
		field.setAccessible(true);
		field.set(item, retValue);
	}

	private String getMethodName(String typeName) {
		return "get" + typeName.substring(0, 1).toUpperCase() + typeName.substring(1);

	}

}
