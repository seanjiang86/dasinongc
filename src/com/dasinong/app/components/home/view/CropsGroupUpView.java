package com.dasinong.app.components.home.view;


import android.app.Dialog;
import android.content.Context;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dasinong.app.R;
import com.dasinong.app.components.home.view.dialog.ConfirmDialog;
import com.dasinong.app.database.task.domain.SubStage;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    private ImageView leftArrowView, rightArrowView;
    private ConfirmDialog confirmDialog;

    private int mCurrentPostion;
    private List<SubStage> mSubStages;



    private static TreeMap<Integer,Integer> cropIconResource = new TreeMap<>();


    private static TreeMap<Integer,Integer> cropNoIconResource = new TreeMap<>();

    private boolean isLeft;

    private static final String TAG = "CropsGroupUpView";

    private HorizontalScrollView mSubstageContainer;


    public CropsGroupUpView(Context context) {
        super(context);
        init(context);
    }

    public CropsGroupUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        initView(context);
        setEvent();
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_top_work_content, this);
        normalParentView = rootView.findViewById(R.id.normal_state);
        harvestTimeView = (TextView) rootView.findViewById(R.id.harvest_time);
        rightStateView = (TextView) rootView.findViewById(R.id.right_state_content);
        addCropViewParent = rootView.findViewById(R.id.add_crop_parent);
        addCropView = (TextView) rootView.findViewById(R.id.add_crop);

        leftArrowView = (ImageView) rootView.findViewById(R.id.left_arrow);

        rightArrowView = (ImageView) rootView.findViewById(R.id.right_arrow);



    }

    private void setEvent() {
        addCropViewParent.setOnClickListener(this);
        leftArrowView.setOnClickListener(this);
        rightArrowView.setOnClickListener(this);
    }


    public void showNormalStatus() {
        normalParentView.setVisibility(View.VISIBLE);
        addCropViewParent.setVisibility(View.GONE);
    }

    public void showNOFieldStatus() {
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
        if(mSubStageLists==null||mSubStageLists.isEmpty()){
            return;
        }
        updateStageIcon();
    }

    private void updateStageIcon() {

        if(mSubstageContainer!=null){
            LinearLayout mIconContainer  = null;
            //TODO:add ImageView
            //根据当前的的状态进行点亮不同的图标，之前的是点亮，之后的是不点的
            //TODO: a row five
            for(Map.Entry<Integer,Integer> entity: cropIconResource.entrySet()){

                //TODO:create imageView

            }

        }

    }


    private int getIconBySubstageId(int subStageId){

        int mCurrentStageId = mSubStages.get(mCurrentPostion).subStageId;

        int resId = 0;
        if(mCurrentStageId>subStageId){
            resId = cropIconResource.get(subStageId);
        }else {
            resId = cropNoIconResource.get(subStageId);
        }
        return resId;

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


    static {

        cropIconResource.put(35,R.drawable.sowbefore);
        cropIconResource.put(36,R.drawable.sow);
        cropIconResource.put(37,R.drawable.germination);
        cropIconResource.put(38,R.drawable.leafe);


        cropIconResource.put(39,R.drawable.twiceleafe);
        cropIconResource.put(40,R.drawable.threeleafe);

        cropIconResource.put(41,R.drawable.pretransplant);
        cropIconResource.put(42,R.drawable.transplant);



        cropIconResource.put(43,R.drawable.seedling);



        cropIconResource.put(44,R.drawable.tillering);
        cropIconResource.put(45,R.drawable.twicetillering);
        cropIconResource.put(46,R.drawable.threetillering);


        cropIconResource.put(47,R.drawable.fourperiod);
        cropIconResource.put(48,R.drawable.threeperiod);
        cropIconResource.put(49,R.drawable.twoperiod);
        cropIconResource.put(50,R.drawable.oneperiod);



        cropIconResource.put(51,R.drawable.flowerhead);
        cropIconResource.put(52,R.drawable.head);
        cropIconResource.put(53,R.drawable.fullhead);

        cropIconResource.put(54,R.drawable.startfill);
        cropIconResource.put(55,R.drawable.fill);
        cropIconResource.put(56,R.drawable.fillstage);
        cropIconResource.put(57,R.drawable.yellowfill);


        cropIconResource.put(58,R.drawable.lastbefore);
        cropIconResource.put(59,R.drawable.last);



        //非点亮
        cropNoIconResource.put(35,R.drawable.sowbeforeno);
        cropNoIconResource.put(36,R.drawable.sowno);
        cropNoIconResource.put(37,R.drawable.germinationno);
        cropNoIconResource.put(38,R.drawable.leafeno);


        cropNoIconResource.put(39,R.drawable.twiceleafeno);
        cropNoIconResource.put(40,R.drawable.threeleafeno);

        cropNoIconResource.put(41,R.drawable.pretransplantno);
        cropNoIconResource.put(42,R.drawable.transplantno);



        cropNoIconResource.put(43,R.drawable.seedlingno);



        cropNoIconResource.put(44,R.drawable.tilleringno);
        cropNoIconResource.put(45,R.drawable.twicetilleringno);
        cropNoIconResource.put(46,R.drawable.threetilleringno);


        cropNoIconResource.put(47,R.drawable.fourperiodno);
        cropNoIconResource.put(48,R.drawable.threeperiodno);
        cropNoIconResource.put(49,R.drawable.twoperiodno);
        cropNoIconResource.put(50,R.drawable.oneperiodno);



        cropNoIconResource.put(51,R.drawable.flowerheadno);
        cropNoIconResource.put(52,R.drawable.headno);
        cropNoIconResource.put(53,R.drawable.fullheadno);

        cropNoIconResource.put(54,R.drawable.startfillno);
        cropNoIconResource.put(55,R.drawable.fillno);
        cropNoIconResource.put(56,R.drawable.fillstageno);
        cropNoIconResource.put(57,R.drawable.yellowfillno);


        cropNoIconResource.put(58,R.drawable.lastbeforeno);
        cropNoIconResource.put(59,R.drawable.lastno);








    }
}
