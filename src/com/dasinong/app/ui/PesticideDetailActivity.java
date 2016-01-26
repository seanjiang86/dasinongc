package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.CPProductEntity;
import com.dasinong.app.entity.CPProductEntity.UseDirection;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.MyTabView;
import com.dasinong.app.ui.view.MyTabView.OnItemClickListener;
import com.dasinong.app.ui.view.TopbarView;

public class PesticideDetailActivity extends BaseActivity {
	private TextView tv_active_ingredient;
	private TextView tv_type;
	private TextView tv_manufacturer;
	private TextView tv_registration_id;
	private TopbarView topBar;
	private MyTabView mtv_pesticide;
	private String id;
	private List<String> list = new ArrayList<String>();
	public static final String USE_DIRECTION = "农药方法";
	public static final String GUIDELINE = "用药指导";
	public static final String TIP = "注意事项";
	private TextView tv_text;
	private LinearLayout ll_table;
	private TextView tv_telephone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesticide_detail);

		id = getIntent().getStringExtra("id");

		topBar = (TopbarView) findViewById(R.id.topbar);
		tv_active_ingredient = (TextView) findViewById(R.id.tv_active_ingredient);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_manufacturer = (TextView) findViewById(R.id.tv_manufacturer);
		tv_registration_id = (TextView) findViewById(R.id.tv_registration_id);
		tv_telephone = (TextView) findViewById(R.id.tv_telephone);
		mtv_pesticide = (MyTabView) findViewById(R.id.mtv_description);
		tv_text = (TextView) findViewById(R.id.tv_text);
		ll_table = (LinearLayout) findViewById(R.id.ll_table);

		list.add(USE_DIRECTION);
		list.add(GUIDELINE);
		list.add(TIP);

		queryData();
	}

	private void initTopBar(CPProductEntity entity) {
		topBar.setCenterText(entity.data.name);
		topBar.setLeftView(true, true);
	}

	private void queryData() {
		startLoadingDialog();
		RequestService.getInstance().getCPProductById(this, id, CPProductEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					CPProductEntity entity = (CPProductEntity) resultData;
					if (entity.data != null) {
						String[] crops = entity.data.crop.split("\n");
						String[] diseases = entity.data.disease.split("\n");
						String[] methods = entity.data.method.split("\n");
						String[] volumns = entity.data.volumn.split("\n");
						entity.data.useDirections = new ArrayList<CPProductEntity.UseDirection>();
						for (int i = 0; i < crops.length; i++) {
							UseDirection useDirection = entity.new UseDirection();
							useDirection.useCrop = crops[i];
							useDirection.useDisease = diseases[i];
							useDirection.useMethod = methods[i];
							useDirection.useVolumn = volumns[i];
							entity.data.useDirections.add(useDirection);
						}
						initTopBar(entity);
						initData(entity);
					}
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void initData(final CPProductEntity entity) {
		tv_active_ingredient.setText(entity.data.activeIngredient);
		tv_type.setText(entity.data.type);
		tv_manufacturer.setText(entity.data.manufacturer);
		tv_registration_id.setText(entity.data.registrationId);
		tv_telephone.setText(entity.data.telephone);

		if (!TextUtils.isEmpty(entity.data.telephone)) {
			tv_telephone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					// 这个意图就是调用系统的拨打活动
					intent.setAction(Intent.ACTION_DIAL);
					// 设置要拨打的电话号号码
					// uriString的格式为“tel:电话号码”
					intent.setData(Uri.parse("tel:" + entity.data.telephone));
					// 开始调整到打电话的活动
					startActivity(intent);
				}
			});
		}

		mtv_pesticide.setData(list);

		mtv_pesticide.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				switch (position) {
				case 0:
					tv_text.setVisibility(View.GONE);
					ll_table.setVisibility(View.VISIBLE);
					for (int i = 0; i < entity.data.useDirections.size(); i++) {
						TableLayout table = (TableLayout) View.inflate(PesticideDetailActivity.this, R.layout.item_table, null);
						TextView tv_crop = (TextView) table.findViewById(R.id.tv_crop);
						TextView tv_disease = (TextView) table.findViewById(R.id.tv_disease);
						TextView tv_volumn = (TextView) table.findViewById(R.id.tv_volumn);
						TextView tv_method = (TextView) table.findViewById(R.id.tv_method);

						tv_crop.setText(entity.data.useDirections.get(i).useCrop);
						tv_disease.setText(entity.data.useDirections.get(i).useDisease);
						tv_volumn.setText(entity.data.useDirections.get(i).useVolumn);
						tv_method.setText(entity.data.useDirections.get(i).useMethod);

						LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
						params.setMargins(0, 0, 0, 20);
						table.setLayoutParams(params);
						ll_table.addView(table);
					}
					break;
				case 1:
					ll_table.setVisibility(View.GONE);
					tv_text.setVisibility(View.VISIBLE);
					tv_text.setText(entity.data.guideline);
					break;
				case 2:
					ll_table.setVisibility(View.GONE);
					tv_text.setVisibility(View.VISIBLE);
					tv_text.setText(entity.data.tip);
					break;
				}
			}
		});

		onFirstInit(entity);
	}

	private void onFirstInit(CPProductEntity entity) {
		tv_text.setVisibility(View.GONE);
		ll_table.setVisibility(View.VISIBLE);
		for (int i = 0; i < entity.data.useDirections.size(); i++) {
			TableLayout table = (TableLayout) View.inflate(PesticideDetailActivity.this, R.layout.item_table, null);
			TextView tv_crop = (TextView) table.findViewById(R.id.tv_crop);
			TextView tv_disease = (TextView) table.findViewById(R.id.tv_disease);
			TextView tv_volumn = (TextView) table.findViewById(R.id.tv_volumn);
			TextView tv_method = (TextView) table.findViewById(R.id.tv_method);

			tv_crop.setText(entity.data.useDirections.get(i).useCrop);
			tv_disease.setText(entity.data.useDirections.get(i).useDisease);
			tv_volumn.setText(entity.data.useDirections.get(i).useVolumn);
			tv_method.setText(entity.data.useDirections.get(i).useMethod);

			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(0, 0, 0, 20);
			table.setLayoutParams(params);
			ll_table.addView(table);
		}
	}

}
