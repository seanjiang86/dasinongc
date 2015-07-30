package com.dasinong.app.database.common;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by liuningning on 15/6/3.
 */
public class LocalDataBaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "dns.db";
    private static final int DATABASE_VERSION = 2;

    public LocalDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

}
