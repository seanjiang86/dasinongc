package com.dasinong.app.ui.adapter;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SmsSubscribeItem;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.MyInfoActivity;
import com.dasinong.app.ui.manager.AccountManager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SmsSubscribeAdapter extends MyBaseAdapter<SmsSubscribeItem> {

	private boolean isDelete = false;
	
	public SmsSubscribeAdapter(Context ctx, List<SmsSubscribeItem> list, boolean flag) {
		super(ctx, list, flag);
	}
	
	public void setDeleteState(boolean state){
		this.isDelete = state;
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
		final SmsSubscribeItem item = list.get(pos);
		holder.nameText.setText(item.getTargetName());
		if(isDelete){
			holder.deleteImage.setVisibility(View.VISIBLE);
			holder.deleteImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showLogoutDialog(item);
				}
			});
		}else{
			holder.deleteImage.setVisibility(View.GONE);
		}
		return view;
	}
	
	protected void deleteItem(final SmsSubscribeItem item) {
		((BaseActivity)context).startLoadingDialog();
		RequestService.getInstance().deleteSmsSub(context, item.getId(), BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				((BaseActivity)context).dismissLoadingDialog();
				if(resultData.isOk()){
					list.remove(item);
					notifyDataSetChanged();
				}else{
					((BaseActivity)context).showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				((BaseActivity)context).dismissLoadingDialog();
			}
		});
	}
	
	
	private void showLogoutDialog(final SmsSubscribeItem item) {
		final Dialog dialog = new Dialog(context, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText("确定删除此条订阅记录?");
		tv.setTextSize(18);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setText("取消");
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setText("确定");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				deleteItem(item);
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public static class ViewHolder {
		ImageView  deleteImage;
		TextView  nameText;
	}
	
}
