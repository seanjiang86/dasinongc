package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.VarietybrowseDao;
import com.dasinong.app.database.encyclopedias.PetdisspecbrowseDao;
import com.dasinong.app.database.encyclopedias.VarietybrowseDao;
import com.dasinong.app.database.encyclopedias.domain.Crop;
import com.dasinong.app.database.encyclopedias.domain.Varietybrowse;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.database.encyclopedias.domain.Varietybrowse;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.PesticideListEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.adapter.DiseaseListAdapter;
import com.dasinong.app.ui.adapter.PesticideListAdapter;
import com.dasinong.app.ui.adapter.VarietyListAdapter;
import com.dasinong.app.ui.view.LetterView;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.LetterView.OnTouchingLetterChangedListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchVarietyResultActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private String type;

	private Handler mHandler = new Handler();
	
	private ListView mListView;
	
	private LetterView letterView;
	private HashMap<String, Integer> alphaIndexer;
	private OverlayThread mOverlayThread;
	private WindowManager mWindowManager;
	private TextView mOverlay;

	protected List<Varietybrowse> query;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		type = getIntent().getStringExtra("type");
		initView();
		setUpView();
		initOverlay();
		initData();
//		requestData();
		
	}

	private void initData() {
		
		
		new Thread(){
			public void run() {
				VarietybrowseDao dao = new VarietybrowseDao(SearchVarietyResultActivity.this);
				query = dao.query(type);
				
				List<Varietybrowse> deleteList = new ArrayList<>();
				for(Varietybrowse crop:query){
					if(crop.varietyName!=null){
						if(crop.varietyName.contains("通用") || crop.varietyName.contains("重复") || crop.varietyName.contains("其他")){
							deleteList.add(crop);
						}
					}
				}
				query.removeAll(deleteList);
				
				query = sortContact(query);

				alphaIndexer.clear();
				for (int i = 0; i < query.size(); i++) {
					Varietybrowse entity = query.get(i);
					if (entity.isTitle) {
						alphaIndexer.put(entity.title, i);
					}
				}
				
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						setAdapter(query);
					}
				});
			};
			
		}.start();
	}

	protected void setAdapter(List<Varietybrowse> query) {
		VarietyListAdapter adapter = new VarietyListAdapter(this, query, false);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_sms_list);
		
		letterView = (LetterView) findViewById(R.id.letterview);
		letterView.setVisibility(View.VISIBLE);
		letterView.setOnTouchingLetterChangedListener(new letterViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		mOverlayThread = new OverlayThread();
	}

	private void setUpView() {
		mTopbarView.setCenterText("品种大全");
		mTopbarView.setLeftView(true, true);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Varietybrowse item = (Varietybrowse) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchVarietyResultActivity.this, SearchVarietyNamedResultActivity.class);
				intent.putExtra("type", item.varietyName);
				startActivity(intent);
			}
		});
	}

//	private void requestData() {
//		startLoadingDialog();
//		RequestService.getInstance().browseCPProductByModel(this, type, PesticideListEntity.class, new RequestListener() {
//			
//			@Override
//			public void onSuccess(int requestCode, BaseEntity resultData) {
//				dismissLoadingDialog();
//				if(resultData.isOk()){
//					PesticideListEntity entity = (PesticideListEntity) resultData;
//					setAdapter(entity.getData());
//				}else{
//					showToast(resultData.getMessage());
//				}
//			}
//			
//			@Override
//			public void onFailed(int requestCode, Exception error, String msg) {
//				dismissLoadingDialog();
//				
//			}
//		});
//	}
	
	private void initOverlay() {
		mHandler = new Handler();
		LayoutInflater inflater = LayoutInflater.from(this);
		mOverlay = (TextView) inflater.inflate(R.layout.contacts_overlay, null);
		mOverlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
		mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(mOverlay, lp);
	}

	@Override
	protected void onDestroy() {
		if (mWindowManager != null) {
			mWindowManager.removeView(mOverlay);
			mWindowManager = null;
		}
		super.onDestroy();
	}

	@Override
	public void finish() {
		try {
			if (mWindowManager != null) {
				mWindowManager.removeView(mOverlay);
				mWindowManager = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.finish();
	}

	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			mOverlay.setVisibility(View.GONE);
		}
	}

	private class letterViewListener implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mListView.setSelection(position);
				mOverlay.setText(s);
				mOverlay.setVisibility(View.VISIBLE);
				mHandler.removeCallbacks(mOverlayThread);
				mHandler.postDelayed(mOverlayThread, 1500);
			}
		}

	}
	
	public static List<Varietybrowse> sortContact(List<Varietybrowse> data) {
		if (data == null) {
			return null;
		}

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(data, new Comparator<Varietybrowse>() {
			@Override
			public int compare(Varietybrowse lhs, Varietybrowse rhs) {

				int lhsascll = lhs.varietyNamePY.charAt(0);
				int rhsascll = rhs.varietyNamePY.charAt(0);

				if (lhsascll < 97 || lhsascll > 122) {
					lhs.varietyNamePY = "~";
				}

				if (rhsascll < 97 || rhsascll > 122) {
					rhs.varietyNamePY = "~";
				}

				return lhs.varietyNamePY.compareTo(rhs.varietyNamePY);
			}
		});

		char mc = '*';
//		List<Varietybrowse> newData = new ArrayList<Varietybrowse>();
		for (int i = 0; i < data.size(); i++) {
			Varietybrowse friend = data.get(i);
			friend.isTitle = false;
			char c = getFirstPy(friend.varietyNamePY);

			if (c < 65 || c > 90) {
				c = '#';
			}

			if (mc != c) {
				mc = c;
//				Varietybrowse f = new Varietybrowse();
//				f.isTitle = true;
//				newData.add(f);
				
				friend.isTitle = true;
				friend.title = String.valueOf(c);
			}
//			newData.add(friend);
		}
		
		return data;
	}
	private static char getFirstPy(String pinyin) {
		if(null == pinyin || pinyin.length() == 0){
			return '#';
		}else{
			return Character.toUpperCase(pinyin.charAt(0));
		}
	}
	
}
