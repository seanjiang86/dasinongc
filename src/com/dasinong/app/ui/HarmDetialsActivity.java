package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.database.disaster.domain.PetSolu;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.HarmDetialEntity;
import com.dasinong.app.entity.HarmDetialEntity.Solution;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.HarmDetialAdapter;
import com.dasinong.app.ui.fragment.HarmFragment;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.GraphicUtils;
import com.liam.imageload.LoadUtils;

/**
 * @author Ming 此类为显示病虫草害详情的页面
 */

public class HarmDetialsActivity extends BaseActivity {

	public static final String FLAG_PREVENT = "prevent";
	public static final String FLAG_CURE = "cure";
	public static final String FLAG_ITEM = "item";

	private ListView lv_detial;
	private View header;
	// 病害名
	private TextView tv_harm_name;
	// 病害描述
	private TextView tv_harm_des;
	// 病害等级
	private RatingBar rb_harm_grade;
	// 病害图片展示
	// private ViewPager vp_pic;
	// 每张图片对应的点
	// private LinearLayout ll_point;
	// 快速诊断按钮
	private LinearLayout ll_rapid_diagnosis;
	// 用来存放图片链接的集合
	// private List<PetSolu> dataList = new ArrayList<PetSolu>();
	private List<Solution> dataList = new ArrayList<Solution>();
	private ImageView imageView;
	private ImageView[] imageViews;
	private String type;
	// TODO MING:本地查询的bean，待数据库导入后，统一Bean
	// private List<PetSolu> petSoluList;
	// private List<PetSolu> petPreventList;
	private List<Solution> petSoluList = new ArrayList<Solution>();
	private List<Solution> petPreventList = new ArrayList<Solution>();
	private PetDisspec pet;
	private DisasterManager manager;
	private TopbarView topbar;
	private ImageView iv_pic;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			HarmDetialEntity detial = (HarmDetialEntity) msg.obj;
			for (HarmDetialEntity.Solution solution : detial.data.petSolutions) {
				if (solution.isRemedy) {
					petSoluList.add(solution);
				} else {
					petPreventList.add(solution);
				}
			}
			dataList.addAll(petSoluList);
			dataList.addAll(petPreventList);
			// TODO MING:查询本地

			initTopBar(detial.data.petDisSpec.petDisSpecName);
			initHeader(detial);
			initListView();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harm_detials);

		manager = DisasterManager.getInstance(this);

		type = getIntent().getExtras().getString("type");

		if (HarmFragment.TYPE_PET.equals(type)) {
			pet = (PetDisspec) getIntent().getExtras().getSerializable("pet");
		} else if (FLAG_PREVENT.equals(type) || FLAG_CURE.equals(type) || FLAG_ITEM.equals(type)) {
			if (getIntent().hasExtra("petDisSpecId")) {
				// TODO MING：如果取不到值不能等于 -1；
				int petDisSpecId = getIntent().getIntExtra("petDisSpecId", -1);
				queryDisease(petDisSpecId);
				// pet = manager.getDisease(petDisSpecId);
			} else {
				this.finish();
				return;
			}
		}

		lv_detial = (ListView) findViewById(R.id.lv_detial);
		topbar = (TopbarView) findViewById(R.id.topbar);

		// initTopBar();

		header = View.inflate(DsnApplication.getContext(), R.layout.harm_detials_header, null);

		// initData(pet.petDisSpecId);

	}

	private void initTopBar(String name) {
		topbar.setCenterText(name);
		topbar.setLeftView(true, true);
	}

	private void initListView() {
		lv_detial.setAdapter(new HarmDetialAdapter(this, dataList, petSoluList.size(), true));

		if (FLAG_CURE.equals(type)) {
			lv_detial.setSelection(1);
		} else if (FLAG_PREVENT.equals(type)) {
			lv_detial.setSelection(petSoluList.size());
		} else {
			lv_detial.setSelection(0);
		}

		lv_detial.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Solution solu = dataList.get(position - 1);
				Intent intent = new Intent(DsnApplication.getContext(), CureDetialActivity.class);

				Bundle bundle = new Bundle();
				bundle.putSerializable("solu", solu);
				intent.putExtras(bundle);

				startActivity(intent);
			}
		});
	}

	// TODO MING:本地查询

	private void initData(int petDisSpecId) {
		//
		// // 获取治疗方案
		// petSoluList = manager.getCureSolution(petDisSpecId);
		// // 获取预防方案
		// petPreventList = manager.getPreventSolution(petDisSpecId);
		//
		// if (petSoluList != null && petSoluList.size() != 0) {
		// dataList.addAll(petSoluList);
		// }
		// if (petPreventList != null && petPreventList.size() != 0) {
		// dataList.addAll(petPreventList);
		// }
		//
		// initHeader();
		//
		// initListView();
	}

	/*
	 * 填充listview的头的信息
	 */
	private void initHeader(HarmDetialEntity pet) {
		tv_harm_name = (TextView) header.findViewById(R.id.tv_harm_name);
		rb_harm_grade = (RatingBar) header.findViewById(R.id.rb_harm_grade);
		tv_harm_des = (TextView) header.findViewById(R.id.tv_harm_des);
		iv_pic = (ImageView) header.findViewById(R.id.iv_pic);

		// TODO MING:查询本地
		tv_harm_name.setText(pet.data.petDisSpec.petDisSpecName);
		// TODO MING:等待真实数据
		rb_harm_grade.setRating(3);
		tv_harm_des.setText(pet.data.petDisSpec.sympton + "\n" + pet.data.petDisSpec.form);
		// tv_harm_name.setText(pet.petDisSpecName);
		// rb_harm_grade.setRating(pet.severity);
		// tv_harm_des.setText(pet.description);
		LoadUtils.getInstance().loadImage(iv_pic, NetConfig.PET_IMAGE + pet.data.petDisSpec.imagePath);

		// TODO MING 多张图片备用
		/*
		 * imageViews = new ImageView[4]; int px = GraphicUtils.dip2px(this, 8);
		 * for (int i = 0; i < 4; i++) { imageView = new ImageView(this);
		 * imageView.setLayoutParams(new LayoutParams(px, px));
		 * 
		 * imageViews[i] = imageView;
		 * 
		 * if (i == 0) {
		 * imageView.setBackgroundResource(R.drawable.selected_point); } else {
		 * imageView.setBackgroundResource(R.drawable.unselect_point); }
		 * 
		 * ll_point.addView(imageView); }
		 * 
		 * ll_rapid_diagnosis = (LinearLayout)
		 * header.findViewById(R.id.ll_rapid_diagnosis);
		 * 
		 * vp_pic.setAdapter(new PagerAdapter() {
		 * 
		 * @Override public int getCount() { return 5; }
		 * 
		 * @Override public boolean isViewFromObject(View arg0, Object arg1) {
		 * return arg0 == arg1; }
		 * 
		 * @Override public Object instantiateItem(ViewGroup container, int
		 * position) { TextView tv = new TextView(getApplicationContext());
		 * tv.setText("我是第" + position + "张图片"); tv.setTextSize(50);
		 * 
		 * container.addView(tv); return tv; }
		 * 
		 * @Override public void destroyItem(ViewGroup container, int position,
		 * Object object) { container.removeView((View) object); } });
		 */

		lv_detial.addHeaderView(header, null, false);
	}

	private void queryDisease(int petDisSpecId) {
		if (!DeviceHelper.checkNetWork(this)) {
			showToast("请检测您的网络连接");
			return;
		}
		startLoadingDialog();
		RequestService.getInstance().getPetDisSpecDetial(this, petDisSpecId, HarmDetialEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if (resultData.isOk()) {
					HarmDetialEntity detial = (HarmDetialEntity) resultData;
					Message msg = handler.obtainMessage();
					msg.obj = detial;
					handler.sendMessage(msg);
					dismissLoadingDialog();
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				// TODO MING 待统一
				dismissLoadingDialog();
			}
		});
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
		Intent intent = new Intent(context, HarmDetialsActivity.class);
		intent.putExtra("petDisSpecId", petDisSpecId);
		intent.putExtra("type", flag);
		return intent;
	}
}
