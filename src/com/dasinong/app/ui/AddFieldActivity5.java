package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SubStageInfo;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.SubStageAdapter;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AddFieldActivity5 extends BaseActivity implements OnClickListener {
	private String varietyId;
	private SubStageInfo subStageInfo;
	private List<String> bigSubStageList;
	private List<String> smallSubStageList;
	private TextView tv_big_substage;
	private TextView tv_small_substage;
	private FrameLayout fl_select_substage;
	private Button btn_no_sure_substage;
	private Button btn_sure_substage;
	private boolean bigIsGone = true;
	private boolean smallIsGone = true;
	private SubStageAdapter subStageAdapter;
	private ListView lv_content;
	private String bigSubStage;
	private String subStageId;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_5);

		tv_big_substage = (TextView) findViewById(R.id.tv_big_substage);
		tv_small_substage = (TextView) findViewById(R.id.tv_small_substage);
		fl_select_substage = (FrameLayout) findViewById(R.id.fl_select_substage);
		btn_no_sure_substage = (Button) findViewById(R.id.btn_no_sure_substage);
		btn_sure_substage = (Button) findViewById(R.id.btn_sure_substage);
		lv_content = (ListView) findViewById(R.id.lv_content);

		varietyId = SharedPreferencesHelper.getString(this, Field.VARIETY_ID, "");
		querySubStage();
		
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
			if (bigIsGone) {
				fl_select_substage.setVisibility(View.VISIBLE);
			} else {
				fl_select_substage.setVisibility(View.GONE);
			}
			bigIsGone = !bigIsGone;

			initBigSubStage();
			break;
		case R.id.tv_small_substage:
			if (smallIsGone) {
				fl_select_substage.setVisibility(View.VISIBLE);
			} else {
				fl_select_substage.setVisibility(View.GONE);
			}
			smallIsGone = !smallIsGone;

			initSmallSubStage();
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

	/**
	 * 请求生长期数据
	 */
	private void querySubStage() {
		RequestService.getInstance().getSubStage(this, varietyId, SubStageInfo.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					subStageInfo = (SubStageInfo) resultData;
					bigSubStageList = new ArrayList<String>(subStageInfo.subStage.keySet());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {

			}
		});
	}

	/**
	 * 填充大生长期
	 */
	protected void initBigSubStage() {
		if (bigSubStageList == null || bigSubStageList.size() == 0) {
			return;
		}
		if (subStageAdapter == null) {
			subStageAdapter = new SubStageAdapter(this, bigSubStageList, false);
		} else {
			subStageAdapter.setData(bigSubStageList);
			subStageAdapter.notifyDataSetChanged();
		}
		lv_content.setAdapter(subStageAdapter);

		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				bigSubStage = bigSubStageList.get(position);
				smallSubStageList = new ArrayList<String>(subStageInfo.subStage.get(bigSubStage).keySet());
			}
		});
	}

	/**
	 * 填充小生长期
	 */
	private void initSmallSubStage() {
		if (TextUtils.isEmpty(bigSubStage)) {
			showToast("请先选择大生长期");
			return;
		}
		subStageAdapter.setData(smallSubStageList);
		subStageAdapter.notifyDataSetChanged();
		
		lv_content.setAdapter(subStageAdapter);
		
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String smallSubStage = smallSubStageList.get(position);
				subStageId = subStageInfo.subStage.get(bigSubStage).get(smallSubStage);
			}
		});
	}
	
	private void goToNext() {
		Intent intent = new Intent(this, AddFieldActivity6.class);
		startActivity(intent);
	}

}
