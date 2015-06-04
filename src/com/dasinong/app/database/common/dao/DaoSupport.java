package com.dasinong.app.database.common.dao;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public interface DaoSupport<T> {
    /**
     *
     * @return 返回数据库中所有的数据
     */
    public List<T> query();

    /**
     *
     * @param selection  exclude where
     * @param selectionArgs where value
     * @return 返回数据库中所有符合的数据
     */
    public List<T> query(String[] selection, String[] selectionArgs);

    /**
     *
     * @param sql
     * @return 返回数据库中所有符合的数据
     */
    public List<T> query(String sql);
}
