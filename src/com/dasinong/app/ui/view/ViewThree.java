package com.dasinong.app.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.TextAdapter;

public class ViewThree extends LinearLayout implements ViewBaseAction {

	private ListView regionListView;
	private ListView plateListView;
	private List<String> groups = new ArrayList<String>();
	private List<String> childrenItem = new ArrayList<String>();
	private List<String> lastList = new ArrayList<String>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter earaListViewAdapter;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private int lastPosition = 0;
	private String showString = "不限";
	private Context context;
	private ListView lastListView;
	private TextAdapter lastListViewAdapter;

	public ViewThree(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public ViewThree(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_three_list, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		lastListView = (ListView) findViewById(R.id.listView3);
		setBackgroundResource(R.drawable.choosearea_bg_left);
	}

	public void initBigAreaData(List<String> bigAreaList, int defaultPosition) {
		this.groups = bigAreaList;
		this.tEaraPosition = defaultPosition;
		earaListViewAdapter = new TextAdapter(context, groups, R.drawable.choose_item_selected, R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		regionListView.setSelection(tEaraPosition);
	}

	public void initSmallAreaData(List<String> smallAreaList, int defaultPostion) {
		this.childrenItem = smallAreaList;
		this.tBlockPosition = defaultPostion;
		plateListViewAdapter = new TextAdapter(context, childrenItem, R.drawable.choose_item_selected, R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListView.setSelection(tBlockPosition);
	}

	public void initLastData(List<String> smallAreaList, int defaultPostion) {
		this.lastList = smallAreaList;
		this.lastPosition = defaultPostion;
		lastListViewAdapter = new TextAdapter(context, lastList, R.drawable.choose_item_selected, R.drawable.choose_eara_item_selector);
		lastListViewAdapter.setTextSize(15);
		lastListViewAdapter.setSelectedPositionNoNotify(lastPosition);
		lastListView.setAdapter(lastListViewAdapter);
		lastListView.setSelection(lastPosition);
	}

	public void setOnBigAreaItemClickListener(TextAdapter.OnItemClickListener listener) {
		earaListViewAdapter.setOnItemClickListener(listener);
	}

	public void setOnSmallAreaItemClickListener(TextAdapter.OnItemClickListener listener) {
		plateListViewAdapter.setOnItemClickListener(listener);
	}
	
	public void setOnLastItemClickListener(TextAdapter.OnItemClickListener listener) {
		lastListViewAdapter.setOnItemClickListener(listener);
	}

	public String getShowText() {
		return showString;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
