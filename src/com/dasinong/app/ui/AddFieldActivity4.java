package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.RequestDate;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.CropList;
import com.dasinong.app.entity.CropList.Crop;
import com.dasinong.app.entity.CropNameList;
import com.dasinong.app.entity.CropNameList.CropName;
import com.dasinong.app.entity.CropNumberList;
import com.dasinong.app.entity.CropNumberList.CropNumber;
import com.dasinong.app.entity.LocationResult;
import com.dasinong.app.entity.NearbyUser;
import com.dasinong.app.entity.VillageInfoList;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestCode;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.utils.LocationUtils;
import com.dasinong.app.utils.LocationUtils.LocationListener;
import com.dasinong.app.utils.Logger;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class AddFieldActivity4 extends BaseActivity implements OnClickListener, OnItemSelectedListener {

	private Spinner spn_crop;
	private Spinner spn_variety_name;
	private Spinner spn_variety_number;
	private List<Crop> cropList = new ArrayList<Crop>();
	private List<CropName> cropNameList = new ArrayList<CropName>();
	private List<CropNumber> cropNumberList = new ArrayList<CropNumber>();
	private ImageView iv_one;
	private View view_one;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_4);

		spn_crop = (Spinner) findViewById(R.id.spn_crop);
		spn_variety_name = (Spinner) findViewById(R.id.spn_variety_name);
		spn_variety_number = (Spinner) findViewById(R.id.spn_variety_number);
		view_one = findViewById(R.id.view_one);
		iv_one = (ImageView) findViewById(R.id.iv_one);
		
		
		view_one.setBackgroundResource(R.color.color_2BAD29);
		iv_one.setImageResource(R.drawable.green_round);
		spn_crop.setAdapter(new ArrayAdapter<Crop>(this, R.layout.spinner_checked_text, cropList));
		RequestService.getInstance().getCrop(this, CropList.class, new NetRequest.RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {

				if (resultData.isOk()) {
					cropList = ((CropList) resultData).getList();
					
				} else {
					showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.spn_crop:
			initCropName(position);
			break;
		case R.id.spn_variety_name:
			initCropNumber(position);
			break;
		case R.id.spn_variety_number:
			
			break;

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private void initCropName(int position) {
		RequestService.getInstance().getCropName(DsnApplication.getContext(), cropList.get(position).getId(), CropNameList.class,
				new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							cropNameList = ((CropNameList) resultData).getList();

						} else {

						}

					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {

					}

				});
		if (cropNameList != null && cropNameList.size() != 0) {
			spn_crop.setAdapter(new ArrayAdapter<CropName>(this, R.layout.spinner_checked_text, cropNameList));
		}

	}

	private void initCropNumber(int position) {
		RequestService.getInstance().getCropName(DsnApplication.getContext(), cropNameList.get(position).getId(), CropNumberList.class,
				new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							cropNumberList = ((CropNumberList) resultData).getList();

						} else {

						}

					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {

					}

				});
		if (cropNumberList != null && cropNumberList.size() != 0) {
			spn_crop.setAdapter(new ArrayAdapter<CropNumber>(this, R.layout.spinner_checked_text, cropNumberList));
		}
	}
}
