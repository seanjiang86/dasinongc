package com.dasinong.app.ui.soil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.soil.adapter.CommonAdapter;
import com.dasinong.app.ui.soil.adapter.ViewHolder.ViewHolder;
import com.dasinong.app.ui.soil.domain.SoilAllEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SoilAllListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private static final int REQUEST_CODE_SOIL_LIST = 190;

    private static final String URL = NetConfig.BASE_URL + "getSoilReport";

    private String mTipText;

    private SimpleDateFormat sdf;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mTipText = getResources().getString(R.string.soil_report_title);

        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        loadDataFromServer();

    }

    @Override
    protected int getMainResourceId() {
        return R.layout.activity_soil_all_list;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.list_view);


    }

    @Override
    protected void initEvent() {

        mListView.setOnItemClickListener(this);
    }

    private void loadDataFromServer() {

        SoilAllEntity.Param param = new SoilAllEntity.Param();
        VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_SOIL_LIST,
                URL,
                param,
                SoilAllEntity.class,
                this

        );


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getItemAtPosition(position)==null){
            return;
        }
        startActivity(SoilEditorActivity.createIntentFromList(this, (SoilAllEntity.DataEntity) parent.getItemAtPosition(position)));
    }

    @Override
    public void onTaskSuccess(int requestCode, Object response) {

        SoilAllEntity entity = (SoilAllEntity) response;
        mListView.setAdapter(new CommonAdapter<SoilAllEntity.DataEntity>(entity.data) {

            @Override
            protected int getResourceId() {
                return R.layout.soil_list_item;
            }

            @Override
            protected View getItemView(int position,View convertView, ViewHolder viewHolder) {

                String text = sdf.format(new Date(getItem(position).testDate))+mTipText;
                viewHolder.setTextValue(R.id.soil_item_text,text);
                return convertView;
            }
        });
    }


    @Override
    public int getTitleText() {
        return R.string.soil_report_title;
    }


}
