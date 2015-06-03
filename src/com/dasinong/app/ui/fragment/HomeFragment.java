package com.dasinong.app.ui.fragment;

import com.dasinong.app.ui.HarmDetialsActivity;
import com.dasinong.app.ui.HarmListActivity;
import com.dasinong.app.ui.RegisterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
				Intent intent = new Intent(getActivity(),RegisterActivity.class);
				getActivity().startActivity(intent);
				
//				Intent intent = new Intent(getActivity(), HarmListActivity.class);
//				getActivity().startActivity(intent);
			}
		});
		
		return tv ;
	}
	
}
