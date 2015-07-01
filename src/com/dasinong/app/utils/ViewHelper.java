package com.dasinong.app.utils;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.LoadingDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ViewHelper {

	
	public static LoadingDialog getLoadingDialog(Context context,String loadingText,boolean cancelable, boolean outeSiteCanceled) {
	    LoadingDialog dialog = new LoadingDialog(context);
	    if (!StringHelper.invalidString(loadingText)) {
		    dialog.setLoadText(loadingText);
		}
	    dialog.setCanceledOnTouchOutside(outeSiteCanceled);
	    dialog.setCancelable(cancelable);
	    return dialog;
    }
	
	public static  void setListVIewEmptyView(Context context,ListView listview) {
		ViewGroup parentView = (ViewGroup) listview.getParent();  
		View emptyView = parentView.findViewById(R.id.layout_empty);
		listview.setEmptyView(emptyView);
	}
	
}
