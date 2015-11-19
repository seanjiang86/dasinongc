package com.dasinong.app.ui.adapter;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.ui.WebBrowserActivity;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HarmPicAdapter extends PagerAdapter {

	private List<String> images;
	private Context context;

	public HarmPicAdapter(List<String> images, Context context) {
		super();
		this.images = images;
		this.context = context;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(context, R.layout.pop_pic_item, null);
		ImageView iv = (ImageView) view.findViewById(R.id.iv);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(iv, "http://120.26.208.198:8080/pic/" + images.get(position % images.size()).replace("../pic/", ""));
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
