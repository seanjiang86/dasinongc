package com.dasinong.app.ui.soil;

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
import com.dasinong.app.ui.view.TopbarView;

public class SoilListActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_list);
        TopbarView topbarView = (TopbarView) findViewById(R.id.top_bar);
        topbarView.setLeftView(true, true);
        topbarView.setCenterText(R.string.soil_check);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(R.array.soil_list)));

        mListView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String url = "";
        switch (position) {
            case 0:
                url = "";
                break;
            case 1:
                url = "";
                break;
            case 2:
                url = "";
                break;
            case 3:
                url = "";
                break;
            case 4:
                url = "";
                break;
            default:
                url = "";
                break;

        }

        Toast.makeText(this,"open url",Toast.LENGTH_SHORT).show();
    }
}
