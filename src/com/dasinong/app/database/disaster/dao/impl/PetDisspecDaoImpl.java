package com.dasinong.app.database.disaster.dao.impl;

import android.content.Context;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.disaster.dao.PetDisspecDao;
import com.dasinong.app.database.disaster.domain.PetDisspec;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public class PetDisspecDaoImpl extends DaoSupportImpl<PetDisspec>  implements PetDisspecDao {


    public PetDisspecDaoImpl(Context context) {
        super(context);
    }

    /**
     * 根据类型得所有的数据
     * @param typeValue 类型的值
     * @return 根据类型得所有的数据
     */
    public  List<PetDisspec> queryDisasterByType(String typeValue){

        return  query(new String[]{"type"},new String[]{typeValue});
    }

}


