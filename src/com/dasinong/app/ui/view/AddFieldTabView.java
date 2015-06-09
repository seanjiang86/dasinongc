package com.dasinong.app.ui.view;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddFieldTabView extends LinearLayout {

	private View view;
	private View view_one;
	private View view_two;
	private View view_three;
	private ImageView iv_one;
	private ImageView iv_two;
	private ImageView iv_three;
	private TextView tv_location;
	private TextView tv_variety;
	private TextView tv_crop_info;
	private TextView tv_crop_date;
	private int viewOneColor;
	private int viewTwoColor;
	private int viewThreeColor;
	private int ivOneColor;
	private int ivTwoColor;
	private int ivThreeColor;
	private int tvLocationColor;
	private int tvVarietyColor;
	private int tvInfoColor;
	private int tvDataColor;
	private int varietyColor;
	private int locationColor;
	private int infoColor;
	private int dataColor;

	public AddFieldTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);

		viewOneColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "viewOneColor", R.color.color_E9E9E9);
		viewTwoColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "viewTwoColor", R.color.color_E9E9E9);
		viewThreeColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "viewThreeColor", R.color.color_E9E9E9);

		ivOneColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "ivOneColor", R.drawable.gray_round);
		ivTwoColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "ivTwoColor", R.drawable.gray_round);
		ivThreeColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "ivThreeColor", R.drawable.gray_round);

		tvLocationColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "tvLocationColor", R.color.color_999999);
		tvVarietyColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "tvVarietyColor", R.color.color_999999);
		tvInfoColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "tvInfoColor", R.color.color_999999);
		tvDataColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/com.dasinong.app", "tvDataColor", R.color.color_999999);

		locationColor = DsnApplication.getContext().getResources().getColor(tvLocationColor);
		varietyColor = DsnApplication.getContext().getResources().getColor(tvVarietyColor);
		infoColor = DsnApplication.getContext().getResources().getColor(tvInfoColor);
		dataColor = DsnApplication.getContext().getResources().getColor(tvDataColor);
		
		if (!isInEditMode()) {
			view_one.setBackgroundResource(viewOneColor);
			view_two.setBackgroundResource(viewTwoColor);
			view_three.setBackgroundResource(viewThreeColor);

			iv_one.setImageResource(ivOneColor);
			iv_two.setImageResource(ivTwoColor);
			iv_three.setImageResource(ivThreeColor);
			
			tv_location.setTextColor(locationColor);
			tv_variety.setTextColor(varietyColor);
			tv_crop_info.setTextColor(infoColor);
			tv_crop_date.setTextColor(dataColor);
		}
	}

	public AddFieldTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);

		if (!isInEditMode()) {
			view_one.setBackgroundResource(viewOneColor);
			view_two.setBackgroundResource(viewTwoColor);
			view_three.setBackgroundResource(viewThreeColor);

			iv_one.setImageResource(ivOneColor);
			iv_two.setImageResource(ivTwoColor);
			iv_three.setImageResource(ivThreeColor);

			tv_location.setTextColor(locationColor);
			tv_variety.setTextColor(varietyColor);
			tv_crop_info.setTextColor(infoColor);
			tv_crop_date.setTextColor(dataColor);
		}
	}

	/**
	 * 加载布局
	 */
	private void initView(Context context) {
		view = View.inflate(context, R.layout.add_field_tab, this);

		view_one = view.findViewById(R.id.view_one);
		view_two = view.findViewById(R.id.view_two);
		view_three = view.findViewById(R.id.view_three);

		iv_one = (ImageView) view.findViewById(R.id.iv_one);
		iv_two = (ImageView) view.findViewById(R.id.iv_two);
		iv_three = (ImageView) view.findViewById(R.id.iv_three);

		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_variety = (TextView) findViewById(R.id.tv_variety);
		tv_crop_info = (TextView) findViewById(R.id.tv_crop_info);
		tv_crop_date = (TextView) findViewById(R.id.tv_crop_date);
	}
}
