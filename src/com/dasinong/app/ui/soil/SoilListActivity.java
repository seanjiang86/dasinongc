package com.dasinong.app.ui.soil;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dasinong.app.R;
import com.dasinong.app.ui.WebBrowserActivity;

public class SoilListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private static final String BASE_URL = "http://192.168.1.136:8080/ploughHelper/html/";


    @Override
    protected int getMainResourceId() {
        return R.layout.activity_soil_list;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(R.array.soil_list)));


    }

    @Override
    protected void initEvent() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public int getTitleText() {
        return R.string.soil_check;
    }

    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
            	intent.setClass(this, WebBrowserActivity.class);
            	intent.putExtra("url", BASE_URL+"SamplingImportance.html");
            	intent.putExtra("title", "为什么要测土");
            	startActivity(intent);
                break;
            case 1:
            	intent.setClass(this, WebBrowserActivity.class);
            	intent.putExtra("url", BASE_URL+"SamplingNotice.html");
            	intent.putExtra("title", "采样须知");
            	startActivity(intent);
                break;
            case 2:
            	
            	intent.setClass(this, WebBrowserActivity.class);
            	intent.putExtra("url", BASE_URL+"soiltest-sample.html");
            	intent.putExtra("title", "测土报告解读");
            	startActivity(intent);
                break;
            case 3:
            	intent.setClass(this, WebBrowserActivity.class);
            	intent.putExtra("url", BASE_URL+"SamplingStation.html");
            	intent.putExtra("title", "哪里可以测土？");
            	startActivity(intent);
                break;
        }
    }


    @Override
    public void onTaskSuccess(int requestCode, Object response) {

    }
}
