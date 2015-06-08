package com.dasinong.app.ui.soil;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SoilDetail;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;

public class SoilInformationActivity extends BaseActivity implements View.OnClickListener, NetRequest.RequestListener {

    private static final  int REQUEST_CODE = 100;

    private TextView soiltype;
    private TextView soilcolor;
    private TextView soilrating;
    private TextView soiltem;
    private TextView soiltime;
    private TextView soilph;
    private TextView soilorganics;
    private TextView soiln;
    private TextView soilmoisture;
    private TextView soilK;
    private TextView soilother;
    private TextView soils;
    private TextView soilFE;
    private TextView soilmn;
    private TextView soilcu;
    private TextView soilZN;
    private TextView soilP;
    private TextView soilcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        RequestService.getInstance().sendRequestWithToken(
                this,
                SoilDetail.class,
                REQUEST_CODE,
                NetConfig.SubUrl.GET_SOIL_DETAIL,
                null,
                this);
        setContentView(R.layout.activity_soil_information);
        this.soilcheck = (TextView) findViewById(R.id.soil_check);
        this.soilP = (TextView) findViewById(R.id.soil_P);
        this.soilZN = (TextView) findViewById(R.id.soil_ZN);
        this.soilcu = (TextView) findViewById(R.id.soil_cu);
        this.soilmn = (TextView) findViewById(R.id.soil_mn);
        this.soilFE = (TextView) findViewById(R.id.soil_FE);
        this.soils = (TextView) findViewById(R.id.soil_s);
        this.soilother = (TextView) findViewById(R.id.soil_other);
        this.soilK = (TextView) findViewById(R.id.soil_K);
        this.soilmoisture = (TextView) findViewById(R.id.soil_moisture);
        this.soiln = (TextView) findViewById(R.id.soil_n);
        this.soilorganics = (TextView) findViewById(R.id.soil_organics);
        this.soilph = (TextView) findViewById(R.id.soil_ph);
        this.soiltime = (TextView) findViewById(R.id.soil_time);
        this.soiltem = (TextView) findViewById(R.id.soil_tem);
        this.soilrating = (TextView) findViewById(R.id.soil_rating);
        this.soilcolor = (TextView) findViewById(R.id.soil_color);
        this.soiltype = (TextView) findViewById(R.id.soil_type);

        this.soilcheck.setOnClickListener(this);

    }


    private void updateView() {

        soiltype.setText("type");
        soilcolor.setText("color");
        soilrating.setText("rating");
        soiltem.setText("tem");
        soiltime.setText("time");
        soilph.setText("time");
        soilorganics.setText("time");
        soiln.setText("time");
        soilmoisture.setText("time");
        soilK.setText("time");
        soilother.setText("time");
        soils.setText("time");
        soilFE.setText("time");
        soilmn.setText("time");
        soilcu.setText("time");
        soilZN.setText("time");

        soilP.setText("time");


    }

    @Override
    public void onClick(View v) {

        Toast.makeText(this,"show soil",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(int requestCode, BaseEntity resultData) {

        updateView();
    }

    @Override
    public void onFailed(int requestCode, Exception error, String msg) {

    }
}
