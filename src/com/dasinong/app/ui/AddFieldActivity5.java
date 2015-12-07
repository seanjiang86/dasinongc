package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.StagesEntity;
import com.dasinong.app.entity.StagesEntity.StageEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.MyBaseAdapter;
import com.dasinong.app.ui.adapter.TextAdapter.OnItemClickListener;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.ExpandTabView;
import com.dasinong.app.ui.view.PPCPopMenu;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.ViewMiddle;
import com.dasinong.app.ui.view.ViewThree;
import com.umeng.analytics.MobclickAgent;

public class AddFieldActivity5 extends MyBaseActivity implements OnClickListener {
	private String varietyId;
	private List<String> bigSubStageList;
	private ArrayList<String> smallSubStageList;
	private Button btn_no_sure_substage;
	private Button btn_sure_substage;
	protected String lastStage;
	private String subStageId;
	private TopbarView topbar;
	private boolean seedingMethod;
	private ExpandTabView etv;
	private ViewMiddle viewMiddle;
	private List<View> viewList = new ArrayList<View>();
	private List<String> textList = new ArrayList<String>();
	private int bigPosition;
	private int smallPosition;
	private String bigSubStage;
	private String smallSubStage;
	private String crop;
	private Map<String, Map<String, Integer>> stagesMap = new LinkedHashMap<String, Map<String, Integer>>();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			List<StageEntity> list = (List<StageEntity>) msg.obj;
			convertData(list);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
		setContentView(R.layout.activity_add_field_5);
		etv = (ExpandTabView) findViewById(R.id.etv);

		crop = SharedPreferencesHelper.getString(this, Field.NEW_CROP, "");

		btn_no_sure_substage = (Button) findViewById(R.id.btn_no_sure_substage);
		btn_sure_substage = (Button) findViewById(R.id.btn_sure_substage);
		topbar = (TopbarView) findViewById(R.id.topbar);

		varietyId = SharedPreferencesHelper.getString(this, Field.VARIETY_ID, "");
		seedingMethod = SharedPreferencesHelper.getBoolean(this, Field.SEEDING_METHOD, false);

		viewMiddle = new ViewMiddle(this);
		viewList.add(viewMiddle);
		textList.add("请选择生长阶段");

		etv.setValue(textList, viewList);

		queryStages();

		initTopBar();

		btn_no_sure_substage.setOnClickListener(this);
		btn_sure_substage.setOnClickListener(this);
	}

	private void queryStages() {
		startLoadingDialog();
		RequestService.getInstance().getStages(this, varietyId, StagesEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					StagesEntity entity = (StagesEntity) resultData;
					Message msg = handler.obtainMessage();
					msg.obj = entity.data;
					handler.sendMessage(msg);
					dismissLoadingDialog();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void convertData(List<StageEntity> list) {

		for (StageEntity stageEntity : list) {
			if (stagesMap.keySet().contains(stageEntity.stageName)) {
				Map<String, Integer> map = stagesMap.get(stageEntity.stageName);
				map.put(stageEntity.subStageName, stageEntity.subStageId);
				stagesMap.put(stageEntity.stageName, map);
			} else {
				Map<String, Integer> map = new LinkedHashMap<String, Integer>();
				map.put(stageEntity.subStageName, stageEntity.subStageId);
				stagesMap.put(stageEntity.stageName, map);
			}
		}

		initBigSubStage();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_no_sure_substage:
			
			MobclickAgent.onEvent(this, "AddFieldSixth");
			
			SharedPreferencesHelper.setString(this, Field.SUBSTAGE_ID, "");
			goToNext();
			break;
		case R.id.btn_sure_substage:
			if (TextUtils.isEmpty(subStageId)) {
				showToast("请完善作物生长阶段，或者选择我不知道");
				return;
			}
			
			MobclickAgent.onEvent(this, "AddFieldSixth");
			
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
	 * 填充大生长期
	 */
	protected void initBigSubStage() {

		bigSubStageList = new ArrayList<String>(stagesMap.keySet());
		if(!TextUtils.isEmpty(crop) && "水稻".equals(crop)){
			bigSubStageList.remove("收获后");
			if(seedingMethod){
				bigSubStageList.remove("移栽");
				bigSubStageList.remove("返青期");
			}
		}

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

				// querySmallSubStage(bigSubStage);

				initSmallSubStage(bigSubStage);
			}
		});
	}

	/**
	 * 填充小生长期
	 */
	private void initSmallSubStage(final String bigSubStage) {

		smallSubStageList = new ArrayList<String>(stagesMap.get(bigSubStage).keySet());

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
				subStageId = String.valueOf(stagesMap.get(bigSubStage).get(smallSubStage));
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

		Intent intent = null;
		if ("芒果".equals(crop)) {
			intent = new Intent(this, AddFieldActivity7.class);
		} else {
			intent = new Intent(this, AddFieldActivity6.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
