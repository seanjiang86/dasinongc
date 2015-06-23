package com.dasinong.app.database.task.dao.impl;

import android.content.Context;
import android.database.Cursor;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.database.task.domain.TaskSpec;
import com.dasinong.app.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 */
public class SubStageDaoImpl extends DaoSupportImpl<SubStage> {
    public SubStageDaoImpl(Context context) {
        super(context);
    }




    /**
     *
     * @return
     */
    public List<SubStage> queryStageSubCategory(String stageName)
    {

        return query("stageName = ? ",new String[]{stageName},"subStageId");
    }

    /**
     *
     * @return
     */
    public List<String> queryStageCategory()
    {

        StringBuffer sb = new StringBuffer();
        sb.append("select distinct stageName ")
                .append("from ")
                .append("substage");


        return querySingleColumn(sb.toString());
    }
}
