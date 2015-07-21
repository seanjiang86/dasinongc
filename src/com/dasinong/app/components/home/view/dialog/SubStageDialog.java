package com.dasinong.app.components.home.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
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
public class SubStageDialog extends BaseDialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ViewGroup contentView;

    private ListView mListView;

    private List<SubStage> mSubStageList;

    private int mCurrentPosition;

    public OnItemClickLisenter mOnItemClickLisenter;

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
        mListView = (ListView) contentView.findViewById(R.id.substage_listview);

    }

    @Override
    protected void initData() {


    }

    public void setDataSource(List<SubStage> subStages, int position) {
        this.mSubStageList = subStages;
        mCurrentPosition = position;
        mListView.setAdapter(new CommonAdapter<SubStage>(mSubStageList) {
            @Override
            protected int getResourceId() {
                return R.layout.substage_item;
            }

            @Override
            protected void updateView(SubStage result, ViewHolder viewHolder, int position) {
                viewHolder.setTextValue(R.id.substage_item_text, result.subStageName);
                RadioButton radioButton = viewHolder.getView(R.id.rb_check);
                if (mCurrentPosition == position) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }

            }
        });


    }


    @Override
    protected void setListener() {
        mListView.setOnItemClickListener(this);
        findViewById(R.id.substage_ok).setOnClickListener(this);
        findViewById(R.id.substage_cancel).setOnClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonAdapter adapter = (CommonAdapter) parent.getAdapter();
        mCurrentPosition = position;
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.substage_ok:

                if (mOnItemClickLisenter != null) {
                    mOnItemClickLisenter.onItemClick(mCurrentPosition);
                }

                this.dismiss();
                break;
            case R.id.substage_cancel:
                this.dismiss();
                break;
        }
    }

    public void setOnItemClickLisenter(OnItemClickLisenter lisenter) {
        this.mOnItemClickLisenter = lisenter;

    }


    public interface OnItemClickLisenter {

        public void onItemClick(int position);
    }

}