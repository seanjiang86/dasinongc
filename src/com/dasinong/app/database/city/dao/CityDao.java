package com.dasinong.app.database.city.dao;

import android.content.Context;

import com.dasinong.app.database.city.domain.City;
import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public class CityDao extends DaoSupportImpl<City> {
    public CityDao(Context context) {
        super(context);
    }

    /**
     *
     * @return 查询得到所的城市
     */
    public List<City> queryAllCity() {

        return query(null, null);
    }
}
