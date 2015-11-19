package com.dasinong.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.utils.FileUtils;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.king.photo.activity.GalleryActivity;
import com.lidroid.xutils.BitmapUtils;

public class ReportHarmActivity extends BaseActivity {

	private TopbarView topbar;
	private ScrollView sv_all;
	private EditText et_des;
	private ImageButton btn_upload_images;
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
	private ArrayList<String> paths = new ArrayList<String>();
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

	public static final int REQUEST_IMAGE = 0;

	public static final int SCAN_IMAGE = 1;

	public static final String CURRENT_LIST = "paths";

	public static final int MAX_NUM = 6;

	private ArrayList<String> newPahts;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String path = (String) msg.obj;
			newPahts = new ArrayList<String>();
			newPahts.add(path);
			flag++;
			if (flag == paths.size()) {
				 upLoadImages(newPahts);
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_harm);
		title = getIntent().getStringExtra("title");

		initView();

		initTopBar();

		fieldId = SharedPreferencesHelper.getLong(this, Field.FIELDID, 1);

		btn_upload_images.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ReportHarmActivity.this, MultiImageSelectorActivity.class);
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, MAX_NUM);

				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
				// 设置默认选择
				if (paths != null && paths.size() > 0) {
					intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, paths);
				}
				startActivityForResult(intent, REQUEST_IMAGE);
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

		btn_upload_images = (ImageButton) findViewById(R.id.btn_upload_images);
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
				if (paths.size() < 1) {
					showToast("请选择您要上传的图片");
					return;
				}
				startLoadingDialog();
				// 压缩图片
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < paths.size(); i++) {
							String fileName = String.valueOf(System.currentTimeMillis());
							FileUtils.saveBitmap(paths.get(i), fileName);

							Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = FileUtils.SDPATH + fileName + ".JPEG";
							handler.sendMessage(msg);

							// newPaths.add(FileUtils.SDPATH + fileName +
							// ".JPEG");
						}
					}
				}).start();
				// ArrayList<String> newPaths = new ArrayList<String>();

				// upLoadImages(newPaths);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		if (paths != null && paths.size() > 0) {
			noScrollgridview.setVisibility(View.VISIBLE);
			initGridView();

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

	protected void upLoadImages(List<String> newPaths) {

		System.out.println("我正在上传图片 ++++++++++++++++++++++++++++++");

		RequestService.getInstance().uploadPetDisPic(this, newPaths, cropName, disasterType, disasterName, affectedArea, eruptionTime, disasterDist,
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
						clearList();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
						clearList();
						error.printStackTrace();
					}
				});
	}

	protected void clearList() {
		newPahts.clear();
	}

	public void initGridView() {

		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Intent intent = new Intent(ReportHarmActivity.this, GalleryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(CURRENT_LIST, paths);
				bundle.putInt("ID", arg2);
				bundle.putString("position", "1");
				intent.putExtras(bundle);
				startActivityForResult(intent, SCAN_IMAGE);
			}
		});
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		// private int selectedPosition = -1;

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return paths.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		// public void setSelectedPosition(int position) {
		// selectedPosition = position;
		// }
		//
		// public int getSelectedPosition() {
		// return selectedPosition;
		// }

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

			if (paths.size() > 0) {
//				LoadUtils.getInstance().loadImage(holder.image, "file:///" + paths.get(position));
				BitmapUtils bitmapUtils = new BitmapUtils(ReportHarmActivity.this);
				bitmapUtils.display(holder.image, "file:///" + paths.get(position));
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				paths.clear();
				paths.addAll(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
				if (paths.size() > 6) {
					paths.remove(0);
				}
			}
		} else if (requestCode == SCAN_IMAGE) {
			if (resultCode == RESULT_OK) {
				paths.clear();
				paths = data.getStringArrayListExtra(CURRENT_LIST);
				System.out.println("paths  " + paths);
			}
		}
	}
}
