package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends BaseActivity {
	private LinearLayout ll_view_group;
	private ViewPager vp;
	private ImageView imageView;
	private int[] ids = { R.drawable.app001, R.drawable.app005, R.drawable.app002, R.drawable.app006, R.drawable.app003, R.drawable.app007,
			R.drawable.app004 };
	private ImageView[] imageViews;
	private Button btn_start_app;
	private boolean isFirst;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		isFirst = getIntent().getBooleanExtra("isFirst", false);

		vp = (ViewPager) findViewById(R.id.vp);
		ll_view_group = (LinearLayout) findViewById(R.id.ll_view_group);
		btn_start_app = (Button) findViewById(R.id.btn_start_app);

		imageViews = new ImageView[7];

		for (int i = 0; i < 7; i++) {
			imageView = new ImageView(this);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
			params.setMargins(10, 0, 10, 0);
			
			imageView.setLayoutParams(params);
			imageView.setPadding(30, 0, 30, 0);

			imageViews[i] = imageView;

			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}

			ll_view_group.addView(imageView);
		}

		vp.setAdapter(new MyPagerAdapter());
		vp.setOnPageChangeListener(new MyOnPageChangeListener());
		
		btn_start_app.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isFirst){
					Intent intent = new Intent(GuideActivity.this, MainTabActivity.class);
					startActivity(intent);
					finish();
					return;
				}else{
					finish();
				}
			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 7;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(GuideActivity.this, R.layout.guide_item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv);
			iv.setImageResource(ids[position]);
			
			container.addView(view);
			
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(((View)object));
		}
	}
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				if(i == arg0){
					imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
				}else {
					imageViews[i].setBackgroundResource(R.drawable.page_indicator);
				}
			}
			if(arg0 == 6){
				btn_start_app.setVisibility(View.VISIBLE);
			}else {
				btn_start_app.setVisibility(View.GONE);
			}
		}
	}
}
