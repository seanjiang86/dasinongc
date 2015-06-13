package com.dasinong.app.components.home.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dasinong.app.R;

/**
 * @author 基础的dialog
 */
public abstract class BaseDialog extends Dialog {

    protected Context mContext;
    protected LayoutInflater inflater;
    private LinearLayout mContentLayout;

    public BaseDialog(Context context) {
        super(context, R.style.dialog_normal);
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        setContentView(R.layout.dialog_base);
        // 基类
        initBaseView();
        // 子类
        initView();
        initData();
        setListener();
    }

    public BaseDialog(Context context, int dialogNormal) {
        super(context, R.style.dialog_normal);
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        setContentView(R.layout.dialog_base);
        // 基类
        initBaseView();
        // 子类
        initView();
        initData();
        setListener();
    }

    protected View inflateView(int resid) {
        return inflater.inflate(resid, null);
    }

    private void initBaseView() {
        mContentLayout = (LinearLayout) findViewById(R.id.ll_dialog_base_content);
        mContentLayout.addView(onAddConentView());
    }


    protected abstract View onAddConentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();
}