package com.dasinong.app.components.home.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dasinong.app.R;


/**
 * @author
 */
public class ConfirmDialog extends BaseDialog {

    private View contentView;
    private Button enterBtn; //右侧按钮
    private Button cancelBtn; //左按钮
    private TextView titleTv; //头部信息
    private TextView contentTv; //提示信息
    private ButtonClickListener buttonClickListener;

    public ConfirmDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View onAddConentView() {
        contentView = inflateView(R.layout.dialog_content_simple);
        return contentView;
    }

    @Override
    protected void initView() {
        titleTv = (TextView) contentView
                .findViewById(R.id.tv_dialog_simple_title);
        contentTv = (TextView) contentView
                .findViewById(R.id.tv_dialog_simple_content);
        enterBtn = (Button) contentView
                .findViewById(R.id.btn_dialog_simple_enter);
        cancelBtn = (Button) contentView
                .findViewById(R.id.btn_dialog_simple_cancel);
    }

    @Override
    protected void initData() {
        // 关闭按钮是否显示
//		showCloseOutside(false);
        setTitle("");
    }

    private View.OnClickListener mClickListener;

    @Override
    protected void setListener() {
        if (mClickListener == null) {
            initClickListener();
        }
        enterBtn.setOnClickListener(mClickListener);
        cancelBtn.setOnClickListener(mClickListener);
    }

    /**
     * 设置左右按钮点击事件
     *
     * @param buttonClickListener
     */
    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    /**
     * 设置左右按钮显示文字  为空则隐藏按钮不显示
     *
     * @param left
     * @param right
     */
    public void setButtonText(CharSequence left, CharSequence right) {
        if (TextUtils.isEmpty(left)) {
            cancelBtn.setVisibility(View.GONE);
        } else if (cancelBtn.getVisibility() == View.GONE) {
            cancelBtn.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(right)) {
            enterBtn.setVisibility(View.GONE);
        } else if (enterBtn.getVisibility() == View.GONE) {
            enterBtn.setVisibility(View.VISIBLE);
        }
        enterBtn.setText(right);
        cancelBtn.setText(left);
    }

    /**
     * 设置dialog title
     */
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            titleTv.setVisibility(View.GONE);
        }
        titleTv.setText(title);
    }

    /**
     * 设置dialog 显示内容
     *
     * @param msg
     */
    public void setMessage(CharSequence msg) {
        // 内容
        contentTv.setText(msg);
    }

    private void initClickListener() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener == null) {
                    return;
                }
                if (v == cancelBtn) {
                    buttonClickListener.onLeftClick(ConfirmDialog.this, cancelBtn.getText().toString());
                } else if (v == enterBtn) {
                    buttonClickListener.onRightClick(ConfirmDialog.this, enterBtn.getText().toString());
                }
            }
        };
    }

    public static interface ButtonClickListener {
        void onLeftClick(Dialog dialog, String txt);

        void onRightClick(Dialog dialog, String txt);
    }
}