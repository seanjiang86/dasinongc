package com.dasinong.app.ui;

import java.util.Calendar;

import com.dasinong.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class AddFieldActivity6 extends BaseActivity implements OnClickListener{
	private DatePicker dp_date;
	private Button btn_no_date;
	private Button btn_sure_date;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_add_field_6);
		
		
		dp_date = (DatePicker) findViewById(R.id.dp_date);
		btn_no_date = (Button) findViewById(R.id.btn_no_date);
		btn_sure_date = (Button) findViewById(R.id.btn_sure_date);
		
		btn_no_date.setOnClickListener(this);
		btn_sure_date.setOnClickListener(this);
	        
	}

	@Override
	public void onClick(View v) {
		int year = dp_date.getYear();
		int month = dp_date.getMonth()+1;
		int day = dp_date.getDayOfMonth();
		
		int id = v.getId();
		switch (id) {
		case R.id.btn_no_date:
			goToNext();
			break;
		case R.id.btn_sure_date:
			// TODO Ming:记录时间
			goToNext();
			break;
		}
	}

	private void goToNext() {
		Intent intent = new Intent(this, AddFieldActivity7.class);
		startActivity(intent);
	}
}
