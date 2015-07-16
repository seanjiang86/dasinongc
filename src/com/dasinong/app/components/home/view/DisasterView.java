package com.dasinong.app.components.home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Debug;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;

import com.dasinong.app.components.domain.FieldEntity;


import com.dasinong.app.database.disaster.dao.impl.PetDisspecDaoImpl;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.ui.HarmDetialsActivity;
import com.dasinong.app.ui.HarmListActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;


/**
 * disaster item view
 * Created by lxn on 15/6/5.
 */
public class DisasterView extends LinearLayout {


    private int mDefaultBottomPadding = 2;

    private LayoutInflater mLayoutInflater;

    private TextView mBottomView;

    private LinearLayout.LayoutParams mBottomLayoutParam;

    private BottomClickListener mBottomClickListener;


    private View mTopView;

    private LinearLayout.LayoutParams mTopLayoutParam;


    private int mPetDisasterRes;

    private PetDisspecDaoImpl dao;


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

        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        mPetDisasterRes = R.drawable.disaster_bg;

        initTopView();

        initBottomView();

        //updateView(null, null);

        dao = new PetDisspecDaoImpl(this.getContext());

    }

    private void initTopView() {
        mTopView = new View(this.getContext());
        mTopLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.home_dimen_20));
        mTopView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ededed")));

    }

    private void initBottomView() {
        int padding = (int) getResources().getDimension(R.dimen.home_dimen_30);
        mBottomView = new TextView(this.getContext());
        mBottomView.setPadding(padding, padding, padding, padding);
        mBottomView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mBottomView.setText(R.string.disaster_show);
        mBottomView.setGravity(Gravity.CENTER);
        mBottomView.setBackgroundResource(R.drawable.button_white_bg_selector);
        mBottomView.setTextColor(Color.parseColor("#1768bc"));
        mBottomLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //set event
        mBottomClickListener = new BottomClickListener();
        mBottomView.setOnClickListener(mBottomClickListener);

    }

    //false,true

    public synchronized void updateView(List<FieldEntity.CurrentFieldEntity.Petdisspecws> list, List<FieldEntity.CurrentFieldEntity.PetdiswsEntity> petdiswsEntities) {

        this.removeAllViews();
        addTopView();
        if ((list == null || list.isEmpty()) && (petdiswsEntities == null||petdiswsEntities.isEmpty())) {
            return;
        }


        updatePetdisspecws(list);

        updatePetView(petdiswsEntities);


        addBottomView();

    }




    private void addTopView() {

        ViewParent parent = mTopView.getParent();
        if (parent != null) {
            ViewGroup container = (ViewGroup) parent;
            container.removeView(mTopView);
        }

        this.addView(mTopView, 0, mTopLayoutParam);

    }



    private void updatePetdisspecws(List<FieldEntity.CurrentFieldEntity.Petdisspecws> items) {
        View child;
        if (items != null && !items.isEmpty()) {
            for (FieldEntity.CurrentFieldEntity.Petdisspecws item : items) {
                child = createPetdisspecws(item);
                this.addView(child);
            }
        }

    }



    private View createPetdisspecws(final FieldEntity.CurrentFieldEntity.Petdisspecws item) {

        View child = mLayoutInflater.inflate(R.layout.view_home_disaster, this, false);

        TextView desc = (TextView) child.findViewById(R.id.disaster_desc);
        ImageView icon = (ImageView) child.findViewById(R.id.disaster_icon);
        TextView name = (TextView) child.findViewById(R.id.disaster_name);
        TextView type = (TextView) child.findViewById(R.id.disaster_type);

        name.setText(item.petDisSpecName);

        type.setBackgroundResource(R.drawable.natdisaster_bg);


        type.setText("易发"+getDisasterString(item.type));
        icon.setImageResource(getDisasterIcon(item.type));
        
//        System.out.println(item.type);

        desc.setText(item.sympton);

        child.findViewById(R.id.disaster_prevent).setOnClickListener(new PreVentClickListener(item.petDisSpecId));
        child.findViewById(R.id.disaster_cure).setOnClickListener(new CureClickListener(item.petDisSpecId));
        child.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = HarmDetialsActivity.createIntent(item.petDisSpecId, HarmDetialsActivity.FLAG_ITEM, getContext());
                getContext().startActivity(intent);

            }


        });
        return child;

    }


    private void updatePetView(List<FieldEntity.CurrentFieldEntity.PetdiswsEntity> petdiswsEntities) {
        View child;
        if (petdiswsEntities != null && !petdiswsEntities.isEmpty()) {
            for (FieldEntity.CurrentFieldEntity.PetdiswsEntity item : petdiswsEntities) {
                child = createPetView(item);
                this.addView(child);
            }
        }

    }

    private View createPetView(final FieldEntity.CurrentFieldEntity.PetdiswsEntity item) {

        View child = mLayoutInflater.inflate(R.layout.view_home_disaster, this, false);

        TextView desc = (TextView) child.findViewById(R.id.disaster_desc);
        ImageView icon = (ImageView) child.findViewById(R.id.disaster_icon);
        TextView name = (TextView) child.findViewById(R.id.disaster_name);
        TextView type = (TextView) child.findViewById(R.id.disaster_type);

        name.setText(item.petDisSpecName);

        type.setBackgroundResource(mPetDisasterRes);


        type.setText(getDisasterString(item.type) + "预警");
        icon.setImageResource(getDisasterIcon(item.type));
        
        
        desc.setText(item.description);
        child.findViewById(R.id.disaster_prevent).setOnClickListener(new PreVentClickListener(item.petDisSpecId));
        child.findViewById(R.id.disaster_cure).setOnClickListener(new CureClickListener(item.petDisSpecId));
        child.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //DONE
                Intent intent = HarmDetialsActivity.createIntent(item.petDisSpecId, HarmDetialsActivity.FLAG_ITEM, getContext());
                getContext().startActivity(intent);

            }


        });
        return child;

    }


    private void addBottomView() {
        ViewParent parent = mBottomView.getParent();
        if (parent != null) {
            ViewGroup container = (ViewGroup) parent;
            container.removeView(mBottomView);
        }

        this.addView(mBottomView, -1, mBottomLayoutParam);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }


    class BottomClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HarmListActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    class PreVentClickListener implements View.OnClickListener {

        private int petDisspecId;

        public PreVentClickListener(int petDisspecId) {
            this.petDisspecId = petDisspecId;

        }

        @Override
        public void onClick(View v) {
            //需要标明你是点击防治，预防，还是该条item跳进来的

            Intent intent = HarmDetialsActivity.createIntent(petDisspecId, HarmDetialsActivity.FLAG_PREVENT, getContext());
            v.getContext().startActivity(intent);

        }
    }

    class CureClickListener implements View.OnClickListener {
        private int petDisspecId;

        public CureClickListener(int petDisspecId) {
            this.petDisspecId = petDisspecId;

        }

        @Override
        public void onClick(View v) {

            Intent intent = HarmDetialsActivity.createIntent(petDisspecId, HarmDetialsActivity.FLAG_CURE, getContext());
            v.getContext().startActivity(intent);
        }
    }


    public String getDisasterString(String type) {
     if(TextUtils.isEmpty(type)){
         return "病害";
     }
        if (type.contains("病害")) {
            return "病害";

        } else if (type.contains("虫害")) {
            return "虫害";

        } else if (type.contains("草害")) {

            return "草害";
        } else {
            return "病害";
        }
    }


    public int getDisasterIcon(String type) {
        if(TextUtils.isEmpty(type)){
            return R.drawable.weed;
        }
        if (type.contains("病害")) {
            return R.drawable.disease;

        } else if (type.contains("虫害")) {
            return R.drawable.pest;

        } else if (type.contains("草害")) {

            return R.drawable.weed;
        } else {
            return R.drawable.naturaldis;
        }
    }
}
 