package com.dasinong.app.ui.adapter;

import com.dasinong.app.ui.fragment.RecommendFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ming on 2015/10/20.
 */
public class RecommendFragmentPagerAdapter extends FragmentPagerAdapter {

    private String [] title;
    private int refuId;

    public RecommendFragmentPagerAdapter(FragmentManager fm , int refuId) {
        super(fm);
        this.refuId = refuId;
        title = refuId >0 ? new String [] {"推荐人"} : new String [] {"推荐人","被推荐人"};
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public Fragment getItem(int position) {
        return RecommendFragment.newInstance(position,refuId);
    }
}
