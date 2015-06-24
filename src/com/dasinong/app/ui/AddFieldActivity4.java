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
import com.dasinong.app.entity.CropInfo;
import com.dasinong.app.entity.VarietyInfo;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.PPCPopMenu;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.Logger;

public class AddFieldActivity4 extends MyBaseActivity implements OnClickListener {

	private List<String> cropList;
	private List<String> varietyList;
	private List<String> varietyNumList;
	private Map<String, String> varietyNumMap;
	private CropInfo cropInfo;
	private VarietyInfo varietyInfo;
	private String locationId;
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
	private String variety;

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

		// TODO MING:此处默认值如何设置
		locationId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "");
		String county = SharedPreferencesHelper.getString(this, Field.COUNTY, "");
		String fieldSize = SharedPreferencesHelper.getString(this, Field.FIELD_SIZE, "50");
		int intFieldSize = Integer.valueOf(fieldSize);
		if(intFieldSize <= 10){
			tv_user_call.setText("呦,自给自足啊,都种了");
		} else if (10 < intFieldSize && intFieldSize <= 200){
			tv_user_call.setText("呦,大户啊,都种了");
		} else if(200 < intFieldSize && intFieldSize <= 1000){
			tv_user_call.setText("呦,超级大户啊,都种了");
		} else {
			tv_user_call.setText("呦,农场主啊,都种了");
		}
		
		
		
		// TODO MING:此处在修改数据库之后需要重新查询
		county = county.substring(0, county.length() - 1);
		queryCrop(county);

		final Context context = AddFieldActivity4.this;
		tv_crop.post(new Runnable() {

			@Override
			public void run() {
				cropMenu = new PPCPopMenu(context,tv_crop,tv_crop.getWidth());
				varietyNameMenu = new PPCPopMenu(context,tv_variety_name,tv_variety_name.getWidth());
				varietyNumMenu = new PPCPopMenu(context,tv_variety_num,tv_variety_num.getWidth());
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
						variety = null;
						if (varietyList != null) {
							varietyList.clear();
							varietyNameMenu.addItems(varietyList);
						}
						if (varietyNumList != null) {
							varietyNumList.clear();
							varietyNumMenu.addItems(varietyNumList);
						}
						lastCrop = currentCrop;
					}
					// TODO MING:正式上线取消该判断
					if ("水稻".equals(currentCrop)) {
						currentCrop = "稻";
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
						variety = varietyList.get(position);
						
						varietyNumMap = varietyInfo.data.get(variety);
						varietyNumList = new ArrayList<String>(varietyNumMap.keySet());
						initVarietyNum();
						tv_variety_name.setText(variety);
						varietyNameMenu.dismiss();
					}
				});
			} else {
				// TODO MING:这里会不会出现无数据情况
				showToast("更多作物正在完善，敬请期待");
			}
			break;
		case R.id.tv_variety_num:
			if (TextUtils.isEmpty(variety)) {
				showToast("请先选择品种");
				return;
			}
			if (isHasData) {
				varietyNumMenu.showAsDropDown();
				varietyNumMenu.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String varietyNum = varietyNumList.get(position);
						String varietyId = varietyNumMap.get(varietyNum);
						System.out.println(varietyId);
						tv_variety_num.setText(varietyNum);
						varietyNumMenu.dismiss();
					}
				});
			} else {
				showToast("更多作物正在完善，敬请期待");
			}
			break;
		case R.id.btn_sure_crop:
//			handler.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					goToNext();
//					
//				}
//			});
			goToNext();
			
			break;
		}
	}

	private void initTopBar() {
		topbar.setCenterText("作物信息");
		topbar.setLeftView(true, true);
	}

	/**
	 * 获取作物
	 */
	private void queryCrop(String county) {
		// 本地查询
		VarietyDaoImp dao = new VarietyDaoImp(this);
		cropList = dao.getVariety(county);

		if (cropList.contains("水稻")) {
			cropList.remove("水稻");
			cropList.add(0, "水稻");
		} else {
			cropList.add(0, "水稻");
		}

		// TODO MING:此处如果没有数据添加什么样的数据 待定
		// if (cropList == null || cropList.size() == 0) {
		// cropList.add("水稻");
		// cropList.add("还是水稻");
		// cropList.add("你一定要种水稻");
		// cropList.add("种水稻啊");
		// cropList.add("种水稻呀");
		// cropList.add("就是种水稻");
		// } else {
		//
		// }

		// TODO MING:以下为网络查询过程，不可删除
		// RequestService.getInstance().getCropList(this, locationId,
		// CropInfo.class, new RequestListener() {
		//
		// @Override
		// public void onSuccess(int requestCode, BaseEntity resultData) {
		// if (resultData.isOk()) {
		// cropInfo = (CropInfo) resultData;
		// cropList = new ArrayList<String>(cropInfo.crop.keySet());
		// }
		// }
		//
		// @Override
		// public void onFailed(int requestCode, Exception error, String msg) {
		//
		// }
		// });

	}

	private void initCrop() {
		cropMenu.addItems(cropList);
	}

	/**
	 * 获取品种信息
	 */
	private void queryVariety(String cropName) {
		// TODO MING:此处参数为假参数 需要修改
		RequestService.getInstance().getVarietyList(DsnApplication.getContext(), "223", "10000", VarietyInfo.class, new NetRequest.RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					varietyInfo = (VarietyInfo) resultData;
					varietyList = new ArrayList<String>(varietyInfo.data.keySet());
					handler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
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
		SharedPreferencesHelper.setString(this, Field.VARIETY_ID, varietyId);
		Intent intent = null;
		
		System.out.println(currentCrop);
		
		if("水稻".equals(lastCrop)){
			intent = new Intent(this, AddFieldActivity8.class);
		} else {
			intent = new Intent(this, AddFieldActivity7.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
