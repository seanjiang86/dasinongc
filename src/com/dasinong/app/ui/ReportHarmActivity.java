package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ReportHarmActivity extends BaseActivity {

	private TopbarView topbar;
	private EditText et_des;
	private Button btn_upload_images;
	private RadioGroup rg_harm_type;
	private RadioGroup rg_happened;
	private RadioGroup rg_distribution;
	private CheckBox cb_part_root;
	private CheckBox cb_part_stems;
	private CheckBox cb_part_leaves;
	private CheckBox cb_part_ear;
	private CheckBox cb_part_grain;
	private EditText et_harm_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_harm);

		initView();
		initTopBar();
	}

	private void initView() {

		topbar = (TopbarView) findViewById(R.id.topbar);

		rg_harm_type = (RadioGroup) findViewById(R.id.rg_harm_type);
		rg_happened = (RadioGroup) findViewById(R.id.rg_happened);
		rg_distribution = (RadioGroup) findViewById(R.id.rg_distribution);

		cb_part_root = (CheckBox) findViewById(R.id.cb_part_root);
		cb_part_stems = (CheckBox) findViewById(R.id.cb_part_stems);
		cb_part_leaves = (CheckBox) findViewById(R.id.cb_part_leaves);
		cb_part_ear = (CheckBox) findViewById(R.id.cb_part_ear);
		cb_part_grain = (CheckBox) findViewById(R.id.cb_part_grain);

		et_harm_name = (EditText) findViewById(R.id.et_harm_name);
		et_des = (EditText) findViewById(R.id.et_des);

		btn_upload_images = (Button) findViewById(R.id.btn_upload_images);

//		rb_disease_harm = (RadioButton) findViewById(R.id.rb_disease_harm);
//		rb_pet_harm = (RadioButton) findViewById(R.id.rb_pet_harm);
//		rb_grass_harm = (RadioButton) findViewById(R.id.rb_grass_harm);
//		rb_nosure_harm = (RadioButton) findViewById(R.id.rb_nosure_harm);
//		rb_suddenly = (RadioButton) findViewById(R.id.rb_suddenly);
//		rb_recently = (RadioButton) findViewById(R.id.rb_recently);
//		rb_recurrence = (RadioButton) findViewById(R.id.rb_recurrence);
//		rb_uniform = (RadioButton) findViewById(R.id.rb_uniform);
//		rb_uneven = (RadioButton) findViewById(R.id.rb_uneven);
	}

	private void initTopBar() {
		topbar.setCenterText("举报病虫草害");
		topbar.setRightText("提交");
		topbar.setLeftView(true, true);

		topbar.setRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int typeCheckedId = rg_harm_type.getCheckedRadioButtonId();
				int hapCheckedId = rg_happened.getCheckedRadioButtonId();
				int disCheckedId = rg_distribution.getCheckedRadioButtonId();

				switch (typeCheckedId) {
				case R.id.rb_disease_harm:
					// TODO MING 病害
					System.out.println("病害");
					break;
				case R.id.rb_pet_harm:
					// TODO MING 虫害
					System.out.println("虫害");
					break;
				case R.id.rb_grass_harm:
					// TODO MING 草害
					System.out.println("草害");
					break;
				case R.id.rb_nosure_harm:
					// TODO MING 不确定
					System.out.println("不确定");
					break;
				}
				
				switch (hapCheckedId) {
				case R.id.rb_suddenly:
					// TODO MING 突然爆发
					System.out.println("突然爆发");
					break;
				case R.id.rb_recently:
					// TODO MING 几天爆发
					System.out.println("近几天");
					break;
				case R.id.rb_recurrence:
					// TODO MING 复发
					System.out.println("复发");
					break;
				}
				
				switch (disCheckedId) {
				case R.id.rb_uniform:
					// TODO MING 均匀受害
					System.out.println("均匀");
					break;
				case R.id.rb_uneven:
					// TODO MING 不规则爆发
					System.out.println("不规则");
					break;
				}
				
				// TODO MING 获取影响部位
				
				String harmName = et_harm_name.getText().toString();
				String des = et_des.getText().toString();

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

}
