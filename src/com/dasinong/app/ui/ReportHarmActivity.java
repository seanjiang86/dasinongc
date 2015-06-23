package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ReportHarmActivity extends BaseActivity {
	
	private TopbarView topbar;
	private EditText et_des;
	private Button btn_upload_images;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_harm);
		topbar = (TopbarView) findViewById(R.id.topbar);
		et_des = (EditText) findViewById(R.id.et_des);
		btn_upload_images = (Button) findViewById(R.id.btn_upload_images);
		
		initTopBar();
		
	}

	private void initTopBar() {
		topbar.setCenterText("举报病虫草害");
		topbar.setLeftView(true, true);
	}
	
	
}
