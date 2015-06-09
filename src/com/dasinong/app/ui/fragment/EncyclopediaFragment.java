package com.dasinong.app.ui.fragment;

import com.dasinong.app.R;
import com.dasinong.app.ui.SearchResultActivity;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class EncyclopediaFragment extends Fragment implements OnClickListener{

	private View mContentView;

	private TopbarView mTopbarView;

	private EditText mSearchEdit;
	private RelativeLayout mAskforLayout;
	private RelativeLayout mIntelligentLayout;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_encyclopedia, null);

			initView();
			setUpView();

		} else {
			ViewGroup parent = (ViewGroup) mContentView.getParent();
			if (parent != null) {
				parent.removeView(mContentView);
			}
		}
		return mContentView;
	}


	private void initView() {
		mTopbarView = (TopbarView) mContentView.findViewById(R.id.topbar);
		mSearchEdit = (EditText) mContentView.findViewById(R.id.edittext_search);
		mAskforLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_ask_for);
		mIntelligentLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_intelligent);
	}

	private void setUpView() {
		
		mTopbarView.setCenterText("百科");
		
		mAskforLayout.setOnClickListener(this);
		mIntelligentLayout.setOnClickListener(this);
		
		mSearchEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if(keyCode == KeyEvent.KEYCODE_ENTER){
//					
//					DeviceHelper.hideIME(mSearchEdit);
//					
//					String keywords = mSearchEdit.getText().toString().trim();
//					if(TextUtils.isEmpty(keywords)){
//						Toast.makeText(getActivity(), "请输入要搜索的内容", 0).show();
//						return false;
//					}
//					
//					Intent intent = new Intent(getActivity(),SearchResultActivity.class);
//					intent.putExtra("keywords", keywords);
//					getActivity().startActivity(intent);
//					return true;
//				}
				return false;
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ask_for:
			
			break;
		case R.id.layout_intelligent:
			
			break;
		}
	}

	
	protected void checkUpdate() {
		
	}
	
}
