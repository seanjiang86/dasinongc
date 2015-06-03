package com.dasinong.app.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @author Ming
 * 屏蔽ViewPaer的滑动事件
 */
public class MyViewPager extends ViewPager {

	private boolean HAS_TOUCH_MODE = false;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}
	
	//向内部控件传递
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	//不响应事件
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	
}
