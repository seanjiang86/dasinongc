package com.dasinong.app.ui.adapter;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.ui.adapter.CityAdapter.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CropAdapter extends MyBaseAdapter<String> {

	public CropAdapter(Context ctx, List<String> list, boolean flag) {
		super(ctx, list, flag);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.spinner_checked_text, null);
			holder.tv = (TextView) view.findViewById(R.id.tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv.setText(list.get(pos));
		return view;
	}
	
	public static class ViewHolder {
		TextView tv ;
	}

}
