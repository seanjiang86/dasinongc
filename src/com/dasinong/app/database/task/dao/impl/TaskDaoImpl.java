package com.dasinong.app.database.task.dao.impl;

import android.content.Context;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.task.domain.Task;

/**
 * Created by liuningning on 15/6/6.
 */
public class TaskDaoImpl extends DaoSupportImpl<Task> {
    public TaskDaoImpl(Context context) {
        super(context);
    }
}
