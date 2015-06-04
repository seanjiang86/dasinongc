package com.dasinong.app.database.common.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dasinong.app.database.LocalDataBaseHelper;
import com.dasinong.app.database.common.dao.DaoSupport;


import java.lang.reflect.Field;
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


    @Override
    public List<T> query() {

        return query(null, null);
    }


    public List<T> query(String sql) {
        List<T> result = new ArrayList<T>();
        Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql.toString(), null);
        convertVo(result, cursor);
        return result;

    }

    public List<T> query(String[] selection, String[] selectionArgs) {

        List<T> result = new ArrayList<T>();
        StringBuffer sql = new StringBuffer();
        buildSQL(selection, selectionArgs, sql);
        Log.d("TAG","sql:"+sql.toString());
        if(selection==null||selectionArgs==null||selection.length==0){
            selectionArgs = null;
        }else {
            for(int i =0;i<selectionArgs.length;i++) {
                Log.d("TAG", selectionArgs[i]);
            }
        }
        Cursor cursor = sqLiteDatabase.getWritableDatabase().rawQuery(sql.toString(), selectionArgs);

        convertVo(result, cursor);


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
            T item =null;
            while (cursor.moveToNext()) {
                try {
                    item = mClass.newInstance();
                    Field[] fields = mClass.getDeclaredFields();
                    String fieldName = "";
                    String methodName = "";
                    String fieldValue = "";
                    String typeName = "";
                    int index = -1;

                    for (Field field : fields) {

                        fieldName = field.getName();
                        index = cursor.getColumnIndex(fieldName);
                        // /先构造出方法的名字
                        typeName = field.getType().getSimpleName();
                        // /int --> Int,doble--->Double
                        methodName = "get" + typeName.substring(0, 1).toUpperCase() + typeName.substring(1);

                        if (index >= 0) {
                            Method method = cursor.getClass().getMethod(methodName, int.class);

                            Object retValue = method.invoke(cursor, index);
                            field.setAccessible(true);
                            field.set(item, retValue);

                        }

                    }
                    result.add(item);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            cursor.close();

        }
    }

}
