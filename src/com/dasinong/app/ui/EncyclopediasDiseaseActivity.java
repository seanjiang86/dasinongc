package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.EncyclopediasDao;
import com.dasinong.app.database.encyclopedias.PetdisspecbrowseDao;
import com.dasinong.app.database.encyclopedias.domain.Crop;
import com.dasinong.app.ui.view.TopbarView;

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
import android.widget.Toast;

/**
 * @ClassName EncyclopediasDiseaseActivity
 * @author linmu
 * @Decription 病害
 * @2015-7-21 下午9:19:00
 */
public class EncyclopediasDiseaseActivity extends BaseActivity implements OnClickListener {

	private TopbarView mTopbarView;

	private EditText mSearchEdit;
	private RelativeLayout mAskforLayout;
	private RelativeLayout mNongyaoLayout;
	private RelativeLayout mBinghaiLayout;
	private RelativeLayout mIntelligentLayout;

	private ImageView mSearchView;

	private String cropId;

	private String cropName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binghai);

		cropId = getIntent().getStringExtra("type");
		cropName = getIntent().getStringExtra("cropName");
		
		if(cropName != null && cropId == null){
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
		mAskforLayout = (RelativeLayout) this.findViewById(R.id.layout_ask_for);
		mNongyaoLayout = (RelativeLayout) this.findViewById(R.id.layout_nongyao);
		mBinghaiLayout = (RelativeLayout) this.findViewById(R.id.layout_bingchongcaohai);
		mIntelligentLayout = (RelativeLayout) this.findViewById(R.id.layout_intelligent);
		mSearchView = (ImageView) this.findViewById(R.id.imageview_search);
	}

	private void setUpView() {
		mTopbarView.setCenterText("病虫草害大全");
		mTopbarView.setLeftView(true, true);

		mAskforLayout.setOnClickListener(this);
		mNongyaoLayout.setOnClickListener(this);
		mBinghaiLayout.setOnClickListener(this);
		mIntelligentLayout.setOnClickListener(this);

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
		case R.id.layout_ask_for:
			Intent intent1 = new Intent(this, SearchDiseaseResultActivity.class);
			intent1.putExtra("type", "病害");
			intent1.putExtra("cropId", cropId);
			startActivity(intent1);
			break;
		case R.id.layout_nongyao:
			Intent intent2 = new Intent(this, SearchDiseaseResultActivity.class);
			intent2.putExtra("type", "虫害");
			intent2.putExtra("cropId", cropId);
			startActivity(intent2);

			break;
		case R.id.layout_bingchongcaohai:
			Intent intent3 = new Intent(this, SearchDiseaseResultActivity.class);
			intent3.putExtra("type", "草害");
			intent3.putExtra("cropId", cropId);
			startActivity(intent3);

			break;
		case R.id.layout_intelligent:
			Intent intent4 = new Intent(this, ReportHarmActivity.class);
			intent4.putExtra("title", "诊断病虫草害");
			startActivity(intent4);
			break;
		}
	}

}
