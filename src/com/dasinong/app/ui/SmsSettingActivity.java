package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SmsSubscribeEntity;
import com.dasinong.app.entity.SmsSubscribeItem;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.SmsSubscribeAdapter;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SmsSettingActivity extends BaseActivity {

	private static final int ADD_SMS_SUBSCRIBE = 100;
	private static final int MODIFI_SMS_SUBSCRIBE = ADD_SMS_SUBSCRIBE + 1;
	
	private TopbarView mTopbarView;
	private ListView mSmsList;
	private List<SmsSubscribeItem> convertData = new ArrayList<SmsSubscribeItem>();
	private SmsSubscribeAdapter mAdapter;
	private boolean isDelete = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sms_sub_list);
		
		initView();
		setUpView();
		getSubScribeLists();
	}

	private void getSubScribeLists() {
		startLoadingDialog();
		RequestService.getInstance().getSubScribeLists(this, SmsSubscribeEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					SmsSubscribeEntity result = (SmsSubscribeEntity) resultData;
					List<SmsSubscribeItem> convertData = result.getConvertData();
					setAdapter(convertData);
				}else{
					
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void setAdapter(List<SmsSubscribeItem> convertData) {
		this.convertData = convertData;
		mAdapter = new SmsSubscribeAdapter(this, convertData, false);
		setDeleteState(false);
		mTopbarView.setRightText("编辑");
		mSmsList.setAdapter(mAdapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mSmsList = (ListView) this.findViewById(R.id.list_sms_list);
	}

	private void setUpView() {
		mTopbarView.setCenterText("免费订阅短信版今日农事");
		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("编辑");
		mTopbarView.setRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isDelete){
					setDeleteState(false);
					mTopbarView.setRightText("编辑");
				}else{
					setDeleteState(true);
					mTopbarView.setRightText("完成");
				}
				
				isDelete = ! isDelete;
			}
		});
		
		View view = View.inflate(this, R.layout.layout_sms_sub_list_footer, null);
		Button button = (Button) view.findViewById(R.id.button_sms_sub);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SmsSettingActivity.this,SmsSubscribeActivity.class);
				startActivityForResult(intent, ADD_SMS_SUBSCRIBE);
			}
		});
		mSmsList.addFooterView(view);
		
		mSmsList.setAdapter(new SmsSubscribeAdapter(this, convertData, false));
		
		mSmsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SmsSubscribeItem smsSubscribeItem = convertData.get(position);
				Intent intent = new Intent(SmsSettingActivity.this,SmsSubscribeModifiActivity.class);
				intent.putExtra("subId", smsSubscribeItem.getId());
				startActivityForResult(intent, MODIFI_SMS_SUBSCRIBE);
			}
		});
	}

	protected void setDeleteState(boolean b) {
		mAdapter.setDeleteState(b);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == RESULT_OK){
			getSubScribeLists();
		}
	}
	
}
