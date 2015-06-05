package com.dasinong.app.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dasinong.app.R;
import java.util.List;

/**
 * 首页--上部绿色背景View
 * Created by lnn on 15/6/2.
 */
public class CropsStateView extends LinearLayout implements View.OnClickListener {
    private View rootView;
    private LinearLayout campaignView;//添加农作物活动的View
    private CropsGroupUpView fieldStateView;//成长状态
    private ImageView addFieldView;
    //田地名称，添加，日期，星期几，天气，左边状态，右边状态
    private TextView fieldView,dayView, weekView, wealthView, leftStateView, rightStateView;
    //田地名称是否可以点击--其判断应该是根据接口传值
    private boolean fieldNameCanClick;
    //目前的农田的名称--用于区别点击的是否是当前；
    private String currentFieldValue;
    private Context context;
    public CropsStateView(Context context) {
        super(context);
        this.context=context;
        init(context);
    }

    public CropsStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_top, this);
        fieldView = (TextView) rootView.findViewById(R.id.field);
        addFieldView = (ImageView) rootView.findViewById(R.id.add_field);
        fieldStateView = (CropsGroupUpView) rootView.findViewById(R.id.field_state);
        dayView = (TextView) rootView.findViewById(R.id.day);
        weekView = (TextView) rootView.findViewById(R.id.week);
        wealthView = (TextView) rootView.findViewById(R.id.wealth);
        leftStateView = (TextView) rootView.findViewById(R.id.left_state);
        rightStateView = (TextView) rootView.findViewById(R.id.right_state);
        campaignView = (LinearLayout) rootView.findViewById(R.id.campaign);
        fieldView.setOnClickListener(this);
        addFieldView.setOnClickListener(this);
    }

    /**
     * 设置textView对应显示的值
     *
     * @param fieldName --田地名称
     * @param dayNum    --日期(天)
     * @param weekDay   --日期(星期几)
     * @param weather   --天气
     */
    public void setTextInfo2View(String fieldName, String dayNum, String weekDay, String weather) {
        this.currentFieldValue=fieldName;
        setText2TextView(fieldView, fieldName);
        setText2TextView(dayView, dayNum);
        setText2TextView(weekView, weekDay);
        setText2TextView(wealthView, weather);
    }

    /**
     * 设置是否适宜工作的状态
     *
     * @param isWork  --是否适合下地
     * @param isSpray --是否适合打药
     */
    public void setWorkState(boolean isWork, boolean isSpray) {
        if (isWork) {
            leftStateView.setText("宜下地");
        } else {
            leftStateView.setText("不宜下地");
        }
        if (isSpray) {
            rightStateView.setText("宜打药");

        } else {
            rightStateView.setText("不宜打药");
        }
    }

    /**
     * 设置农活内容
     *
     * @param contents    --农活的内容
     * @param isFirstLoad --是否第一次加载
     */
    public void setWorkContent(List<String> contents, boolean isFirstLoad) {
        //TODO
        if(null==contents||contents.size()==0)return;
        campaignView.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<contents.size();i++){
            View view=LayoutInflater.from(context).inflate(R.layout.view_home_work_content, null);
            TextView contentView= (TextView) view.findViewById(R.id.work_content);
            View lineView=view.findViewById(R.id.line);
            final LinearLayout checkedView= (LinearLayout) view.findViewById(R.id.iv_check);
            contentView.setText(contents.get(i));
            if(i==contents.size()-1){
                lineView.setVisibility(View.GONE);
            }
            checkedView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkedView.setSelected(!checkedView.isSelected());
                }
            });
            campaignView.addView(view);
        }

    }

    //判断value是否为null或空
    private boolean isEmptyValue(String value) {
        return !TextUtils.isEmpty(value);
    }

    //给textView设置显示的值(textValue)
    private void setText2TextView(TextView textView, String textValue) {
        if (isEmptyValue(textValue) && null != textView) {
            textView.setText(textValue);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.field:
                //点击田地
                if(!fieldNameCanClick){
                    return;
                }

                //弹出window,回显，刷新界面

                break;
            case R.id.add_field:
                //点击添加--最右上角按钮

                break;
        }
    }
}
