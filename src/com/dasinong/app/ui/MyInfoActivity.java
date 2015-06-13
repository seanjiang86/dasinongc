package com.dasinong.app.ui;

import java.io.File;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import cn.smssdk.SMSSDK;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.GraphicUtils;
import com.dasinong.app.utils.Logger;
import com.dasinong.app.utils.PhotoUtils;
import com.liam.imageload.LoadUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends BaseActivity implements OnClickListener, CropHandler {

	private TopbarView mTopbarView;

	private RelativeLayout mHeadviewLayout;
	private ImageView mHeadviewImage;

	private RelativeLayout mPhoneLayout;
	private TextView mPhoneText;
	private Button mAuthPhoneButton;

	private RelativeLayout mResetPwdLayout;

	private RelativeLayout mNameLayout;
	private TextView mNameText;

	private RelativeLayout mAddressLayout;
	private TextView mAddressText;

	private RelativeLayout mHomephoneLayout;
	private TextView mHomephoneText;

	private String password;

	protected String mPhotoPath;
	private String mCropPath;

	CropParams mCropParams = new CropParams();

	private Spinner spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);

		initView();
		setUpView();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mHeadviewLayout = (RelativeLayout) this.findViewById(R.id.layout_headview);
		mHeadviewImage = (ImageView) this.findViewById(R.id.imageview_headview);
		mPhoneLayout = (RelativeLayout) this.findViewById(R.id.layout_phoneNumber);
		mPhoneText = (TextView) this.findViewById(R.id.textview_phone_number);
		mAuthPhoneButton = (Button) this.findViewById(R.id.button_auth_phone);
		mResetPwdLayout = (RelativeLayout) this.findViewById(R.id.layout_reset_password);
		mNameLayout = (RelativeLayout) this.findViewById(R.id.layout_name);
		mNameText = (TextView) this.findViewById(R.id.textview_name);
		mAddressLayout = (RelativeLayout) this.findViewById(R.id.layout_address);
		mAddressText = (TextView) this.findViewById(R.id.textview_address);
		mHomephoneLayout = (RelativeLayout) this.findViewById(R.id.layout_home_phone);
		mHomephoneText = (TextView) this.findViewById(R.id.textview_home_phone);

	}

	private void setUpView() {
		mTopbarView.setCenterText("个人信息");
		mTopbarView.setLeftView(true, true);

		mHeadviewLayout.setOnClickListener(this);
		mPhoneLayout.setOnClickListener(this);
		mAuthPhoneButton.setOnClickListener(this);
		mResetPwdLayout.setOnClickListener(this);
		mNameLayout.setOnClickListener(this);
		mAddressLayout.setOnClickListener(this);
		mHomephoneLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_headview:
			showHeadViewDialog();
			break;
		case R.id.layout_phoneNumber:
			editInfo(MyInfoSetActivity.EDIT_PHONE);
			break;
		case R.id.button_auth_phone:

			break;
		case R.id.layout_reset_password:
			editInfo(MyInfoSetActivity.EDIT_PASSWORD);

			break;
		case R.id.layout_name:
			editInfo(MyInfoSetActivity.EDIT_NAME);

			break;
		case R.id.layout_address:
			editInfo(MyInfoSetActivity.EDIT_ADDRESS);

			break;
		case R.id.layout_home_phone:
			editInfo(MyInfoSetActivity.EDIT_HOME_PHONE);

			break;
		}
	}

	private void showHeadViewDialog() {

		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		View view = View.inflate(this, R.layout.dialog_authcode_no, null);
		Button againBt = (Button) view.findViewById(R.id.button_get_code_again);
		Button skipBt = (Button) view.findViewById(R.id.button_skip_auth);

		againBt.setText("拍照");
		skipBt.setText("相册");

		againBt.setTextSize(18);
		skipBt.setTextSize(18);

		dialog.setContentView(view);
		againBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// String fileName = System.currentTimeMillis() + ".png";
				// mPhotoPath = PhotoUtils.PHOTOS_DIR + File.separator +
				// fileName;
				// PhotoUtils.fromCapture(MyInfoActivity.this,
				// PhotoUtils.PHOTOS_DIR, fileName);

				Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
				startActivityForResult(intent, CropHelper.REQUEST_CAMERA);

				dialog.dismiss();

			}
		});

		skipBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// PhotoUtils.fromGallery(MyInfoActivity.this);

				CropHelper.clearCachedCropFile(mCropParams.uri);
				startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);

				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void editInfo(int type) {
		Intent intent = new Intent(this, MyInfoSetActivity.class);
		intent.putExtra("editType", type);
		startActivityForResult(intent, type);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int arg1, Intent data) {
	// super.onActivityResult(requestCode, arg1, data);
	//
	// if(arg1 == RESULT_OK){
	//
	// String fileName = "";
	// switch (requestCode) {
	// case PhotoUtils.REQUESTCODE_CAPTURE:
	// File file = null;
	// int degree = PhotoUtils.readPictureDegree(mPhotoPath);
	// if (degree != 0) {
	// Toast.makeText(MyInfoActivity.this, "图片处理中", Toast.LENGTH_SHORT).show();
	// Bitmap bitmap = PhotoUtils.rotateBitmap(mPhotoPath, degree);
	// fileName = System.currentTimeMillis() + ".png";
	// String photoPath = PhotoUtils.PHOTOS_DIR + File.separator + fileName;
	// PhotoUtils.writeBitmapToPath(bitmap, PhotoUtils.PHOTOS_DIR, fileName);
	// file = new File(photoPath);
	// } else {
	// file = new File(mPhotoPath);
	// }
	// Uri uri = Uri.fromFile(file);
	// fileName = System.currentTimeMillis() + ".png";
	// mCropPath = PhotoUtils.PHOTOS_DIR + File.separator + fileName;
	// PhotoUtils.startPhotoCrop(MyInfoActivity.this, uri, 1, 1, 300, 300,
	// PhotoUtils.PHOTOS_DIR, fileName);
	// break;
	// case PhotoUtils.REQUESTCODE_GALLERY:
	// if (data != null && data.getData() != null) {
	// fileName = System.currentTimeMillis() + ".png";
	// mCropPath = PhotoUtils.PHOTOS_DIR + File.separator + fileName;
	// PhotoUtils.startPhotoCrop(MyInfoActivity.this, data.getData(), 1, 1, 300,
	// 300, PhotoUtils.PHOTOS_DIR, fileName);
	// } else {
	// Toast.makeText(MyInfoActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
	// }
	// break;
	// case PhotoUtils.REQUESTCODE_CROP:
	// LoadUtils.getInstance().loadImage(mHeadviewImage, "file://" + mCropPath);
	// upload(mCropPath);
	// break;
	// case MyInfoSetActivity.EDIT_PHONE:
	// String info = data.getStringExtra("info");
	// mPhoneText.setText(info);
	// break;
	// case MyInfoSetActivity.EDIT_NAME:
	// String info1 = data.getStringExtra("info");
	// mNameText.setText(info1);
	// break;
	// case MyInfoSetActivity.EDIT_PASSWORD:
	// String info2 = data.getStringExtra("info");
	// password = info2;
	// break;
	// case MyInfoSetActivity.EDIT_ADDRESS:
	// String info3 = data.getStringExtra("info");
	// mAddressText.setText(info3);
	// break;
	// case MyInfoSetActivity.EDIT_HOME_PHONE:
	// String info4 = data.getStringExtra("info");
	// mHomephoneText.setText(info4);
	// break;
	// }
	// }
	//
	// }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("mPhotoPath", mPhotoPath);
		outState.putString("mCropPath", mCropPath);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		mPhotoPath = savedInstanceState.getString("mPhotoPath");
		mCropPath = savedInstanceState.getString("mCropPath");
	}

	private void upload(String path) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CropHelper.handleResult(this, requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case MyInfoSetActivity.EDIT_PHONE:
				String info = data.getStringExtra("info");
				mPhoneText.setText(info);
				break;
			case MyInfoSetActivity.EDIT_NAME:
				String info1 = data.getStringExtra("info");
				mNameText.setText(info1);
				break;
			case MyInfoSetActivity.EDIT_PASSWORD:
				String info2 = data.getStringExtra("info");
				password = info2;
				break;
			case MyInfoSetActivity.EDIT_ADDRESS:
				String info3 = data.getStringExtra("info");
				mAddressText.setText(info3);
				break;
			case MyInfoSetActivity.EDIT_HOME_PHONE:
				String info4 = data.getStringExtra("info");
				mHomephoneText.setText(info4);
				break;
			}
		}

	}

	@Override
	public CropParams getCropParams() {
		return mCropParams;
	}

	@Override
	public void onPhotoCropped(Uri uri) {
		Logger.d("MyInfoActivity", "Crop Uri in path: " + uri.getPath());
		// Toast.makeText(this, "Photo cropped!", Toast.LENGTH_LONG).show();
		mHeadviewImage.setImageBitmap(CropHelper.decodeUriAsBitmap(this, mCropParams.uri));
	}

	@Override
	public void onCropCancel() {
		// Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCropFailed(String message) {
		Toast.makeText(this, "Crop failed:" + message, Toast.LENGTH_LONG).show();
	}

	@Override
	public Activity getContext() {
		return this;
	}

	@Override
	protected void onDestroy() {
		if (getCropParams() != null)
			CropHelper.clearCachedCropFile(getCropParams().uri);
		super.onDestroy();
	}

}