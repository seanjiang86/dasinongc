package com.dasinong.app.database.task.domain;

/**
 * Created by liuningning on 15/6/6.
 */
public class SubStage {
    /**
     *  `subStageId` int(10) unsigned NOT NULL AUTO_INCREMENT,
     `subStageName` varchar(30) NOT NULL,
     `stageName` varchar(30) NOT NULL,
     `triggerAccumulatedTemp` int(10) DEFAULT NULL,
     `reqMinTemp` int(10) DEFAULT NULL,
     `reqAvgTemp` int(10) DEFAULT NULL,
     `maxFieldHumidity` int(10) DEFAULT NULL,
     `minFieldHumidity` int(10) DEFAULT NULL,
     `durationLow` double DEFAULT NULL,
     `durationMid` double DEFAULT NULL,
     */

    public int subStageId;
    public String subStageName;
    public String stageName;
    public String triggerAccumulatedTemp;
    public int reqMinTemp;
    public int reqAvgTemp;
    public int maxFieldHumidity;
    public int minFieldHumidity;
    public double durationLow;
    public double durationMid;
    public double durationHigh;
}
