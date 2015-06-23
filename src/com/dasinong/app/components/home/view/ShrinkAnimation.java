package com.dasinong.app.components.home.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class ShrinkAnimation  extends Animation{

	private View mView;
	private int mHeight;
	public ShrinkAnimation(View view,int height){
		this.mView = view;
		this.mHeight = height;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		mView.getLayoutParams().height = mHeight - (int)(mHeight*interpolatedTime);
		mView.requestLayout();
	}
	
	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
