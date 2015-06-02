package com.dasinong.app.database.disaster.service;

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
     * 根据类型得到灾害
     * @param type
     * @return
     */
    public List<PetDisspec> getDisease(String type){

        return mPetDisspecDao.queryByType(type);
    }


    /**
     * 得到自然灾害
     *
     * @return
     */
    public List<NatDisspec> getNatDisease(){

        return mNatDisspecDao.queryAllNatDispec();
    }

    /**
     * 病虫草的id所有的治疗方案
     * @return
     */
    public  List<PetSolu> getCureSolution(int petDisSpecId ){

        return mPetSolutionDao.QueryIsCure(petDisSpecId);
    }


    /**
     * 病虫草的id所有的治疗方案
     * @return
     */
    public  List<PetSolu> getPreventSolution(int petDisSpecId ){

        return mPetSolutionDao.QueryIsPrevent(petDisSpecId);
    }

    /**
     * 根据防治方案的id所有的相关的药
     * @param petSoluId 防治方案的id
     * @return
     */
    public List<CPProduct> getAllDrug(int petSoluId){

        return mCpproductDao.queryAll(petSoluId);
    }

}
