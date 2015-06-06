package com.dasinong.app.ui.fragment;

import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetConfig.ResponseCode;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.AuthCodeActivity;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.HarmDetialsActivity;
import com.dasinong.app.ui.HarmListActivity;
import com.dasinong.app.ui.RegisterActivity;
import com.dasinong.app.ui.RegisterPasswordActivity;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.manager.AccountManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Button tv = new Button(getActivity());
		tv.setText("首页");
		tv.setTextSize(50);
		
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!AccountManager.isLogin(getActivity())){
					Intent intent = new Intent(getActivity(),RegisterPhoneActivity.class);
					getActivity().startActivity(intent);
				}else{
					Toast.makeText(getActivity(), "您已登录", 0).show();
				}
			}
		});
		
		return tv ;
	}
	
	private void checkUser(final String phone) {
		((BaseActivity)getActivity()).startLoadingDialog();
		RequestService.getInstance().checkUser(getActivity(), phone, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				((BaseActivity)getActivity()).dismissLoadingDialog();
				if(resultData.isOk()){
					Intent intent = new Intent(getActivity(),RegisterPasswordActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("isLogin", true);
					getActivity().startActivity(intent);
				}else if(resultData.isCode(ResponseCode.CODE_100)){
					Intent intent = new Intent(getActivity(),RegisterPasswordActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("isLogin", false);
					getActivity().startActivity(intent);
				}else{
					((BaseActivity)getActivity()).showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				((BaseActivity)getActivity()).dismissLoadingDialog();
			}
		});
	}
	
}
