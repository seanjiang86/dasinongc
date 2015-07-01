package com.dasinong.app.ui.adapter;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SearchItem;
import com.dasinong.app.entity.SmsSubscribeItem;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.MyInfoActivity;
import com.dasinong.app.ui.manager.AccountManager;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchResultAdapter extends MyBaseAdapter<SearchItem> {

	private boolean isDelete = false;
	
	public SearchResultAdapter(Context ctx, List<SearchItem> list, boolean flag) {
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
			view = View.inflate(context, R.layout.view_search_result_item, null);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			holder.desText = (TextView) view.findViewById(R.id.textview_description);
			holder.typeText = (TextView) view.findViewById(R.id.textview_title_type);
			holder.line1View =  view.findViewById(R.id.view_line1);
			holder.contentLayout =  view.findViewById(R.id.layout_content);
			holder.typeLayout =  view.findViewById(R.id.layout_type);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final SearchItem item = list.get(pos);
		
		if(item.isType()){
			holder.contentLayout.setVisibility(View.GONE);
			holder.line1View.setVisibility(View.VISIBLE);
			holder.typeLayout.setVisibility(View.VISIBLE);
			holder.typeText.setText(item.getName());
		}else{
			holder.nameText.setText(item.getName());
			holder.desText.setText(Html.fromHtml(item.getSource()));
			
			holder.contentLayout.setVisibility(View.VISIBLE);
			holder.line1View.setVisibility(View.GONE);
			holder.typeLayout.setVisibility(View.GONE);
		}
		
		return view;
	}
	

	public static class ViewHolder {
		TextView  nameText;
		TextView  desText;
		View contentLayout;
		
		View line1View;
		View typeLayout;
		TextView typeText;
	}
	
}
