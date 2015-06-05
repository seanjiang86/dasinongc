package com.dasinong.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dasinong.app.components.HomeWeatherView;

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
		;
		HomeWeatherView view = new HomeWeatherView(getActivity());
//		return inflater.inflate(R.layout.view_home_weather,container,false) ;
		return view;
	}
	
	
	
}
