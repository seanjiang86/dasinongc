package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.fragment.HomeFragment;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddFieldActivity7 extends BaseActivity {
	private EditText et_target_production;
	private Button btn_finish_add_field;
	private String target;
	private TopbarView topbar;
	private  Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			sendFieldInfo();
		};
	} ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_7);
		
		et_target_production = (EditText) findViewById(R.id.et_target_production);
		btn_finish_add_field = (Button) findViewById(R.id.btn_finish_add_field);
		topbar = (TopbarView) findViewById(R.id.topbar);
		
		initTopBar();
		
		btn_finish_add_field.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				target = et_target_production.getText().toString().trim();
				handler.sendEmptyMessage(0);
			}
		});
	}
	
	private void initTopBar() {
		topbar.setCenterText("完成添加");
		topbar.setLeftView(true, true);
	}
	
	protected void sendFieldInfo() {
		// TODO Ming：发送请求
		
		Intent intent = new Intent(this, MainTabActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
}
