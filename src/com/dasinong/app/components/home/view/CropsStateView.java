package com.dasinong.app.components.home.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.components.domain.FieldEntity;
import com.dasinong.app.components.domain.TaskStatus;
import com.dasinong.app.components.home.view.popupwidow.CommSelectPopWindow;

import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 首页--上部绿色背景View
 * Created by lnn on 15/6/2.
 */
public class CropsStateView extends LinearLayout implements View.OnClickListener {

    private LinearLayout campaignView;//添加农作物活动的View
    private CropsGroupUpView fieldStateView;//成长状态
    private View addFieldView;
    //田地名称，添加，日期，星期几，天气，左边状态，右边状态
    private TextView fieldView, dayView, weekView, wealthView, leftStateView, rightStateView;
    //田地名称是否可以点击--其判断应该是根据接口传值--默认可以点击
    private boolean fieldNameCanClick = true;
    //目前的农田的名称--用于区别点击的是否是当前；
    private String currentFieldValue;
    private Context context;
    //田地的集合
    private List<String> fieldList;

    private String[] weeks;
    // 农活内容点击事件
    private MyOnAddFieldClickListener onAddFieldClickListener;

    private List<SubStage> mSubStageLists;
    private SubStage mCurrentSubStage;
    private int mPosition;
    private String rightStateInfo;


    public CropsStateView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public CropsStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        weeks = getResources().getStringArray(R.array.weeks);
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_top, this);
        fieldView = (TextView) rootView.findViewById(R.id.field);
        addFieldView = rootView.findViewById(R.id.add_field);
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

            @Override
            public void onRightArrowViewClick() {

                if (null != onAddFieldClickListener) {
                    onAddFieldClickListener.onLeafViewConfirmClick();
                }
            }
        });

    }

    /**
     * 添加田地，农作物，活动内容等监听事件
     *
     * @param onAddFieldClickListener --注册的监听器
     */
    public void setOnAddFieldClickListener(MyOnAddFieldClickListener onAddFieldClickListener) {
        this.onAddFieldClickListener = onAddFieldClickListener;
    }

    /**
     * 更新view---给view设置数据
     *
     * @param entity
     */
    public void updateView(FieldEntity entity) {

        if (null == entity)
            return;

        //设置田地的名称
        if (null != entity.currentField) {
            //DONE
            String fieldName = entity.currentField.fieldName;
            if (!TextUtils.isEmpty(fieldName)) {
                this.currentFieldValue = fieldName;
                setText2TextView(fieldView, currentFieldValue);
            }
            //设置作物的状态和当前的收获时间--横向滑动的叶子的下面view
            //DONE
            String harvestDay = getHarvestDay(entity);

            FieldEntity.CurrentFieldEntity fieldEntity = entity.currentField;

            String rightStateInfo = "水稻";
            if (fieldEntity != null) {
                mCurrentSubStage = getCurrentStage(fieldEntity.currentStageID);
                mSubStageLists = getSubStages();
                int size = mSubStageLists.size();
                for (int i = 0; i < size; i++) {
                    if (mSubStageLists.get(i).subStageId == mCurrentSubStage.subStageId) {
                        mPosition = i;
                        break;
                    }
                }


                fieldStateView.setPostionAndList(mPosition, mSubStageLists);


                rightStateInfo = rightStateInfo + mCurrentSubStage.stageName + mCurrentSubStage.subStageName;
            }


            setCropStateInfo(harvestDay, rightStateInfo);
        }

        if (null != entity.fieldList && !entity.fieldList.isEmpty()) {

            Map<String, Long> maps = entity.fieldList;
            if (maps.size() != 0) {
                //有田地
                List<String> list = new ArrayList<String>();
                for (Map.Entry<String, Long> entrySet : maps.entrySet()) {
                    String key = entrySet.getKey();
                    if (!TextUtils.isEmpty(key)) {
                        list.add(key);
                    }
                }

                //记住田地数据集合的内容--用以点击展开popouwindow
                setFieldList(list);
            }
        } else {

            //当前没有田地--
            setCurrentState(1);
        }
        //设置日期，星期几和天气
        //DONE
        setDatWeekAndWeatherView(entity);

        //设置当前是否是个打药，适合下地干活
        //DONE
        setWorkState(entity.currentField.workable, entity.currentField.sprayable);


        //设置任务的内容


        setWorkContent(entity.currentField);

    }

    private String getHarvestDay(FieldEntity entity) {
        String harvestDay = "";
        if (entity.currentField.daytoharvest > 0) {

            harvestDay = "离收获还有" + entity.currentField.daytoharvest + "天";
        }

        return harvestDay;
    }

    private void updateLeftHarvestDay(String harvestDay) {


    }


    /**
     * 设置农活任务(活动任务)的显示实体
     *
     * @param entity --农活实体model
     */
    public void setWorkContent(FieldEntity entity) {
        if (null == entity || null == entity.currentField) {
            return;
        }
        setWorkContent(entity.currentField);
    }


    //给日期，星期和天气设置相对应的值
    private void setDatWeekAndWeatherView(FieldEntity innerEntity) {
        if (null == innerEntity) return;
        FieldEntity.HomeDate date = innerEntity.date;
        int index = date.day % 7;
        seInfo2TextView(date.date, weeks[index], date.lunar);
    }


    /**
     * 设置textView对应显示的值
     *
     * @param dayNum  --日期(天)
     * @param weekDay --日期(星期几)
     * @param weather --天气
     */
    private void seInfo2TextView(String dayNum, String weekDay, String weather) {
        setText2TextView(dayView, dayNum);
        setText2TextView(weekView, weekDay);
        setText2TextView(wealthView, weather);
    }

    /**
     * 设置当前是否有作物-
     *
     * @param state 0：有；其他：没有
     */
    private void setCurrentState(int state) {
        fieldStateView.setCurrentState(state);
    }

    /**
     * 设置是否适宜工作的状态
     *
     * @param isWork  --是否适合下地;true:是；false:否
     * @param isSpray --是否适合打药;true:是；false:否
     */
    private void setWorkState(boolean isWork, boolean isSpray) {
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
    private void setCropStateInfo(String leftStateInfo, String rightStateInfo) {
        fieldStateView.setCropStateInfo(leftStateInfo, rightStateInfo);
    }

    /**
     * 设置农活内容
     *
     * @param innerEntity --农活的内容
     */
    private void setWorkContent(FieldEntity.CurrentFieldEntity innerEntity) {
        if (null == innerEntity) return;

        if (null == innerEntity.taskws || innerEntity.taskws.size() == 0) return;
        List<FieldEntity.CurrentFieldEntity.TaskwsEntity> tasks = innerEntity.taskws;
        campaignView.setOrientation(LinearLayout.VERTICAL);
        campaignView.removeAllViews();
        for (int i = 0; i < tasks.size(); i++) {
            final int tempPos = i;
            View view = LayoutInflater.from(context).inflate(R.layout.view_home_work_content, null);

            TextView contentView = (TextView) view.findViewById(R.id.work_content);
            View lineView = view.findViewById(R.id.line);
            final LinearLayout checkedView = (LinearLayout) view.findViewById(R.id.iv_check);
            final FieldEntity.CurrentFieldEntity.TaskwsEntity entity = tasks.get(i);
            final String value = entity.taskSpecName;
            contentView.setText(value);
            View rightView = view.findViewById(R.id.right_view);

            Gson gson = new Gson();
            String key = "task_" + entity.fieldId + entity.stageName;
            String result = SharedPreferencesHelper.getString(getContext(), key, null);
            List<TaskStatus> lists = new ArrayList<>();
            if (result != null) {
                lists.clear();
                lists = gson.fromJson(result, new TypeToken<List<TaskStatus>>() {
                }.getType());
            }
            if(!lists.isEmpty()){
                Iterator<TaskStatus> iterator = lists.iterator();
                while (iterator.hasNext()) {
                    TaskStatus next = iterator.next();

                    checkedView.setSelected(next.isCheck);




                }
            }

            rightView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onAddFieldClickListener) {
                        onAddFieldClickListener.onWorKContentItemClick(value, tempPos, checkedView.isSelected());
                    }
                }
            });
            if (i == tasks.size() - 1) {
                lineView.setVisibility(View.GONE);
            }

            checkedView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkedView.setSelected(!checkedView.isSelected());
                    saveTaskStatus(checkedView.isSelected(), entity);
                }
            });

            campaignView.addView(view);

        }

    }

    private void saveTaskStatus(boolean selected, FieldEntity.CurrentFieldEntity.TaskwsEntity entity) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.subStageId = entity.subStageId;
        taskStatus.isCheck = selected;

        Gson gson = new Gson();
        String key = "task_" + entity.fieldId + entity.stageName;
        String result = SharedPreferencesHelper.getString(getContext(), key, null);
        List<TaskStatus> lists = new ArrayList<>();
        if (result != null) {
            lists.clear();
            lists = gson.fromJson(result, new TypeToken<List<TaskStatus>>() {
            }.getType());
        }

        if (lists.contains(taskStatus)) {
            Iterator<TaskStatus> iterator = lists.iterator();
            while (iterator.hasNext()) {
                TaskStatus next = iterator.next();
                if (next.subStageId == taskStatus.subStageId) {
                    iterator.remove();
                    break;
                }


            }
        }

        lists.add(taskStatus);


        SharedPreferencesHelper.setString(this.getContext(), key, gson.toJson(lists));
    }

    //设置田地的数据集合,记住当前数据集合--给fieldList赋值
    private void setFieldList(List<String> list) {
        if (null == list || list.size() == 0) {
            fieldNameCanClick = false;
            return;
        }
        this.fieldList = list;
        this.fieldNameCanClick = true;
        if (fieldList.size() > 1) {
            //当有2个或者两个以上的农田时，才显示下拉的箭头图标
            fieldView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
            fieldView.setPadding(0, 0, 20, 0);
        }
        fieldView.setText(list.get(0));


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
        switch (v.getId()) {
            case R.id.field:
                //点击田地
                if (!fieldNameCanClick) {
                    return;
                }
                if (null == fieldList || fieldList.size() <= 1) {
                    return;
                }

                //TODO 弹出window,回显，刷新界面
                final CommSelectPopWindow popWindow = new CommSelectPopWindow(context);
                popWindow.setDatas(fieldList);
                popWindow.setPopWidth(fieldView.getMeasuredWidth());
                popWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {
                    @Override
                    public void itemSelected(CommSelectPopWindow window, int position, CharSequence tag) {
                        Log.d("dding", "当前选择的pos--:tag--:" + position + "," + tag);
                        if (tag.toString().equalsIgnoreCase(currentFieldValue)) {
                            //点击的位置和当前显示的一样--不请求网络
                            return;
                        }
                        fieldView.setText(tag);
                        popWindow.disMiss();
                        if (null != onAddFieldClickListener) {
                            onAddFieldClickListener.onPopWindowItemClick();
                        }

                    }
                });
                popWindow.showAsDropDown(fieldView);

                break;
            case R.id.add_field:
                //点击添加--最右上角按钮
                if (null != onAddFieldClickListener) {
                    onAddFieldClickListener.onRightTopViewClick();
                }

                //TODO MING:测试添加田地 正式上线需要修改
                Intent intent = new Intent(getContext(), AddFieldActivity1.class);
                getContext().startActivity(intent);

                break;
        }
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

        /**
         * 点击叶子view旁边左右箭头的弹出dialog的确认按钮监听
         */
        void onLeafViewConfirmClick();

        /**
         * popwindow的item点击事件
         */
        void onPopWindowItemClick();

    }


    private SubStage getCurrentStage(int currentStageID) {

        //
        SubStageDaoImpl dao = new SubStageDaoImpl(this.getContext());
        return dao.queryStageByID(String.valueOf(currentStageID));

    }


    private List<SubStage> getSubStages() {

        SubStageDaoImpl dao = new SubStageDaoImpl(this.getContext());
        return dao.queryStageSubCategory(mCurrentSubStage.stageName);

    }
}
