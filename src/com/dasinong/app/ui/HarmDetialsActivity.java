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
import com.dasinong.app.entity.HarmDetialEntity.HarmDetial;
import com.dasinong.app.entity.HarmDetialEntity.HarmInfo;
import com.dasinong.app.entity.HarmDetialEntity.Solutions;
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
	private List<Solutions> dataList = new ArrayList<Solutions>();
	private ImageView imageView;
	private ImageView[] imageViews;
	private String type;
	//网络查询结果
	private List<Solutions> petSoluList = new ArrayList<Solutions>();
	private List<Solutions> petPreventList = new ArrayList<Solutions>();
	//本地查询结果
	private List<PetSolu> LocaPetSoluList = new ArrayList<PetSolu>();
	private List<PetSolu> LocaPetPreventList = new ArrayList<PetSolu>();
	private PetDisspec pet;
	private DisasterManager manager;
	private TopbarView topbar;
	private ImageView iv_pic;
	private int petDisSpecId;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			HarmDetialEntity detial = (HarmDetialEntity) msg.obj;
			for (Solutions solution : detial.data.petSolutions) {
				if (solution.isRemedy) {
					petSoluList.add(solution);
				} else {
					petPreventList.add(solution);
				}
			}
			dataList.addAll(petSoluList);
			dataList.addAll(petPreventList);
			initTopBar(detial.data.petDisSpec.petDisSpecName);
			initHeader(detial);
			initListView();
		};
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harm_detials);
		
		lv_detial = (ListView) findViewById(R.id.lv_detial);
		topbar = (TopbarView) findViewById(R.id.topbar);
		header = View.inflate(DsnApplication.getContext(), R.layout.harm_detials_header, null);

		manager = DisasterManager.getInstance(this);

		type = getIntent().getExtras().getString("type");

		if (HarmFragment.TYPE_PET.equals(type)) {
			pet = (PetDisspec) getIntent().getExtras().getSerializable("pet");
		} else if (FLAG_PREVENT.equals(type) || FLAG_CURE.equals(type) || FLAG_ITEM.equals(type)) {
			if (getIntent().hasExtra("petDisSpecId")) {
				petDisSpecId = getIntent().getIntExtra("petDisSpecId", -1);
				if (DeviceHelper.checkNetWork(this)) {
					queryDisease(petDisSpecId);
				} else {
					initData(petDisSpecId);
				}
			} else {
				this.finish();
				return;
			}
		}
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
				Solutions solu = dataList.get(position - 1);
				Intent intent = new Intent(DsnApplication.getContext(), CureDetialActivity.class);

				Bundle bundle = new Bundle();
				bundle.putSerializable("solu", solu);
				bundle.putInt("position", position);
				bundle.putInt("size", petSoluList.size());
				intent.putExtras(bundle);

				startActivity(intent);
			}
		});
	}

	private void initData(int petDisSpecId) {
		PetDisspec pet = manager.getDisease(petDisSpecId);

		HarmDetialEntity detial = new HarmDetialEntity();
		detial.data = new HarmDetial();
		detial.data.petDisSpec = new HarmInfo();
		
		detial.data.petDisSpec.alias = pet.alias;
		detial.data.petDisSpec.form = pet.forms;
		detial.data.petDisSpec.habbit = pet.habits;
		detial.data.petDisSpec.id = pet.petDisSpecId;
		detial.data.petDisSpec.imagePath = pet.pictureIds;
		detial.data.petDisSpec.petDisSpecName = pet.petDisSpecName;
		detial.data.petDisSpec.rule = pet.rules;
		detial.data.petDisSpec.sympton = pet.sympthon;
		
		// 获取治疗方案
		LocaPetSoluList = manager.getCureSolution(petDisSpecId);
		// 获取预防方案
		LocaPetPreventList = manager.getPreventSolution(petDisSpecId);
		
		for (PetSolu solu : LocaPetPreventList) {
			Solutions solution = new Solutions();
			if(solu.isCPSolu == 1){
				solution.isCPSolu = true;
			}else{
				solution.isCPSolu = false;
			}
			
			if(solu.isRemedy == 1){
				solution.isRemedy = true;
			} else {
				solution.isRemedy = false;
			}
			solution.petDisSpecId = solu.petDisSpecId;
			solution.petSoluDes = solu.petSoluDes;
			solution.petSoluId = solu.petSoluId;
			solution.providedBy = solu.providedBy;
			solution.rank = solu.rank;
			solution.subStageId = solu.subStageId;
			
			petPreventList.add(solution);
		}
		
		for (PetSolu solu : LocaPetPreventList) {
			Solutions solution = new Solutions();
			if(solu.isCPSolu == 1){
				solution.isCPSolu = true;
			}else{
				solution.isCPSolu = false;
			}
			
			if(solu.isRemedy == 1){
				solution.isRemedy = true;
			} else {
				solution.isRemedy = false;
			}
			solution.petDisSpecId = solu.petDisSpecId;
			solution.petSoluDes = solu.petSoluDes;
			solution.petSoluId = solu.petSoluId;
			solution.providedBy = solu.providedBy;
			solution.rank = solu.rank;
			solution.subStageId = solu.subStageId;
			
			petPreventList.add(solution);
		}

		if (petSoluList != null && petSoluList.size() != 0) {
			dataList.addAll(petSoluList);
		}
		if (petPreventList != null && petPreventList.size() != 0) {
			dataList.addAll(petPreventList);
		}

		initHeader(detial);

		initListView();
	}

	/*
	 * 填充listview的头的信息
	 */
	private void initHeader(HarmDetialEntity detial) {
		tv_harm_name = (TextView) header.findViewById(R.id.tv_harm_name);
		rb_harm_grade = (RatingBar) header.findViewById(R.id.rb_harm_grade);
		tv_harm_des = (TextView) header.findViewById(R.id.tv_harm_des);
		iv_pic = (ImageView) header.findViewById(R.id.iv_pic);

		tv_harm_name.setText(detial.data.petDisSpec.petDisSpecName);
		// TODO MING:等待真实数据
		rb_harm_grade.setRating(3);

		String sympton = ToDBC(detial.data.petDisSpec.sympton);
		String form = ToDBC(detial.data.petDisSpec.form);

		tv_harm_des.setText(sympton + "\n\n" + form);

		LoadUtils.getInstance().loadImage(iv_pic, NetConfig.PET_IMAGE + detial.data.petDisSpec.imagePath);

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

	private void queryDisease(final int petDisSpecId) {
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
				dismissLoadingDialog();
				initData(petDisSpecId);
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

	/**
	 * 半角转全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}
