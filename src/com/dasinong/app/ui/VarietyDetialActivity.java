package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.VarietyEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.HarmDescriptionFragmentPagerAdapter;
import com.dasinong.app.ui.view.MyTabView;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

public class VarietyDetialActivity extends BaseActivity {
	private TopbarView topBar;
	private TextView tv_name;
	private TextView tv_registration_id;
	private TextView tv_owner;
	private TextView tv_suitableArea;
	private ViewPager pager;
	private MyTabView mtv;
	private List<String> list = new ArrayList<String>();

	public static final String YIELD_PERFORMANCE = "产量表现";
	public static final String CHARACTERISTICS = "品种特性";
	private String id;
	private String title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_variety_detial);
		
		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");

		topBar = (TopbarView) findViewById(R.id.topbar);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_registration_id = (TextView) findViewById(R.id.tv_registration_id);
		tv_owner = (TextView) findViewById(R.id.tv_owner);
		tv_suitableArea = (TextView) findViewById(R.id.tv_suitableArea);

		mtv = (MyTabView) findViewById(R.id.mtv_description);
		pager = (ViewPager) findViewById(R.id.pager);

		list.add(YIELD_PERFORMANCE);
		list.add(CHARACTERISTICS);

		queryData();
		initTopBar();

	}

	private void initTopBar() {
		topBar.setCenterText(title);
		topBar.setLeftView(true, true);
	}

	private void queryData() {
		RequestService.getInstance().getVarietyBaiKeById(this, id, VarietyEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					VarietyEntity entity = (VarietyEntity) resultData;
					if (entity.data != null) {
						initView(entity);
					} else {
						showToast("网络异常，请稍候重试");
					}
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {

			}
		});
	}

	protected void initView(VarietyEntity entity) {
		tv_name.setText(entity.data.varietyName);
		tv_registration_id.setText(entity.data.registrationId);
		tv_owner.setText(entity.data.owner);
		tv_suitableArea.setText(entity.data.suitableArea);

		mtv.setData(list);
		HarmDescriptionFragmentPagerAdapter adapter = new HarmDescriptionFragmentPagerAdapter(getSupportFragmentManager(), list, entity);
		pager.setAdapter(adapter);
		mtv.setViewPager(pager);
	}
}
