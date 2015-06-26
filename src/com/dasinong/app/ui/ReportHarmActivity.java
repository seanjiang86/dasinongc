package com.dasinong.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.view.TopbarView;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.GalleryActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;

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
import android.support.annotation.VisibleForTesting;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ReportHarmActivity extends BaseActivity {

	private TopbarView topbar;
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
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private String fileName;
	private List <String> paths = new ArrayList<String>();
	private int flag = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			case 2:
				String path = (String) msg.obj;
				paths.add(path);
				System.out.println(path);
				flag++;
				if(flag == Bimp.tempSelectBitmap.size()){
					upLoadImages();
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_harm);

		PublicWay.activityList.add(this);

		// TODO MING:修改方法名？
		init();

		initView();
		initTopBar();

		btn_upload_images.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showHeadViewDialog();
			}
		});

	}

	private void initView() {

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

	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	private void initTopBar() {
		topbar.setCenterText("举报病虫草害");
		topbar.setRightText("提交");
		topbar.setLeftView(true, true);

		topbar.setRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// 压缩图片
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
							Bitmap revitionImageSize = Bimp.revitionImageSize(Bimp.tempSelectBitmap.get(i).getImagePath());
							String fileName = String.valueOf(System.currentTimeMillis());
							FileUtils.saveBitmap(revitionImageSize, fileName);
							Message msg = handler.obtainMessage();
							msg.what = 2;
							msg.obj = FileUtils.SDPATH + fileName + ".JPEG";
							handler.sendMessage(msg);
						}
					}
				}).start();

				getTableData();
			}
		});
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
			// TODO MING 病害
			System.out.println("病害");
			break;
		case R.id.rb_pet_harm:
			// TODO MING 虫害
			System.out.println("虫害");
			break;
		case R.id.rb_grass_harm:
			// TODO MING 草害
			System.out.println("草害");
			break;
		case R.id.rb_nosure_harm:
			// TODO MING 不确定
			System.out.println("不确定");
			break;
		}

		switch (hapCheckedId) {
		case R.id.rb_suddenly:
			// TODO MING 突然爆发
			System.out.println("突然爆发");
			break;
		case R.id.rb_recently:
			// TODO MING 几天爆发
			System.out.println("近几天");
			break;
		case R.id.rb_recurrence:
			// TODO MING 复发
			System.out.println("复发");
			break;
		}

		switch (disCheckedId) {
		case R.id.rb_uniform:
			// TODO MING 均匀受害
			System.out.println("均匀");
			break;
		case R.id.rb_uneven:
			// TODO MING 不规则爆发
			System.out.println("不规则");
			break;
		}

		// TODO MING 获取影响部位

		String harmName = et_harm_name.getText().toString();
		String des = et_des.getText().toString();

	}

	protected void upLoadImages() {

		RequestService.getInstance().uploadPetDisPic(this, paths, new NetRequest.RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					showToast("上传成功");
				} else {
					showToast("上传失败");
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
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

	public void init() {

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
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
			loading();
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
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Activity.DEFAULT_KEYS_DIALER:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				System.out.println(bm.getWidth());
				System.out.println(bm.getHeight());

				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
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
}
