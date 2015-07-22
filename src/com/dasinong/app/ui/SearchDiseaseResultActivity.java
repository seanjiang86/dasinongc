package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.PetdisspecbrowseDao;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.DiseaseListAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

public class SearchDiseaseResultActivity extends BaseActivity {

	private ListView mListview;
	
	private Handler mHandler = new Handler();

	private String type;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		initView();
		setUpView();
		initData();
		requestData();
		
	}

	private void initData() {
		
		type = getIntent().getStringExtra("type");
		
		new Thread(){
			public void run() {
				PetdisspecbrowseDao dao = new PetdisspecbrowseDao(SearchDiseaseResultActivity.this);
				final List<Petdisspecbrowse> query = dao.query(type);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						setAdapter(query);
					}
				});
			};
			
		}.start();
	}

	protected void setAdapter(List<Petdisspecbrowse> query) {
		DiseaseListAdapter adapter = new DiseaseListAdapter(this, query, false);
		mListview.setAdapter(adapter);
	}

	private void initView() {
		mListview = (ListView) this.findViewById(R.id.list_sms_list);
	}

	private void setUpView() {
		// TODO Auto-generated method stub
		
	}

	private void requestData() {
		RequestService.getInstance().browsePetDisByType(this, "虫害", BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				
			}
		});
	}
	
}
