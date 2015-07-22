package com.dasinong.app.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.entity.DiseaseEntity;

public class DiseaseListAdapter extends MyBaseAdapter<Petdisspecbrowse> {
	
	public DiseaseListAdapter(Context ctx, List<Petdisspecbrowse> list, boolean flag) {
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
		final Petdisspecbrowse item = list.get(pos);
		holder.nameText.setText(item.petDisSpecName);
		return view;
	}

	public static class ViewHolder {
		ImageView  deleteImage;
		TextView  nameText;
	}
	
}
