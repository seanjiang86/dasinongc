package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.RecommendFragmentPagerAdapter;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.PagerSlidingTabStrip;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.AppInfoUtils;


import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.content.Intent;
import android.os.Bundle;

public class RecommendActivity extends BaseActivity {

    private TopbarView topbar;
    private boolean isShow = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		int refuId = SharedPreferencesHelper.getInt(this, Field.REFUID, -1);
		int serverInstitutionId = SharedPreferencesHelper.getInt(this, Field.INSTITUTIONID, 0);
        int appInstitutionId = AppInfoUtils.getInstitutionId(this);
        
        if(refuId > 0 || serverInstitutionId > 0 || appInstitutionId > 0){
        	isShow = false;
        }
		
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pagers = (ViewPager) findViewById(R.id.pager);
        topbar = (TopbarView) findViewById(R.id.topbar);
        
        initTopBar();

        tabs.setIndicatorColorResource(R.color.color_2BAD2A);
        tabs.setBackgroundResource(R.color.color_F0F0F0);
        tabs.setDividerColorResource(R.color.color_F0F0F0);
        tabs.setIndicatorHeight(5);
        tabs.setShouldExpand(true);
        tabs.setTextSize(32);
        tabs.setSelectedTextColorResource(R.color.color_2BAD2A);
        tabs.setSelectedTextSize(32);


        RecommendFragmentPagerAdapter adapter = new RecommendFragmentPagerAdapter(getSupportFragmentManager() , isShow);
        pagers.setOffscreenPageLimit(1);
        pagers.setAdapter(adapter);


        tabs.setViewPager(pagers);
    }

	private void initTopBar() {
		topbar.setCenterText("有奖推荐");
		topbar.setLeftView(true, true);
	}
}
