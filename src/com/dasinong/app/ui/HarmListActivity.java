package com.dasinong.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.HarmFragmentPagerAdapter;
import com.dasinong.app.ui.view.MyViewPager;
import com.dasinong.app.ui.view.PagerSlidingTabStrip;
import com.dasinong.app.utils.GraphicUtils;

/**
 * 
 * @author Ming 病虫草害列表页
 */

public class HarmListActivity extends BaseActivity {

	private PagerSlidingTabStrip tabs;
	private MyViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harm_list);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setIndicatorColorResource(R.color.color_2BAD2A);

		pager = (MyViewPager) findViewById(R.id.pager);

		HarmFragmentPagerAdapter adapter = new HarmFragmentPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(1);

		tabs.setBackgroundResource(R.color.color_F0F0F0);

		tabs.setDividerColorResource(R.color.color_F0F0F0);

		tabs.setIndicatorHeight(5);

		tabs.setShouldExpand(false);

		tabs.setTextSize(GraphicUtils.dip2px(this, 16));

		tabs.setSelectedTextColorResource(R.color.color_2BAD2A);

		tabs.setSelectedTextSize(GraphicUtils.dip2px(this, 16));

		tabs.setViewPager(pager);
	}
}
