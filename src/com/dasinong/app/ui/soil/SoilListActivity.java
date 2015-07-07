package com.dasinong.app.ui.soil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SoilDetail;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.RegisterServiceActivity;
import com.dasinong.app.ui.WebViewActivity;
import com.dasinong.app.ui.view.TopbarView;

public class SoilListActivity extends SoilBaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;


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
            	intent.setClass(this, RegisterServiceActivity.class);
            	intent.putExtra("url", "SamplingImportance.htm");
            	intent.putExtra("title", "为什么要测土");
            	startActivity(intent);
                break;
            case 1:
            	intent.setClass(this, RegisterServiceActivity.class);
            	intent.putExtra("url", "SamplingExample.htm");
            	intent.putExtra("title", "测土配方报告示例");
            	startActivity(intent);
                break;
            case 2:
            	intent.setClass(this, RegisterServiceActivity.class);
            	intent.putExtra("url", "SamplingNotice.htm");
            	intent.putExtra("title", "采样须知");
            	startActivity(intent);
                break;
            case 3:
            	
            	intent.setClass(this, RegisterServiceActivity.class);
            	intent.putExtra("url", "SamplingUnscramble.htm");
            	intent.putExtra("title", "测土报告解读");
            	startActivity(intent);
                break;
            case 4:
            	intent.setClass(this, RegisterServiceActivity.class);
            	intent.putExtra("url", "SamplingStation.htm");
            	intent.putExtra("title", "哪里可以测土？");
            	startActivity(intent);
                break;
        }
    }


    @Override
    public void onTaskSuccess(int requestCode, Object response) {

    }
}
