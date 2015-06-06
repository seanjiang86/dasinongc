package com.dasinong.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.dasinong.app.R;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.AddFieldActivity4;


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
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), AddFieldActivity4.class);
				startActivity(intent);
			}
		});
        
        return tv;
    }


}
