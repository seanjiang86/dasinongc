package com.dasinong.app.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.entity.PetDisSpecsListEntity;
import com.dasinong.app.entity.PetDisSpecsListEntity.PetDisSpecs;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.adapter.DiseaseListAdapter;
import com.dasinong.app.ui.view.TopbarView;

public class SearchDiseaseResultActivity extends BaseActivity {

	private ListView mListview;

	private String type;
	private String cropId;

	protected List<Petdisspecbrowse> query;

	private TopbarView mTopbarView;

	private PetDisSpecsListEntity entity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);

		entity = (PetDisSpecsListEntity) getIntent().getExtras().getSerializable("data");

		type = getIntent().getStringExtra("type");
		cropId = getIntent().getStringExtra("cropId");

		initView();
		setUpView();

	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);

		mListview = (ListView) this.findViewById(R.id.list_sms_list);

	}

	private void setUpView() {
		mTopbarView.setCenterText(type);
		mTopbarView.setLeftView(true, true);

		DiseaseListAdapter adapter = new DiseaseListAdapter(this, entity.data, false);
		mListview.setAdapter(adapter);

		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PetDisSpecs item = (PetDisSpecs) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchDiseaseResultActivity.this, HarmDetailsActivity.class);
				intent.putExtra("id", item.petDisSpecId + "");
				startActivity(intent);
			}
		});
	}
}
