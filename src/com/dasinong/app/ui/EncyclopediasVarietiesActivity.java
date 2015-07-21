package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.EncyclopediasDao;
import com.dasinong.app.database.encyclopedias.domain.Crop;

import android.os.Bundle;

/**
 * @ClassName EncyclopediasVarietiesActivity
 * @author linmu
 * @Decription 品种大全
 * @2015-7-21 下午9:18:30
 */
public class EncyclopediasVarietiesActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_varieties);
		
		initData();
		initView();
		setUpView();
	}

	private void initData() {
		EncyclopediasDao dao = new EncyclopediasDao(this);
		List<Crop> queryStageCategory = dao.queryStageCategory("水果");
		
		showToast("queryStageCategory:"+queryStageCategory.size());
	}

	private void initView() {
		
	}

	private void setUpView() {
		
	}
	
}
