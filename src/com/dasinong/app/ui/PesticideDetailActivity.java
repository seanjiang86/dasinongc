package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.CPProductEntity;
import com.dasinong.app.entity.CPProductEntity.UseDirection;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.HarmDescriptionFragmentPagerAdapter;
import com.dasinong.app.ui.view.MyTabView;
import com.dasinong.app.ui.view.MyViewPager;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

public class PesticideDetailActivity extends BaseActivity {
	private TextView tv_active_ingredient;
	private TextView tv_type;
	private TextView tv_manufacturer;
	private TextView tv_registration_id;
	private TopbarView topBar;
	private MyTabView mtv_pesticide;
	private ViewPager pager;
	private String id;
	private String title;
	private TextView tv_name;
	private List<String> list = new ArrayList<String>();
	public static final String USE_DIRECTION = "农药方法";
	public static final String GUIDELINE = "用药指导";
	public static final String TIP = "注意事项";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesticide_detail);

		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");

		topBar = (TopbarView) findViewById(R.id.topbar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_active_ingredient = (TextView) findViewById(R.id.tv_active_ingredient);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_manufacturer = (TextView) findViewById(R.id.tv_manufacturer);
		tv_registration_id = (TextView) findViewById(R.id.tv_registration_id);

		mtv_pesticide = (MyTabView) findViewById(R.id.mtv_description);
		
		pager = (ViewPager) this.findViewById(R.id.pager);

		list.add(USE_DIRECTION);
		list.add(GUIDELINE);
		list.add(TIP);

		queryData();
		initTopBar();
	}

	private void initTopBar() {
		topBar.setCenterText(title);
		topBar.setLeftView(true, true);
	}

	private void queryData() {
		RequestService.getInstance().getCPProductById(this, id, CPProductEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
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

						initView(entity);
					}
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast(msg);
			}
		});
	}

	protected void initView(final CPProductEntity entity) {
		tv_name.setText(entity.data.name);
		tv_active_ingredient.setText(entity.data.activeIngredient);
		tv_type.setText(entity.data.type);
		tv_manufacturer.setText(entity.data.manufacturer);
		tv_registration_id.setText(entity.data.registrationId);

		mtv_pesticide.setData(list);
		HarmDescriptionFragmentPagerAdapter adapter = new HarmDescriptionFragmentPagerAdapter(getSupportFragmentManager(), list, entity);
		pager.setAdapter(adapter);
		mtv_pesticide.setViewPager(pager);

	}
}
