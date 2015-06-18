package com.dasinong.app.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dasinong.app.R;

public class PPCPopMenu {
	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private PopAdapter adapter;

	public PPCPopMenu(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.ppc_popmenu, null);
		itemList = new ArrayList<String>();
		// 设置 listview
		listView = (ListView) view.findViewById(R.id.listView);
		
		adapter = new PopAdapter();
		
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	// 设置菜单项点击监听器
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	
	public void addItems(List<String> list) {
		if(itemList != null){
			itemList.clear();
		}
		itemList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	// 单个添加菜单项
	public void addItem(String item) {
		itemList.add(item);
	}

	public Object getItem(int position) {
		return itemList.get(position);
	}

	// 下拉式 弹出 pop菜单 parent
	public void showAsDropDown(View parent) {
		
		// 保证尺寸是根据屏幕像素密度来的
		popupWindow.showAsDropDown(parent, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		if(popupWindow != null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {

		public int getCount() {
			return itemList.size();
		}

		public Object getItem(int position) {
			return itemList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(context).inflate(R.layout.ppc_pomenu_item, null);
			TextView groupItem = (TextView) convertView.findViewById(R.id.textViews);
			groupItem.setText(itemList.get(position));
			return convertView;
		}
	}
}
