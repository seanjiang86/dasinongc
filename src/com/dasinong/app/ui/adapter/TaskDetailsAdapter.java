package com.dasinong.app.ui.adapter;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.task.domain.Steps;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SmsSubscribeItem;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.MyInfoActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.liam.imageload.LoadUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskDetailsAdapter extends MyBaseAdapter<Steps> {

	public TaskDetailsAdapter(Context ctx, List<Steps> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_task_steps_item, null);
			holder.stepImage = (ImageView) view.findViewById(R.id.imageview_steps_image);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			holder.contentText = (TextView) view.findViewById(R.id.textview_content);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Steps item = list.get(pos);

		LoadUtils.getInstance().loadImage(holder.stepImage, "http://182.254.129.101:8080/nongshi/" + item.picture + ".jpg");

		holder.nameText.setText(item.stepName);
		holder.contentText.setText(item.description);
		return view;
	}

	public static class ViewHolder {
		ImageView stepImage;
		TextView nameText;
		TextView contentText;
	}

}
