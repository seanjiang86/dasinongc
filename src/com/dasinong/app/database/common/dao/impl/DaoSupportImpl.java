package com.dasinong.app.database.common.dao.impl;

import android.content.Context;
import android.database.Cursor;

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
        sqLiteDatabase = new LocalDataBaseHelper(context);

    }

    public List<T> query(String sql) {
        List<T> result = new ArrayList<T>();
        Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql.toString(), null);
        convertVo(result, cursor);
        return result;

    }

    @Override
    public List<String> querySingleColumn(String sql) {


            List<String > result = new ArrayList<>();

            Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql,null);

            if (cursor != null) {
                String item ;
                while (cursor.moveToNext()) {
                    result.add(cursor.getString(0));
                }

                cursor.close();

            }


        return result;
    }

    public List<T> query(String[] selection, String[] selectionArgs) {

        List<T> result = new ArrayList<T>();
        StringBuffer sql = new StringBuffer();
        buildSQL(selection, selectionArgs, sql);
        Logger.d(TAG, sql.toString());
        if (selection == null || selectionArgs == null || selection.length == 0) {
            selectionArgs = null;
        }
        Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql.toString(), selectionArgs);

        convertVo(result, cursor);
        if (cursor != null) {
            cursor.close();
        }

        return result;


    }

    private void buildSQL(String[] selection, String[] selectionArgs, StringBuffer sql) {
        sql.append("select * from ").append(mTableName);
        if (selection != null && selectionArgs != null) {
            int selectionLength = selection.length;
            int selectArgsLength = selectionArgs.length;
            if (selectionLength == selectArgsLength && selectArgsLength > 0) {
                sql.append(" where ");
                for (int i = 0; i < selectionLength; i++) {
                    sql.append(" ")
                            .append(selection[i]).append("= ? ");
                    if (i != selectArgsLength - 1) {
                        sql.append(" and ");
                    }

                }

            }

        }
    }

    private void convertVo(List<T> result, Cursor cursor) {

        if (cursor != null) {
            T item ;
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
     * @param cursor     cursor
     * @param item       instance
     * @param methodName methodName
     * @param index      cursor index
     * @param field      field
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void setFieldValue(Cursor cursor, T item, String methodName, int index, Field field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = cursor.getClass().getMethod(methodName, int.class);
        Object retValue = method.invoke(cursor, index);
        field.setAccessible(true);
        field.set(item, retValue);
    }

    private String getMethodName(String typeName) {
        return "get" + typeName.substring(0, 1).toUpperCase() + typeName.substring(1);

    }







}
