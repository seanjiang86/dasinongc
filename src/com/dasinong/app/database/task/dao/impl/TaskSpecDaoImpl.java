package com.dasinong.app.database.task.dao.impl;

import android.content.Context;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.task.dao.TaskSpecDao;
import com.dasinong.app.database.task.domain.TaskSpec;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public class TaskSpecDaoImpl extends DaoSupportImpl<TaskSpec> implements TaskSpecDao {
    public TaskSpecDaoImpl(Context context) {
        super(context);
    }



    /**
     * 根据substage查询所有的taskSpec
     * @param subStageId
     * @return 根据substage查询所有的taskSpec
     */

    public List<TaskSpec> queryTaskSpecWithSubStage(int subStageId){
        return  query("subStage = ? ",new String[]{String.valueOf(subStageId)});

    }
}
