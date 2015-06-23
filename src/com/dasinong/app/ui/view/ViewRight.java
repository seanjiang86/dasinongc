package com.dasinong.app.ui.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.TextAdapter;

public class ViewRight extends RelativeLayout implements ViewBaseAction {

	private ListView mListView;
	private List<String> villageList;
	private TextAdapter adapter;
	private Context mContext;


	public ViewRight(Context context) {
		super(context);
		mContext = context;
	}

	public ViewRight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public ViewRight(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void initData(List<String> villageList) {
		this.villageList = villageList;
		init();
	}

	private void init() {

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_distance, this, true);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_right));
		mListView = (ListView) findViewById(R.id.listView);
		adapter = new TextAdapter(mContext, villageList, R.drawable.choose_item_right, R.drawable.choose_eara_item_selector);
		adapter.setTextSize(17);
		mListView.setAdapter(adapter);
	}

	public void setOnItemclickListener(TextAdapter.OnItemClickListener listener) {
		adapter.setOnItemClickListener(listener);
	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}

}
