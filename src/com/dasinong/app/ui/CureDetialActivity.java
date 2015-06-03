package com.dasinong.app.ui;


import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.MyBaseAdapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CureDetialActivity extends BaseActivity {
	
	private ListView lv_medicine;
	private List<String> list = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cure_detials);
		
		lv_medicine = (ListView) findViewById(R.id.lv_medicine);
		
		initData();
		
		lv_medicine.setAdapter(new MyBaseAdapter<String>(DsnApplication.getContext(), list, true) {
			
			@Override
			public View getView(int pos, View view, ViewGroup group) {
				view = view.inflate(context, R.layout.medicine_item, null);
				return view;
			}
		});
	}

	private void initData() {
		list.add("1");
		list.add("1");
		list.add("1");
		list.add("1");
		list.add("1");
	}
}
