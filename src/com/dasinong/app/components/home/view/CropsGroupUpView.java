package com.dasinong.app.components.home.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.home.view.dialog.ConfirmDialog;
import com.dasinong.app.database.task.domain.SubStage;

import java.util.List;

/**
 * 农作物成长状况View--用于首页上部绿色背景中部的显示
 * Created by lnn on 15/6/2.
 */
public class CropsGroupUpView extends LinearLayout implements View.OnClickListener {
    //收获时间，时候时间右侧的状态，添加作物view(当没有作物的时候显示),叶子后面的内容
    private TextView harvestTimeView, rightStateView, addCropView;//leafContent;
    //正常view的父View,没有作物的parent
    private View normalParentView, addCropViewParent;

    //添加农作物
    private MyAddCropOnClickListener onAddCropClickListener;
    private ImageView leftArrowView, leafView, rightArrowView;
    private ConfirmDialog confirmDialog;

    private int mCurrentPostion;
    private List<SubStage> mSubStages;


    private boolean isLeft;

    private static final String TAG = "CropsGroupUpView";


    public CropsGroupUpView(Context context) {
        super(context);
        init(context);
    }

    public CropsGroupUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_top_work_content, this);
        normalParentView = rootView.findViewById(R.id.normal_state);
        harvestTimeView = (TextView) rootView.findViewById(R.id.harvest_time);
        rightStateView = (TextView) rootView.findViewById(R.id.right_state_content);
        addCropViewParent = rootView.findViewById(R.id.add_crop_parent);
        addCropView = (TextView) rootView.findViewById(R.id.add_crop);

        leftArrowView = (ImageView) rootView.findViewById(R.id.left_arrow);
        leafView = (ImageView) rootView.findViewById(R.id.leaf);
        rightArrowView = (ImageView) rootView.findViewById(R.id.right_arrow);
        addCropViewParent.setOnClickListener(this);
        leftArrowView.setOnClickListener(this);
        rightArrowView.setOnClickListener(this);
    }



    public void showNormalStatus() {
        normalParentView.setVisibility(View.VISIBLE);
        addCropViewParent.setVisibility(View.GONE);
    }

    public void showNOFildStatus() {
        normalParentView.setVisibility(View.GONE);
        addCropViewParent.setVisibility(View.VISIBLE);
    }


    public void updateHarvestDay(String harvestDay ){
        harvestTimeView.setText(harvestDay);
    }

    public void updateStageStatus(String stageStatus ){
        rightStateView.setText(stageStatus);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_crop_parent:
                // 添加作物


                break;
            case R.id.left_arrow:
                //左侧箭头
                isLeft = true;
                showConfirmDialog();
                break;
            case R.id.right_arrow:
                //右侧箭头
                isLeft = false;
                showConfirmDialog();
                break;
        }
    }

    private void showConfirmDialog() {

        if (mSubStages == null || mSubStages.isEmpty()) {
            return;
        }
        if (!isLeft) {


            if (mCurrentPostion + 1 >= mSubStages.size()) {
                Toast.makeText(this.getContext(), "sorry", Toast.LENGTH_SHORT).show();
                return;
            }

            mCurrentPostion += 1;

        } else {

            if (mCurrentPostion - 1 < 0) {
                Toast.makeText(this.getContext(), "sorry", Toast.LENGTH_SHORT).show();
                return;
            }

            mCurrentPostion -= 1;
        }
        initDialog();

        confirmDialog.setTitle("确认切换状态");


        confirmDialog.setMessage("您真的确认要切换到" + mSubStages.get(mCurrentPostion).subStageName + "的状态吗");
        confirmDialog.show();

    }

    private void initDialog() {
        if (null == confirmDialog) {
            confirmDialog = new ConfirmDialog(getContext());

            confirmDialog.setButtonClickListener(new ConfirmDialog.ButtonClickListener() {

                @Override
                public void onLeftClick(Dialog dialog, String txt) {
                    confirmDialog.dismiss();
                }

                @Override
                public void onRightClick(Dialog dialog, String txt) {
                    if (null != onAddCropClickListener) {
                        confirmDialog.dismiss();
                        onAddCropClickListener.onArrowViewClick(mCurrentPostion);
                    }
                }
            });
        }
    }


    /**
     * 添加农作物
     *
     * @param onAddCropClickListener --监听器
     */
    public void setOnAddCropClickListener(MyAddCropOnClickListener onAddCropClickListener) {
        this.onAddCropClickListener = onAddCropClickListener;
    }

    public void setPostionAndList(int mPosition, List<SubStage> mSubStageLists) {

        this.mCurrentPostion = mPosition;
        this.mSubStages = mSubStageLists;

    }

    //添加农作物
    public interface MyAddCropOnClickListener {
        /**
         * 添加农作物--（没有作物的加号）
         */
        void onAddCropClick();

        /**
         * 叶子右侧的箭头的点击
         */
        void onArrowViewClick(int position);
    }


    private void DEBUG(String msg) {
        if (true) {
            Log.d(TAG, msg);
        }
    }
}
