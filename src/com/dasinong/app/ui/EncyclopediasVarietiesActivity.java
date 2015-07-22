package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.EncyclopediasDao;
import com.dasinong.app.database.encyclopedias.domain.Crop;
import com.dasinong.app.ui.adapter.VarietiesFirstListAdapter;
import com.dasinong.app.ui.adapter.VarietiesSecondListAdapter;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @ClassName EncyclopediasVarietiesActivity
 * @author linmu
 * @Decription 品种大全
 * @2015-7-21 下午9:18:30
 */
public class EncyclopediasVarietiesActivity extends BaseActivity {

	private ListView mFirstList;
	private ListView mSecondList;
	
	private Handler mHandler = new Handler();
	
	private EditText mSearchEdit;
	private ImageView mSearchView;
	private TopbarView mTopbarView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_varieties);
		
		initView();
		setUpView();
		initData("粮食作物");
	}

	private void initData(final String type) {
		
		startLoadingDialog();
		new Thread(){
			public void run() {
				EncyclopediasDao dao = new EncyclopediasDao(EncyclopediasVarietiesActivity.this);
				final List<Crop> queryStageCategory = dao.queryStageCategory(type);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						dismissLoadingDialog();
						setAdapter(queryStageCategory);
					}

				});
			};
		}.start();
		
	}

	private void setAdapter(List<Crop> queryStageCategory) {
		VarietiesSecondListAdapter adapter = new VarietiesSecondListAdapter(this, queryStageCategory, false);
		mSecondList.setAdapter(adapter);
	}
	
	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		
		mFirstList = (ListView) this.findViewById(R.id.listview_type_list_first);
		mSecondList = (ListView) this.findViewById(R.id.listview_type_list_second);
		mSearchView = (ImageView) this.findViewById(R.id.imageview_search);
		mSearchEdit = (EditText) this.findViewById(R.id.edittext_search);
	}

	private void setUpView() {
		
		mTopbarView.setCenterText("品种大全");
		mTopbarView.setLeftView(true, true);
		
		VarietiesFirstListAdapter adapter = new VarietiesFirstListAdapter(this, null, false);
		mFirstList.setAdapter(adapter);
		
		mFirstList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String type = (String) mFirstList.getItemAtPosition(position);
				initData(type);
			}
		});
		
		mSecondList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Crop crop = (Crop) parent.getItemAtPosition(position);
				Intent intent = new Intent(EncyclopediasVarietiesActivity.this,SearchVarietyResultActivity.class);
				intent.putExtra("type", crop.cropId+"");
				startActivity(intent);
			}
		});
		
		
		mSearchEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
					
//					DeviceHelper.hideIME(mSearchEdit);
					
					search();
					return true;
				}
				return false;
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
		String keywords = mSearchEdit.getText().toString().trim();
		if(TextUtils.isEmpty(keywords)){
			Toast.makeText(this, "请输入要搜索的内容", 0).show();
			return;
		}
		
		Intent intent = new Intent(this,SearchResultActivity.class);
		intent.putExtra("keywords", keywords);
		this.startActivity(intent);
	}
	
}
