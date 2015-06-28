package com.dasinong.app.ui.soil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.ui.soil.domain.DataEntity;

import java.util.Calendar;

/**
 * 测土的详情页面
 */
public class SoilInformationActivity extends SoilBaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SOIL_INFORMATION = 145;
    //private static final String URL = NetConfig.BASE_URL;
    private static final String EXTRA_LIST_ENTITY = "enity";

    private TextView mSoilType;
    private TextView mSoilColor;
    private TextView mSoilFertility;
    private TextView mSoilTime;
    private TextView mSoilPH;
    private TextView mSoilOrganics;
    private TextView mSoilAN;
    private TextView mSoilQN;
    private TextView mSoilP;
    private TextView mSoilQK;
    private TextView mSoilSK;
    private TextView mSoilMO;

    private TextView mSoilFE;
    private TextView mSoilMN;
    private TextView mSoilCU;
    private TextView mSoilB;
    private TextView mSoilZN;
    private TextView mSoilCA;
    private TextView mSoilMG;
    private TextView mSoilS;
    private TextView mSoilSI;
    private TextView soilcheck;
    private DataEntity mListDataEntity;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        getIntentDate();

    }


    @Override
    protected int getMainResourceId() {
        return R.layout.activity_soil_information;
    }

    protected void initView() {
        mSoilType = (TextView) findViewById(R.id.soil_type);
        mSoilColor = (TextView) findViewById(R.id.soil_color);
        mSoilFertility = (TextView) findViewById(R.id.soil_fertility);
        mSoilTime = (TextView) findViewById(R.id.soil_time);
        mSoilPH = (TextView) findViewById(R.id.soil_ph);
        mSoilOrganics = (TextView) findViewById(R.id.soil_organics);
        mSoilAN = (TextView) findViewById(R.id.soil_an);
        mSoilQN = (TextView) findViewById(R.id.soil_qn);
        mSoilP = (TextView) findViewById(R.id.soil_P);
        mSoilQK = (TextView) findViewById(R.id.soil_qk);
        mSoilSK = (TextView) findViewById(R.id.soil_sk);
        mSoilMO = (TextView) findViewById(R.id.soil_mo);
        mSoilFE = (TextView) findViewById(R.id.soil_FE);
        mSoilMN = (TextView) findViewById(R.id.soil_mn);
        mSoilCU = (TextView) findViewById(R.id.soil_cu);
        mSoilZN = (TextView) findViewById(R.id.soil_ZN);
        mSoilB = (TextView) findViewById(R.id.soil_B);
        mSoilCA = (TextView) findViewById(R.id.soil_ca);
        mSoilMG = (TextView) findViewById(R.id.soil_mg);
        mSoilS = (TextView) findViewById(R.id.soil_s);
        mSoilSI = (TextView) findViewById(R.id.soil_si);

        this.soilcheck = (TextView) findViewById(R.id.soil_check);


        this.soilcheck.setOnClickListener(this);

    }

    @Override
    protected void initEvent() {

    }


    private void getIntentDate() {
        Bundle bundle = getIntent().getExtras();
        Calendar calendar = Calendar.getInstance();
        if (bundle != null && bundle.containsKey(EXTRA_LIST_ENTITY)) {

            mListDataEntity = bundle.getParcelable(EXTRA_LIST_ENTITY);
            if (mListDataEntity != null) {
                calendar.setTimeInMillis(mListDataEntity.testDate);
            }

        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (mListDataEntity != null) {
            updateView(mListDataEntity);
        }

         mSoilTime.setText(year + "-" + month + "-" + day);
    }


    @Override
    public void onClick(View v) {

        Toast.makeText(this, "show soil", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getTitleText() {
        return R.string.soil_all_report;
    }


    private void updateView(DataEntity mListDataEntity) {
        if (mListDataEntity != null) {


            if (!TextUtils.isEmpty(mListDataEntity.type)) {
                mSoilType.setText(mListDataEntity.type);
            }
            if (!TextUtils.isEmpty(mListDataEntity.fertility)) {
                mSoilFertility.setText(mListDataEntity.fertility);
            }

            if (!TextUtils.isEmpty(mListDataEntity.color)) {
                mSoilColor.setText(mListDataEntity.color);
            }

            //phValue
            if (!TextUtils.isEmpty(mListDataEntity.phValue)) {
                mSoilPH.setText(mListDataEntity.phValue.trim());
            }

            //有机质organic=organic

            if (!TextUtils.isEmpty(mListDataEntity.organic)) {

                mSoilOrganics.setText(mListDataEntity.organic);
            }

            //有效磷p=100
            if (!TextUtils.isEmpty(mListDataEntity.p)) {

                mSoilP.setText(mListDataEntity.p);
            }


            //全氮  an=12.1
            if (!TextUtils.isEmpty(mListDataEntity.an)) {
                mSoilAN.setText(mListDataEntity.an);
            }

            //碱解氮qn=10.2
            if (!TextUtils.isEmpty(mListDataEntity.qn)) {
                mSoilQN.setText(mListDataEntity.qn);
            }
            //速效钾qK   soil_K
            if (!TextUtils.isEmpty(mListDataEntity.qK)) {
                mSoilQK.setText(mListDataEntity.qK);
            }
            //缓效钾sK=1.2&  soil_K_slow

            if (!TextUtils.isEmpty(mListDataEntity.sK)) {

                mSoilSK.setText(mListDataEntity.sK);
            }


            //mo=12.0


            if (!TextUtils.isEmpty(mListDataEntity.mo)) {
                mSoilMO.setText(mListDataEntity.mo);
            }

            //铁Fe fe=3.0 soil_FE
            if (!TextUtils.isEmpty(mListDataEntity.fe)) {
                mSoilFE.setText(mListDataEntity.fe);
            }
            //锰Mn mn=12 soil_mn
            if (!TextUtils.isEmpty(mListDataEntity.mn)) {
                mSoilMN.setText(mListDataEntity.mn);
            }
            //铜Cu cu=21.0   soil_cu
            if (!TextUtils.isEmpty(mListDataEntity.cu)) {
                mSoilCU.setText(mListDataEntity.cu);
            }

            //锌Zn zn=1 soil_ZN

            if (!TextUtils.isEmpty(mListDataEntity.zn)) {
                mSoilZN.setText(mListDataEntity.zn);
            }
            //硼B  b=90 soil_B
            if (!TextUtils.isEmpty(mListDataEntity.b)) {
                mSoilB.setText(mListDataEntity.b);
            }


            //ca=1.0
            if (!TextUtils.isEmpty(mListDataEntity.ca)) {
                mSoilCA.setText(mListDataEntity.ca);
            }

            //s=0.1
            if (!TextUtils.isEmpty(mListDataEntity.s)) {
                mSoilS.setText(mListDataEntity.s);
            }
            // si=45

            if (!TextUtils.isEmpty(mListDataEntity.si)) {
                mSoilSI.setText(mListDataEntity.si);
            }
            // mg=2.3
            if (!TextUtils.isEmpty(mListDataEntity.mg)) {
                mSoilMG.setText(mListDataEntity.mg);
            }


        }
    }

    public static Intent createIntent(Context context, DataEntity itemAtPosition) {

        Intent intent = new Intent(context, SoilInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_LIST_ENTITY, itemAtPosition);

        intent.putExtras(bundle);

        return intent;

    }

    @Override
    public void onTaskSuccess(int requestCode, Object response) {

    }
}
