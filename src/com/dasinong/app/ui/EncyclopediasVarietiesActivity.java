package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.EncyclopediasDao;
import com.dasinong.app.database.encyclopedias.domain.Crop;
import com.dasinong.app.ui.adapter.VarietiesFirstListAdapter;
import com.dasinong.app.ui.adapter.VarietiesSecondListAdapter;
import com.dasinong.app.ui.view.LetterView;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.LetterView.OnTouchingLetterChangedListener;
import com.dasinong.app.utils.HanziToPingyin;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @ClassName EncyclopediasVarietiesActivity
 * @author linmu
 * @Decription 品种大全
 * @2015-7-21 下午9:18:30
 */
public class EncyclopediasVarietiesActivity extends BaseActivity {

	private ListView mFirstList;
	private ListView mSecondList;
	
	private Handler mHandler = new Handler();
	
	private EditText mSearchEdit;
	private ImageView mSearchView;
	private TopbarView mTopbarView;
	
	private LetterView letterView;
	private HashMap<String, Integer> alphaIndexer;
	private OverlayThread mOverlayThread;
	private WindowManager mWindowManager;
	private TextView mOverlay;
	protected List<Crop> query;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_varieties);
		
		initView();
		setUpView();
		initOverlay();
		initData("粮食作物");
	}

	private void initData(final String type) {
		
		startLoadingDialog();
		new Thread(){
			public void run() {
				EncyclopediasDao dao = new EncyclopediasDao(EncyclopediasVarietiesActivity.this);
				
				query = dao.queryStageCategory(type);
				query = sortContactTitle(query);

				alphaIndexer.clear();
				for (int i = 0; i < query.size(); i++) {
					Crop entity = query.get(i);
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

	private void setAdapter(List<Crop> queryStageCategory) {
		VarietiesSecondListAdapter adapter = new VarietiesSecondListAdapter(this, queryStageCategory, false);
		mSecondList.setAdapter(adapter);
	}
	
	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		
		mFirstList = (ListView) this.findViewById(R.id.listview_type_list_first);
		mSecondList = (ListView) this.findViewById(R.id.listview_type_list_second);
		mSearchView = (ImageView) this.findViewById(R.id.imageview_search);
		mSearchEdit = (EditText) this.findViewById(R.id.edittext_search);
		
		letterView = (LetterView) findViewById(R.id.letterview);
		letterView.setOnTouchingLetterChangedListener(new letterViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		mOverlayThread = new OverlayThread();
	}

	private void setUpView() {
		
		mTopbarView.setCenterText("品种大全");
		mTopbarView.setLeftView(true, true);
		
		final VarietiesFirstListAdapter adapter = new VarietiesFirstListAdapter(this, null, false);
		mFirstList.setAdapter(adapter);
		
		mFirstList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.setSelectPosition(position);
				String type = (String) mFirstList.getItemAtPosition(position);
				initData(type);
			}
		});
		
		mSecondList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Crop crop = (Crop) parent.getItemAtPosition(position);
				Intent intent = new Intent(EncyclopediasVarietiesActivity.this,SearchVarietyResultActivity.class);
				intent.putExtra("type", crop.cropId+"");
				startActivity(intent);
			}
		});
		
		
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
		
		Intent intent = new Intent(this,SearchTypeResultActivity.class);
		intent.putExtra("keywords", keywords);
		intent.putExtra("type", "variety");
		this.startActivity(intent);
	}
	
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
				mSecondList.setSelection(position);
				mOverlay.setText(s);
				mOverlay.setVisibility(View.VISIBLE);
				mHandler.removeCallbacks(mOverlayThread);
				mHandler.postDelayed(mOverlayThread, 1500);
			}
		}

	}
	
	public static List<Crop> sortContactTitle(List<Crop> data) {
		if (data == null) {
			return null;
		}

		for (Crop userInfoEntity : data) {
			if (TextUtils.isEmpty(userInfoEntity.pinyin)) {
				String name = userInfoEntity.cropName;
				String pinyin = com.dasinong.app.ui.view.HanziToPingyin.getPinYin(name);
				pinyin = TextUtils.isEmpty(pinyin) ? "~" : pinyin;
				userInfoEntity.pinyin = pinyin;
			}
		}

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(data, new Comparator<Crop>() {
			@Override
			public int compare(Crop lhs, Crop rhs) {

				int lhsascll = lhs.pinyin.charAt(0);
				int rhsascll = rhs.pinyin.charAt(0);

				if (lhsascll < 97 || lhsascll > 122) {
					lhs.pinyin = "~" ;
				}

				if (rhsascll < 97 || rhsascll > 122) {
					rhs.pinyin = "~";
				}

				return lhs.pinyin.compareTo(rhs.pinyin);
			}
		});

		char mc = '*';
//		List<Crop> newData = new ArrayList<Crop>();
		for (int i = 0; i < data.size(); i++) {
			Crop friend = data.get(i);
			friend.isTitle = false;
			char c = HanziToPingyin.getFirstPinYinChar(friend.pinyin);
			// char c = HanziToPingyin
			// .getFirstPinYinChar(TextUtils.isEmpty(friend.getFriendMark().getRemarkName())
			// ? friend.getName()
			// : friend.getFriendMark().getRemarkName());

			if (c < 65 || c > 90) {
				c = '#';
			}

			if (mc != c) {
				mc = c;
//				Crop f = new Crop();
				friend.isTitle = true;
				friend.title = String.valueOf(c);
//				newData.add(f);
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
