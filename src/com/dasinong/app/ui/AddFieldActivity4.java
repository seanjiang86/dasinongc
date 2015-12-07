package com.dasinong.app.ui;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.variety.dao.impl.VarietyDaoImp;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.VarietyInfo;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.TextAdapter.OnItemClickListener;
import com.dasinong.app.ui.adapter.VarietyListAdapter;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.ExpandTabView;
import com.dasinong.app.ui.view.PPCPopMenu;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.ViewThree;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.GraphicUtils;
import com.dasinong.app.utils.Logger;
import com.umeng.analytics.MobclickAgent;

public class AddFieldActivity4 extends MyBaseActivity implements OnClickListener {

	private List<String> cropList = new ArrayList<String>();
	private List<String> varietyNameList;
	private List<String> varietyNumList;
	private Map<String, String> varietyNumMap = new HashMap<String, String>();
	private VarietyInfo varietyInfo;
	private String varietyId;
	private Button btn_sure_crop;
	private TopbarView topbar;
	private TextView tv_user_call;
	private ExpandTabView etv;
	private String villageId;
	private List<String> textList = new ArrayList<String>();
	private List<View> viewList = new ArrayList<View>();
	private int cropPosition;
	private int namePosition;
	private int numPosition;
	private String varietyName;
	private String varietyNum;
	private ViewThree viewThree;
	private MyComparator comparator = new MyComparator();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String crop = (String)msg.obj;
			initVarietyNameList(crop);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_4);
		tv_user_call = (TextView) findViewById(R.id.tv_user_call);
		etv = (ExpandTabView) findViewById(R.id.etv);
		btn_sure_crop = (Button) findViewById(R.id.btn_sure_crop);
		topbar = (TopbarView) findViewById(R.id.topbar);

		villageId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "1");

		String fieldSize = SharedPreferencesHelper.getString(this, Field.FIELD_SIZE, "50");
		double intFieldSize = Double.valueOf(fieldSize);
		if (intFieldSize <= 10) {
			tv_user_call.setText("呦,自给自足啊,都种了");
		} else if (10 < intFieldSize && intFieldSize <= 200) {
			tv_user_call.setText("呦,大户啊,都种了");
		} else if (200 < intFieldSize && intFieldSize <= 1000) {
			tv_user_call.setText("呦,超级大户啊,都种了");
		} else {
			tv_user_call.setText("呦,农场主啊,都种了");
		}

		viewThree = new ViewThree(this);
		viewList.add(viewThree);
		textList.add("请选择品种");

		etv.setValue(textList, viewList);
		villageId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "1");

		initCropList();

		initTopBar();

		btn_sure_crop.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_sure_crop:
			goToNext();
			break;
		}
	}

	private void initTopBar() {
		topbar.setCenterText("作物信息");
		topbar.setLeftView(true, true);
	}

	/**
	 * 第一个版本临时填充 CropList 集合
	 */
	private void initCropList() {
		String[] stringArray = DsnApplication.getContext().getResources().getStringArray(R.array.cropList);
		for (int i = 0; i < stringArray.length; i++) {
			cropList.add(stringArray[i]);
		}
		viewThree.initBigAreaData(cropList, cropPosition);
		
		viewThree.setOnBigAreaItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				String crop = null;
				if (!cropList.get(position).equals(crop)) {
					crop = cropList.get(position);
					varietyName = null;
					varietyNum = null;
					namePosition = 0;
					numPosition = 0;
				}
				crop = cropList.get(position);

				cropPosition = position;

				queryVariety(crop);
			}
		});
	}

	/**
	 * 获取品种信息
	 */
	private void queryVariety(final String crop) {
		if (!DeviceHelper.checkNetWork(this)) {
			showToast("请检测您的网络连接");
			return;
		}
		startLoadingDialog();
		RequestService.getInstance().getVarietyList(DsnApplication.getContext(), crop, villageId, VarietyInfo.class,
				new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							varietyInfo = (VarietyInfo) resultData;
							varietyNameList = new ArrayList<String>(varietyInfo.data.keySet());
							Collections.sort(varietyNameList, comparator);
							if (varietyNameList.contains("其他" + crop)) {
								varietyNameList.remove("其他" + crop);
								varietyNameList.add(0, ("其他" + crop));
							}
							Message msg = handler.obtainMessage();
							msg.obj = crop;
							handler.sendMessage(msg);
						} else {
							showToast(resultData.getMessage() + requestCode);
						}
						dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
					}
				});
	}

	protected void initVarietyNameList(final String crop) {
		viewThree.initSmallAreaData(varietyNameList, namePosition);
		setDefaultList(crop, namePosition);

		viewThree.setOnSmallAreaItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (varietyNameList.get(position).equals(varietyName)) {
					varietyName = varietyNameList.get(position);
					varietyNum = null;
					numPosition = 0;
				}
				setDefaultList(crop, position);
			}
		});
	}

	private void setDefaultList(String crop, int position) {
		varietyName = varietyNameList.get(position);

		namePosition = position;

		varietyNumMap = varietyInfo.data.get(varietyName);

		varietyNumList = new ArrayList<String>();
		
		varietyNumList.addAll(varietyNumMap.keySet());
		
		if(varietyNumList.size() == 1 && TextUtils.isEmpty(varietyNumList.get(0))){
			varietyNumList.clear();
			varietyNumList.add(varietyName);
		}

		initVarietyNumList(crop);
	}

	protected void initVarietyNumList(final String crop) {
		
		Collections.sort(varietyNumList);
		
		viewThree.initLastData(varietyNumList, numPosition);

		viewThree.setOnLastItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				varietyNum = varietyNumList.get(position);
				numPosition = position;
				onrefresh(crop + "-" + varietyName + "-" + varietyNum);
				varietyId = varietyNumMap.get(varietyNum);
				
				SharedPreferencesHelper.setString(AddFieldActivity4.this, Field.NEW_CROP, crop);
				
				if(TextUtils.isEmpty(varietyId)){
					varietyId = varietyNumMap.get("");
				}
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
		if (TextUtils.isEmpty(varietyId)) {
			showToast("请完善作物信息");
			return;
		}
		
		MobclickAgent.onEvent(this, "AddFieldFourth");
		
		SharedPreferencesHelper.setString(this, Field.VARIETY_ID, varietyId);
		Intent intent = null;
		String newCrop = SharedPreferencesHelper.getString(this, Field.NEW_CROP, "");
		if(!TextUtils.isEmpty(newCrop)){
			if("水稻".equals(newCrop)){
				intent = new Intent(this, AddFieldActivity8.class);
			} else if ("小麦".equals(newCrop)){
				intent = new Intent(this, AddFieldActivity5.class);
//				intent.putExtra("crop", "小麦");
			} else if ("芒果".equals(newCrop)){
				intent = new Intent(this, AddFieldActivity5.class);
//				intent.putExtra("crop", "芒果");
			} else {
				intent = new Intent(this, AddFieldActivity7.class);
				
				SharedPreferencesHelper.setBoolean(this, Field.SEEDING_METHOD, false);
				SharedPreferencesHelper.setString(this, Field.SUBSTAGE_ID, "");
				SharedPreferencesHelper.setString(this, Field.PLANTING_DATE, "");
			}
		} else {
			showToast("请重新选择作物");
		}
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	// 比较器
	public class MyComparator implements Comparator<String> {
		Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

		@Override
		public int compare(String str1, String str2) {
			if (cmp.compare(str1, str2) > 0) {
				return 1;
			} else if (cmp.compare(str1, str2) < 0) {
				return -1;
			}
			return 0;
		}
	}
}
