package com.dasinong.app.ui.soil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.BaseResponse;
import com.dasinong.app.components.home.view.popupwidow.CommSelectPopWindow;
import com.dasinong.app.components.net.NetError;
import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.soil.domain.DataEntity;
import com.dasinong.app.ui.soil.domain.SoilAllEntity;
import com.dasinong.app.ui.soil.domain.SoilPostEntity;

import java.util.Calendar;

public class SoilEditorActivity extends SoilBaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_SOIL_POST = 139;
    private static final String URL = NetConfig.BASE_URL + "insertSoilReport";


    private DataEntity mListDataEntity;

    private static final String EXTRA_LIST_ENTITY = "list_entity";


    private static final String TAG = "SoilEditorActivity";


    private CommSelectPopWindow mTypePopuWindow;
    private CommSelectPopWindow mColorPopuWindow;

    private CommSelectPopWindow mFertilityPopuWindow;


    private TextView mSoilType;
    private TextView mSoilColor;
    private TextView mSoilFertility;
    private EditText mSoilTime;
    private EditText mSoilPH;
    private EditText mSoilOrganics;
    private EditText mSoilAN;
    private EditText mSoilQN;
    private EditText mSoilP;
    private EditText mSoilQK;
    private EditText mSoilSK;
    private EditText mSoilMO;

    private EditText mSoilFE;
    private EditText mSoilMN;
    private EditText mSoilCU;
    private EditText mSoilB;
    private EditText mSoilZN;
    private EditText mSoilCA;
    private EditText mSoilMG;
    private EditText mSoilS;
    private EditText mSoilSI;

    private int year;
    private int month;
    private int day;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        Calendar calendar = Calendar.getInstance();

        if (bundle != null && bundle.containsKey(EXTRA_LIST_ENTITY)) {

            mListDataEntity = bundle.getParcelable(EXTRA_LIST_ENTITY);
            DEBUG(mListDataEntity.toString());
            if (mListDataEntity != null) {
                calendar.setTimeInMillis(mListDataEntity.testDate);
            }

        }

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (mListDataEntity != null) {
            updateView(mListDataEntity);
        }

        mSoilTime.setText(year + "-" + month + "-" + day);
        setRightText(R.string.soil_editor_post);
        mTopBarView.setRightClickListener(this);
    }

    @Override
    protected int getMainResourceId() {
        return R.layout.activity_soil_edit;
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


    protected void initView() {

        mSoilType = (TextView) findViewById(R.id.soil_type);
        mSoilColor = (TextView) findViewById(R.id.soil_color);
        mSoilFertility = (TextView) findViewById(R.id.soil_fertility);
        mSoilTime = (EditText) findViewById(R.id.soil_time);
        mSoilPH = (EditText) findViewById(R.id.soil_ph);
        mSoilOrganics = (EditText) findViewById(R.id.soil_organics);
        mSoilAN = (EditText) findViewById(R.id.soil_an);
        mSoilQN = (EditText) findViewById(R.id.soil_qn);
        mSoilP = (EditText) findViewById(R.id.soil_P);
        mSoilQK = (EditText) findViewById(R.id.soil_qk);
        mSoilSK = (EditText) findViewById(R.id.soil_sk);
        mSoilMO = (EditText) findViewById(R.id.soil_mo);
        mSoilFE = (EditText) findViewById(R.id.soil_FE);
        mSoilMN = (EditText) findViewById(R.id.soil_mn);
        mSoilCU = (EditText) findViewById(R.id.soil_cu);
        mSoilZN = (EditText) findViewById(R.id.soil_ZN);
        mSoilB = (EditText) findViewById(R.id.soil_B);
        mSoilCA = (EditText) findViewById(R.id.soil_ca);
        mSoilMG = (EditText) findViewById(R.id.soil_mg);
        mSoilS = (EditText) findViewById(R.id.soil_s);
        mSoilSI = (EditText) findViewById(R.id.soil_si);


    }


    protected void initEvent() {

        mSoilType.setOnClickListener(this);
        mSoilColor.setOnClickListener(this);
        mSoilFertility.setOnClickListener(this);
        mSoilTime.setOnClickListener(this);
        findViewById(R.id.soil_all_report).setOnClickListener(this);

    }

    private void postSoilInformation(SoilPostEntity.Param param) {
        VolleyManager.getInstance().addPostRequest(
                REQUEST_CODE_SOIL_POST,
                URL,
                param,
                BaseResponse.class,
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
//userId=15&fieldId=10&type=type&color=red&fertility=fertility&humidity=30.5&testDate=2015/04/01
// &phValue=6.7
// &&&&&&&
// &&
                param.type = "";
                param.color = "";

                //phValue
                if (TextUtils.isEmpty(mSoilPH.getText())) {
                    param.phValue = "0";
                } else {
                    param.phValue = mSoilPH.getText().toString().trim();
                }

                //有机质organic=organic

                if (TextUtils.isEmpty(mSoilOrganics.getText())) {
                    param.organic = "0";
                } else {
                    param.organic = mSoilOrganics.getText().toString().trim();
                }

                //有效磷p=100
                if (TextUtils.isEmpty(mSoilP.getText())) {
                    param.organic = "0";
                } else {
                    param.organic = mSoilP.getText().toString().trim();
                }


                //全氮  an=12.1
                if (TextUtils.isEmpty(mSoilAN.getText())) {
                    param.an = "0";
                } else {
                    param.an = mSoilAN.getText().toString().trim();
                }

                //碱解氮qn=10.2
                if (TextUtils.isEmpty(mSoilQN.getText())) {
                    param.qn = "0";
                } else {
                    param.qn = mSoilQN.getText().toString().trim();
                }
                //速效钾qK   soil_K
                if (TextUtils.isEmpty(mSoilQK.getText())) {
                    param.qK = "0";
                } else {
                    param.qK = mSoilQK.getText().toString().trim();
                }
                //缓效钾sK=1.2&  soil_K_slow

                if (TextUtils.isEmpty(mSoilSK.getText())) {
                    param.sK = "0";
                } else {
                    param.sK = mSoilSK.getText().toString().trim();
                }


                //mo=12.0


                if (TextUtils.isEmpty(mSoilMO.getText())) {
                    param.mo = "0";
                } else {
                    param.mo = mSoilMO.getText().toString().trim();
                }

                //铁Fe fe=3.0 soil_FE
                if (TextUtils.isEmpty(mSoilFE.getText())) {
                    param.fe = "0";
                } else {
                    param.fe = mSoilFE.getText().toString().trim();
                }
                //锰Mn mn=12 soil_mn
                if (TextUtils.isEmpty(mSoilMN.getText())) {
                    param.mn = "0";
                } else {
                    param.mn = mSoilMN.getText().toString().trim();
                }
                //铜Cu cu=21.0   soil_cu
                if (TextUtils.isEmpty(mSoilCU.getText())) {
                    param.cu = "0";
                } else {
                    param.cu = mSoilCU.getText().toString().trim();
                }

                //锌Zn zn=1 soil_ZN

                if (TextUtils.isEmpty(mSoilZN.getText())) {
                    param.zn = "0";
                } else {
                    param.zn = mSoilZN.getText().toString().trim();
                }
                //硼B  b=90 soil_B
                if (TextUtils.isEmpty(mSoilB.getText())) {
                    param.b = "0";
                } else {
                    param.b = mSoilB.getText().toString().trim();
                }


                //ca=1.0
                if (TextUtils.isEmpty(mSoilCA.getText())) {
                    param.ca = "0";
                } else {
                    param.ca = mSoilCA.getText().toString().trim();
                }

                //s=0.1
                if (TextUtils.isEmpty(mSoilS.getText())) {
                    param.s = "0";
                } else {
                    param.s = mSoilS.getText().toString().trim();
                }
                // si=45

                if (TextUtils.isEmpty(mSoilSI.getText())) {
                    param.si = "0";
                } else {
                    param.si = mSoilSI.getText().toString().trim();
                }
                // mg=2.3
                if (TextUtils.isEmpty(mSoilMG.getText())) {
                    param.mg = "0";
                } else {
                    param.mg = mSoilMG.getText().toString().trim();
                }


                postSoilInformation(param);
                break;

            case R.id.soil_type:

                initTypePopuWindow(v);

                mTypePopuWindow.showAsDropDown(v);
                break;

            case R.id.soil_color:

                initColorPopuWindow(v);

                mColorPopuWindow.showAsDropDown(v);
                break;
            case R.id.soil_fertility:

                initFertilityPopuWindow(v);

                mFertilityPopuWindow.showAsDropDown(v);
                break;

            case R.id.soil_tem:
                //TODO:H5 土壤湿度
                Toast.makeText(this, "H5 open", Toast.LENGTH_SHORT).show();
                break;

            case R.id.soil_check:
                //TODO:H5(possible) 解读测土报告
                Toast.makeText(this, "h5 report", Toast.LENGTH_SHORT).show();
                break;

            case R.id.soil_time:
                showChooseDate();
            default:
                break;

        }
    }

    private void initFertilityPopuWindow(View v) {

        if (mFertilityPopuWindow == null) {
            mFertilityPopuWindow = new CommSelectPopWindow(this);
            mFertilityPopuWindow.setDatas(getResources().getStringArray(R.array.soil_type));
            mFertilityPopuWindow.setPopWidth(v.getMeasuredWidth());
            mFertilityPopuWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {
                @Override
                public void itemSelected(CommSelectPopWindow window, int position, CharSequence tag) {

                    mSoilFertility.setText(tag.toString());
                }
            });
        }


    }

    private void initColorPopuWindow(View v) {


        if (mColorPopuWindow == null) {
            mColorPopuWindow = new CommSelectPopWindow(this);
            mColorPopuWindow.setDatas(getResources().getStringArray(R.array.soil_type));
            mColorPopuWindow.setPopWidth(v.getMeasuredWidth());
            mColorPopuWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {
                @Override
                public void itemSelected(CommSelectPopWindow window, int position, CharSequence tag) {

                    mSoilColor.setText(tag.toString());
                }
            });
        }


    }

    private void initTypePopuWindow(View v) {
        if (mTypePopuWindow == null) {
            mTypePopuWindow = new CommSelectPopWindow(this);

            mTypePopuWindow.setDatas(getResources().getStringArray(R.array.soil_type));
            mTypePopuWindow.setPopWidth(v.getMeasuredWidth());
            mTypePopuWindow.setmPopItemSelectListener(new CommSelectPopWindow.PopItemSelectListener() {
                @Override
                public void itemSelected(CommSelectPopWindow window, int position, CharSequence tag) {

                    mSoilType.setText(tag.toString());
                }
            });
        }

    }

    @Override
    public void onTaskSuccess(int requestCode, Object response) {


    }

    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {

    }

    @Override
    public int getTitleText() {
        return R.string.soil_editor_title;
    }


    public static Intent createIntent(Context context, DataEntity entity) {

        Intent intent = new Intent(context, SoilEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_LIST_ENTITY, entity);

        intent.putExtras(bundle);

        return intent;
    }


    private void DEBUG(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }


    private void showChooseDate() {
        DEBUG("year:" + year);
        DatePickerDialog dateDialog = new DatePickerDialog(this, new MyOnDateSetListener(), year, month, day);
        dateDialog.show();
    }


    class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {

            year = y;
            month = monthOfYear;
            day = dayOfMonth;
            mSoilTime.setText(year + "-" + month + "-" + day);
        }
    }

}
