package com.dasinong.app.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.entity.DiseaseEntity;
import com.dasinong.app.entity.PesticideNamedListEntity.Pesticide;
import com.dasinong.app.entity.VarietyNamedListEntity.Variety;
import com.dasinong.app.ui.adapter.DiseaseListAdapter.ViewHolder;

public class VarietyNamedListAdapter extends MyBaseAdapter<Variety> {

	public VarietyNamedListAdapter(Context ctx, List<Variety> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_disease_item, null);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			holder.desText = (TextView) view.findViewById(R.id.textview_description);
			holder.moreImage = (ImageView) view.findViewById(R.id.imageview_more);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Variety item = list.get(pos);
		holder.nameText.setText(item.getVarietyName() + " " +item.getSubId());
		holder.desText.setText(item.getOwner());
		holder.desText.setLines(1);
		if (TextUtils.isEmpty(item.getOwner())) {
			holder.desText.setVisibility(View.GONE);
		} else {
			holder.desText.setVisibility(View.VISIBLE);
		}
		holder.moreImage.setVisibility(View.VISIBLE);
		return view;
	}

	public static class ViewHolder {
		TextView nameText;
		TextView desText;
		ImageView moreImage;
	}

}
