package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.CropInfo;
import com.dasinong.app.entity.VarietyInfo;

import com.dasinong.app.entity.CropNumberList.CropNumber;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.CropAdapter;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.utils.Logger;

public class AddFieldActivity4 extends BaseActivity implements OnClickListener {

	//private List<Crop> cropList = new ArrayList<Crop>();
	//private List<CropName> cropNameList = new ArrayList<CropName>();
	private ImageView iv_one;
	private View view_one;
	private List<String> cropList;
	private List<String> varietyList;
	private List<String> varietyNumList;
	private Map<String, String> varietyNumMap;
	private CropInfo cropInfo;
	private VarietyInfo varietyInfo;
	private TextView tv_crop;
	private TextView tv_variety;
	private TextView tv_variety_num;
	private boolean cropIsGone = true;
	private boolean varietyIsGone = true;
	private boolean numIsGone = true;
	private FrameLayout fl_select_crop;
	private ListView lv_content;
	private CropAdapter cropAdapter;
	private String locationId;
	private String cropId;
	private String varietyId;
	private Button btn_sure_crop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_4);
		tv_crop = (TextView) findViewById(R.id.tv_crop);
		tv_variety = (TextView) findViewById(R.id.tv_variety);
		tv_variety_num = (TextView) findViewById(R.id.tv_variety_num);
		fl_select_crop = (FrameLayout) findViewById(R.id.fl_select_crop);
		lv_content = (ListView) findViewById(R.id.lv_content);
		btn_sure_crop = (Button) findViewById(R.id.btn_sure_crop);
		
//		view_one = findViewById(R.id.view_one);
//		iv_one = (ImageView) findViewById(R.id.iv_one);
		
		locationId = SharedPreferencesHelper.getString(this, Field.VILLAGE_ID, "");
		
		queryCrop();

//		view_one.setBackgroundResource(R.color.color_2BAD2A);
//		iv_one.setImageResource(R.drawable.green_round);
		
		tv_crop.setOnClickListener(this);
		tv_variety.setOnClickListener(this);
		tv_variety_num.setOnClickListener(this);
		btn_sure_crop.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.tv_crop:
			if(cropIsGone){
				fl_select_crop.setVisibility(View.VISIBLE);
			}else{
				fl_select_crop.setVisibility(View.GONE);
			}
			cropIsGone = !cropIsGone;
			
			initCrop();
			break;
		case R.id.tv_variety:
			if(varietyIsGone){
				fl_select_crop.setVisibility(View.VISIBLE);
			}else{
				fl_select_crop.setVisibility(View.GONE);
			}
			varietyIsGone = !varietyIsGone;
			
			queryVariety();
			break;
		case R.id.tv_variety_num:
			if(numIsGone){
				fl_select_crop.setVisibility(View.VISIBLE);
			}else{
				fl_select_crop.setVisibility(View.GONE);
			}
			numIsGone = !numIsGone;
			initVarietyNum();
			break;
		case R.id.btn_sure_crop:
			goToNext();
			break;
		}
	}
	

	/**
	 * 获取植物种类
	 */
	private void queryCrop() {
		
		RequestService.getInstance().getCropList(this, locationId, CropInfo.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					cropInfo = (CropInfo) resultData;
					cropList = new ArrayList<String>(cropInfo.crop.keySet());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				
			}
		});
	}
	
	/**
	 * 获取品种信息
	 */
	private void queryVariety() {
		if(TextUtils.isEmpty(cropId)){
			showToast("请先选择作物");
			return;
		}
		RequestService.getInstance().getVarietyList(DsnApplication.getContext(),cropId ,locationId, VarietyInfo.class,
				new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if(resultData.isOk()){
							Log.d("TAG..........", ((VarietyInfo) resultData).variety.toString());
							varietyInfo = (VarietyInfo) resultData;
							
							varietyList = new ArrayList<String>(varietyInfo.variety.keySet());
							
							initVariety();
						}
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {

					}

				});
	}
	
	/**
	 * 填充植物信息
	 */
	private void initCrop() {
		if(cropList == null ||cropList.size() == 0){
			return;
		}
		if(cropAdapter == null){
			cropAdapter = new CropAdapter(this, cropList, false);
		} else {
			cropAdapter.setData(cropList);
			cropAdapter.notifyDataSetChanged();
		}
		lv_content.setAdapter(cropAdapter);
		
		lv_content.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String crop = cropList.get(position);
				cropId = cropInfo.crop.get(crop);
			}
		});
	}
	/**
	 * 填充品种信息
	 */
	private void initVariety() {
		if(varietyList == null ||varietyList.size() == 0){
			lv_content.setVisibility(View.GONE);
			return;
		}
		
		cropAdapter.setData(varietyList);
		cropAdapter.notifyDataSetChanged();
		
		lv_content.setAdapter(cropAdapter);
		
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String varietyName = varietyList.get(position);
				varietyNumMap = varietyInfo.variety.get(varietyName);
				varietyList = new ArrayList<String>(varietyNumMap.keySet());
			}
		});
		
	}
	/**
	 * 填充品种编号信息
	 */
	private void initVarietyNum() {
		cropAdapter.setData(varietyNumList);
		cropAdapter.notifyDataSetChanged();
		
		lv_content.setAdapter(cropAdapter);
		
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String varietyNumName = varietyNumList.get(position);
				
				varietyId = varietyNumMap.get(varietyNumName);
			}
		});
	}

	private void goToNext() {
		SharedPreferencesHelper.setString(this, Field.VARIETY_ID, varietyId);
		
		Intent intent = new Intent(this, AddFieldActivity5.class);
		startActivity(intent);
	}



//	private void initCropName(int position) {
//		//cropId=190&locationId=10000
//		
//		Log.d("TAG","initCropName");
//		RequestService.getInstance().getVarietyList(DsnApplication.getContext(),"190" ,locationId, CropNameList.class,
//				new NetRequest.RequestListener() {
//
//					@Override
//					public void onSuccess(int requestCode, BaseEntity resultData) {
////						if (resultData.isOk()) {
////							cropNameList = ((CropNameList) resultData).getList();
////
////						} else {
////
////						}
//				
//						Log.d("TAG..........", ((CropNameList) resultData).data.toString());
//					
//					}
//
//					@Override
//					public void onFailed(int requestCode, Exception error, String msg) {
//
//					}
//
//				});
////		if (cropNameList != null && cropNameList.size() != 0) {
////			spn_crop.setAdapter(new ArrayAdapter<CropName>(this, R.layout.spinner_checked_text, cropNameList));
////		}
//
//	}
	
	private void login() {

		
	    RequestService.getInstance().authcodeLoginReg(this, "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

	        @Override
	        public void onSuccess(int requestCode, BaseEntity resultData) {
	        	Log.d("=================TAG", "isOK"+resultData.isOk());
	            if(resultData.isOk()){
	                LoginRegEntity entity = (LoginRegEntity) resultData;
	                
	                AccountManager.saveAccount(AddFieldActivity4.this, entity.getData());
	                //fetchScropType();
	                
	                initVariety();

	            }else{
	                Logger.d("TAG", resultData.getMessage());
	            }
	        }

	        @Override
	        public void onFailed(int requestCode, Exception error, String msg) {

	            Logger.d("TAGerrot","msg"+msg);
	            initVariety();
	        }
	    });
	}
	
	
	
	public void login(View v){
		login();
	}
	
}
