package com.dasinong.app.components.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;

/**
 * Created by liuningning on 15/6/5.
 */
public class BannerView extends LinearLayout implements View.OnClickListener {

    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_home_banner, this,true);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO banner

    }
}
