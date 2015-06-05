package com.dasinong.app.components.home.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;


import static android.util.TypedValue.*;


/**
 * Created by liuningning on 15/6/5.
 */
public class DisasterView extends LinearLayout {

    private TextView mDisasterType;
    private TextView mDisasterName;
    private TextView mDisasterDesc;
    private TextView mDisasterPrevent;
    private TextView mDisasterCure;
    private ImageView mDisasterIcon;

    private int mDefaultBottomPadding = 2;

    public DisasterView(Context context) {
        super(context);
        initView();
    }

    public DisasterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        this.setBackgroundColor(Color.WHITE);
        /*get displayMetrics*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mDefaultBottomPadding = (int) applyDimension(COMPLEX_UNIT_DIP, mDefaultBottomPadding, displayMetrics);

        /**set bottomPadding*/
        this.setPadding(0, 0, 0, mDefaultBottomPadding);
        LayoutInflater.from(getContext()).inflate(R.layout.view_home_disaster, this, true);

        /**find all view*/
        mDisasterType = (TextView) findViewById(R.id.disaster_type);
        mDisasterName = (TextView) findViewById(R.id.disaster_name);
        mDisasterIcon = (ImageView) findViewById(R.id.disaster_icon);
        mDisasterDesc = (TextView) findViewById(R.id.disaster_desc);
        mDisasterPrevent = (TextView) findViewById(R.id.disaster_prevent);
        mDisasterCure = (TextView) findViewById(R.id.disaster_cure);

    }


    public void updateView() {

        mDisasterType.setText("病害预警");
        mDisasterName.setText("稻瘟病");
        mDisasterDesc.setText("稻瘟病是水稻重要病害之一 可引起大幅度减产，严重时减产40%～50%" +
                "，甚至颗粒无收。世界各稻区均匀发生。本病在各地均有发生，其中以叶部、节部发生为多" +
                "，发生后可造成不同程度减产可造成白穗以致绝产。");


    }


}
 