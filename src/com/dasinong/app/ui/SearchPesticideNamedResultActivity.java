package com.dasinong.app.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.CpproductbrowseDao;
import com.dasinong.app.database.encyclopedias.CpproductbrowseDao;
import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.PesticideListEntity;
import com.dasinong.app.entity.PesticideNamedListEntity;
import com.dasinong.app.entity.PesticideNamedListEntity.Pesticide;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.adapter.DiseaseListAdapter;
import com.dasinong.app.ui.adapter.PesticideListAdapter;
import com.dasinong.app.ui.adapter.PesticideNamedListAdapter;
import com.dasinong.app.ui.view.LetterView;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.LetterView.OnTouchingLetterChangedListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchPesticideNamedResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private String type;

	private ListView mListView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		type = getIntent().getStringExtra("type");
		
		initView();
		setUpView();
		requestData();
	}


	protected void setAdapter(List<Pesticide> query) {
		
		if(query!=null && query.size()==1){
			Pesticide item = query.get(0);
			Intent intent = new Intent(SearchPesticideNamedResultActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.URL, NetConfig.getBaikeUrl("pesticide", item.getId()+""));
			intent.putExtra(WebBrowserActivity.TITLE, Html.fromHtml(item.getActiveIngredient()).toString());
			startActivity(intent);
			finish();
			return;
		}
		
		PesticideNamedListAdapter adapter = new PesticideNamedListAdapter(this, query, false);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_sms_list);
		
		findViewById(R.id.letterview).setVisibility(View.GONE);
		
	}

	private void setUpView() {
		mTopbarView.setCenterText(type);
		mTopbarView.setLeftView(true, true);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Pesticide item = (Pesticide) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchPesticideNamedResultActivity.this, WebBrowserActivity.class);
				intent.putExtra(WebBrowserActivity.URL, NetConfig.getBaikeUrl("pesticide", item.getId()+""));
				intent.putExtra(WebBrowserActivity.TITLE, Html.fromHtml(item.getActiveIngredient()).toString());
				startActivity(intent);
			}
		});
	}

	private void requestData() {
		startLoadingDialog();
		RequestService.getInstance().getCPProdcutsByIngredient(this, type, PesticideNamedListEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					PesticideNamedListEntity entity = (PesticideNamedListEntity) resultData;
					setAdapter(entity.getData());
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
				
			}
		});
	}
	
}
