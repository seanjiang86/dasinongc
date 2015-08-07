package com.dasinong.app.ui;

import java.util.Date;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;
import com.mob.tools.utils.Data;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ContactUsActivity extends BaseActivity {
	private TextView btn_send_email;
	private TextView btn_call_phone;
	private TopbarView topBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);
		
		btn_send_email = (TextView) findViewById(R.id.btn_send_email);
		btn_call_phone = (TextView) findViewById(R.id.btn_call_phone);
		topBar = (TopbarView) findViewById(R.id.topbar);
		
		initTopBar();
		
		
		btn_send_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String[] email = {"cs@dasinong.net"}; // 需要注意，email必须以数组形式传入  
				Intent intent = new Intent(Intent.ACTION_SEND);  
				intent.setType("message/rfc822"); // 设置邮件格式  
				intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人  
				intent.putExtra(Intent.EXTRA_CC, email); // 抄送人  
				intent.putExtra(Intent.EXTRA_SUBJECT, "安卓用户反馈"); // 主题  
				intent.putExtra(Intent.EXTRA_TEXT, "请填写您的手机号，并描述您遇到的问题"); // 正文  
				startActivity(Intent.createChooser(intent, "请选择邮件类应用")); 
			}
		});
		
		
		btn_call_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Time time = new Time();
				time.setToNow();
				int hour = time.hour;
				
				if(hour > 18 || hour < 10){
					// TODO MING : 设置提示语
				} else {
					Intent intent = new Intent();
					//这个意图就是调用系统的拨打活动
					intent.setAction(Intent.ACTION_CALL);
					//设置要拨打的电话号号码
					intent.setData(Uri.parse("tel:4000556050"));//uriString的格式为“tel:电话号码”
					//开始调整到打电话的活动
					startActivity(intent);
				}
			}
		});
	}

	private void initTopBar() {
		topBar.setCenterText("联系我们");
		topBar.setLeftView(true, true);
	}
}
