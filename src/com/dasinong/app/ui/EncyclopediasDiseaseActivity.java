package com.dasinong.app.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.EncyclopediasDao;
import com.dasinong.app.database.encyclopedias.domain.Crop;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.PetDisSpecsListEntity;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;

/**
 * @ClassName EncyclopediasDiseaseActivity
 * @author linmu
 * @Decription 病害
 * @2015-7-21 下午9:19:00
 */
public class EncyclopediasDiseaseActivity extends BaseActivity implements OnClickListener {

	private TopbarView mTopbarView;

	private EditText mSearchEdit;
	private RelativeLayout mDiseaseLayout;
	private RelativeLayout mPestisLayout;
	private RelativeLayout mGrassLayout;
	private RelativeLayout mIntelligentLayout;

	private ImageView mSearchView;

	private String cropId;

	private String cropName;

	private TextView tv_binghai;

	private TextView tv_chonghai;

	private TextView tv_caohai;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binghai);

		cropId = getIntent().getStringExtra("cropId");
		cropName = getIntent().getStringExtra("cropName");

		if (cropName != null && cropId == null) {
			EncyclopediasDao dao = new EncyclopediasDao(this);
			List<Crop> list = dao.queryCropId(cropName);
			cropId = String.valueOf(list.get(0).cropId);
		}

		initView();
		setUpView();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mSearchEdit = (EditText) this.findViewById(R.id.edittext_search);
		mDiseaseLayout = (RelativeLayout) this.findViewById(R.id.layout_disease);
		mPestisLayout = (RelativeLayout) this.findViewById(R.id.layout_pestis);
		mGrassLayout = (RelativeLayout) this.findViewById(R.id.layout_grass);

		mIntelligentLayout = (RelativeLayout) this.findViewById(R.id.layout_intelligent);
		mSearchView = (ImageView) this.findViewById(R.id.imageview_search);

		tv_binghai = (TextView) this.findViewById(R.id.tv_binghai);
		tv_chonghai = (TextView) this.findViewById(R.id.tv_chonghai);
		tv_caohai = (TextView) this.findViewById(R.id.tv_caohai);
	}

	private void setUpView() {
		mTopbarView.setCenterText(cropName + "病虫草害大全");
		mTopbarView.setLeftView(true, true);

		mDiseaseLayout.setOnClickListener(this);
		mPestisLayout.setOnClickListener(this);
		mGrassLayout.setOnClickListener(this);
		mIntelligentLayout.setOnClickListener(this);

		tv_binghai.setText(cropName + "常见病害");
		tv_chonghai.setText(cropName + "常见虫害");
		tv_caohai.setText(cropName + "常见草害");

		mSearchEdit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

					// DeviceHelper.hideIME(mSearchEdit);

					search();
					return true;
				}
				return false;
			}

		});

		mSearchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search();
			}
		});
	}

	private void search() {
		String keywords = mSearchEdit.getText().toString().trim();
		if (TextUtils.isEmpty(keywords)) {
			Toast.makeText(this, "请输入要搜索的内容", 0).show();
			return;
		}

		Intent intent = new Intent(this, SearchTypeResultActivity.class);
		intent.putExtra("keywords", keywords);
		intent.putExtra("type", "petdisspec");
		this.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_disease:
			queryData(cropId, "病害");
			break;
		case R.id.layout_pestis:
			queryData(cropId, "虫害");
			break;
		case R.id.layout_grass:
			queryData(cropId, "草害");
			break;
		case R.id.layout_intelligent:
			Intent intent4 = new Intent(this, ReportHarmActivity.class);
			intent4.putExtra("title", "诊断病虫草害");
			startActivity(intent4);
			break;
		}
	}

	public void queryData(String cropId, final String type) {

		startLoadingDialog();

		RequestService.getInstance().browsePetDisSpecsByCropIdAndType(this, cropId, type, PetDisSpecsListEntity.class,
				new NetRequest.RequestListener() {
					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							PetDisSpecsListEntity entity = (PetDisSpecsListEntity) resultData;
							if (entity != null && entity.data != null && entity.data.size() > 0) {
								Intent intent = new Intent(EncyclopediasDiseaseActivity.this, SearchDiseaseResultActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("data", entity);
								bundle.putString("type", cropName+"常见"+type);
								intent.putExtras(bundle);
								startActivity(intent);
							} else {
								showToast("该作物无常见" + type);
							}
						} else {
							showToast(resultData.getMessage());
						}
						dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
						showToast(msg);
					}
				});
	}

}
