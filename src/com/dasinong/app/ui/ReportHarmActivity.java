package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.view.View;

public class ReportHarmActivity extends BaseActivity {
	
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_harm);
		topbar = (TopbarView) findViewById(R.id.topbar);
		initTopBar();
	}

	private void initTopBar() {
		topbar.setCenterText("举报病虫草害");
		topbar.setLeftView(true, true);
	}
}
