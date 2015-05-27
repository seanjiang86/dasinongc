package com.dasinong.app.utils;

import com.dasinong.app.ui.view.LoadingDialog;

import android.content.Context;

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
	
}
