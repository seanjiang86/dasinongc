package com.dasinong.app.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dasinong.app.R;

/**农作物成长状况View--用于首页上部绿色背景中部的显示
 * Created by lnn on 15/6/2.
 */
public class CropsGroupUpView extends LinearLayout implements View.OnClickListener{
    //收获时间，时候时间右侧的状态，添加作物view(当没有作物的时候显示)
    private TextView harvestTimeView,rightStateView,addCropView;
    //正常view的父View,没有作物的parent
    private View normalParentView,addCropViewParent;
    //标记当前是否有作物
    private int hasCrop=0;//默认有作物
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
        addCropViewParent.setOnClickListener(this);
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
                //TODO 添加作物
                break;
        }

    }
}
