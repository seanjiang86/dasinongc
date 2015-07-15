package com.dasinong.app.database.disaster.service;

import android.app.Application;
import android.content.Context;

import com.dasinong.app.database.disaster.dao.CPProductDao;
import com.dasinong.app.database.disaster.dao.NatDisspecDao;
import com.dasinong.app.database.disaster.dao.PetSolutionDao;
import com.dasinong.app.database.disaster.dao.PetDisspecDao;
import com.dasinong.app.database.disaster.dao.impl.CPProductDaoImpl;
import com.dasinong.app.database.disaster.dao.impl.NatDisspecDaoImpl;
import com.dasinong.app.database.disaster.dao.impl.PetSolutionDaoImpl;
import com.dasinong.app.database.disaster.dao.impl.PetDisspecDaoImpl;
import com.dasinong.app.database.disaster.domain.CPProduct;
import com.dasinong.app.database.disaster.domain.NatDisspec;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.database.disaster.domain.PetSolu;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 * 病虫草害的数据管理类
 */
public class DisasterManager {

    private CPProductDao mCpproductDao;
    private NatDisspecDao mNatDisspecDao;
    private PetDisspecDao mPetDisspecDao;
    private PetSolutionDao mPetSolutionDao;

    private static DisasterManager instance;
    private DisasterManager(Context context) {

        if(!(context instanceof Application)){
            context = context.getApplicationContext();
        }

        mCpproductDao = new CPProductDaoImpl(context);
        mNatDisspecDao = new NatDisspecDaoImpl(context);
        mPetDisspecDao = new PetDisspecDaoImpl(context);
        mPetSolutionDao = new PetSolutionDaoImpl(context);

    }

    public static  DisasterManager getInstance(Context context) {

        if (instance == null) {
            synchronized (DisasterManager.class) {

                if (instance == null) {
                    instance = new DisasterManager(context);
                }
            }
        }

        return instance;
    }

    /**
     *
     * @param type 类型
     * @return 根据类型得到灾害
     */

    public List<PetDisspec> getDisease(String type){

        return mPetDisspecDao.queryDisasterByType(type);
    }
    
    /**
     * xiyao
     */
    public List<PetDisspec> getDisease(String type,String cropName){

        return mPetDisspecDao.queryDisasterByTypeandCropName(new String[]{type,cropName});
    }

    
    /**
     * 得到自然灾害
     *
     * @return 得到自然灾害
     */
    public List<NatDisspec> getNatDisease(){

        return mNatDisspecDao.queryAllNatDisaster();
    }

    /**
     * @param petDisSpecId 病虫草的id
     *
     * @return 病虫草的id所有的治疗方案
     */
    public  List<PetSolu> getCureSolution(int petDisSpecId ){

        return mPetSolutionDao.QuerySolutionIsCure(petDisSpecId);
    }


    /**
     *病虫草的id所有的治疗方案
     * @param petDisSpecId 病虫草的id
     * @return 病虫草的id所有的治疗方案
     */
    public  List<PetSolu> getPreventSolution(int petDisSpecId ){

        return mPetSolutionDao.QuerySolutionIsPrevent(petDisSpecId);
    }

    /**
     *根据防治方案的id所有的相关的药
     * @param petSoluId 防治方案的id
     * @return 根据防治方案的id所有的相关的药
     */
    public List<CPProduct> getAllDrug(int petSoluId){

        return mCpproductDao.queryAllCpProduct(petSoluId);
    }


    /**
     *
     * @param id id
     * @return 根据id得到灾害
     */

    public PetDisspec getDisease(int id){

        return mPetDisspecDao.queryDisasterById(id);
    }


}
