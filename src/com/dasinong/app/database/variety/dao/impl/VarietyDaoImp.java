package com.dasinong.app.database.variety.dao.impl;

import android.content.Context;

import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.variety.domain.Variety;
import com.dasinong.app.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuningning on 15/6/11.
 */
public class VarietyDaoImp extends DaoSupportImpl<Variety> {
	public VarietyDaoImp(Context context) {
		super(context);
	}

	public List<String> getVariety(String queryKey) {

		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append("variety ").append("where region like '%").append(queryKey).append("%'");
		Logger.d("VarietyDaoImp", sql.toString());
		List<Variety> varieties = query(sql.toString());
		Variety tem;
		List<String> result = new ArrayList<String>(12);
		if (!varieties.isEmpty()) {
			int len = varieties.size();
			for (int i = 0; i < len; i++) {
				tem = varieties.get(i);
				result.add(tem.cropOne);
				result.add(tem.cropTwo);
				result.add(tem.cropThree);
				result.add(tem.cropFour);
				result.add(tem.cropFive);
				result.add(tem.cropSix);
				result.add(tem.cropSeven);
				result.add(tem.cropEight);
				result.add(tem.cropNine);
				result.add(tem.cropTen);
				result.add(tem.cropEleven);
				result.add(tem.cropTwelve);
			}
		}
		return result;
	}
}
