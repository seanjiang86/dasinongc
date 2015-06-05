package com.dasinong.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD

import com.dasinong.app.components.HomeWeatherView;
=======
import android.widget.ImageView;
import android.widget.TextView;
>>>>>>> origin/master

import com.dasinong.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment {

<<<<<<< HEAD
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
	
	
	
=======


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Button tv = new Button(getActivity());
        tv.setText("首页");
        tv.setTextSize(50);

        return tv;
    }


>>>>>>> origin/master
}
