package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SearchItem;
import com.dasinong.app.entity.SearchResultEntity;
import com.dasinong.app.entity.SearchResultEntity.SearchData;
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

public class SearchResultActivity extends BaseActivity {

	private static final String TYPE_DISEASE = "Disease";
	private static final String TYPE_PEST = "Pest";
	private static final String TYPE_WEEDS = "Weeds";
	private static final String TYPE_VARIETY = "Variety";
	private static final String TYPE_CPPRODUCT = "Cpproduct";

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
		RequestService.getInstance().searchWord(this, key, type, SearchResultEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					SearchResultEntity entity = (SearchResultEntity) resultData;
					updateUi(entity);
				} else {
					showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void updateUi(SearchResultEntity entity) {// 病害 虫害 草害 品种 药物
		if (entity == null) {
			return;
		}

		SearchData data = entity.getData();

		List<SearchItem> searchData = new ArrayList<SearchItem>();
		if (!data.getDisease().isEmpty()) {
			SearchItem item = new SearchItem();
			item.setType(true);
			item.setName("病害");
			item.setResId(R.drawable.binghai);
			searchData.add(item);
			searchData.addAll(data.getDisease());
		}
		if (!data.getPest().isEmpty()) {
			SearchItem item = new SearchItem();
			item.setType(true);
			item.setName("虫害");
			item.setResId(R.drawable.chonghai);
			searchData.add(item);
			searchData.addAll(data.getPest());
		}
		if (!data.getWeeds().isEmpty()) {
			SearchItem item = new SearchItem();
			item.setType(true);
			item.setName("草害");
			item.setResId(R.drawable.caohai);
			searchData.add(item);
			searchData.addAll(data.getWeeds());
		}
		if (!data.getVariety().isEmpty()) {
			SearchItem item = new SearchItem();
			item.setType(true);
			item.setName("品种");
			item.setResId(R.drawable.pinzhong);
			searchData.add(item);
			searchData.addAll(data.getVariety());
		}
		if (!data.getCpproduct().isEmpty()) {
			SearchItem item = new SearchItem();
			item.setType(true);
			item.setName("药物");
			item.setResId(R.drawable.nongyao);
			searchData.add(item);
			searchData.addAll(data.getCpproduct());
		}

		if (searchData.isEmpty()) {
			Intent intent = new Intent(this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.URL, "https://www.baidu.com/s?wd=" + keywords);
			intent.putExtra(WebBrowserActivity.TITLE, "搜索结果");
			startActivity(intent);
		}

		mAdapter.setData(searchData);
		// mResultListview.requestFocusFromTouch();
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

		// mSearchEdit.setFocusable(false);
		// mSearchEdit.setEnabled(false);
		// mHandler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// mSearchEdit.setFocusable(true);
		// mSearchEdit.setEnabled(true);
		// }
		// }, 2000);

		mAdapter = new SearchResultAdapter(this, new ArrayList<SearchItem>(), false);
		mResultListview.setAdapter(mAdapter);

		mSearchEdit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

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
				if (item.isType()) {
				} else {

					Intent intent = new Intent();

					if ("pesticide".equals(item.getType())) {
						// 农药
						intent.setClass(SearchResultActivity.this, PesticideDetailActivity.class);
					} else if ("pest".equals(item.getType())) {
						// 病虫草害
						intent.setClass(SearchResultActivity.this, HarmDetailsActivity.class);
					} else if ("variety".equals(item.getType())) {
						// 品种
						intent.setClass(SearchResultActivity.this, VarietyDetailActivity.class);
					}
					intent.putExtra("id", item.getId());
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

		keywords = mSearchEdit.getText().toString().trim();
		if (TextUtils.isEmpty(keywords)) {
			Toast.makeText(SearchResultActivity.this, "请输入要搜索的内容", 0).show();
			return;
		}

		requestData(keywords);
	}

}
