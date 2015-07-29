package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import butterknife.OnClick;

import com.dasinong.app.R;
import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.adapter.MyBaseAdapter;
import com.dasinong.app.ui.adapter.TextAdapter.OnItemClickListener;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.ExpandTabView;
import com.dasinong.app.ui.view.PPCPopMenu;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.ViewMiddle;
import com.dasinong.app.ui.view.ViewThree;

public class AddFieldActivity5 extends MyBaseActivity implements OnClickListener {
	private String varietyId;
	private List<String> bigSubStageList;
	private ArrayList<String> smallSubStageList;
	private Map<String, Integer> smallSubStageMap = new LinkedHashMap<String, Integer>();
	private Button btn_no_sure_substage;
	private Button btn_sure_substage;
	private String currentStage;
	protected String lastStage;
	private String subStageId;
	private TopbarView topbar;
	private SubStageDaoImpl dao;
	private String seedingMethod;
	private ExpandTabView etv;
	private ViewMiddle viewMiddle;
	private List<View> viewList = new ArrayList<View>();
	private List<String> textList = new ArrayList<String>();
	private int bigPosition;
	private int smallPosition;
	private String bigSubStage;
	private String smallSubStage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
		setContentView(R.layout.activity_add_field_5);
		etv = (ExpandTabView) findViewById(R.id.etv);

		btn_no_sure_substage = (Button) findViewById(R.id.btn_no_sure_substage);
		btn_sure_substage = (Button) findViewById(R.id.btn_sure_substage);
		topbar = (TopbarView) findViewById(R.id.topbar);

		// TODO MING:默认值
		varietyId = SharedPreferencesHelper.getString(this, Field.VARIETY_ID, "");
		seedingMethod = SharedPreferencesHelper.getString(this, Field.SEEDING_METHOD, "");

		dao = new SubStageDaoImpl(this);

		viewMiddle = new ViewMiddle(this);
		viewList.add(viewMiddle);
		textList.add("请选择品种");

		etv.setValue(textList, viewList);

		queryBigSubStage();

		initTopBar();

		btn_no_sure_substage.setOnClickListener(this);
		btn_sure_substage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_no_sure_substage:
			SharedPreferencesHelper.setString(this, Field.SUBSTAGE_ID, "");
			goToNext();
			break;
		case R.id.btn_sure_substage:
			if (TextUtils.isEmpty(subStageId)) {
				showToast("请完善作物生长阶段，或者选择我不知道");
				return;
			}
			SharedPreferencesHelper.setString(this, Field.SUBSTAGE_ID, subStageId);
			goToNext();
			break;
		}
	}

	private void initTopBar() {
		topbar.setCenterText("种植方式");
		topbar.setLeftView(true, true);
	}

	/**
	 * 请求大长期数据
	 */
	private void queryBigSubStage() {
		bigSubStageList = dao.queryStageCategory();
		bigSubStageList.remove("收获后");
		if (AddFieldActivity8.DIRECT.equals(seedingMethod)) {
			bigSubStageList.remove("移栽");
			bigSubStageList.remove("返青期");
		}
		initBigSubStage();
	}

	/**
	 * 填充大生长期
	 */
	protected void initBigSubStage() {
		viewMiddle.initBigAreaData(bigSubStageList, bigPosition);
		viewMiddle.setOnBigAreaItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				if (!bigSubStageList.get(position).equals(bigSubStage)) {
					bigSubStage = bigSubStageList.get(position);
					smallSubStage = null;
					smallPosition = 0;
				}
				bigSubStage = bigSubStageList.get(position);

				bigPosition = position;

				querySmallSubStage(bigSubStage);
			}
		});
	}

	/**
	 * 请求小长期数据
	 */
	private void querySmallSubStage(String stageName) {
		List<SubStage> list = dao.queryStageSubCategory(stageName);
		smallSubStageMap.clear();
		for (int i = 0; i < list.size(); i++) {
			SubStage stage = list.get(i);
			if (AddFieldActivity8.DIRECT.equals(seedingMethod) && "移栽前准备".equals(stage.subStageName))
				continue;
			smallSubStageMap.put(stage.subStageName, stage.subStageId);
		}
		initSmallSubStage();
	}

	/**
	 * 填充小生长期
	 */
	private void initSmallSubStage() {
		smallSubStageList = new ArrayList<String>(smallSubStageMap.keySet());
		smallSubStageList.add(0, "我不确定");
		viewMiddle.initSmallAreaData(smallSubStageList, smallPosition);
		viewMiddle.setOnSmallAreaItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				smallSubStage = smallSubStageList.get(position);
				smallPosition = position;
				onrefresh(bigSubStage + "-" + smallSubStage);

				if ("我不确定".equals(smallSubStage)) {
					smallSubStage = smallSubStageList.get(1);
				}
				subStageId = String.valueOf(smallSubStageMap.get(smallSubStage));
			}
		});
	}

	private void onrefresh(String showText) {
		etv.onPressBack();

		if (!etv.getTitle(0).equals(showText) && !TextUtils.isEmpty(showText)) {
			etv.setTitle(showText, 0);
		}
	}

	private void goToNext() {
		Intent intent = new Intent(this, AddFieldActivity6.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
