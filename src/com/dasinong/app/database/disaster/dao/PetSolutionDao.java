package com.dasinong.app.database.disaster.dao;

import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.disaster.domain.PetSolu;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public interface PetSolutionDao extends DaoSupport<PetSolu> {

    /**
     * 病虫草的id所有的治疗方案
     *
     * @param petDisSpecId 病虫草的id
     * @return
     */
    public List<PetSolu> QuerySolutionIsCure(int petDisSpecId);

    /**
     * 病虫草的id所有的预防方案
     *
     * @param petDisSpecId 病虫草的id
     * @return
     */
    public List<PetSolu> QuerySolutionIsPrevent(int petDisSpecId);
}
