package com.dasinong.app.database.city.dao.impl;

import android.content.Context;

import com.dasinong.app.database.city.dao.CityDao;

import com.dasinong.app.database.city.domain.City;
import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.utils.Logger;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public class CityDaoImpl extends DaoSupportImpl<City> implements CityDao{

    private static  final  String  TAG="CityDaoImpl";
    public CityDaoImpl(Context context) {
        super(context);
    }

    /**
     * 查询所有的省
     * @return 所有的省
     */

    public List<String> getProvince() {

        StringBuilder sb = new StringBuilder();
        sb.append("select DISTINCT province from ")
                .append("city");
        Logger.d(TAG, sb.toString());
        return querySingleColumn(sb.toString());
    }

    /**
     *根据province 得到所有城市
     * @param province 省
     * @return 根据province 得到所有城市
     */
    public List<String> getCity(String province) {
        StringBuilder sb = new StringBuilder();
        sb.append("select DISTINCT city from  ")
                .append("city ")
                .append("where ")
                .append("province = '")
                .append(province)
                .append("'");
        Logger.d(TAG,sb.toString());
        return querySingleColumn(sb.toString());

    }

    /**
     *根据city 得到所有county
     * @param city 省
     * @return 根据province 得到所有城市
     */
    public List<String> getCounty(String province, String city) {

        StringBuilder sb = new StringBuilder();
        sb.append("select DISTINCT county from ")
                .append("city ")
                .append("where ")
                .append("city = '")
                .append(city)
                .append("'")
                .append(" and")
                .append(" province = '")
                .append(province)
                .append("'");
        Logger.d(TAG,sb.toString());
        return querySingleColumn(sb.toString());

    }

    /**
     * 根据county 得到所有district
     * @param county
     * @return
     */

    public List<String> getDistrict(String province, String city, String county) {


        StringBuilder sb = new StringBuilder();
        sb.append("select DISTINCT district from ")
                .append("city ")
                .append("where ")
                .append("county = '")
                .append(county)
                .append("'")
                .append(" and")
                .append(" province = '")
                .append(province)
                .append("'")
                .append(" and")
                .append(" city = '")
                .append(city)
                .append("'");
        Logger.d(TAG, sb.toString());
        return querySingleColumn(sb.toString());
    }
    
    /**
     * 查找数据库中是否有某个省份
     */
    
    public boolean hasProvince(String province){
    	StringBuilder sb = new StringBuilder();
    	sb.append("select province from city where province is '")
    		.append(province+"'");
    	List<String> list = querySingleColumn(sb.toString());
    	if(list != null && list.size() > 0){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 查找数据库中是否有某个市
     */
    
    public boolean hasCity(String province){
    	StringBuilder sb = new StringBuilder();
    	sb.append("select city from city where city is '")
    		.append(province+"'");
    	List<String> list = querySingleColumn(sb.toString());
    	if(list != null && list.size() > 0){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 查找数据库中是否有某个三级单位
     */
    
    public boolean hasCounty(String province){
    	StringBuilder sb = new StringBuilder();
    	sb.append("select county from city where county is '")
    		.append(province+"'");
    	List<String> list = querySingleColumn(sb.toString());
    	if(list != null && list.size() > 0){
    		return true;
    	}
    	return false;
    }
}
