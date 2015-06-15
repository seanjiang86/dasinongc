package com.dasinong.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.HarmFragmentPagerAdapter;
import com.dasinong.app.ui.view.MyViewPager;
import com.dasinong.app.ui.view.PagerSlidingTabStrip;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.GraphicUtils;

/**
 * 
 * @author Ming 病虫草害列表页
 */

public class HarmListActivity extends BaseActivity {

	private PagerSlidingTabStrip tabs;
	private MyViewPager pager;
	private TopbarView topbar;
	private LinearLayout ll_report;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harm_list);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (MyViewPager) findViewById(R.id.pager);
		topbar = (TopbarView) findViewById(R.id.topbar);
		ll_report = (LinearLayout) findViewById(R.id.ll_report);

		tabs.setIndicatorColorResource(R.color.color_2BAD2A);
		tabs.setBackgroundResource(R.color.color_F0F0F0);
		tabs.setDividerColorResource(R.color.color_F0F0F0);
		tabs.setIndicatorHeight(5);
		tabs.setShouldExpand(false);
		tabs.setTextSize(GraphicUtils.dip2px(this, 16));
		tabs.setSelectedTextColorResource(R.color.color_2BAD2A);
		tabs.setSelectedTextSize(GraphicUtils.dip2px(this, 16));
		tabs.setTabPaddingLeftRight(GraphicUtils.dip2px(this, 44));

		HarmFragmentPagerAdapter adapter = new HarmFragmentPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(1);

		initTopBar();

		ll_report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HarmListActivity.this, ReportHarmActivity.class);
				startActivity(intent);
			}
		});

		tabs.setViewPager(pager);
	}

	private void initTopBar() {
		topbar.setCenterText("本地常见病虫草害");
		topbar.setLeftView(true, true);
	}
}
