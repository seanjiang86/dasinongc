package com.dasinong.app.components.home.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.FieldEntity;
import com.dasinong.app.components.domain.TaskStatus;
import com.dasinong.app.components.domain.WeatherEntity;
import com.dasinong.app.components.home.view.popupwidow.CommSelectPopWindow;

import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.database.task.domain.TaskSpec;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.TaskDetailsActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.utils.DeviceHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

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
    private TextView mFieldNameView, dayView, weekView, wealthView, leftStateView, rightStateView;

    private Context context;


    private String[] weeks;
    // 农活内容点击事件
    private MyOnAddFieldClickListener onAddFieldClickListener;

    private List<SubStage> mSubStageLists;


    private SubStage mCurrentSubStage;


    /*田地id,name的map*/
    private Map<String, Long> mFieldMap = new HashMap<String, Long>();
    /*田地的集合*/
    private List<String> mFieldList = new ArrayList<String>();
    //目前的农田的名称--用于区别点击的是否是当前；
    private String mCurrentFieldName;


    private List<TaskStatus> mCurrentTaskSpec = new ArrayList<>();


    private SparseArray<List<TaskStatus>> mAllTasks = new SparseArray<>();

    private TaskSpecDaoImpl taskSpecDao;


    private CommSelectPopWindow popWindow;

    private static final String TAG = "CropsStateView";

    private static final String PREFIX = "field";


    private View mTaskTitle;

    private View mNoLogin;
    private View mImageAddField;


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
        mFieldNameView = (TextView) rootView.findViewById(R.id.field);
        mNoLogin = rootView.findViewById(R.id.no_login_text);
        mImageAddField = rootView.findViewById(R.id.iv_add_field);
        addFieldView = rootView.findViewById(R.id.add_field);
        fieldStateView = (CropsGroupUpView) rootView.findViewById(R.id.field_state);
        dayView = (TextView) rootView.findViewById(R.id.day);
        weekView = (TextView) rootView.findViewById(R.id.week);
        wealthView = (TextView) rootView.findViewById(R.id.wealth);
        leftStateView = (TextView) rootView.findViewById(R.id.left_state);
        rightStateView = (TextView) rootView.findViewById(R.id.right_state);
        campaignView = (LinearLayout) rootView.findViewById(R.id.campaign);
        mTaskTitle = findViewById(R.id.task_title);
        mFieldNameView.setOnClickListener(this);
        addFieldView.setOnClickListener(this);
        fieldStateView.setOnAddCropClickListener(new CropsGroupUpView.MyAddCropOnClickListener() {

            @Override
            public void onAddCropClick() {
                if (null != onAddFieldClickListener) {
                    onAddFieldClickListener.onAddCroupClick();
                }
            }

            @Override
            public void onArrowViewClick(int position) {
                mCurrentSubStage = mSubStageLists.get(position);
                mCurrentTaskSpec = getTaskBySubStageId();
                updateTask();

                if (onAddFieldClickListener != null) {
                    onAddFieldClickListener.onDialogClick(mCurrentSubStage.subStageId);
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
    public void updateView(FieldEntity entity, String defaultLocation) {
        initFieldName();
        initTask();
        if (null == entity) {

            return;
        }

        mFieldNameView.setText(defaultLocation);
        mFieldNameView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.gps), null);
        if (!AccountManager.isLogin(this.getContext())) {
            mNoLogin.setVisibility(View.VISIBLE);
            mImageAddField.setVisibility(View.GONE);
            fieldStateView.showNOLogin();
        } else {
            mNoLogin.setVisibility(View.GONE);
            mImageAddField.setVisibility(View.VISIBLE);
            if (null != entity.fieldList && !entity.fieldList.isEmpty()) {
                mFieldMap = entity.fieldList;
                updateFieldNameMenue();
                fieldStateView.showNormalStatus();
            } else {
                fieldStateView.showNOFieldStatus();
            }
        }


        //设置田地的名称
        if (null != entity.currentField) {
            SharedPreferencesHelper.setString(this.getContext(), "current_subStage_id", entity.currentField.currentStageID + "");

            FieldEntity.CurrentFieldEntity currentFieldEntity = entity.currentField;
            //DONE
            if (mFieldMap != null && mFieldMap.containsKey(currentFieldEntity.fieldName)) {
                mCurrentFieldName = currentFieldEntity.fieldName;
            }
            updateFieldName();
            //设置当前是否是个打药，适合下地干活


            updateFieldTimeAndStage(entity.currentField);
            //任务相关的
            convertTask(currentFieldEntity.taskws);

            if(mCurrentSubStage!=null) {
                mCurrentTaskSpec = getTaskBySubStageId();
            }else {
                mCurrentTaskSpec.clear();
            }

            if (null != entity.fieldList && !entity.fieldList.isEmpty()&&mCurrentSubStage!=null) {
                mTaskTitle.setVisibility(View.VISIBLE);
                updateTask();
            } else {
                campaignView.setOrientation(LinearLayout.VERTICAL);
                campaignView.removeAllViews();
                campaignView.setVisibility(View.GONE);
                mTaskTitle.setVisibility(View.GONE);

            }

        }


        //设置日期，星期几和天气
        setDatWeekAndWeatherView(entity.date);


    }

    //设置是否适合下地
    public void updateWorkStage(WeatherEntity entity) {
        if (entity != null) {
            setWorkState(entity.workable, entity.sprayable);
        }


    }

    private void initTask() {
        campaignView.setOrientation(LinearLayout.VERTICAL);
        campaignView.removeAllViews();
        mTaskTitle.setVisibility(View.GONE);
        campaignView.setVisibility(View.GONE);
    }

    private void initFieldName() {
        mFieldList.clear();
        mFieldNameView.setClickable(false);
    }

    private void updateFieldTimeAndStage(FieldEntity.CurrentFieldEntity currentFieldEntity) {
        if (currentFieldEntity == null) {
            return;
        }

        //DONE 状态
        mCurrentSubStage = getCurrentStage(currentFieldEntity.currentStageID);

        if (currentFieldEntity.currentStageID == 0 || currentFieldEntity.currentStageID == 10) {
            mCurrentSubStage = null;
        }


        mSubStageLists = getSubStages();
        int size = mSubStageLists.size();

        int currentPosition = -1;
        for (int i = 0; i < size; i++) {
            if (mSubStageLists.get(i) == null || mCurrentSubStage == null) {
                continue;
            }


            if (mSubStageLists.get(i).subStageId == mCurrentSubStage.subStageId) {
                currentPosition = i;
                break;
            }
        }


        fieldStateView.setPositionAndList(currentPosition, mSubStageLists);

    }


    private void updateFieldName() {
        if (!TextUtils.isEmpty(mCurrentFieldName)) {
            mFieldNameView.setText(mCurrentFieldName);

            if(mFieldMap!=null&&!mFieldMap.isEmpty()&&mFieldMap.get(mCurrentFieldName)!=null) {
                SharedPreferencesHelper.setLong(this.getContext(), SharedPreferencesHelper.Field.FIELDID, mFieldMap.get(mCurrentFieldName));
            }

        }


    }

    private void convertTask(List<FieldEntity.CurrentFieldEntity.TaskwsEntity> taskws) {

        if (mCurrentSubStage == null) {
            return;
        }
        List<TaskStatus> taskSpecs = mAllTasks.get(mCurrentSubStage.subStageId, null);
        if (taskSpecs != null) {
            return;
        }

        //read local
        taskSpecs = readTaskStatus();
        if (!taskSpecs.isEmpty()) {
            mAllTasks.put(mCurrentSubStage.subStageId, taskSpecs);
            return;
        }

        for (FieldEntity.CurrentFieldEntity.TaskwsEntity entity : taskws) {
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.subStageId = entity.subStageId;
            taskStatus.taskSpecId = entity.taskSpecId;
            taskStatus.taskSpecName = entity.taskSpecName;
            taskStatus.isCheck = entity.taskStatus;
            taskSpecs = mAllTasks.get(entity.subStageId);
            if (taskSpecs == null) {
                taskSpecs = new ArrayList<>();
            }
            taskSpecs.add(taskStatus);
            mAllTasks.delete(entity.subStageId);
            mAllTasks.put(entity.subStageId, taskSpecs);

        }
        //save all  task
        if (mAllTasks != null) {
            for (int i = 0; i < mAllTasks.size(); i++) {
                List<TaskStatus> statusList = mAllTasks.get(i);
                if (statusList != null) {
                    saveTaskStatus(statusList);
                }
            }
        }


    }

    /**
     * getTaskBySubStageId
     *
     * @return
     */
    private List<TaskStatus> getTaskBySubStageId() {

//        if (mCurrentSubStage == null) {
//            return new ArrayList<>();
//
//        }
//
//        if (mAllTasks.get(mCurrentSubStage.subStageId, null) != null) {
//            return mAllTasks.get(mCurrentSubStage.subStageId);
//        }


        //read local
        List<TaskStatus> status = readTaskStatus();
        if (!status.isEmpty()) {
            mAllTasks.put(mCurrentSubStage.subStageId, status);
            return status;
        }


        if (taskSpecDao == null) {
            taskSpecDao = new TaskSpecDaoImpl(this.getContext());
        }

        List<TaskSpec> list = taskSpecDao.queryTaskSpecWithSubStage(mCurrentSubStage.subStageId);
        int size = list.size();


        for (int i = 0; i < size; i++) {
            TaskSpec spec = list.get(i);
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.taskSpecName = spec.taskSpecName;
            taskStatus.subStageId = spec.subStage;
            taskStatus.taskSpecId = spec.taskSpecId;
            status.add(taskStatus);
        }

        mAllTasks.put(mCurrentSubStage.subStageId, status);
        return mAllTasks.get(mCurrentSubStage.subStageId);
    }


    //给日期，星期和天气设置相对应的值
    private void setDatWeekAndWeatherView(FieldEntity.HomeDate date) {
        if (date == null) {
            return;
        }

        int index = date.day % 7;

        setText2TextView(dayView, date.date);
        setText2TextView(weekView, weeks[index]);
        setText2TextView(wealthView, date.lunar);
    }


    /**
     * 设置是否适宜工作的状态
     *
     * @param isWork  --是否适合下地;true:是；false:否
     * @param isSpray --是否适合打药;true:是；false:否
     */
    private void setWorkState(int isWork, int isSpray) {
        //值我改成了1，0，-1是宜，0是不宜，-1是不显示
        leftStateView.setVisibility(View.VISIBLE);
        rightStateView.setVisibility(View.VISIBLE);
        if (isWork == 1) {
            leftStateView.setText("宜下地");
        } else if (isWork == 0) {
            leftStateView.setText("不宜下地");
        } else {
            leftStateView.setVisibility(View.GONE);
        }
        if (isSpray == 1) {
            rightStateView.setText("宜打药");

        } else if (isSpray == 0) {
            rightStateView.setText("不宜打药");
        } else {
            rightStateView.setVisibility(View.GONE);
        }
    }


    //设置田地的数据集合,记住当前数据集合--给fieldList赋值
    private void updateFieldNameMenue() {

        if (mFieldMap == null || mFieldMap.isEmpty()) {
            return;
        }

        mFieldNameView.setClickable(true);
        mFieldList.clear();
        //有田地
        for (Map.Entry<String, Long> entrySet : mFieldMap.entrySet()) {
            String key = entrySet.getKey();
            if (!TextUtils.isEmpty(key)) {
                mFieldList.add(key);
            }

        }

        if (mFieldList.size() > 1) {
            //当有2个或者两个以上的农田时，才显示下拉的箭头图标
            mFieldNameView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
            mFieldNameView.setPadding(0, 0, 20, 0);
        }
        mFieldNameView.setText(mFieldList.get(0));


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
                if (null == mFieldList || mFieldList.size() <= 1) {
                    return;
                }

                initPoPuWindow();
                popWindow.showAsDropDown(mFieldNameView);

                break;
            case R.id.add_field:
                //点击添加--最右上角按钮

                if (DeviceHelper.checkNetWork(context)) {

                    if (!DeviceHelper.checkGPS(context) && AccountManager.isLogin(context) ) {
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
                    //TODO Ming 待确定文字
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

                break;
            default:
                break;
        }
    }

    private void initPoPuWindow() {
        if (popWindow != null) {
            return;
        }
        popWindow = new CommSelectPopWindow(context);
        popWindow.setDatas(mFieldList);
        popWindow.setPopWidth(mFieldNameView.getMeasuredWidth());
        popWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {
            @Override
            public void itemSelected(CommSelectPopWindow window, int position, CharSequence fieldName) {

                if (fieldName.toString().equalsIgnoreCase(mCurrentFieldName)) {
                    //点击的位置和当前显示的一样--不请求网络
                    return;
                }
                mCurrentFieldName = fieldName.toString();

                updateFieldName();
                popWindow.disMiss();
                if (null != onAddFieldClickListener) {
                    onAddFieldClickListener.onPopWindowItemClick(mFieldMap.get(mCurrentFieldName));
                }

            }
        });
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
         * popwindow的item点击事件
         *
         * @param filedId
         */
        void onPopWindowItemClick(Long filedId);

        /**
         * 更新disaster
         */

        void onDialogClick(int substage);

    }


    /**
     * 得到当前的subStage
     *
     * @param currentStageID
     * @return
     */
    private SubStage getCurrentStage(int currentStageID) {
        SubStageDaoImpl dao = new SubStageDaoImpl(this.getContext());
        return dao.queryStageByID(String.valueOf(currentStageID));

    }


    private List<SubStage> getSubStages() {

        SubStageDaoImpl dao = new SubStageDaoImpl(this.getContext());
        return dao.queryAllOderBy();

    }


    /**
     * 更新任务
     */
    private void updateTask() {

        if (campaignView.getVisibility() == View.GONE) {
            campaignView.setVisibility(View.VISIBLE);
        }
        campaignView.setOrientation(LinearLayout.VERTICAL);
        campaignView.removeAllViews();

        if (mCurrentTaskSpec == null || mCurrentTaskSpec.isEmpty()) {
            mTaskTitle.setVisibility(View.GONE);
            campaignView.setVisibility(View.GONE);
            campaignView.setOrientation(LinearLayout.VERTICAL);
            campaignView.removeAllViews();
            campaignView.setVisibility(View.GONE);
            mTaskTitle.setVisibility(View.GONE);
            return;
        }

        int length = mCurrentTaskSpec.size();

        for (int i = 0; i < length; i++) {
            final TaskStatus item = mCurrentTaskSpec.get(i);
            final View view = LayoutInflater.from(context).inflate(R.layout.view_home_work_content, null);
            TextView contentView = (TextView) view.findViewById(R.id.work_content);
            View lineView = view.findViewById(R.id.line);
            final LinearLayout checkedView = (LinearLayout) view.findViewById(R.id.iv_check);
            contentView.setText(item.taskSpecName);
            View rightView = view.findViewById(R.id.right_view);

            checkedView.setSelected(item.isCheck);
            rightView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //done
                    Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
                    intent.putExtra(TaskDetailsActivity.TASK_ID, item.taskSpecId);
                    intent.putExtra(TaskDetailsActivity.TASK_TITLE, item.taskSpecName);
                    getContext().startActivity(intent);


                }
            });
            if (i == length - 1) {
                lineView.setVisibility(View.GONE);
            }

            view.setTag(item);
            checkedView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedView.setSelected(!checkedView.isSelected());
                    TaskStatus item = (TaskStatus) view.getTag();
                    item.isCheck = checkedView.isSelected();
                    int childCount = campaignView.getChildCount();

                    List<TaskStatus> lists = new ArrayList<TaskStatus>();
                    for (int i = 0; i < childCount; i++) {
                        View childView = campaignView.getChildAt(i);
                        lists.add((TaskStatus) childView.getTag());
                    }

                    saveTaskStatus(lists);

                }
            });

            campaignView.addView(view);
            mTaskTitle.setVisibility(View.VISIBLE);


        }

    }


    private List<TaskStatus> readTaskStatus() {

        Gson gson = new Gson();

        String result = null;
        if(mFieldMap!=null&&mFieldMap.get(mCurrentFieldName)!=null&&mCurrentSubStage!=null) {
                String key = getSaveKey();
             result = SharedPreferencesHelper.getString(getContext(), key, null);
        }

        List<TaskStatus> lists = new ArrayList<TaskStatus>();
        if (result != null) {
            lists.clear();
            lists = gson.fromJson(result, new TypeToken<List<TaskStatus>>() {
            }.getType());
        }

        return lists;
    }


    private void saveTaskStatus(List<TaskStatus> lists) {


        if (lists.isEmpty()) {
            return;
        }
        String key = getSaveKey();
        Gson gson = new Gson();
        mAllTasks.put(mCurrentSubStage.subStageId,lists);
        SharedPreferencesHelper.setString(this.getContext(), key, gson.toJson(lists));


    }


    private String getSaveKey() {
        return PREFIX + mFieldMap.get(mCurrentFieldName) + mCurrentSubStage.subStageId;
    }


    private void DEBUG(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    private void showRemindDialog(String title, String content, String sureButton, String cancelButton, final MyDialogClickListener dialogClickListener) {
        final Dialog dialog = new Dialog(context, R.style.CommonDialog);
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
        void onSureButtonClick();

        void onCancelButtonClick();
    }

}
