package com.dasinong.app.ui.soil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.components.home.view.CropsStateView.MyDialogClickListener;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.WebBrowserActivity;

public class SoilListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {

	private ListView mListView;

	@Override
	protected int getMainResourceId() {
		return R.layout.activity_soil_list;
	}

	@Override
	protected void initView() {
		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(
				R.array.soil_list)));

	}

	@Override
	protected void initEvent() {
		mListView.setOnItemClickListener(this);
	}

	@Override
	public int getTitleText() {
		return R.string.soil_check;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			intent.setClass(this, WebBrowserActivity.class);
			intent.putExtra("url", "file:///android_asset/SamplingImportance.html");
			intent.putExtra("title", "为什么要测土");
			startActivity(intent);
			break;
		case 1:
			intent.setClass(this, WebBrowserActivity.class);
			intent.putExtra("url", "file:///android_asset/SamplingNotice.html");
			intent.putExtra("title", "采样须知");
			startActivity(intent);
			break;
		case 2:

			intent.setClass(this, WebBrowserActivity.class);
			intent.putExtra("url", "file:///android_asset/soiltest-sample.html");
			intent.putExtra("title", "测土报告解读");
			startActivity(intent);
			break;
		case 3:
			showRemindDialog("测土服务即将上线", "我们的全国免费测土点，会在近期开放，敬请期待", "确定", new MyDialogClickListener() {

				@Override
				public void onSureButtonClick() {

				}

				@Override
				public void onCancelButtonClick() {

				}
			});
			break;
		}
	}

	private void showRemindDialog(String title, String content, String sureButton, final MyDialogClickListener dialogClickListener) {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.confirm_gps_network_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		TextView tv_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);

		System.out.println(tv_title + "tv _ title");

		tv_title.setText(title);
		tv.setTextSize(22);

		tv.setText(content);
		tv.setTextSize(18);

		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setVisibility(View.GONE);

		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setText(sureButton);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogClickListener.onSureButtonClick();
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	public void onTaskSuccess(int requestCode, Object response) {

	}
}
