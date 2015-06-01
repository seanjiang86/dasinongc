
package com.dasinong.app.ui.view;

import com.dasinong.app.R;
import com.dasinong.app.utils.StringHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

    private Context mContext;
    private LayoutInflater inflater;
    private LayoutParams lp;
    private TextView loadtext;
	private ProgressBar progressBar;

    public LoadingDialog(Context context) {
        super(context, R.style.confirm_dialog);
        this.mContext = context;

        View layout = LayoutInflater.from(context).inflate(R.layout.layout_loadingdialog, null);
        loadtext = (TextView) layout.findViewById(R.id.loading_text);
        progressBar = (ProgressBar) layout.findViewById(R.id.loading_bar);
        setContentView(layout);
        
        Window window = getWindow();
    	window.setWindowAnimations(R.style.customDialog_anim_style);
        
//        // 设置window属性
//        lp = getWindow().getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.dimAmount = 0; // 去背景遮盖
//        lp.alpha = 0.5f;
        
    }

    public void setDimAmount(boolean b){
    	if (b) {
    		getWindow().setAttributes(lp);
		}
    }
    
    public void setLoadText(String content) {
        if (loadtext.getVisibility() != View.VISIBLE) {
            loadtext.setVisibility(View.VISIBLE);
        }
        if (!StringHelper.invalidString(content)) {
			loadtext.setText(content);
		}
    }
    
    public void setLoadText(int resId){
        if (loadtext.getVisibility() != View.VISIBLE) {
            loadtext.setVisibility(View.VISIBLE);
        }
        loadtext.setText(resId);
    }
    @Override
    public void show() {
    	super.show();
    }
    
    public void dismissByResult(boolean result,String loadingText,int duration) {
    	if (duration <= 0) {
			dismiss();
		}
		// TODO Auto-generated method stub
		if (StringHelper.invalidString(loadingText)) {
			setLoadText(result?"成功":"失败");
		}else{
			setLoadText(loadingText);
		}
    	progressBar.setVisibility(View.GONE);
    	new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		}, duration);
	}
}
