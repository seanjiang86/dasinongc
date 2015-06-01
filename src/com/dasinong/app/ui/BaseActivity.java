package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.LoadingDialog;
import com.dasinong.app.utils.ViewHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

	protected final String tag = getClass().getSimpleName();
	private LoadingDialog mLoadingDiag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
//	protected void initView(){}
//	protected void setView(){}

	
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int res) {
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	public void startLoadingDialog(String loadingText) {
		startLoadingDialog(loadingText, false);
	}

	public void startLoadingDialog() {
		startLoadingDialog(R.string.please_wait, true);
	}

	public void startLoadingDialog(int resId, boolean outeSiteCanceled) {
		startLoadingDialog(getResources().getString(resId), outeSiteCanceled);
	}

	public void startLoadingDialog(String loadingText, boolean outeSiteCanceled) {
		mLoadingDiag = ViewHelper.getLoadingDialog(this, loadingText, true, outeSiteCanceled);
		mLoadingDiag.show();
	}

	public void dismissLoadingDialog() {
		if (mLoadingDiag != null) {
			mLoadingDiag.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLoadingDiag != null) {
			mLoadingDiag.dismiss();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
}
