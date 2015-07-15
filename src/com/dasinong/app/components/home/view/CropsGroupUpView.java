package com.dasinong.app.components.home.view;


import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.home.view.dialog.ConfirmDialog;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.GraphicUtils;

import java.util.Iterator;
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



    private static TreeMap<Integer,Integer> cropIconResource = new TreeMap<Integer,Integer>();


    private static TreeMap<Integer,Integer> cropNoIconResource = new TreeMap<Integer,Integer>();

    private boolean isLeft;

    private static final String TAG = "CropsGroupUpView";

    private HorizontalScrollView mLeafParent;

    private LinearLayout mLeafContainer;
    private int itemWidth;


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

        mLeafParent = (HorizontalScrollView) findViewById(R.id.leaf_parent);

        mLeafContainer = (LinearLayout) findViewById(R.id.leaf_container);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        itemWidth = (width - GraphicUtils.dip2px(getContext(), 100)) / 5;




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
                final Context context=getContext();
                // TODO: 15/7/7 add crop
                if (DeviceHelper.checkNetWork(context)) {
                    if(!DeviceHelper.checkGPS(context)){
                        showRemindDialog("无法获取当前位置","请前往“设置”打开GPS卫星，设置完成后点”返回“键就可以回到今日农事","前往设置","暂不开启", new MyDialogClickListener() {
                            @Override
                            public void onSureButtonClick() {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelButtonClick() {
                                // TODO MING:该方法是否为检测登陆的方法
                                if (AccountManager.checkLogin(context)) {
                                    Intent intent = new Intent(context, AddFieldActivity1.class);
                                    context.startActivity(intent);
                                }
                            }
                        });
                    }else{
                        if (AccountManager.checkLogin(context)) {
                            Intent intent = new Intent(context, AddFieldActivity1.class);
                            context.startActivity(intent);
                        }
                    }
                } else {
                    showRemindDialog("请先开启网络","开网啊","好","不好", new MyDialogClickListener() {

                        @Override
                        public void onSureButtonClick() {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            context.startActivity(intent);
                        }
                        @Override
                        public void onCancelButtonClick() {

                        }
                    });
                }

                break;
            case R.id.left_arrow:
                //左侧箭头
                isLeft = true;
                isClickMove = false;
                showConfirmDialog();
                break;
            case R.id.right_arrow:
                //右侧箭头
                isLeft = false;
                isClickMove = false;
                showConfirmDialog();
                break;
        }
    }

    //是否是点击图片进行移动
    private boolean isClickMove = false;

    private void showConfirmDialog() {

        if (mSubStages == null || mSubStages.isEmpty()) {
            return;
        }
        if (!isClickMove) {
            if (!isLeft) {


                if (mCurrentPostion + 1 >= mSubStages.size()) {
                    Toast.makeText(this.getContext(), "sorry", Toast.LENGTH_SHORT).show();
                    return;
                }

//            mCurrentPostion += 1;

                mCurrentPostion = getCurrentPosition(itemWidth) + 1;
            } else {

                if (mCurrentPostion - 1 < 0) {
                    Toast.makeText(this.getContext(), "sorry", Toast.LENGTH_SHORT).show();
                    return;
                }

//            mCurrentPostion -= 1;
                mCurrentPostion = getCurrentPosition(itemWidth) - 1;
            }
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
//                    mCurrentPostion = mCurrentPostion+1;
                    mCurrentPostion = getCurrentPosition(itemWidth) + 1;
                }

                @Override
                public void onRightClick(Dialog dialog, String txt) {
                    if (null != onAddCropClickListener) {
                        confirmDialog.dismiss();
                        onAddCropClickListener.onArrowViewClick(mCurrentPostion);
                        updateStageIcon();

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

        if(mLeafParent !=null){


            mLeafContainer.removeAllViews();

            //根据当前的的状态进行点亮不同的图标，之前的是点亮，之后的是不点的

            Iterator<SubStage> iterator  = mSubStages.iterator();
            int count = 0;
            while(iterator.hasNext()){

                SubStage item = iterator.next();
                final LinearLayout layout = new LinearLayout(getContext());
                layout.setTag(count++);
                LayoutParams linLayoutParams = new LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
                //TODO:create imageView
                //TODO image = getIconBySubstageId(enity.key)
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                ImageView imageView = new ImageView(getContext());
                imageView.setBackgroundResource(getIconBySubstageId(item.subStageId));
                layout.addView(imageView, params);
                layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentCount = (int) layout.getTag();
                        if (mCurrentPostion == currentCount) {
                            return;
                        }
                       DEBUG("dding currentCount:" + currentCount);
                        mCurrentPostion = currentCount;
                        isClickMove = true;
                        showConfirmDialog();
//                        moveToDes();

                    }
                });
                mLeafContainer.addView(layout, linLayoutParams);

            }

            moveToDes();

        }

    }

    private void moveToDes() {
        DEBUG("dding当前mCurrentPostion前--：" + mCurrentPostion);
        if (mCurrentPostion > 2) {
            //移动到中间

            mLeafParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                   // mLeafParent.scrollTo(0, 0);
                    mLeafParent.smoothScrollTo(itemWidth * (mCurrentPostion - 2), 0);
                }
            });
            mLeafParent.post(new Runnable() {
                @Override
                public void run() {
                    DEBUG("post...");
                    DEBUG("post:" + itemWidth * (mCurrentPostion - 2));


                }
            });

        } else {

            mLeafParent.post(new Runnable() {
                @Override
                public void run() {
                    mLeafParent.smoothScrollTo(0, 0);
                }
            });

        }
    }

    private int getCurrentPosition(int itemWidth) {
        int tempScrollX = mLeafParent.getScrollX();
        int scrollX = tempScrollX + 2 * itemWidth;
        if (tempScrollX > 0) {
            if (scrollX > mCurrentPostion * itemWidth) {
                return mCurrentPostion - (mCurrentPostion * itemWidth - scrollX) / itemWidth;
            } else {
                return mCurrentPostion + (scrollX - mCurrentPostion * itemWidth) / itemWidth;
            }
        }

        return mCurrentPostion;
    }



    private int getIconBySubstageId(int subStageId){

        int mCurrentStageId = mSubStages.get(mCurrentPostion).subStageId;

        int resId;
        if(mCurrentStageId>=subStageId){
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
        if (BuildConfig.DEBUG) {
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




    private void showRemindDialog(String title, String content, String sureButton,String cancelButton, final MyDialogClickListener dialogClickListener){
        final Dialog dialog = new Dialog(getContext(), R.style.CommonDialog);
        dialog.setContentView(R.layout.confirm_gps_network_dialog);
        TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);

        System.out.println(tv_title+"tv _ title");

        tv_title.setText(title);
        tv.setTextSize(22);

        tv.setText(content);
        tv.setTextSize(18);

        Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        waitBtn.setText(cancelButton);
        waitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialogClickListener.onCancelButtonClick();
                dialog.dismiss();
            }
        });
        Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
        backBtn.setText(sureButton);
        backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialogClickListener.onSureButtonClick();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public interface MyDialogClickListener{
        public void onSureButtonClick();
        public void onCancelButtonClick();
    }

}
