package com.dasinong.app.components.home.view;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;


public class ShrinkAnimation  extends Animation{

	private TextView mView;
	private int mHeight;
	public ShrinkAnimation(TextView view,int height){
		this.mView = view;
		this.mHeight = height;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		mView.getLayoutParams().height = mHeight - (int)(mHeight*interpolatedTime);
		mView.requestLayout();
	}
}
