package com.dasinong.app.database.encyclopedias;

import java.util.List;

import android.content.Context;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.app.database.encyclopedias.domain.Crop;

/**
 * @ClassName CpproductbrowseDao
 * @author linmu
 * @Decription 农药
 * @2015-7-22 上午12:06:03
 */
public class CpproductbrowseDao extends DaoSupportImpl<Cpproductbrowse> {

	public CpproductbrowseDao(Context context) {
		super(context);
	}

	public List<Cpproductbrowse> query(String model) {
		return query("model = ? ",new String[]{model});
	}
	
}
