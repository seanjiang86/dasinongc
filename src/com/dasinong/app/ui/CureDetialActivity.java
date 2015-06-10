package com.dasinong.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.CPProduct;
import com.dasinong.app.database.disaster.domain.PetSolu;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.ui.adapter.CureAdapter;
import com.dasinong.app.ui.adapter.MyBaseAdapter;

import android.os.Bundle;
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
	private List<CPProduct> drugList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cure_detials);
		lv_medicine = (ListView) findViewById(R.id.lv_medicine);

		tv_cure_name = (TextView) findViewById(R.id.tv_cure_name);
		tv_cure_stage = (TextView) findViewById(R.id.tv_cure_stage);
		tv_cure_provider = (TextView) findViewById(R.id.tv_cure_provider);
		tv_cure_content = (TextView) findViewById(R.id.tv_cure_content);

		PetSolu solu = (PetSolu) getIntent().getExtras().getSerializable("solu");
		
		//TODO MING:等待数据
		tv_cure_name.setText("防治方案A");
		tv_cure_stage.setText("种子阶段 等待数据");
		tv_cure_provider.setText("提供者  等待数据");
		tv_cure_content.setText(solu.petSoluDes);
		
		initData(solu.petSoluId);
	}

	private void initData(int petSoluId) {
		drugList = DisasterManager.getInstance(this).getAllDrug(petSoluId);
		initListView();
	}

	private void initListView() {
		lv_medicine.setAdapter(new CureAdapter(this, drugList, false));
		
		lv_medicine.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showToast("open url");
			}
		});
	}
}
