package com.dasinong.app.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.NatDisspec;
import com.dasinong.app.database.disaster.domain.PetDisspec;

public class HarmAdapter<T> extends MyBaseAdapter<T> {
	
	private int fragmentPosition;
	
	public HarmAdapter(Context ctx, List<T> list, int fragmentPosition, boolean flag) {
		super(ctx, list, flag);
		this.fragmentPosition = fragmentPosition;
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.harm_list_item, null);
			holder.iv_harm_pic = (ImageView) view.findViewById(R.id.iv_harm_pic);
			holder.tv_harm_name = (TextView) view.findViewById(R.id.tv_harm_name);
			holder.tv_harm_des = (TextView) view.findViewById(R.id.tv_harm_des);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (fragmentPosition == 3) {
			NatDisspec nat = (NatDisspec) list.get(pos);
			holder.iv_harm_pic.setVisibility(View.GONE);
			holder.tv_harm_name.setText(nat.natDisSpecName);
			holder.tv_harm_des.setText(nat.solution);
		} else {
			PetDisspec pet = (PetDisspec) list.get(pos);
			holder.iv_harm_pic.setImageResource(R.drawable.test_pic);
			holder.tv_harm_name.setText(pet.petDisSpecName);
			holder.tv_harm_des.setText(pet.description);
		}

		return view;
	}

	public static class ViewHolder {
		ImageView iv_harm_pic;
		TextView tv_harm_name;
		TextView tv_harm_des;
	}
}
