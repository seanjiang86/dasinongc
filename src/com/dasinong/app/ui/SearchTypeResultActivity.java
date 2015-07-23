package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SearchItem;
import com.dasinong.app.entity.SearchResultEntity;
import com.dasinong.app.entity.SearchResultEntity.SearchData;
import com.dasinong.app.entity.SearchTypeResultEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.SearchResultAdapter;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.ViewHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchTypeResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private EditText mSearchEdit;
	private ListView mResultListview;
	
	private String keywords;
	private String type;

	private SearchResultAdapter mAdapter;
	
	private ImageView mSearchView;
	
	private Handler mHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_result);
		
		initData();
		initView();
		setUpView();
		requestData(keywords);
	}

	private void requestData(String key) {
		startLoadingDialog();
		RequestService.getInstance().searchWord(this, key,type, SearchTypeResultEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					SearchTypeResultEntity entity = (SearchTypeResultEntity) resultData;
					updateUi(entity);
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
				showToast(R.string.please_check_netword);
			}
		});
	}

	protected void updateUi(SearchTypeResultEntity entity) {//病害 虫害 草害 品种  药物
		if(entity == null){
			return;
		}
		
		mAdapter.setData(entity.getData());
//		mResultListview.requestFocusFromTouch();
		mResultListview.setSelection(0);
		ViewHelper.setListVIewEmptyView(this, mResultListview);
	}

	private void initData() {
		keywords = getIntent().getStringExtra("keywords");
		type = getIntent().getStringExtra("type");
		
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mSearchEdit = (EditText) this.findViewById(R.id.edittext_search);
		mResultListview = (ListView) this.findViewById(R.id.listview_search_result);
		
		mSearchView = (ImageView) this.findViewById(R.id.imageview_search);
	}

	private void setUpView() {
		mTopbarView.setCenterText("搜索结果");
		mTopbarView.setLeftView(true, true);
		
		mSearchEdit.setText(keywords);
		mSearchEdit.setSelection(mSearchEdit.getText().length());
		
//		mSearchEdit.setFocusable(false);
//		mSearchEdit.setEnabled(false);
//		mHandler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				mSearchEdit.setFocusable(true);
//				mSearchEdit.setEnabled(true);
//			}
//		}, 2000);
		
		mAdapter = new SearchResultAdapter(this, new ArrayList<SearchItem>(), false);
		mResultListview.setAdapter(mAdapter);
		
		
		mSearchEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
					
					search();
					
					return true;
				}
				return false;
			}

		});
		
		mResultListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SearchItem item = (SearchItem) parent.getItemAtPosition(position);
				if(item.isType()){
				} else {
					Intent intent = new Intent(SearchTypeResultActivity.this, WebBrowserActivity.class);
					intent.putExtra(WebBrowserActivity.URL, NetConfig.getBaikeUrl(item.getType(), item.getId()));
					if(TextUtils.isEmpty(item.getName())){
						intent.putExtra(WebBrowserActivity.TITLE, Html.fromHtml(item.getSource()).toString());
					}else{
						intent.putExtra(WebBrowserActivity.TITLE, Html.fromHtml(item.getName()).toString());
					}
					startActivity(intent);
				}
			}
		});
		
		mSearchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				search();
			}
		});
		
	}
	
	private void search() {
		DeviceHelper.hideIME(mSearchEdit);
		
		String keywords = mSearchEdit.getText().toString().trim();
		if(TextUtils.isEmpty(keywords)){
			Toast.makeText(SearchTypeResultActivity.this, "请输入要搜索的内容", 0).show();
			return;
		}
		
		requestData(keywords);
	}
	
}
