package com.dasinong.app.components.home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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

import com.dasinong.app.ui.HarmDetialsActivity;
import com.dasinong.app.ui.HarmListActivity;

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

    private int mNatDisasterRes;
    private int mPetDisasterRes;

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

        mNatDisasterRes = R.drawable.natdisaster_bg;
        mPetDisasterRes = R.drawable.disaster_bg;

        initTopView();

        initBottomView();

        updateView(null, null);

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
        mBottomView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        mBottomView.setText(R.string.disaster_show);
        mBottomView.setGravity(Gravity.CENTER);
        mBottomView.setTextColor(Color.parseColor("#1768bc"));
        mBottomLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //set event
        mBottomClickListener = new BottomClickListener();
        mBottomView.setOnClickListener(mBottomClickListener);

    }


    public void updateView(List<FieldEntity.CurrentFieldEntity.NatdiswsEntity> natdiswsEntityList, List<FieldEntity.CurrentFieldEntity.PetdiswsEntity> petdiswsEntities) {
        this.removeAllViews();
        addTopView();
        updatePetView(petdiswsEntities);
        updateNatView(natdiswsEntityList);
        addBottomView();

    }

    private void addTopView() {

        ViewParent parent = mTopView.getParent();
        if (parent != null) {
            ViewGroup container = (ViewGroup) parent;
            container.removeView(mTopView);
        }

        this.addView(mTopView, 0,mTopLayoutParam);

    }

    private void updateNatView(List<FieldEntity.CurrentFieldEntity.NatdiswsEntity> natdiswsEntityList) {
        View child;
        if(natdiswsEntityList!=null&&!natdiswsEntityList.isEmpty()){
            for (FieldEntity.CurrentFieldEntity.NatdiswsEntity item:natdiswsEntityList) {
                child = createNatView(item);
                this.addView(child);
            }
        }


    }

    private void updatePetView(List<FieldEntity.CurrentFieldEntity.PetdiswsEntity> petdiswsEntities) {
        View child ;
        if(petdiswsEntities!=null&&!petdiswsEntities.isEmpty()){
            for (FieldEntity.CurrentFieldEntity.PetdiswsEntity item:petdiswsEntities) {
                child = createPetView(item);
                this.addView(child);
            }
        }

    }

    private View createPetView(FieldEntity.CurrentFieldEntity.PetdiswsEntity item) {

        View child = mLayoutInflater.inflate(R.layout.view_home_disaster, this, false);

        TextView disasterdesc = (TextView) child.findViewById(R.id.disaster_desc);
        ImageView disastericon = (ImageView) child.findViewById(R.id.disaster_icon);
        TextView disastername = (TextView) child.findViewById(R.id.disaster_name);
        TextView disastertype = (TextView) child.findViewById(R.id.disaster_type);

        disastername.setText(item.petDisSpecName);
        disastertype.setBackgroundResource(mPetDisasterRes);

        disastertype.setText("病害预警");

        disasterdesc.setText("稻瘟病是水稻重要病害之一 可引起大幅度减产，严重时减产40%～50%，甚至颗粒无收。" +
                "世界各稻区均匀发生。本病在各地均有发生，其中以叶部、节部发生为多，发生后可造成不同程度减产可造成白穗以致绝产");
        child.findViewById(R.id.disaster_prevent).setOnClickListener(new PreVentClickListener(16));
        child.findViewById(R.id.disaster_cure).setOnClickListener(new CureClickListener(16));
        
        child.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//TODO: flag editor
				Intent intent=HarmDetialsActivity.createIntent(16, HarmDetialsActivity.FLAG_ITEM, getContext());
				getContext().startActivity(intent);
				
			}
        	
        	
        	
        });
        return child;

    }



    private View createNatView(FieldEntity.CurrentFieldEntity.NatdiswsEntity item) {

        View child = mLayoutInflater.inflate(R.layout.view_home_disaster, this, false);

        TextView disasterdesc = (TextView) child.findViewById(R.id.disaster_desc);
        ImageView disastericon = (ImageView) child.findViewById(R.id.disaster_icon);
        TextView disastername = (TextView) child.findViewById(R.id.disaster_name);
        TextView disastertype = (TextView) child.findViewById(R.id.disaster_type);
        disastername.setText(item.natDisSpecName);
        disastertype.setBackgroundResource(mNatDisasterRes);

        disastertype.setText("病害预警");
        disasterdesc.setText("稻瘟病是水稻重要病害之一 可引起大幅度减产，严重时减产40%～50%，甚至颗粒无收。" +
                "世界各稻区均匀发生。本病在各地均有发生，其中以叶部、节部发生为多，发生后可造成不同程度减产可造成白穗以致绝产");
        child.findViewById(R.id.disaster_prevent).setOnClickListener(new PreVentClickListener(16));
        child.findViewById(R.id.disaster_cure).setOnClickListener(new CureClickListener(16));
        
        
        child.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//TODO:nat disaster H5
				Toast.makeText(getContext(), "natdisaster url", Toast.LENGTH_SHORT).show();
				
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
        public PreVentClickListener(int petDisspecId){
            this.petDisspecId = petDisspecId;

        }
        @Override
        public void onClick(View v) {
            //需要标明你是点击防治，预防，还是该条item跳进来的
            Intent intent = HarmDetialsActivity.createIntent(petDisspecId,HarmDetialsActivity.FLAG_PREVENT,getContext());
            v.getContext().startActivity(intent);

        }
    }

    class CureClickListener implements View.OnClickListener {
        private int petDisspecId;
        public CureClickListener(int petDisspecId){
            this.petDisspecId = petDisspecId;

        }
        @Override
        public void onClick(View v) {

            Intent intent = HarmDetialsActivity.createIntent(petDisspecId,HarmDetialsActivity.FLAG_CURE,getContext());
            v.getContext().startActivity(intent);
        }
    }
}
 