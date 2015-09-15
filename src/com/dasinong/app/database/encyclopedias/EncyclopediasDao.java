package com.dasinong.app.database.encyclopedias;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.encyclopedias.domain.Crop;

/**
 * @ClassName EncyclopediasDao
 * @author linmu
 * @Decription 品种
 * @2015-7-22 上午12:06:22
 */
public class EncyclopediasDao extends DaoSupportImpl<Crop> {

	public EncyclopediasDao(Context context) {
		super(context);
	}

	public List<Crop> queryStageCategory(String type) {
		return query("type LIKE ? ",new String[]{"%" + type + "%"});
	}

}
