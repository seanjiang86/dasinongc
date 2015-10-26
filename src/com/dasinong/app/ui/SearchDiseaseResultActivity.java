package com.dasinong.app.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.PetdisspecbrowseDao;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.PetDisSpecsListEntity;
import com.dasinong.app.entity.PetDisSpecsListEntity.Data;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.DiseaseListAdapter;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchDiseaseResultActivity extends BaseActivity {

	private ListView mListview;

	private Handler mHandler = new Handler();

	private String type;
	private String cropId;

	private LetterView letterView;
	private HashMap<String, Integer> alphaIndexer;
	private OverlayThread mOverlayThread;
	private WindowManager mWindowManager;
	private TextView mOverlay;

	protected List<Petdisspecbrowse> query;

	private TopbarView mTopbarView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		// TODO MING : CONTENUE
		
		PetDisSpecsListEntity.Data data = (Data) getIntent().getSerializableExtra("data");

		type = getIntent().getStringExtra("type");
		cropId = getIntent().getStringExtra("cropId");

		initView();
		setUpView();
		initOverlay();
		initData();
//		requestData();

	}

	private void initData() {

		startLoadingDialog();
		new Thread() {
			public void run() {
//				PetdisspecbrowseDao dao = new PetdisspecbrowseDao(SearchDiseaseResultActivity.this);
//				if ("草害".equals(type)) {
//					query = dao.queryCaohai(type, cropId);
//				} else {
//					query = dao.query(type, cropId);
//				}
				
				
				query = sortContact(query);

				alphaIndexer.clear();
				for (int i = 0; i < query.size(); i++) {
					Petdisspecbrowse entity = query.get(i);
					if (entity.isTitle) {
						alphaIndexer.put(entity.title, i);
					}
				}
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						dismissLoadingDialog();
						setAdapter(query);
					}
				});
			};

		}.start();
	}

	protected void setAdapter(List<Petdisspecbrowse> query) {
		DiseaseListAdapter adapter = new DiseaseListAdapter(this, query, false);
		mListview.setAdapter(adapter);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);

		mListview = (ListView) this.findViewById(R.id.list_sms_list);

		letterView = (LetterView) findViewById(R.id.letterview);
		letterView.setOnTouchingLetterChangedListener(new letterViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		mOverlayThread = new OverlayThread();
	}

	private void setUpView() {
		mTopbarView.setCenterText(type);
		mTopbarView.setLeftView(true, true);

		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Petdisspecbrowse item = (Petdisspecbrowse) parent.getItemAtPosition(position);
				Intent intent = new Intent(SearchDiseaseResultActivity.this, WebBrowserActivity.class);
				intent.putExtra(WebBrowserActivity.URL, NetConfig.getBaikeUrl("pest", item.petDisSpecId + ""));
				intent.putExtra(WebBrowserActivity.TITLE, Html.fromHtml(item.petDisSpecName).toString());
				startActivity(intent);
			}
		});
	}

//	private void requestData() {
//		RequestService.getInstance().browsePetDisByType(this, "虫害", BaseEntity.class, new RequestListener() {
//
//			@Override
//			public void onSuccess(int requestCode, BaseEntity resultData) {
//
//			}
//
//			@Override
//			public void onFailed(int requestCode, Exception error, String msg) {
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
				mListview.setSelection(position);
				mOverlay.setText(s);
				mOverlay.setVisibility(View.VISIBLE);
				mHandler.removeCallbacks(mOverlayThread);
				mHandler.postDelayed(mOverlayThread, 1500);
			}
		}

	}

	public static List<Petdisspecbrowse> sortContact(List<Petdisspecbrowse> data) {
		if (data == null) {
			return null;
		}

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(data, new Comparator<Petdisspecbrowse>() {
			@Override
			public int compare(Petdisspecbrowse lhs, Petdisspecbrowse rhs) {

				int lhsascll = lhs.petDisSpecNamePY.charAt(0);
				int rhsascll = rhs.petDisSpecNamePY.charAt(0);

				if (lhsascll < 97 || lhsascll > 122) {
					lhs.petDisSpecNamePY = "~";
				}

				if (rhsascll < 97 || rhsascll > 122) {
					rhs.petDisSpecNamePY = "~";
				}

				return lhs.petDisSpecNamePY.compareTo(rhs.petDisSpecNamePY);
			}
		});

		char mc = '*';
		// List<Petdisspecbrowse> newData = new ArrayList<Petdisspecbrowse>();
		for (int i = 0; i < data.size(); i++) {
			Petdisspecbrowse friend = data.get(i);
			friend.isTitle = false;
			char c = getFirstPy(friend.petDisSpecNamePY);

			if (c < 65 || c > 90) {
				c = '#';
			}

			if (mc != c) {
				mc = c;
				// Petdisspecbrowse f = new Petdisspecbrowse();
				// f.isTitle = true;
				// newData.add(f);

				friend.isTitle = true;
				friend.title = String.valueOf(c);
			}
			// newData.add(friend);
		}

		return data;
	}

	private static char getFirstPy(String pinyin) {
		if (null == pinyin || pinyin.length() == 0) {
			return '#';
		} else {
			return Character.toUpperCase(pinyin.charAt(0));
		}
	}

}
