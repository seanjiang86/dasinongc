package com.dasinong.app.database.disaster.domain;

import java.io.Serializable;

/**
 * Created by liuningning on 15/6/2.
 */
public class PetSolu implements Serializable {
    /**
     * `petSoluId` int(10) unsigned NOT NULL AUTO_INCREMENT,
     `petSoluDes` varchar(2000) NOT NULL,
     `petDisSpecId` int(10) unsigned NOT NULL,
     `providedBy` varchar(50) DEFAULT NULL,
     `isRemedy` tinyint(1) DEFAULT '1',
     `isCPSolu` tinyint(1) DEFAULT '1',
     `rank` int(4) DEFAULT NULL,
     `subStageId` varchar(50) NOT NULL DEFAULT '',
     */
    public int  petSoluId;
    public String petSoluDes;
    public int petDisSpecId;
    public String providedBy;
    public int isRemedy;
    public int isCPSolu;
    public int rank;
    public String subStageId;


}
