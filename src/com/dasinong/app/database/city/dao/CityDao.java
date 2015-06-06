package com.dasinong.app.database.city.dao;

import android.content.Context;

import com.dasinong.app.database.city.domain.City;
import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public interface CityDao extends DaoSupport {

    /**
     * 查询所有的省
     *
     * @return 所有的省
     */

    public List<String> getProvince();

    /**
     * 根据province 得到所有城市
     *
     * @param province 省
     * @return 根据province 得到所有城市
     */
    public List<String> getCity(String province);


    /**
     * 根据city 得到所有county
     *
     * @param city 省
     * @return 根据province 得到所有城市
     */
    public List<String> getCounty(String city);


    /**
     * 根据county 得到所有district
     *
     * @param county
     * @return
     */

    public List<String> getDistrict(String county);

}

