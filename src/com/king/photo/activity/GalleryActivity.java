package com.king.photo.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.ReportHarmActivity;
import com.king.photo.zoom.PhotoView;
import com.king.photo.zoom.ViewPagerFixed;
import com.liam.imageload.LoadUtils;

/**
 * 这个是用于进行图片浏览时的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:47:53
 */
public class GalleryActivity extends BaseActivity {
	private Intent intent;
	// 返回按钮
	private Button back_bt;
	// 发送按钮
	private Button send_bt;
	// 删除按钮
	private Button del_bt;
	// 顶部显示预览图片位置的textview
	private TextView positionTextView;
	// 获取前一个activity传过来的position
	private int position;
	// 当前的位置
	private int location = 0;

	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	private Context mContext;

	// 存放当前图片地址的集合
	private ArrayList<String> paths;

	RelativeLayout photo_relativeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);
		mContext = this;
		send_bt = (Button) findViewById(R.id.send_button);
		del_bt = (Button) findViewById(R.id.gallery_del);
		send_bt.setOnClickListener(new GallerySendListener());
		del_bt.setOnClickListener(new DelListener());
		intent = getIntent();

		Bundle bundle = intent.getExtras();
		paths = bundle.getStringArrayList(ReportHarmActivity.CURRENT_LIST);
		position = Integer.parseInt(bundle.getString("position"));
		int id = bundle.getInt("ID", 0);

		isShowOkBt();
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < paths.size(); i++) {
			initListViews(paths.get(i));
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
		pager.setCurrentItem(id);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initListViews(String path) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		LoadUtils.getInstance().loadImage(img, "file:///" + path);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			if (listViews.size() == 1) {
				paths.clear();
				send_bt.setText("完成" + "(" + paths.size() + "/" + ReportHarmActivity.MAX_NUM + ")");
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(ReportHarmActivity.CURRENT_LIST, paths);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				paths.remove(location);
				pager.removeAllViews();
				listViews.remove(location);
				adapter.setListViews(listViews);
				send_bt.setText("完成" + "(" + paths.size() + "/" + ReportHarmActivity.MAX_NUM + ")");
				adapter.notifyDataSetChanged();
			}
		}
	}

	// 完成按钮的监听
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(ReportHarmActivity.CURRENT_LIST, paths);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}

	}

	public void isShowOkBt() {
		if (paths.size() > 0) {
			send_bt.setText("完成" + "(" + paths.size() + "/" + ReportHarmActivity.MAX_NUM + ")");
			send_bt.setPressed(true);
			send_bt.setClickable(true);
			send_bt.setTextColor(Color.WHITE);
		} else {
			send_bt.setPressed(false);
			send_bt.setClickable(false);
			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
