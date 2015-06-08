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

public class SoilInEditorActivity extends BaseActivity implements  NetRequest.RequestListener {

    private static final  int REQUEST_CODE = 101;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        RequestService.getInstance().sendRequestWithToken(
                this,
                BaseEntity.class,
                REQUEST_CODE,
                NetConfig.SubUrl.GET_SOIL_POST,
                null,
                this);
        setContentView(R.layout.activity_soil_edit);




    }


    private void updateView() {




    }


    @Override
    public void onSuccess(int requestCode, BaseEntity resultData) {

        updateView();
    }

    @Override
    public void onFailed(int requestCode, Exception error, String msg) {

    }
}
