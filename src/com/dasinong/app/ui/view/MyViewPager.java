package com.dasinong.app.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 * @author Ming 屏蔽ViewPaer的滑动事件
 */
public class MyViewPager extends ViewPager {

	private boolean HAS_TOUCH_MODE = false;
	private List<Integer> heightList = new ArrayList<Integer>();

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	// 向内部控件传递
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	// 不响应事件
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int height = 0;
		// 下面遍历所有child的高度
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			heightList.add(h);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void getChildHeight(int position) {
		if (heightList.size() > 0) {
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) getLayoutParams();
			params.height = heightList.get(position) + 50;
			setLayoutParams(params);
		}
	}
}
