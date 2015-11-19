package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.ui.adapter.HarmDescriptionFragmentPagerAdapter;
import com.dasinong.app.ui.adapter.HarmPicAdapter;
import com.dasinong.app.ui.view.MyTabView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class HarmDetailsActivity1 extends BaseActivity {

	private ViewPager vp_pic;
	private MyTabView mtv_description;
	private ViewPager pager;
	private List<String> list = new ArrayList<String>();
	private List<String> imageList ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.harm_details_header1);

		vp_pic = (ViewPager) findViewById(R.id.vp_pic);
		mtv_description = (MyTabView) findViewById(R.id.mtv_variety);
		pager = (ViewPager) findViewById(R.id.pager);

		list.add("形态");
		list.add("习性");
		list.add("为害症状");
		list.add("发生规律");

		setData();
	}

	private void setData() {
		if(imageList != null){
			vp_pic.setAdapter(new HarmPicAdapter(imageList , this));
		}
		
		mtv_description.setData(list);
		HarmDescriptionFragmentPagerAdapter adapter = new HarmDescriptionFragmentPagerAdapter(getSupportFragmentManager(), list,new BaseEntity());
		pager.setAdapter(adapter);
		mtv_description.setViewPager(pager);
	}
	
	
}
