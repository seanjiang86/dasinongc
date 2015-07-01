package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

public class SearchResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private EditText mSearchEdit;
	private ListView mResultListview;
	
	private String keywords;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_result);
		
		initData();
		initView();
		setUpView();
		requestData();
	}

	private void requestData() {
		RequestService.getInstance().searchWord(this, keywords, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				
			}
		});
	}

	private void initData() {
		keywords = getIntent().getStringExtra("keywords");
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mSearchEdit = (EditText) this.findViewById(R.id.edittext_search);
		mResultListview = (ListView) this.findViewById(R.id.listview_search_result);
	}

	private void setUpView() {
		mTopbarView.setCenterText("搜索结果");
		mTopbarView.setLeftView(true, true);
		
		mSearchEdit.setText(keywords);
		mSearchEdit.setSelection(mSearchEdit.getText().length());
		
		mSearchEdit.setFocusable(false);
	}
	
}
