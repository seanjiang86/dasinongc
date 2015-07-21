package com.dasinong.app.components.home.view;


import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.home.view.dialog.SubStageDialog;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.utils.DeviceHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.TreeMap;

/**
 * 农作物成长状况View--用于首页上部绿色背景中部的显示
 * Created by lnn on 15/6/2.
 */
public class CropsGroupUpView extends LinearLayout implements View.OnClickListener {
    //收获时间，时候时间右侧的状态，添加作物view(当没有作物的时候显示),叶子后面的内容

    //正常view的父View,没有作物的parent
    private View normalParentView, addCropViewParent;

    //添加农作物
    private MyAddCropOnClickListener onAddCropClickListener;

    private SubStageDialog confirmDialog;
    private int mCurrentPostion;
    private List<SubStage> mSubStages;

    private TextView mSubStageName;


    private static TreeMap<Integer, Integer> cropIconResource = new TreeMap<Integer, Integer>();


    private static TreeMap<Integer, Integer> cropNoIconResource = new TreeMap<Integer, Integer>();



    private static final String TAG = "CropsGroupUpView";


    private View mNoLoginView;

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

        addCropViewParent = rootView.findViewById(R.id.add_crop_parent);

        mSubStageName = (TextView) findViewById(R.id.substage_name_text);


        mNoLoginView = rootView.findViewById(R.id.no_login_container);
        mNoLoginView.findViewById(R.id.no_login_container_content).setOnClickListener(this);


    }

    private void setEvent() {
        addCropViewParent.setOnClickListener(this);

    }


    public void showNormalStatus() {
        normalParentView.setVisibility(View.VISIBLE);
        mNoLoginView.setVisibility(View.GONE);
        addCropViewParent.setVisibility(View.GONE);
    }

    public void showNOFieldStatus() {
        normalParentView.setVisibility(View.GONE);
        mNoLoginView.setVisibility(View.GONE);
        addCropViewParent.setVisibility(View.VISIBLE);
    }

    public void showNOLogin() {
        normalParentView.setVisibility(View.GONE);
        mNoLoginView.setVisibility(View.VISIBLE);
        addCropViewParent.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_crop_parent:
                // 添加作物
                final Context context = getContext();
                // TODO: 15/7/7 add crop

                // 友盟自定义事件统计
                MobclickAgent.onEvent(context, "BigButtonAddField");

                startAddFieldActivity(context);

                break;

            case R.id.no_login_container_content:
                //no login
                startAddFieldActivity(this.getContext());
                break;

            case R.id.substage_name_text:
                showDialog();
                break;
            default:
                break;
        }
    }

    private void startAddFieldActivity(final Context context) {

        if (DeviceHelper.checkNetWork(context)) {
            if (!DeviceHelper.checkGPS(context)) {
                showRemindDialog("无法获取当前位置", "请前往“设置”打开GPS卫星，设置完成后点”返回“键就可以回到今日农事", "前往设置", "暂不开启", new MyDialogClickListener() {
                    @Override
                    public void onSureButtonClick() {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelButtonClick() {
                        if (AccountManager.checkLogin(context)) {
                            Intent intent = new Intent(context, AddFieldActivity1.class);
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                if (AccountManager.checkLogin(context)) {
                    Intent intent = new Intent(context, AddFieldActivity1.class);
                    context.startActivity(intent);
                }
            }
        } else {
            showRemindDialog("呀！网络断了...", "请检查你的手机是否联网，如果只是信号不好，也许等等就好啦", "前往设置", "取消", new MyDialogClickListener() {

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
    }


    private void showDialog() {

        if (mSubStages == null || mSubStages.isEmpty()) {
            return;
        }


        initDialog();
        if (confirmDialog.isShowing()) {
            return;
        }
        confirmDialog.setDataSource(mSubStages, mCurrentPostion);
        confirmDialog.show();


    }

    private void initDialog() {
        if (null == confirmDialog) {
            confirmDialog = new SubStageDialog(getContext());
            confirmDialog.setOnItemClickListener(new SubStageDialog.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    if (mCurrentPostion == position) {
                        return;
                    }
                    mCurrentPostion = position;
                    mSubStageName.setText("水稻" + mSubStages.get(position).subStageName);
                    normalParentView.setVisibility(VISIBLE);
                    onAddCropClickListener.onArrowViewClick(position);


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

    public void setPositionAndList(int mPosition, List<SubStage> mSubStageLists) {

        this.mCurrentPostion = mPosition;
        this.mSubStages = mSubStageLists;
        mSubStageName.setText("");
        if (mSubStageLists == null || mSubStageLists.isEmpty()) {
            return;
        }
        if (mPosition < 0 || mSubStageLists.get(mPosition).subStageId == 10) {

            normalParentView.setVisibility(View.GONE);


        } else {
            mSubStageName.setText("水稻" + mSubStageLists.get(mPosition).subStageName);
            mSubStageName.setOnClickListener(this);
            normalParentView.setVisibility(VISIBLE);

        }

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

    private void showRemindDialog(String title, String content, String sureButton, String cancelButton, final MyDialogClickListener dialogClickListener) {
        final Dialog dialog = new Dialog(getContext(), R.style.CommonDialog);
        dialog.setContentView(R.layout.confirm_gps_network_dialog);
        TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);



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

    public interface MyDialogClickListener {
        public void onSureButtonClick();

        public void onCancelButtonClick();
    }

}
