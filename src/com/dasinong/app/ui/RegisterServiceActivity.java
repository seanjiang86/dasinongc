package com.dasinong.app.ui;

import com.dasinong.app.R;

import android.os.Bundle;
import android.widget.TextView;

public class RegisterServiceActivity extends BaseActivity {

	private TextView mServiceText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register_service);
		
		
		mServiceText = (TextView) this.findViewById(R.id.textview_text);
		
	}
	
}
