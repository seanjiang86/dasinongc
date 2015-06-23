package com.dasinong.app.components.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;

/**
 * Created by sczhang on 15/6/5.
 */
public class HomeWeatherView extends LinearLayout implements View.OnClickListener {

	LinearLayout lyWeekWeather;
	TextView tvCloseWeekTemp;
	private View mRoot;
	private boolean mIsWeekWeatherShow = false;
	private HumidityView mHumView;
	private HorizontalScrollView mHorHumView;
	private static final String TAG = "[HomeWeatherView]";

	private ShrinkAnimation mShrinkAnimation;
	private ExpandAnimation mExpandAnimation;

	public HomeWeatherView(Context context) {
		this(context, null);
	}

	public HomeWeatherView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HomeWeatherView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		setViewListener();
	}

	private void setViewListener() {
		tvCloseWeekTemp.setOnClickListener(this);
	}

	private void init() {
		mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather, null);
		lyWeekWeather = (LinearLayout) mRoot.findViewById(R.id.lyWeekWeather);
		tvCloseWeekTemp = (TextView) mRoot.findViewById(R.id.tvCloseWeekTemp);
		mHorHumView = (HorizontalScrollView) mRoot.findViewById(R.id.horHumView);
		mHumView = (HumidityView) mRoot.findViewById(R.id.humView);

		mIsWeekWeatherShow = false;
		tvCloseWeekTemp.setText(getContext().getString(R.string.weather_open_one_week));
		lyWeekWeather.setVisibility(View.GONE);
		addView(mRoot);

		setWeatherData(new Object());

	}

	/**
	 * Called when a view has been clicked.
	 * 
	 * @param v
	 *            The view that was clicked.
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvCloseWeekTemp:
			initAnimation();
			if (mIsWeekWeatherShow) {
				mIsWeekWeatherShow = false;
				// gone
				startShrinkAnimation();
				tvCloseWeekTemp.setText(getContext().getString(R.string.weather_open_one_week));
			} else {
				mIsWeekWeatherShow = true;
				// Visibility
				startExpandAnimation();
				tvCloseWeekTemp.setText(getContext().getString(R.string.weather_close_one_week));
			}
			break;
		}
	}

	public void setWeatherData(Object obj) {
		if (null != obj) {
			setOneWeekWeather(new Object());
			setOneDayWeather(new Object());
		}
	}

	private void setOneDayWeather(Object o) {
		if (null != o) {
			mHumView.setOneDayWeatherData(new Object());
			if (null == mHorHumView) {
				Log.e(TAG, "null == mHorHumView");
			} else {
				mHorHumView.smoothScrollTo(0, 600);
			}

		}
	}

	private void setOneWeekWeather(Object o) {
		if (null != o) {
			for (int i = 0; i < 7; i++) {
				View weekWeather = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather_week_item, null);
				TextView tvItemDay = (TextView) weekWeather.findViewById(R.id.tvItemDay);
				ImageView ivItemWind = (ImageView) weekWeather.findViewById(R.id.ivItemWind);
				TextView tvItemTempLow = (TextView) weekWeather.findViewById(R.id.tvItemTempLow);
				TextView tvItemTempHight = (TextView) weekWeather.findViewById(R.id.tvItemTempHight);
				TextView tvItemWeather = (TextView) weekWeather.findViewById(R.id.tvItemWeather);
				TextView tvItemWindSpeed = (TextView) weekWeather.findViewById(R.id.tvItemWindSpeed);
				tvItemDay.setText("周" + i);
				tvItemTempLow.setText(6 + i + "");
				tvItemTempHight.setText(18 + i + "~");
				tvItemWeather.setText("晴天" + i);
				tvItemWindSpeed.setText("3-4级");
				lyWeekWeather.addView(weekWeather);
			}
		}
	}

	private void initAnimation() {
		if (mShrinkAnimation == null || mExpandAnimation == null) {
			int height = tvCloseWeekTemp.getHeight();
			mShrinkAnimation = new ShrinkAnimation(tvCloseWeekTemp, height);
			mExpandAnimation = new ExpandAnimation(tvCloseWeekTemp, height);
			long duration = (long) (height / getResources().getDisplayMetrics().density);
			mShrinkAnimation.setDuration(duration);
			mExpandAnimation.setDuration(duration);
		}

	}

	private void startShrinkAnimation() {
		tvCloseWeekTemp.clearAnimation();
		tvCloseWeekTemp.startAnimation(mShrinkAnimation);
	}

	private void startExpandAnimation() {
		tvCloseWeekTemp.clearAnimation();
		tvCloseWeekTemp.startAnimation(mExpandAnimation);
	}
}
