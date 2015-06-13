package com.dasinong.app.components.home.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dasinong.app.R;
import com.dasinong.app.components.home.view.dialog.ConfirmDialog;

/**农作物成长状况View--用于首页上部绿色背景中部的显示
 * Created by lnn on 15/6/2.
 */
public class CropsGroupUpView extends LinearLayout implements View.OnClickListener{
    //收获时间，时候时间右侧的状态，添加作物view(当没有作物的时候显示),叶子后面的内容
    private TextView harvestTimeView, rightStateView, addCropView, leafContent;
    //正常view的父View,没有作物的parent
    private View normalParentView,addCropViewParent;
    //标记当前是否有作物
    private int hasCrop=0;//默认有作物
    //添加农作物
    private MyAddCropOnClickListener onAddCropClickListener;
    private ImageView leftArrowView, leafView, rightArrowView;
    private ConfirmDialog confirmDialog;
    private Activity activity;

    public CropsGroupUpView(Context context) {
        super(context);
        init(context);
    }

    public CropsGroupUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View rootView=LayoutInflater.from(context).inflate(R.layout.view_home_top_work_content,this);
        normalParentView=rootView.findViewById(R.id.normal_state);
        harvestTimeView= (TextView) rootView.findViewById(R.id.harvest_time);
        rightStateView=(TextView)rootView.findViewById(R.id.right_state);
        addCropViewParent=rootView.findViewById(R.id.add_crop_parent);
        addCropView=(TextView)rootView.findViewById(R.id.add_crop);
        leafContent = (TextView) rootView.findViewById(R.id.leaf_content);
        leftArrowView = (ImageView) rootView.findViewById(R.id.left_arrow);
        leafView = (ImageView) rootView.findViewById(R.id.leaf);
        rightArrowView = (ImageView) rootView.findViewById(R.id.right_arrow);
        addCropViewParent.setOnClickListener(this);
        leftArrowView.setOnClickListener(this);
        rightArrowView.setOnClickListener(this);
    }

    /**
     * 设置当前是否有作物-
     * @param state  0：有；其他：没有
     */
    public void setCurrentState(int state){
        this.hasCrop=state;
        if(0==hasCrop){
            normalParentView.setVisibility(View.VISIBLE);
            addCropViewParent.setVisibility(View.GONE);
        }else{
            normalParentView.setVisibility(View.GONE);
            addCropViewParent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置作物状态和收获时间
     * @param leftStateInfo   --左侧的收获时间
     * @param rightStateInfo  --右侧的成长状态
     */
    public void setCropStateInfo(String leftStateInfo,String rightStateInfo){
        if(TextUtils.isEmpty(leftStateInfo)){
            leftStateInfo="";
        }
        harvestTimeView.setText(leftStateInfo);
        if(TextUtils.isEmpty(rightStateInfo)){
            rightStateInfo="";
        }
        rightStateView.setText(rightStateInfo);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_crop_parent:
                // 添加作物
                if (null != onAddCropClickListener) {
                    onAddCropClickListener.onAddCropClick();
                }
                break;
            case R.id.left_arrow:
                //左侧箭头
                showConfirmDialog();
                break;
            case R.id.right_arrow:
                //右侧箭头
                showConfirmDialog();
                break;
        }
    }

    private void showConfirmDialog() {
        if (null == activity) {
            return;
        }
        if (null == confirmDialog) {
            confirmDialog = new ConfirmDialog(activity);
            confirmDialog.setTitle("确认切换状态");
            confirmDialog.setMessage("您真的确认要切换当前作物的状态吗");
            confirmDialog.setButtonClickListener(new ConfirmDialog.ButtonClickListener() {

                @Override
                public void onLeftClick(Dialog dialog, String txt) {
                    confirmDialog.dismiss();
                }

                @Override
                public void onRightClick(Dialog dialog, String txt) {
                    if (null != onAddCropClickListener) {
                        onAddCropClickListener.onRightArrowViewClick();
                    }
                }
            });
        }
        confirmDialog.show();

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setLeafViewAndLeafContent(int resId, String leafContentValue) {
        leftArrowView.setBackgroundResource(resId);
        if (!TextUtils.isEmpty(leafContentValue)) {
            leafContent.setText(leafContentValue);
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

    //添加农作物
    public interface MyAddCropOnClickListener {
        /**
         * 添加农作物--（没有作物的加号）
         */
        void onAddCropClick();

        /**
         * 叶子右侧的箭头的点击
         */
        void onRightArrowViewClick();
    }
}
