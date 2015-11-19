package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.PetDisSpecEntity;
import com.dasinong.app.entity.PetDisSpecEntity.PetDisSpec.Solution;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.HarmDetailAdapter1;
import com.dasinong.app.ui.adapter.HarmPicAdapter;
import com.dasinong.app.ui.view.MyTabView;
import com.dasinong.app.ui.view.TopbarView;

public class HarmDetailsActivity extends BaseActivity implements OnClickListener {

	private ViewPager vp_pic;
	private MyTabView mtv_description;
	private List<String> list = new ArrayList<String>();
	private String id;
	private View header;

	public static final String SYMPTON = "为害症状";
	public static final String FORM = "形态";
	public static final String HABBIT = "习性";
	public static final String RULE = "发生规律";
	private ListView lv_detail;
	private TopbarView topbar;
	private ImageButton left;
	private ImageButton right;
	private TextView tv_show_position;
	private TextView tv_text;
	private String type;

	public static final String FLAG_PREVENT = "prevent";
	public static final String FLAG_CURE = "cure";
	public static final String FLAG_ITEM = "item";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harm_details);

		lv_detail = (ListView) findViewById(R.id.lv_detail);
		topbar = (TopbarView) findViewById(R.id.topbar);
		header = View.inflate(this, R.layout.harm_details_header, null);

		initHeader();

		queryData();
	}

	private void initHeader() {
		id = getIntent().getStringExtra("id");
		type = getIntent().getStringExtra("type");

		vp_pic = (ViewPager) header.findViewById(R.id.vp_pic);
		mtv_description = (MyTabView) header.findViewById(R.id.mtv_variety);
		left = (ImageButton) header.findViewById(R.id.left_button);
		right = (ImageButton) header.findViewById(R.id.right_button);
		tv_show_position = (TextView) header.findViewById(R.id.tv_show_position);
		tv_text = (TextView) header.findViewById(R.id.tv_text);

		left.setOnClickListener(this);
		right.setOnClickListener(this);

		list.add(SYMPTON);
		list.add(FORM);
		list.add(HABBIT);
		list.add(RULE);
	}

	private void queryData() {

		startLoadingDialog();

		RequestService.getInstance().getPetDisSpecBaiKeById(this, id, PetDisSpecEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if (resultData.isOk()) {
					PetDisSpecEntity entity = (PetDisSpecEntity) resultData;
					if (entity.data != null) {
						initHeaderData(entity);

						if (entity.data.solutions != null && !entity.data.solutions.isEmpty()) {
							entity.data.petSoluList = new ArrayList<Solution>();
							entity.data.petPreventList = new ArrayList<Solution>();
							for (Solution solution : entity.data.solutions) {
								if (solution.isRemedy) {

									entity.data.petSoluList.add(solution);
								} else {
									entity.data.petPreventList.add(solution);
								}
							}
						}
						entity.data.solutions.clear();
						entity.data.solutions.addAll(entity.data.petSoluList);
						entity.data.solutions.addAll(entity.data.petPreventList);

						initTopBar(entity.data.petDisSpecName);

						initListView(entity);
					}
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	protected void initTopBar(String petDisSpecName) {
		topbar.setCenterText(petDisSpecName);
		topbar.setLeftView(true, true);
	}

	private void initHeaderData(final PetDisSpecEntity entity) {

		int middlePosition = Integer.MAX_VALUE / 2;
		int firstPosition = middlePosition / entity.data.imagesPath.size() * entity.data.imagesPath.size();

		if (entity.data.imagesPath != null) {
			vp_pic.setAdapter(new HarmPicAdapter(entity.data.imagesPath, this));
		}

		tv_show_position.setText(1 + "/" + entity.data.imagesPath.size());

		vp_pic.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				tv_show_position.setText((arg0 % entity.data.imagesPath.size() + 1) + "/" + entity.data.imagesPath.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mtv_description.setData(list);

		mtv_description.setOnItemClickListener(new MyTabView.OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				switch (position) {
				case 0:
					tv_text.setText(entity.data.sympton);
					break;
				case 1:
					tv_text.setText(entity.data.form);
					break;
				case 2:
					tv_text.setText(entity.data.habbit);
					break;
				case 3:
					tv_text.setText(entity.data.rule);
					break;
				}
			}
		});

		tv_text.setText(entity.data.sympton);

		vp_pic.setCurrentItem(firstPosition);

		lv_detail.addHeaderView(header, null, false);
	}

	protected void initListView(final PetDisSpecEntity entity) {
		lv_detail.setAdapter(new HarmDetailAdapter1(this, entity.data.solutions, entity.data.petSoluList.size(), true));

		// 根据首页的点击的按钮跳到对应的位置 例如 我要治疗，我要预防 ...
		if (FLAG_CURE.equals(type)) {
			lv_detail.setSelection(1);
		} else if (FLAG_PREVENT.equals(type)) {
			lv_detail.setSelection(entity.data.petSoluList.size());
		} else {
			lv_detail.setSelection(0);
		}

		lv_detail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Solution solu = entity.data.solutions.get(position - 1);
				Intent intent = new Intent(DsnApplication.getContext(), CureDetailActivity.class);

				Bundle bundle = new Bundle();
				bundle.putSerializable("solu", solu);
				bundle.putInt("position", position);
				bundle.putInt("size", entity.data.petSoluList.size());
				intent.putExtras(bundle);

				startActivity(intent);
			}
		});

	}

	@Override
	public void onClick(View v) {
		int position = vp_pic.getCurrentItem();
		switch (v.getId()) {
		case R.id.left_button:
			if (position > 0) {
				vp_pic.setCurrentItem(position - 1);
			}
			break;
		case R.id.right_button:
			if (position <= vp_pic.getAdapter().getCount() - 2) {
				vp_pic.setCurrentItem(position + 1);
			}
			break;
		}
	}

	/**
	 * @param petDisSpecId
	 *            病虫草害中petDisSpec中的 petDisSpecId
	 * @param flag
	 *            标记，需要标明你是点击防治，预防，还是该条item跳进来的
	 * @param context
	 * @return
	 */
	public static Intent createIntent(int petDisSpecId, String flag, Context context) {
		Intent intent = new Intent(context, HarmDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", petDisSpecId + "");
		bundle.putString("type", flag);
		intent.putExtras(bundle);
		return intent;
	}

}
