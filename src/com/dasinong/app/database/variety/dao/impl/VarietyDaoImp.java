package com.dasinong.app.database.variety.dao.impl;

import android.content.Context;

import com.dasinong.app.database.common.dao.DaoSupport;
import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.variety.domain.Variety;
import com.dasinong.app.utils.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		List<String> result = new ArrayList<>(12);
		if (!varieties.isEmpty()) {
			int len = varieties.size();
			for (int i = 0; i < len; i++) {
				tem = varieties.get(i);

				duplicate(tem.cropOne,result);
				duplicate(tem.cropTwo,result);
				duplicate(tem.cropThree,result);
				duplicate(tem.cropFour,result);
				duplicate(tem.cropFive,result);
				duplicate(tem.cropSix,result);
				duplicate(tem.cropSeven,result);
				duplicate(tem.cropEight,result);
				duplicate(tem.cropNine,result);
				duplicate(tem.cropTen,result);
				duplicate(tem.cropEleven,result);
				duplicate(tem.cropTwelve,result);

			}
		}


		return result;
	}


	private void duplicate(String text,List<String> result){

		if(!result.contains(text.trim())){
			result.add(text);
		}


	}
}
