package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.CpproductbrowseDao;
import com.dasinong.app.database.encyclopedias.PetdisspecbrowseDao;
import com.dasinong.app.database.encyclopedias.VarietybrowseDao;
import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.database.encyclopedias.domain.Varietybrowse;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.PesticideListEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.adapter.DiseaseListAdapter;
import com.dasinong.app.ui.adapter.PesticideListAdapter;
import com.dasinong.app.ui.adapter.VarietyListAdapter;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchVarietyResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private String type;

	private Handler mHandler = new Handler();
	
	private ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		type = getIntent().getStringExtra("type");
		initView();
		setUpView();
		initData();
//		requestData();
		
	}

	private void initData() {
		
		
		new Thread(){
			public void run() {
				VarietybrowseDao dao = new VarietybrowseDao(SearchVarietyResultActivity.this);
				final List<Varietybrowse> query = dao.query(type);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						setAdapter(query);
					}
				});
			};
			
		}.start();
	}

	protected void setAdapter(List<Varietybrowse> query) {
		VarietyListAdapter adapter = new VarietyListAdapter(this, query, false);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_sms_list);
	}

	private void setUpView() {
		mTopbarView.setCenterText("品种大全");
		mTopbarView.setLeftView(true, true);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Varietybrowse item = (Varietybrowse) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchVarietyResultActivity.this, WebBrowserActivity.class);
				intent.putExtra(WebBrowserActivity.URL, NetConfig.getBaikeUrl("variety", item.varietyId+""));
				intent.putExtra(WebBrowserActivity.TITLE, Html.fromHtml(item.varietyName).toString());
				startActivity(intent);
			}
		});
	}

//	private void requestData() {
//		startLoadingDialog();
//		RequestService.getInstance().browseCPProductByModel(this, type, PesticideListEntity.class, new RequestListener() {
//			
//			@Override
//			public void onSuccess(int requestCode, BaseEntity resultData) {
//				dismissLoadingDialog();
//				if(resultData.isOk()){
//					PesticideListEntity entity = (PesticideListEntity) resultData;
//					setAdapter(entity.getData());
//				}else{
//					showToast(resultData.getMessage());
//				}
//			}
//			
//			@Override
//			public void onFailed(int requestCode, Exception error, String msg) {
//				dismissLoadingDialog();
//				
//			}
//		});
//	}
}
