package com.dasinong.app.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.utils.GraphicUtils;

/**
 * Created by Ming on 2015/11/4.
 */
public class MyTabView extends LinearLayout {

	private Context context;
	private int itemWidth;
	private OnItemClickListener listener;
	private int count;
	private int width;
	private int currentPositon;
	private TextView tv;

	public MyTabView(Context context) {
		this(context, null);
	}

	public MyTabView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyTabView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	private void initView(int count) {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		this.count = count;

		width = width - GraphicUtils.dip2px(context, 30);

		itemWidth = width / count;

	}

	public void setData(List<String> list) {
		initView(list.size());
		for (int i = 0; i < list.size(); i++) {
			View itemView = View.inflate(context, R.layout.item_mytabview, null);
			tv = (TextView) itemView.findViewById(R.id.tv);
			View view = itemView.findViewById(R.id.view);
			if (i == count - 1) {
				view.setVisibility(View.GONE);
			}
			tv.setText(list.get(i));

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					int position = MyTabView.this.indexOfChild(v);
					
					listener.onItemClick(position);

					for (int j = 0; j < count; j++) {
						if (MyTabView.this.getChildAt(j) == v) {
							if (j == 0) {
								MyTabView.this.getChildAt(j).setBackgroundResource(R.drawable.left_bg);
							} else if (j == count - 1) {
								MyTabView.this.getChildAt(j).setBackgroundResource(R.drawable.right_bg);
							} else {
								MyTabView.this.getChildAt(j).setBackgroundResource(R.drawable.middle_bg);
							}
							((TextView) MyTabView.this.getChildAt(j).findViewById(R.id.tv)).setTextColor(Color.WHITE);
						} else {
							MyTabView.this.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
							((TextView) MyTabView.this.getChildAt(j).findViewById(R.id.tv)).setTextColor(Color.BLACK);
						}
					}
				}
			});
			addView(itemView, i, params);
		}
		setCurrentPosition(0);
	}

	public void setCurrentPosition(int position) {
		for (int i = 0; i < count; i++) {
			if (position == i) {
				getChildAt(position).setBackgroundResource(R.drawable.left_bg);
				((TextView) MyTabView.this.getChildAt(i).findViewById(R.id.tv)).setTextColor(Color.WHITE);
			}
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position);
	}
}
