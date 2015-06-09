package com.dasinong.app.ui.fragment;

import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.TaskDetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MeFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		TextView tv = new TextView(getActivity());
		tv.setText("个人信息");
		tv.setTextSize(50);
		
//		tv.setOnClickListener(new OnClickListener() {
//          
//          @Override
//          public void onClick(View v) {
//            
//            Intent intent = new Intent(getActivity(),RegisterPhoneActivity.class);
//            startActivity(intent);
//            
//          }
//        });
		
		return tv ;
	}
	
}
