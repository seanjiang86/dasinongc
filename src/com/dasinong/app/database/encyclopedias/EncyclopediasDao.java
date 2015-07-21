package com.dasinong.app.database.encyclopedias;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.encyclopedias.domain.Crop;

public class EncyclopediasDao extends DaoSupportImpl<Crop> {

	public EncyclopediasDao(Context context) {
		super(context);
	}

	public List<Crop> queryStageCategory(String type) {
		return query("type = ? ",new String[]{type});
	}

}
