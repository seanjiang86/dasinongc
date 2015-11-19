package com.dasinong.app.ui.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.ui.fragment.HarmDescriptionFragment;
import com.dasinong.app.ui.fragment.HarmFragment;

/**
 * 
 * @author Ming 此类为病虫草害列表页ViewPager的适配器
 */

public class HarmDescriptionFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<String> list;
	private BaseEntity entity;

	public HarmDescriptionFragmentPagerAdapter(FragmentManager fm, List<String> list, BaseEntity entity) {
		super(fm);
		this.list = list;
		this.entity = entity;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int position) {
		return HarmDescriptionFragment.newInstance(list.get(position),entity);
	}
}
