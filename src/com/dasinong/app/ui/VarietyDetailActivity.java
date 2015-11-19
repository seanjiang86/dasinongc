package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.VarietyEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.MyTabView;
import com.dasinong.app.ui.view.MyTabView.OnItemClickListener;
import com.dasinong.app.ui.view.TopbarView;

public class VarietyDetailActivity extends BaseActivity {
	private TopbarView topBar;
	private TextView tv_registration_id;
	private TextView tv_owner;
	private TextView tv_suitableArea;
	private MyTabView mtv;
	private List<String> list = new ArrayList<String>();

	public static final String YIELD_PERFORMANCE = "产量表现";
	public static final String CHARACTERISTICS = "品种特性";
	private String id;
	private TextView tv_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_variety_detail);

		id = getIntent().getStringExtra("id");

		topBar = (TopbarView) findViewById(R.id.topbar);

		tv_registration_id = (TextView) findViewById(R.id.tv_registration_id);
		tv_owner = (TextView) findViewById(R.id.tv_owner);
		tv_suitableArea = (TextView) findViewById(R.id.tv_suitableArea);

		mtv = (MyTabView) findViewById(R.id.mtv_description);

		tv_text = (TextView) findViewById(R.id.tv_text);

		list.add(YIELD_PERFORMANCE);
		list.add(CHARACTERISTICS);

		queryData();
		

	}

	private void initTopBar(VarietyEntity entity) {
		topBar.setCenterText(entity.data.varietyName);
		topBar.setLeftView(true, true);
	}

	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().getVarietyBaiKeById(this, id, VarietyEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					VarietyEntity entity = (VarietyEntity) resultData;
					if (entity.data != null) {
						initTopBar(entity);
						initData(entity);
					} else {
						showToast("网络异常，请稍候重试");
					}
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void initData(final VarietyEntity entity) {
		tv_registration_id.setText(entity.data.registrationId);
		tv_owner.setText(entity.data.owner);
		tv_suitableArea.setText(entity.data.suitableArea);

		mtv.setData(list);

		mtv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				switch (position) {
				case 0:
					tv_text.setText(entity.data.yieldPerformance);
					break;
				case 1:
					tv_text.setText(entity.data.characteristics);
					break;
				}
			}
		});
		
		tv_text.setText(entity.data.yieldPerformance);
	}
}
