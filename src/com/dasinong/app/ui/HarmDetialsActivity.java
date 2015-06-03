package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.MyBaseAdapter;
import com.dasinong.app.utils.GraphicUtils;

/**
 * @author Ming 此类为显示病虫草害详情的页面
 */

public class HarmDetialsActivity extends BaseActivity {

	private ListView lv_detial;
	private View header;
	// 病害名
	private TextView tv_harm_name;
	// 病害描述
	private TextView tv_harm_des;
	// 病害等级
	private RatingBar rb_harm_grade;
	// 病害图片展示
	private ViewPager vp_pic;
	// 每张图片对应的点
	private LinearLayout ll_point;
	// 快速诊断按钮
	private LinearLayout ll_rapid_diagnosis;
	// 用来存放图片链接的集合
	private List<String> dataList = new ArrayList<String>();
	private ImageView imageView;
	private ImageView [] imageViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harm_detials);

		lv_detial = (ListView) findViewById(R.id.lv_detial);

		header = View.inflate(DsnApplication.getContext(),
				R.layout.harm_detials_header, null);

		initData();

		initHeader();

		lv_detial.addHeaderView(header);

		lv_detial.setAdapter(new HarmDetialAdapter(this, dataList, true));

		lv_detial.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(DsnApplication.getContext(),
						CureDetialActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		dataList.add("1");
		dataList.add("1");
		dataList.add("1");
		dataList.add("1");
		dataList.add("1");
	}

	/*
	 * 填充listview的头的信息
	 */
	private void initHeader() {
		tv_harm_name = (TextView) header.findViewById(R.id.tv_harm_name);
		rb_harm_grade = (RatingBar) header.findViewById(R.id.rb_harm_grade);
		tv_harm_des = (TextView) header.findViewById(R.id.tv_harm_des);
		vp_pic = (ViewPager) header.findViewById(R.id.vp_pic);
		ll_point = (LinearLayout) header.findViewById(R.id.ll_point);
		
		
		imageViews = new ImageView[dataList.size()];
		int px = GraphicUtils.dip2px(this, 8);
		for (int i = 0; i < dataList.size(); i++) {
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams( px,px ));
			
			imageViews[i] = imageView;
			
			if(i == 0){
				imageView.setBackgroundResource(R.drawable.selected_point);
			}else{
				imageView.setBackgroundResource(R.drawable.unselect_point);
			}
			
			ll_point.addView(imageView);
		}
		

		ll_rapid_diagnosis = (LinearLayout) header.findViewById(R.id.ll_rapid_diagnosis);

		vp_pic.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				return 5;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("我是第" + position + "张图片");
				tv.setTextSize(50);

				container.addView(tv);
				return tv;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}
		});
	}

	class HarmDetialAdapter extends MyBaseAdapter<String> {

		public HarmDetialAdapter(Context ctx, List<String> list, boolean flag) {
			super(ctx, list, flag);
		}

		@Override
		public View getView(int pos, View view, ViewGroup group) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.treatment_item, null);

				holder.ll_title = (LinearLayout) view
						.findViewById(R.id.ll_title);
				holder.small_line = view.findViewById(R.id.small_line);
				holder.tv_method_name = (TextView) view
						.findViewById(R.id.tv_method_name);
				holder.tv_crop_stage = (TextView) view
						.findViewById(R.id.tv_crop_stage);
				holder.tv_procider = (TextView) view
						.findViewById(R.id.tv_procider);
				holder.tv_content = (TextView) view
						.findViewById(R.id.tv_content);
				holder.big_line = view.findViewById(R.id.big_line);

				view.setTag(holder);

			} else {
				holder = (ViewHolder) view.getTag();
			}

			if (pos == 0) {
				holder.big_line.setVisibility(View.GONE);
			} else if (pos == 2) {
				holder.ll_title.setVisibility(View.GONE);
				holder.small_line.setVisibility(View.GONE);
			} else if (pos == 3) {
				holder.big_line.setVisibility(View.GONE);
			} else {
				holder.ll_title.setVisibility(View.GONE);
				holder.small_line.setVisibility(View.GONE);
				holder.big_line.setVisibility(View.GONE);
			}

			return view;
		}
	}

	public static class ViewHolder {
		LinearLayout ll_title;
		View small_line;
		TextView tv_method_name;
		TextView tv_crop_stage;
		TextView tv_procider;
		TextView tv_content;
		View big_line;
	}
}
