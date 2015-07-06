package com.dasinong.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.Logger;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.GalleryActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.king.photo.util.Res;
import com.liam.imageload.LoadUtils;

public class ReportHarmActivity extends BaseActivity {

	private TopbarView topbar;
	private ScrollView sv_all;
	private EditText et_des;
	private Button btn_upload_images;
	private RadioGroup rg_harm_type;
	private RadioGroup rg_happened;
	private RadioGroup rg_distribution;
	private CheckBox cb_part_root;
	private CheckBox cb_part_stems;
	private CheckBox cb_part_leaves;
	private CheckBox cb_part_ear;
	private CheckBox cb_part_grain;
	private EditText et_harm_name;
	private Button btn_submit_harm;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private String fileName;
	private String title;
	private List<String> paths = new ArrayList<String>();
	private int flag = 0;
	// 作物名称
	private String cropName = "水稻";
	// 灾害类型
	private String disasterType = "";
	// 灾害名称
	private String disasterName = "";
	// 发生部位
	private String affectedArea = "";
	// 爆发时间
	private String eruptionTime = "";
	// 灾害分布
	private String disasterDist = "";
	// 农事操作
	private String fieldOperations = "";
	private long fieldId;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String path = (String) msg.obj;
			paths.add(path);
			flag++;
			if (flag == Bimp.tempSelectBitmap.size()) {
				upLoadImages();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_harm);
		Res.init(DsnApplication.getContext());
		login();

		PublicWay.activityList.add(this);

		title = getIntent().getStringExtra("title");

		initView();

		initTopBar();

		fieldId = SharedPreferencesHelper.getLong(this, Field.FIELDID, 1);

		btn_upload_images.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showHeadViewDialog();
			}
		});

	}

	private void initView() {

		sv_all = (ScrollView) findViewById(R.id.sv_all);

		topbar = (TopbarView) findViewById(R.id.topbar);

		rg_harm_type = (RadioGroup) findViewById(R.id.rg_harm_type);
		rg_happened = (RadioGroup) findViewById(R.id.rg_happened);
		rg_distribution = (RadioGroup) findViewById(R.id.rg_distribution);

		cb_part_root = (CheckBox) findViewById(R.id.cb_part_root);
		cb_part_stems = (CheckBox) findViewById(R.id.cb_part_stems);
		cb_part_leaves = (CheckBox) findViewById(R.id.cb_part_leaves);
		cb_part_ear = (CheckBox) findViewById(R.id.cb_part_ear);
		cb_part_grain = (CheckBox) findViewById(R.id.cb_part_grain);

		et_harm_name = (EditText) findViewById(R.id.et_harm_name);
		et_des = (EditText) findViewById(R.id.et_des);

		btn_upload_images = (Button) findViewById(R.id.btn_upload_images);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		btn_submit_harm = (Button) findViewById(R.id.btn_submit_harm);

		btn_submit_harm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getTableData();
				if (TextUtils.isEmpty(disasterType)) {
					showToast("请选择病虫草害类型");
					return;
				}
				if (Bimp.tempSelectBitmap.size() < 1) {
					showToast("请选择您要上传的图片");
					return;
				}
				startLoadingDialog();
				// 压缩图片
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
							Bitmap revitionImageSize = Bimp.revitionImageSize(Bimp.tempSelectBitmap.get(i).getImagePath());
							String fileName = String.valueOf(System.currentTimeMillis());
							FileUtils.saveBitmap(revitionImageSize, fileName);
							Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = FileUtils.SDPATH + fileName + ".JPEG";
							handler.sendMessage(msg);
						}
					}
				}).start();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Bimp.tempSelectBitmap != null && Bimp.tempSelectBitmap.size() > 0) {
			noScrollgridview.setVisibility(View.VISIBLE);
			initGridView();
			
			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
				System.out.println(Bimp.tempSelectBitmap.get(i).imagePath);
			}
			
			// 使scrollView滚动至底部
			handler.post(new Runnable() {

				@Override
				public void run() {
					sv_all.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		} else {
			noScrollgridview.setVisibility(View.GONE);
		}
	}

	private void initTopBar() {
		topbar.setCenterText(title);
		topbar.setLeftView(true, true);
	}

	/**
	 * 获取表单数据
	 */
	protected void getTableData() {
		int typeCheckedId = rg_harm_type.getCheckedRadioButtonId();
		int hapCheckedId = rg_happened.getCheckedRadioButtonId();
		int disCheckedId = rg_distribution.getCheckedRadioButtonId();

		switch (typeCheckedId) {
		case R.id.rb_disease_harm:
			disasterType = "病害";
			break;
		case R.id.rb_pet_harm:
			disasterType = "虫害";
			break;
		case R.id.rb_grass_harm:
			disasterType = "草害";
			break;
		case R.id.rb_nosure_harm:
			disasterType = "不确定";
			break;
		}

		switch (hapCheckedId) {
		case R.id.rb_suddenly:
			eruptionTime = "突然爆发";
			break;
		case R.id.rb_recently:
			eruptionTime = "近几天";
			break;
		case R.id.rb_recurrence:
			eruptionTime = "复发";
			break;
		}

		switch (disCheckedId) {
		case R.id.rb_uniform:
			disasterDist = "均匀受害";
			break;
		case R.id.rb_uneven:
			disasterDist = "不规则爆发";
			break;
		}

		StringBuilder sb = new StringBuilder();
		if (cb_part_root.isChecked()) {
			String root = cb_part_root.getText().toString();
			sb.append(root + ",");
		}
		if (cb_part_stems.isChecked()) {
			String stems = cb_part_stems.getText().toString();
			sb.append(stems + ",");
		}
		if (cb_part_leaves.isChecked()) {
			String leaves = cb_part_leaves.getText().toString();
			sb.append(leaves + ",");
		}
		if (cb_part_ear.isChecked()) {
			String ear = cb_part_ear.getText().toString();
			sb.append(ear + ",");
		}
		if (cb_part_grain.isChecked()) {
			String grain = cb_part_grain.getText().toString();
			sb.append(grain + ",");
		}
		affectedArea = sb.toString();
		if (!TextUtils.isEmpty(affectedArea)) {
			affectedArea = affectedArea.substring(0, affectedArea.length() - 1);
		}
		cropName = "水稻";
		disasterName = et_harm_name.getText().toString();
		fieldOperations = et_des.getText().toString();
	}

	protected void upLoadImages() {

		RequestService.getInstance().uploadPetDisPic(this, paths, cropName, disasterType, disasterName, affectedArea, eruptionTime, disasterDist,
				fieldOperations, fieldId + "", new NetRequest.RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							if ("诊断病虫草害".equals(title)) {
								showToast("感谢您的反馈，我们会在与专家沟通后联系您");
							} else {
								showToast("感谢您的反馈");
							}

							dismissLoadingDialog();
						} else {
							showToast("上传失败");
							dismissLoadingDialog();
						}
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						showToast("网络异常，请检测您的网络或稍后再试");
						error.printStackTrace();
					}
				});
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
				photo();
				dialog.dismiss();
			}
		});

		skipBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReportHarmActivity.this, AlbumActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public void initGridView() {

		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Intent intent = new Intent(ReportHarmActivity.this, GalleryActivity.class);
				intent.putExtra("position", "1");
				intent.putExtra("ID", arg2);
				startActivity(intent);
			}
		});
	}

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileName = String.valueOf(System.currentTimeMillis());
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FileUtils.SDPATH + fileName + ".jpg")));
		startActivityForResult(openCameraIntent, Activity.DEFAULT_KEYS_DIALER);
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			// loading();
		}

		public int getCount() {
			return Bimp.tempSelectBitmap.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (Bimp.tempSelectBitmap.size() > 0) {

				LoadUtils.getInstance().loadImage(holder.image, "file:///" + Bimp.tempSelectBitmap.get(position).imagePath);

			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Activity.DEFAULT_KEYS_DIALER:
			if (Bimp.tempSelectBitmap.size() < 6 && resultCode == RESULT_OK) {
				Bitmap bitmap = Bimp.revitionImageSize(FileUtils.SDPATH + fileName + ".jpg");

				ImageItem takePhoto = new ImageItem();
				takePhoto.setImagePath(FileUtils.SDPATH + fileName + ".jpg");
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Bimp.tempSelectBitmap != null && Bimp.tempSelectBitmap.size() > 0) {
			Bimp.tempSelectBitmap.clear();
			finish();
		}

	}

	// TODO MING 测试代码将来去掉
	private void login() {
		if (AccountManager.isLogin(ReportHarmActivity.this)) {
			return;
		}
		RequestService.getInstance().authcodeLoginReg(ReportHarmActivity.this, "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {

				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;
					AccountManager.saveAccount(ReportHarmActivity.this, entity.getData());
					showToast("登录成功");
				} else {
					Logger.d("TAG", resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {

				Logger.d("TAG", "msg" + msg);
			}
		});
	}
}
