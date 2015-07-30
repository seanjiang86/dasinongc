package com.dasinong.app.database.encyclopedias;

import java.util.List;

import android.content.Context;

import com.dasinong.app.database.common.dao.impl.DaoSupportImpl;
import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;

/**
 * @ClassName PetdisspecbrowseDao
 * @author linmu
 * @Decription 病害
 * @2015-7-22 上午12:06:15
 */
public class PetdisspecbrowseDao extends DaoSupportImpl<Petdisspecbrowse> {

	public PetdisspecbrowseDao(Context context) {
		super(context);
	}

	public List<Petdisspecbrowse> query(String type) {
		return query("type = ? ",new String[]{type});
	}
	public List<Petdisspecbrowse> queryCaohai(String type) {
		return query("type LIKE ? ",new String[]{"%草%"});
	}
	
}
