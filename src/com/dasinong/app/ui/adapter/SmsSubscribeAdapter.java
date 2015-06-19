package com.dasinong.app.ui.adapter;

import java.util.List;

import com.dasinong.app.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SmsSubscribeAdapter extends MyBaseAdapter<String> {

	public SmsSubscribeAdapter(Context ctx, List<String> list, boolean flag) {
		super(ctx, list, flag);
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_sms_sub_item, null);
			holder.deleteImage = (ImageView) view.findViewById(R.id.imageview_delete_image);
			holder.nameText = (TextView) view.findViewById(R.id.textview_item_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
//		holder.tv.setText(list.get(pos));
		return view;
	}
	
	public static class ViewHolder {
		ImageView  deleteImage;
		TextView  nameText;
	}
	
}
