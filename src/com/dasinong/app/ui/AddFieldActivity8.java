package com.dasinong.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dasinong.app.R;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;

public class AddFieldActivity8 extends BaseActivity implements OnClickListener{
	private Button btn_direct_seeding;
	private Button btn_transplanting;
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_8);
		
		btn_direct_seeding = (Button) findViewById(R.id.btn_direct_seeding);
		btn_transplanting = (Button) findViewById(R.id.btn_transplanting);
		topbar = (TopbarView) findViewById(R.id.topbar);
		
		initTopBar();
		
		btn_direct_seeding.setOnClickListener(this);
		btn_transplanting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_direct_seeding:
			SharedPreferencesHelper.setString(this, Field.SEEDING_METHOD, "direct");
			break;
		case R.id.btn_transplanting:
			SharedPreferencesHelper.setString(this, Field.SEEDING_METHOD, "transplanting");
			break;
		}
		Intent intent = new Intent(this, AddFieldActivity5.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	
	private void initTopBar() {
		topbar.setCenterText("种植方式");
		topbar.setLeftView(true, true);
	}
}
