package com.dasinong.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.CPProduct;
import com.dasinong.app.database.disaster.domain.PetSolu;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.DrugListEntity;
import com.dasinong.app.entity.DrugListEntity.Drug;
import com.dasinong.app.entity.HarmDetialEntity.Solutions;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.CureAdapter;
import com.dasinong.app.ui.adapter.MyBaseAdapter;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CureDetialActivity extends BaseActivity {

	private ListView lv_medicine;
	private TextView tv_cure_name;
	private TextView tv_cure_stage;
	private TextView tv_cure_provider;
	private TextView tv_cure_content;
	private TopbarView topbar;
	private View header;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			DrugListEntity drugList = (DrugListEntity) msg.obj;
			initHeader(drugList.data.petSolutions);
			initListView(drugList.data.cPProducts);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cure_detials);
		lv_medicine = (ListView) findViewById(R.id.lv_medicine);
		topbar = (TopbarView) findViewById(R.id.topbar);
		
		header = View.inflate(this, R.layout.cure_detials_header, null);

		tv_cure_name = (TextView) header.findViewById(R.id.tv_cure_name);
		tv_cure_stage = (TextView) header.findViewById(R.id.tv_cure_stage);
		tv_cure_provider = (TextView) header.findViewById(R.id.tv_cure_provider);
		tv_cure_content = (TextView) header.findViewById(R.id.tv_cure_content);

		initTopBar();

		Solutions solu = (Solutions) getIntent().getExtras().getSerializable("solu");
		int position = getIntent().getExtras().getInt("position");
		int size = getIntent().getExtras().getInt("size");

		if (position > size) {
			// 预防
			char c = (char) (65 + position - size - 1);
			tv_cure_name.setText("预防方案" + c);
		} else {
			// 治疗
			char c = (char) (65 + position - 1);
			tv_cure_name.setText("治疗方案" + c);
		}
		// initData(solu.petSoluId);
		// TODO MING 临时数据
		if (DeviceHelper.checkNetWork(this)) {
			queryDrug(8100);
		} else {
			initHeader(solu);
			initData(8100);
		}
	}

	private void initHeader(Solutions solu) {
		// TODO MING:等待数据
		if (TextUtils.isEmpty(solu.subStageId)) {
			tv_cure_stage.setVisibility(View.GONE);
		} else {
			tv_cure_stage.setText(solu.subStageId);
		}
		tv_cure_provider.setText(solu.providedBy);
		tv_cure_content.setText(solu.petSoluDes);
		
	}

	private void queryDrug(int petSoluId) {
		startLoadingDialog();
		RequestService.getInstance().getPetSolu(this, petSoluId, DrugListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					DrugListEntity drugList = (DrugListEntity) resultData;
					
					Message msg = handler.obtainMessage();
					msg.obj = drugList;
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

	private void initTopBar() {
		topbar.setCenterText("防治方案详情");
		topbar.setLeftView(true, true);
	}

	private void initData(int petSoluId) {
		List<CPProduct> drugList = DisasterManager.getInstance(this).getAllDrug(petSoluId);
		//TODO MING 增加本地查询
//		initListView(drugList);
	}

	private void initListView(List<Drug> drugList) {
		lv_medicine.addHeaderView(header, null, false);
		lv_medicine.setAdapter(new CureAdapter(this, drugList, false));

		lv_medicine.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showToast("open url");
			}
		});
	}
}
