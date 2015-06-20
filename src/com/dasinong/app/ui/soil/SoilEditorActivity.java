package com.dasinong.app.ui.soil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.components.home.view.popupwidow.CommSelectPopWindow;
import com.dasinong.app.components.net.INetRequest;
import com.dasinong.app.components.net.NetError;
import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.soil.domain.SoilAllEntity;
import com.dasinong.app.ui.soil.domain.SoilPostEntity;
import com.dasinong.app.ui.view.TopbarView;

public class SoilEditorActivity extends SoilBaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_SOIL_POST = 139;
    private static final String URL = NetConfig.BASE_URL + "insertSoilReport";


    private TextView soiltype;
    private TextView soilcolor;
    private TextView soilrating;
    private TextView soiltemtext;
    private TextView soiltem;
    private TextView soilallreport;
    private EditText soiltime;
    private EditText soilph;
    private EditText soilorganics;
    private EditText soiln;
    private EditText soilmoisture;
    private EditText soilK;
    private EditText soilKslow;
    private TextView soilother;
    private EditText soils;
    private EditText soilFE;
    private EditText soilmn;
    private EditText soilcu;
    private EditText soilZN;
    private EditText soilP;
    private TextView soilcheck;


    private CommSelectPopWindow mTypePopuWindow;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRightText(R.string.soil_editor_post);
        mTopBarView.setRightClickListener(this);
    }

    @Override
    protected int getMainResourceId() {
        return R.layout.activity_soil_edit;
    }


    protected void initView() {

        this.soilcheck = (TextView) findViewById(R.id.soil_check);
        this.soilP = (EditText) findViewById(R.id.soil_P);
        this.soilZN = (EditText) findViewById(R.id.soil_ZN);
        this.soilcu = (EditText) findViewById(R.id.soil_cu);
        this.soilmn = (EditText) findViewById(R.id.soil_mn);
        this.soilFE = (EditText) findViewById(R.id.soil_FE);
        this.soils = (EditText) findViewById(R.id.soil_s);
        this.soilother = (TextView) findViewById(R.id.soil_other);
        this.soilKslow = (EditText) findViewById(R.id.soil_K_slow);
        this.soilK = (EditText) findViewById(R.id.soil_K);
        this.soilmoisture = (EditText) findViewById(R.id.soil_moisture);
        this.soiln = (EditText) findViewById(R.id.soil_n);
        this.soilorganics = (EditText) findViewById(R.id.soil_organics);
        this.soilph = (EditText) findViewById(R.id.soil_ph);
        this.soiltime = (EditText) findViewById(R.id.soil_time);
        this.soilallreport = (TextView) findViewById(R.id.soil_all_report);
        this.soiltem = (TextView) findViewById(R.id.soil_tem);
        this.soiltemtext = (TextView) findViewById(R.id.soil_tem_text);
        this.soilrating = (TextView) findViewById(R.id.soil_rating);
        this.soilcolor = (TextView) findViewById(R.id.soil_color);
        this.soiltype = (TextView) findViewById(R.id.soil_type);


    }


    protected void initEvent() {

        soiltype.setOnClickListener(this);

        soilallreport.setOnClickListener(this);
        soiltem.setOnClickListener(this);
        soilcheck.setOnClickListener(this);
    }

    private void postSoilInformation(SoilPostEntity.Param param) {

        VolleyManager.getInstance().addPostRequest(
                REQUEST_CODE_SOIL_POST,
                URL,
                param,
                SoilAllEntity.class,
                this
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.soil_all_report:

                Intent intent = new Intent(this, SoilAllListActivity.class);
                startActivity(intent);
                break;

            case R.id.textview_topbar_right_text:
                SoilPostEntity.Param param = new SoilPostEntity.Param();
                postSoilInformation(param);
                break;

            case R.id.soil_type:

                if (mTypePopuWindow == null) {
                    mTypePopuWindow = new CommSelectPopWindow(this);
                }
                mTypePopuWindow.setDatas(getResources().getStringArray(R.array.soil_type));
                mTypePopuWindow.setPopWidth(v.getMeasuredWidth());
                mTypePopuWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {
                    @Override
                    public void itemSelected(CommSelectPopWindow window, int position, CharSequence tag) {


                    }
                });
                mTypePopuWindow.showAsDropDown(v);
                break;


            case R.id.soil_tem:
                //TODO:H5 土壤湿度
                Toast.makeText(this, "H5 open", Toast.LENGTH_SHORT).show();
                break;

            case R.id.soil_check:
                //TODO:H5(possible) 解读测土报告
                Toast.makeText(this, "h5 report", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;

        }
    }

    @Override
    public void onTaskSuccess(int requestCode, Object response) {

        SoilPostEntity entity = (SoilPostEntity) response;

    }

    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {

    }

    @Override
    public int getTitleText() {
        return R.string.soil_editor_title;
    }


}
