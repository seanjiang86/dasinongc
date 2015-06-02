package com.dasinong.app.database.disaster.dao.impl;

import android.content.Context;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.disaster.dao.NatDisspecDao;
import com.dasinong.app.database.disaster.domain.NatDisspec;

import java.util.List;

/**
 * Created by liuningning on 15/6/2.
 */
public class NatDisspecDaoImpl extends DaoSupportImpl<NatDisspec> implements NatDisspecDao {


    public NatDisspecDaoImpl(Context context) {
        super(context);
    }

    /**
     *
     * @return 返回所有的自然灾害
     */

    @Override
    public List<NatDisspec> queryAllNatDispec() {
        return query();
    }
}


