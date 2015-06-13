package com.dasinong.app.ui.soil;

import android.os.Bundle;
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

public class SoilAllListActivity extends BaseActivity implements AdapterView.OnItemClickListener, INetRequest<SoilAllEntity> {


    private ListView mListView;
    private static final int REQUEST_CODE_SOIL_LIST = 190;

    private static final String URL = NetConfig.BASE_URL + "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_all_list);
        TopbarView topbarView = (TopbarView) findViewById(R.id.top_bar);
        topbarView.setLeftView(true, true);
        topbarView.setCenterText(R.string.soil_all_list_title);
        mListView = (ListView) findViewById(R.id.list_view);

        mListView.setOnItemClickListener(this);

        loadDataFromServer();

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
    public void onTaskSuccess(int requestCode, SoilAllEntity response) {

        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.soil_list_item, R.id.soil_item_text, getResources().getStringArray(R.array.soil_list)));
    }

    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {

    }

    @Override
    public void onCache(int requestCode, SoilAllEntity response) {

    }
}
