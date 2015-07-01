package com.dasinong.app.ui;

import java.util.Date;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.fragment.HomeFragment;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddFieldActivity7 extends BaseActivity {
	private EditText et_target_production;
	private Button btn_finish_add_field;
	private String yield;
	private TopbarView topbar;
	private String seedingortransplant;
	private String area;
	private String startDate;
	private String locationId;
	private String varietyId;
	private String currentStageId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_7);

		et_target_production = (EditText) findViewById(R.id.et_target_production);
		btn_finish_add_field = (Button) findViewById(R.id.btn_finish_add_field);
		topbar = (TopbarView) findViewById(R.id.topbar);

		getData();

		initTopBar();

		btn_finish_add_field.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!DeviceHelper.checkNetWork(AddFieldActivity7.this)) {
					showToast("请检测您的网络连接");
					return;
				}
				startLoadingDialog();
				yield = et_target_production.getText().toString().trim();
				RequestService.getInstance().createField(AddFieldActivity7.this, seedingortransplant, area, startDate, locationId, varietyId,
						currentStageId, yield, BaseEntity.class, new NetRequest.RequestListener() {

							@Override
							public void onSuccess(int requestCode, BaseEntity resultData) {
								if (resultData.isOk()) {
									showToast("请求成功");
									backToHome();
									dismissLoadingDialog();
								} else {
									// TODO MING:待定显示什么内容
									showToast("创建失败");
								}
							}

							@Override
							public void onFailed(int requestCode, Exception error, String msg) {
								dismissLoadingDialog();
								showToast("请求失败，请检查网络或稍候再试");
							}

						});
			}
		});
	}

	private void getData() {
		seedingortransplant = SharedPreferencesHelper.getString(this, Field.SEEDING_METHOD, "false");
		area = SharedPreferencesHelper.getString(this, Field.FIELD_SIZE, "");
		startDate = SharedPreferencesHelper.getString(this, Field.PLANTING_DATE, "");

		if (TextUtils.isEmpty(startDate)) {
			Date date = new Date();
			long currentTime = date.getTime();
			startDate = String.valueOf(currentTime);
		}

		locationId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "");
		varietyId = SharedPreferencesHelper.getString(this, Field.VARIETY_ID, "");
		currentStageId = SharedPreferencesHelper.getString(this, Field.SUBSTAGE_ID, "");
	}

	private void initTopBar() {
		topbar.setCenterText("完成添加");
		topbar.setLeftView(true, true);
	}

	protected void backToHome() {
		// TODO Ming：发送请求

		Intent intent = new Intent(this, MainTabActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
