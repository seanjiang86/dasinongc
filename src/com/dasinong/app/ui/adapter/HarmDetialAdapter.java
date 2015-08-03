package com.dasinong.app.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.PetSolu;
import com.dasinong.app.entity.HarmDetialEntity.Solutions;

public class HarmDetialAdapter extends MyBaseAdapter<Solutions> {

	private int soluSize;

	public HarmDetialAdapter(Context ctx, List<Solutions> list, int soluSize, boolean flag) {
		super(ctx, list, flag);
		this.soluSize = soluSize;
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.treatment_item, null);
			holder.ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.small_line = view.findViewById(R.id.small_line);
			holder.tv_method_name = (TextView) view.findViewById(R.id.tv_method_name);
			holder.tv_crop_stage = (TextView) view.findViewById(R.id.tv_crop_stage);
			holder.tv_provider = (TextView) view.findViewById(R.id.tv_provider);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.big_line = view.findViewById(R.id.big_line);
			holder.tv_drug = (TextView) view.findViewById(R.id.tv_drug);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (pos == 0) {
			holder.big_line.setVisibility(View.GONE);
		} else if (pos == soluSize - 1) {
			holder.ll_title.setVisibility(View.GONE);
			holder.small_line.setVisibility(View.GONE);
		} else if (pos == soluSize) {
			holder.big_line.setVisibility(View.GONE);
		} else {
			holder.ll_title.setVisibility(View.GONE);
			holder.small_line.setVisibility(View.GONE);
			holder.big_line.setVisibility(View.GONE);
		}
		if (pos < soluSize) {
			char c = (char) (65 + pos);
			holder.tv_method_name.setText("治疗方案" + c);
		} else {
			char c = (char) (65 + pos - soluSize);
			holder.tv_title.setText("预防方法");
			holder.tv_method_name.setText("预防方案" + c);
		}

		holder.tv_content.setText(list.get(pos).petSoluDes);

		// TODO MING:等待数据
		if (TextUtils.isEmpty(list.get(pos).subStageId) || "0".equals(list.get(pos).subStageId)) {
			holder.tv_crop_stage.setVisibility(View.GONE);
		} else {
			holder.tv_crop_stage.setVisibility(View.VISIBLE);
			holder.tv_crop_stage.setText(list.get(pos).subStageId);
		}
		
		if (TextUtils.isEmpty(list.get(pos).providedBy) || "0".equals(list.get(pos).providedBy)) {
			holder.tv_provider.setVisibility(View.GONE);
		} else {
			holder.tv_provider.setVisibility(View.VISIBLE);
			holder.tv_provider.setText(list.get(pos).providedBy);
		}
		
		if(TextUtils.isEmpty(list.get(pos).snapshotCP)){
			holder.tv_drug.setVisibility(View.GONE);
		} else {
			holder.tv_drug.setVisibility(View.VISIBLE);
			holder.tv_drug.setText("相关药物："+list.get(pos).snapshotCP);
		}
		return view;
	}

	public static class ViewHolder {
		LinearLayout ll_title;
		View small_line;
		TextView tv_title;
		TextView tv_method_name;
		TextView tv_crop_stage;
		TextView tv_provider;
		TextView tv_content;
		TextView tv_drug;
		View big_line;
	}

}
