package com.dasinong.app.ui.soil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.components.net.INetRequest;
import com.dasinong.app.components.net.NetError;
import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.soil.domain.SoilAllEntity;
import com.dasinong.app.ui.view.TopbarView;

public class SoilAllListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private static final int REQUEST_CODE_SOIL_LIST = 190;

    private static final String URL = NetConfig.BASE_URL + "loadReports";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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


        Toast.makeText(this, "open url", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskSuccess(int requestCode, Object response) {

        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(R.array.soil_list)));
    }


    @Override
    public int getTitleText() {
        return R.string.soil_all_list_title;
    }


}
