package com.dasinong.app.components.home.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dasinong.app.R;

import java.util.ArrayList;
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
    //田地的集合
    private List<String> fieldList;
    // 农活内容点击事件
    private MyOnAddFieldClickListener onAddFieldClickListener;
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
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_top,this);
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
        fieldStateView.setOnAddCropClickListener(new CropsGroupUpView.MyAddCropOnClickListener() {

            @Override
            public void onAddCropClick() {
                if (null != onAddFieldClickListener) {
                    onAddFieldClickListener.onAddCroupClick();
                }
            }
        });
        List<String
> list=new ArrayList<String>();
        for(int i=0;i<5;i++){
            list.add("水分很好");
        }
        setWorkContent(list);
    }

    /**
     * 设置textView对应显示的值
     *
     * @param dayNum    --日期(天)
     * @param weekDay   --日期(星期几)
     * @param weather   --天气
     */
    public void seInfo2TextView(String dayNum, String weekDay, String weather) {

        setText2TextView(dayView, dayNum);
        setText2TextView(weekView, weekDay);
        setText2TextView(wealthView, weather);
    }

    /**
     * 设置当前是否有作物-
     *
     * @param state 0：有；其他：没有
     */
    public void setCurrentState(int state) {
        fieldStateView.setCurrentState(state);
    }

    /**
     * 设置是否适宜工作的状态
     *
     * @param isWork  --是否适合下地;true:是；false:否
     * @param isSpray --是否适合打药;true:是；false:否
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
     * 设置作物状态和收获时间
     *
     * @param leftStateInfo  --左侧的收获时间
     * @param rightStateInfo --右侧的成长状态
     */
    public void setCropStateInfo(String leftStateInfo, String rightStateInfo) {
        fieldStateView.setCropStateInfo(leftStateInfo, rightStateInfo);
    }

    /**
     * 设置农活内容
     *
     * @param contents    --农活的内容
     */
    public void setWorkContent(List<String> contents) {
        //TODO
        if(null==contents||contents.size()==0)return;
        campaignView.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<contents.size(); i++) {
            final int tempPos=i;
            View view=LayoutInflater.from(context).inflate(R.layout.view_home_work_content, null);
            TextView contentView= (TextView) view.findViewById(R.id.work_content);
            View lineView=view.findViewById(R.id.line);
            final LinearLayout checkedView= (LinearLayout) view.findViewById(R.id.iv_check);
            final String value = contents.get(i);
            contentView.setText(value);
            View rightView = view.findViewById(R.id.right_view);
            rightView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onAddFieldClickListener) {
                        onAddFieldClickListener.onWorKContentItemClick(value, tempPos, checkedView.isSelected());
                    }
                }
            });
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

    /**
     * 活动内容的点击监听事件
     *
     * @param onAddFieldClickListener --注册的监听器
     */
    public void setOnAddFieldClickListener(MyOnAddFieldClickListener onAddFieldClickListener) {
        this.onAddFieldClickListener = onAddFieldClickListener;
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

    //设置田地的数据集合,并将第一条设置给田地
    private void setFieldList(List<String> list) {
        if (null == list || list.size() == 0) {
            fieldNameCanClick = false;
            return;
        }
        this.fieldList = list;
        this.currentFieldValue = list.get(0);
        setText2TextView(fieldView, currentFieldValue);
        fieldView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
        fieldView.setPadding(0,0,20,0);
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
                if (null != onAddFieldClickListener) {
                    onAddFieldClickListener.onRightTopViewClick();
                }
                break;
        }
    }

    public void updateView() {
        //TODO 此方法将会是终极版本，调用现在一切public方法
    }


    public interface MyOnAddFieldClickListener {
        /**
         * @param itemValue --当前条目的值(显示的字符串)
         * @param pos       --(点击的位置)
         * @param isSelect  --当前条目是否处于选中状态：true:选中；false:未选中
         */
        void onWorKContentItemClick(String itemValue, int pos, boolean isSelect);

        /**
         * 添加农作物
         */
        void onAddCroupClick();

        /**
         * 右上角view("+")点击事件
         */
        void onRightTopViewClick();

    }
}
