package com.dasinong.app.database.task.dao;

import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.task.domain.Steps;
import com.dasinong.app.database.task.domain.TaskSpec;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public interface TaskSpecDao extends DaoSupport<TaskSpec> {
    /**
     * 根据substage查询所有的taskSpec
     * @param subStageId
     * @return 根据substage查询所有的taskSpec
     */

    public List<TaskSpec> queryTaskSpecWithSubStage(int subStageId);
}
