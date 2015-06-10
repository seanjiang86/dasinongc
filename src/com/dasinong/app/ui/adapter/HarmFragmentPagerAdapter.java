package com.dasinong.app.ui.adapter;



import com.dasinong.app.DsnApplication;
import com.dasinong.app.ui.fragment.HarmFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * 
 * @author Ming
 * 此类为病虫草害列表页ViewPager的适配器
 */

public class HarmFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private final String [] TITLES = {"病害","虫害","草害","自然灾害"};
	
	public HarmFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}
	
	@Override
	public int getCount() {
		return TITLES.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}
	
	@Override
	public Fragment getItem(int position) {
		return new HarmFragment().newInstance(position);
	}
	
}
