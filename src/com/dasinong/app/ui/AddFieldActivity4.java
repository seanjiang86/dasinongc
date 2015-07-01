package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.variety.dao.impl.VarietyDaoImp;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.VarietyInfo;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.PPCPopMenu;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.GraphicUtils;
import com.dasinong.app.utils.Logger;

public class AddFieldActivity4 extends MyBaseActivity implements OnClickListener {

	private List<String> cropList = new ArrayList<String>();
	private List<String> varietyList;
	private List<String> varietyNumList;
	private Map<String, String> varietyNumMap;
	private VarietyInfo varietyInfo;
	private String varietyId;
	private Button btn_sure_crop;
	private TopbarView topbar;
	private TextView tv_user_call;
	private TextView tv_crop;
	private TextView tv_variety_name;
	private TextView tv_variety_num;
	private static boolean isHasData = true;
	private PPCPopMenu cropMenu;
	private PPCPopMenu varietyNameMenu;
	private PPCPopMenu varietyNumMenu;
	private String currentCrop;
	private String lastCrop;
	private String currentVariety;
	private String lastVariety;
	private String villageId;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (varietyList == null || varietyList.size() == 0) {
				isHasData = false;
			} else {
				isHasData = true;
			}
			initVariety();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_4);
		tv_user_call = (TextView) findViewById(R.id.tv_user_call);
		tv_crop = (TextView) findViewById(R.id.tv_crop);
		tv_variety_name = (TextView) findViewById(R.id.tv_variety_name);
		tv_variety_num = (TextView) findViewById(R.id.tv_variety_num);
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

		// county = county.substring(0, county.length() - 1);
		// queryCrop("临沧");

		initCropList();

		final Context context = AddFieldActivity4.this;
		tv_crop.post(new Runnable() {

			@Override
			public void run() {
				cropMenu = new PPCPopMenu(context, tv_crop, tv_crop.getWidth());
				varietyNameMenu = new PPCPopMenu(context, tv_variety_name, tv_variety_name.getWidth());
				varietyNumMenu = new PPCPopMenu(context, tv_variety_num, tv_variety_num.getWidth());
			}
		});

		initTopBar();

		tv_crop.setOnClickListener(this);
		tv_variety_name.setOnClickListener(this);
		tv_variety_num.setOnClickListener(this);
		btn_sure_crop.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_crop:
			initCrop();
			cropMenu.showAsDropDown();
			cropMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					currentCrop = cropList.get(position);
					if (!currentCrop.equals(lastCrop)) {
						tv_crop.setText(currentCrop);
						tv_variety_name.setText("品种");
						tv_variety_num.setText("编号");
						currentVariety = null;
						if (varietyList != null) {
							varietyList.clear();
							varietyNameMenu.addItems(varietyList);
						}
						if (varietyNumList != null) {
							varietyNumList.clear();
							varietyNumMenu.addItems(varietyNumList);
						}
						varietyId = "";
						lastCrop = currentCrop;
					}
					
					if("其他".equals(currentCrop)){
						varietyId = "4";
						tv_variety_name.setClickable(false);
						tv_variety_num.setClickable(false);
						showToast("更多作物正在完善，敬请期待,请点击确定按钮");
					} else {
						tv_variety_name.setClickable(true);
						tv_variety_num.setClickable(true);
					}
					
					queryVariety(currentCrop);

					cropMenu.dismiss();
				}
			});
			break;
		case R.id.tv_variety_name:
			if (TextUtils.isEmpty(currentCrop)) {
				showToast("请先选择作物");
				return;
			}
			if (isHasData) {
				varietyNameMenu.showAsDropDown();
				varietyNameMenu.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						currentVariety = varietyList.get(position);
						if (!currentVariety.equals(lastVariety)) {
							tv_variety_name.setText(currentVariety);
							tv_variety_num.setText("编号");
							
							if (varietyNumList != null) {
								varietyNumList.clear();
								varietyNumMenu.addItems(varietyNumList);
							}
							varietyId = "";
							lastVariety = currentVariety;
						}

						varietyNumMap = varietyInfo.data.get(currentVariety);
						varietyNumList = new ArrayList<String>(varietyNumMap.keySet());
						initVarietyNum();
						tv_variety_name.setText(currentVariety);
						varietyNameMenu.dismiss();
					}
				});
			} else {
				// TODO MING:这里会不会出现无数据情况
				showToast("更多作物正在完善，敬请期待");
			}
			break;
		case R.id.tv_variety_num:
			if (TextUtils.isEmpty(currentVariety)) {
				showToast("请先选择品种");
				return;
			}
			if (isHasData) {
				varietyNumMenu.showAsDropDown();
				varietyNumMenu.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String varietyNum = varietyNumList.get(position);
						varietyId = varietyNumMap.get(varietyNum);
						tv_variety_num.setText(varietyNum);
						varietyNumMenu.dismiss();
					}
				});
			} else {
				// TODO MING:这里会不会出现无数据情况
				showToast("更多作物正在完善，敬请期待");
			}
			break;
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
	 * 获取作物 第一个版本不用该方法 原因：查询数据库大量耗时
	 */
	// private void queryCrop(String county) {
	// // 本地查询
	// VarietyDaoImp dao = new VarietyDaoImp(this);
	// cropList = dao.getVariety(county);
	//
	// if (cropList.contains("水稻")) {
	// cropList.remove("水稻");
	// cropList.add(0, "水稻");
	// } else {
	// cropList.add(0, "水稻");
	// }
	// }

	/**
	 * 第一个版本临时填充 CropList 集合
	 */
	private void initCropList() {
		String[] stringArray = DsnApplication.getContext().getResources().getStringArray(R.array.cropList);
		for (int i = 0; i < stringArray.length; i++) {
			cropList.add(stringArray[i]);
		}
	}

	private void initCrop() {
		cropMenu.addItems(cropList);
	}

	/**
	 * 获取品种信息
	 */
	private void queryVariety(String cropName) {
		if(!DeviceHelper.checkNetWork(this)){
			showToast("请检测您的网络连接");
			return;
		}
		startLoadingDialog();
		RequestService.getInstance().getVarietyList(DsnApplication.getContext(), cropName, villageId, VarietyInfo.class,
				new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							varietyInfo = (VarietyInfo) resultData;
							varietyList = new ArrayList<String>(varietyInfo.data.keySet());
							handler.sendEmptyMessage(0);
						}else {
							// TODO MING:请求不到时会出现空toast
							showToast(resultData.getMessage()+requestCode);
						}
						dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
						showToast("请求失败，请检查网络或稍候再试");
						Logger.d("MING", "msg =============== " + msg);
					}

				});
	}

	/**
	 * 填充品种信息
	 */
	private void initVariety() {
		varietyNameMenu.addItems(varietyList);
	}

	/**
	 * 填充品种编号信息
	 */
	private void initVarietyNum() {
		varietyNumMenu.addItems(varietyNumList);
	}

	private void goToNext() {
		if (TextUtils.isEmpty(varietyId)) {
			showToast("请完善作物信息");
			return;
		}
		
		SharedPreferencesHelper.setString(this, Field.VARIETY_ID, varietyId);
		Intent intent = null;
		if (TextUtils.isEmpty(currentCrop)) {
			showToast("请选择作物");
			return;
		}
		if ("水稻".equals(currentCrop)) {
			intent = new Intent(this, AddFieldActivity8.class);
		} else {
			intent = new Intent(this, AddFieldActivity7.class);
			SharedPreferencesHelper.setString(this, Field.SEEDING_METHOD, "false");
			SharedPreferencesHelper.setString(this, Field.SUBSTAGE_ID, "");
			SharedPreferencesHelper.setString(this, Field.PLANTING_DATE, "");
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
