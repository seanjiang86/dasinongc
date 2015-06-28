package com.dasinong.app.components.domain;

/**
 * Created by liuningning on 15/6/28.
 */
public class TaskStatus implements Comparable<TaskStatus> {

    public long subStageId;
    public boolean isCheck;

    @Override
    public int compareTo(TaskStatus another) {
        return (int)(this.subStageId-another.subStageId);
    }

    @Override
    public boolean equals(Object o) {
        return this.subStageId==((TaskStatus)(o)).subStageId;
    }
}
