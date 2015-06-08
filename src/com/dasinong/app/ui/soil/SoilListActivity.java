package com.dasinong.app.ui.soil;

import android.os.Bundle;
import android.view.View;
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

public class SoilListActivity extends BaseActivity {


    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_list);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(R.array.soil_list)));


    }


}
