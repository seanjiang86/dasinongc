package com.dasinong.app.database.disaster.dao;

import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.disaster.domain.CPProduct;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public interface CPProductDao  extends DaoSupport<CPProduct> {

    /**
     * 根据防治方案的id所有的相关的药
     * @param petSoluId
     * @return
     */
    public List<CPProduct> queryAllCpProduct(int petSoluId);
}
