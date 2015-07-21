package com.dasinong.app.components.home.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.soil.adapter.CommonAdapter;
import com.dasinong.app.ui.soil.adapter.ViewHolder.ViewHolder;
import com.dasinong.app.ui.soil.domain.DataEntity;

import java.util.List;


/**
 * @author
 */
public class SubStageDialog extends BaseDialog implements AdapterView.OnItemClickListener {

    private ViewGroup contentView;

    private ListView mListView;

    private List<SubStage> mSubStageList;


    public SubStageDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View onAddConentView() {
        contentView = (ViewGroup) inflateView(R.layout.dialog_substage);
        return contentView;
    }

    @Override
    protected void initView() {
        mListView = (ListView)contentView.findViewById(R.id.substage_listview);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {


    }

    public void setDataSource(List<SubStage> subStages){
        this.mSubStageList = subStages;
        mListView.setAdapter(new CommonAdapter<SubStage>(mSubStageList) {
            @Override
            protected int getResourceId() {
                return R.layout.substage_item;
            }

            @Override
            protected void updateView(SubStage result, ViewHolder viewHolder) {


                viewHolder.setTextValue(R.id.substage_item_text,result.subStageName);

            }
        });

    }



    @Override
    protected void setListener() {


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}