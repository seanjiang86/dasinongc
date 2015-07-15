package com.dasinong.app.database.disaster.dao;

import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.disaster.domain.PetDisspec;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public interface PetDisspecDao extends DaoSupport<PetDisspec> {

    /**
     * 根据类型得所有的数据
     * @param typeValue 类型的值
     * @return 根据类型得所有的数据
     */
    public  List<PetDisspec> queryDisasterByType(String typeValue);
    
    /**
     * 希遥
     * @param value
     * @return
     */
    public  List<PetDisspec> queryDisasterByTypeandCropName(String[] value);

    /**
     * 根据id得所有的数据
     * @param typeValue 类型的值
     * @return 根据类型得所有的数据
     */
    public  PetDisspec queryDisasterById(int typeValue);
    
    
}
