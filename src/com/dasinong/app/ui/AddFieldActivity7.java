package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.fragment.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddFieldActivity7 extends BaseActivity {
	private EditText et_target_production;
	private Button btn_finish_add_field;
	private String target;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_7);
		
		et_target_production = (EditText) findViewById(R.id.et_target_production);
		btn_finish_add_field = (Button) findViewById(R.id.btn_finish_add_field);
		
		btn_finish_add_field.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				target = et_target_production.getText().toString().trim();
				if(TextUtils.isEmpty(target)){
					showToast("请输入有效值");
					return;
				}
				sendFieldInfo();
			}
		});
	}

	protected void sendFieldInfo() {
		// TODO Ming：发送请求
		
		Intent intent = new Intent(this, MainTabActivity.class);
		startActivity(intent);
	}
}
