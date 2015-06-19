package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.adapter.MyBaseAdapter;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.PPCPopMenu;
import com.dasinong.app.ui.view.TopbarView;

public class AddFieldActivity5 extends BaseActivity implements OnClickListener {
	private String varietyId;
	private List<String> bigSubStageList;
	private ArrayList<String> smallSubStageList;
	private Map<String, Integer> smallSubStageMap = new HashMap<String, Integer>();
	private TextView tv_big_substage;
	private TextView tv_small_substage;
	private Button btn_no_sure_substage;
	private Button btn_sure_substage;
	private String currentStage;
	protected String lastStage;
	private String subStageId;
	private TopbarView topbar;
	private SubStageDaoImpl dao;
	private PPCPopMenu bigStageMenu;
	private PPCPopMenu smallStageMenu;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_5);

		tv_big_substage = (TextView) findViewById(R.id.tv_big_substage);
		tv_small_substage = (TextView) findViewById(R.id.tv_small_substage);
		btn_no_sure_substage = (Button) findViewById(R.id.btn_no_sure_substage);
		btn_sure_substage = (Button) findViewById(R.id.btn_sure_substage);
		topbar = (TopbarView) findViewById(R.id.topbar);

		varietyId = SharedPreferencesHelper.getString(this, Field.VARIETY_ID, "");

		dao = new SubStageDaoImpl(this);

		bigStageMenu = new PPCPopMenu(this);
		smallStageMenu = new PPCPopMenu(this);

		queryBigSubStage();

		initTopBar();

		tv_big_substage.setOnClickListener(this);
		tv_small_substage.setOnClickListener(this);
		btn_no_sure_substage.setOnClickListener(this);
		btn_sure_substage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_big_substage:
			bigStageMenu.showAsDropDown(tv_big_substage);
			bigStageMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					currentStage = bigSubStageList.get(position);
					if(!currentStage.equals(lastStage)){
						tv_big_substage.setText(currentStage);
						tv_small_substage.setText("小生长期");
						if(smallSubStageList != null){
							smallSubStageList.clear();
							smallStageMenu.addItems(smallSubStageList);
						}
						lastStage = currentStage;
					}
					querySmallSubStage(currentStage);
					bigStageMenu.dismiss();
				}
			});
			break;
		case R.id.tv_small_substage:
			if(currentStage == null){
				showToast("请先选择大生长期");
				return;
			}
			smallStageMenu.showAsDropDown(tv_small_substage);
			smallStageMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String smallSubStage = smallSubStageList.get(position);
					subStageId = String.valueOf(smallSubStageMap.get(smallSubStage));
					tv_small_substage.setText(smallSubStage);
					smallStageMenu.dismiss();
				}
			});
			break;
		case R.id.btn_no_sure_substage:
			goToNext();
			break;
		case R.id.btn_sure_substage:
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
		initBigSubStage();
	}

	/**
	 * 填充大生长期
	 */
	protected void initBigSubStage() {
		bigStageMenu.addItems(bigSubStageList);
	}

	/**
	 * 请求小长期数据
	 */
	private void querySmallSubStage(String stageName) {
		List<SubStage> list = dao.queryStageSubCategory(stageName);
		smallSubStageMap.clear();
		for (int i = 0; i < list.size(); i++) {
			SubStage stage = list.get(i);
			smallSubStageMap.put(stage.subStageName, stage.subStageId);
		}
		initSmallSubStage();
	}

	/**
	 * 填充小生长期
	 */
	private void initSmallSubStage() {
		if (TextUtils.isEmpty(currentStage)) {
			showToast("请先选择大生长期");
			return;
		}
		smallSubStageList = new ArrayList<String>(smallSubStageMap.keySet());
		smallStageMenu.addItems(smallSubStageList);
	}

	private void goToNext() {
		Intent intent = new Intent(this, AddFieldActivity6.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	class SubStageAdapter extends MyBaseAdapter<SubStage> {

		public SubStageAdapter(Context ctx, List<SubStage> list, boolean flag) {
			super(ctx, list, flag);
		}

	}

}
