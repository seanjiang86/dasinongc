package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.variety.dao.impl.VarietyDaoImp;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.CropInfo;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.entity.VarietyInfo;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.CropAdapter;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.Logger;

public class AddFieldActivity4 extends MyBaseActivity implements OnClickListener {

	// private List<Crop> cropList = new ArrayList<Crop>();
	// private List<CropName> cropNameList = new ArrayList<CropName>();
	private List<String> cropList;
	private List<String> varietyList;
	private List<String> varietyNumList;
	private Map<String, String> varietyNumMap;
	private CropInfo cropInfo;
	private VarietyInfo varietyInfo;
	private CropAdapter cropAdapter;
	private String locationId;
	private String varietyId;
	private Button btn_sure_crop;
	private TopbarView topbar;
	private Spinner spn_crop;
	private Spinner spn_variety_name;
	private Spinner spn_variety_num;
	private CropAdapter nameAdapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initVariety();
		};
	};
	private CropAdapter numAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_4);
		spn_crop = (Spinner) findViewById(R.id.spn_crop);
		spn_variety_name = (Spinner) findViewById(R.id.spn_variety_name);
		spn_variety_num = (Spinner) findViewById(R.id.spn_variety_num);
		btn_sure_crop = (Button) findViewById(R.id.btn_sure_crop);
		topbar = (TopbarView) findViewById(R.id.topbar);

		// TODO MING:此处默认值如何设置
		locationId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "");
		String county = SharedPreferencesHelper.getString(this, Field.COUNTY, "");

		county = county.substring(0, county.length() - 1);
		queryCrop(county);

		initTopBar();
		btn_sure_crop.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		goToNext();
	}

	private void initTopBar() {
		topbar.setCenterText("作物信息");
		topbar.setLeftView(true, true);
	}

	/**
	 * 获取植物种类
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

		initCrop();
	}

	/**
	 * 填充植物信息
	 */
	private void initCrop() {
		if (cropList == null || cropList.size() == 0) {
			return;
		}
		spn_crop.setAdapter(new CropAdapter(this, cropList, false));

		spn_crop.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	/**
	 * 获取品种信息
	 */
	private void queryVariety(String cropName) {
		// TODO MING:此处参数为假参数 需要修改
		RequestService.getInstance().getVarietyList(DsnApplication.getContext(), "190", "10000", VarietyInfo.class, new NetRequest.RequestListener() {

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
		nameAdapter = new CropAdapter(this, varietyList, false);
		spn_variety_name.setAdapter(nameAdapter);
		spn_variety_name.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	/**
	 * 填充品种编号信息
	 */
	private void initVarietyNum() {
		numAdapter = new CropAdapter(this, varietyNumList, false);
		spn_variety_num.setAdapter(numAdapter);
		spn_variety_num.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	private void goToNext() {
		SharedPreferencesHelper.setString(this, Field.VARIETY_ID, varietyId);

		Intent intent = new Intent(this, AddFieldActivity8.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	class MyOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			int spnId = parent.getId();
			switch (spnId) {
			case R.id.spn_crop:
				String crop = cropList.get(position);
				if ("水稻".equals(crop)) {
					queryVariety(crop);
				} else {
					varietyList.clear();
					varietyNumList.clear();
					nameAdapter.setData(varietyList);
					nameAdapter.notifyDataSetChanged();
					
					numAdapter.setData(varietyNumList);
					numAdapter.notifyDataSetChanged();
				}
				break;
			case R.id.spn_variety_name:
				String varietyName = varietyList.get(position);
				varietyNumMap = varietyInfo.data.get(varietyName);
				varietyNumList = new ArrayList<String>(varietyNumMap.keySet());
				initVarietyNum();
				break;
			case R.id.spn_variety_num:
				String varietyNum = varietyNumList.get(position);
				varietyId = varietyNumMap.get(varietyNum);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	// private void initCropName(int position) {
	// //cropId=190&locationId=10000
	//
	// Log.d("TAG","initCropName");
	// RequestService.getInstance().getVarietyList(DsnApplication.getContext(),"190"
	// ,locationId, CropNameList.class,
	// new NetRequest.RequestListener() {
	//
	// @Override
	// public void onSuccess(int requestCode, BaseEntity resultData) {
	// // if (resultData.isOk()) {
	// // cropNameList = ((CropNameList) resultData).getList();
	// //
	// // } else {
	// //
	// // }
	//
	// Log.d("TAG..........", ((CropNameList) resultData).data.toString());
	//
	// }
	//
	// @Override
	// public void onFailed(int requestCode, Exception error, String msg) {
	//
	// }
	//
	// });
	// // if (cropNameList != null && cropNameList.size() != 0) {
	// // spn_crop.setAdapter(new ArrayAdapter<CropName>(this,
	// R.layout.spinner_checked_text, cropNameList));
	// // }
	//
	// }

}
