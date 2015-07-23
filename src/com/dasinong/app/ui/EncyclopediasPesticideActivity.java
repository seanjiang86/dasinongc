package com.dasinong.app.ui;

import com.dasinong.app.R;
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
 * @ClassName EncyclopediasPesticideActivity
 * @author linmu
 * @Decription 农药大全
 * @2015-7-21 下午9:18:42
 */
public class EncyclopediasPesticideActivity extends BaseActivity implements OnClickListener{

	private TopbarView mTopbarView;

	private EditText mSearchEdit;
	private RelativeLayout mAskforLayout;
	private RelativeLayout mNongyaoLayout;
	private RelativeLayout mBinghaiLayout;
	private RelativeLayout mIntelligentLayout;
	private RelativeLayout mShamanjiLayout;
	private RelativeLayout mLaoshujiLayout;
	
	private ImageView mSearchView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesticide);
		
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
		
		mShamanjiLayout = (RelativeLayout) this.findViewById(R.id.layout_shamanji);
		mLaoshujiLayout = (RelativeLayout) this.findViewById(R.id.layout_laoshuji);
	}

	private void setUpView() {
		mTopbarView.setCenterText("农药大全");
		mTopbarView.setLeftView(true, true);
		
		mAskforLayout.setOnClickListener(this);
		mNongyaoLayout.setOnClickListener(this);
		mBinghaiLayout.setOnClickListener(this);
		mIntelligentLayout.setOnClickListener(this);
		mShamanjiLayout.setOnClickListener(this);
		mLaoshujiLayout.setOnClickListener(this);
		
		mSearchEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
					
//					DeviceHelper.hideIME(mSearchEdit);
					
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
		if(TextUtils.isEmpty(keywords)){
			Toast.makeText(this, "请输入要搜索的内容", 0).show();
			return;
		}
		
		Intent intent = new Intent(this,SearchResultActivity.class);
		intent.putExtra("keywords", keywords);
		this.startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ask_for:
			Intent intent1 = new Intent(this,SearchPesticideResultActivity.class);
			intent1.putExtra("type", "杀菌剂");
			startActivity(intent1);
			break;
		case R.id.layout_nongyao:
			Intent intent2 = new Intent(this,SearchPesticideResultActivity.class);
			intent2.putExtra("type", "杀虫剂");
			startActivity(intent2);
			break;
		case R.id.layout_bingchongcaohai:
			Intent intent3 = new Intent(this,SearchPesticideResultActivity.class);
			intent3.putExtra("type", "除草剂");
			startActivity(intent3);
			break;
		case R.id.layout_intelligent:
			Intent intent4 = new Intent(this,SearchPesticideResultActivity.class);
			intent4.putExtra("type", "生长调节剂");
			startActivity(intent4);
			break;
		case R.id.layout_shamanji:
			Intent intent5 = new Intent(this,SearchPesticideResultActivity.class);
			intent5.putExtra("type", "杀螨剂");
			startActivity(intent5);
			break;
		case R.id.layout_laoshuji:
			Intent intent6 = new Intent(this,SearchPesticideResultActivity.class);
			intent6.putExtra("type", "老鼠剂");
			startActivity(intent6);
			break;
		}
	}
	
}
