package com.dasinong.app.ui.fragment;

import com.dasinong.app.R;
import com.dasinong.app.ui.ReportHarmActivity;
import com.dasinong.app.ui.SearchResultActivity;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.DeviceHelper;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * @ClassName EncyclopediaFragment
 * @author linmu
 * @Decription 百科
 * @2015-6-9 下午10:30:08
 */
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
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
					
					DeviceHelper.hideIME(mSearchEdit);
					
					String keywords = mSearchEdit.getText().toString().trim();
					if(TextUtils.isEmpty(keywords)){
						Toast.makeText(getActivity(), "请输入要搜索的内容", 0).show();
						return false;
					}
					
					Intent intent = new Intent(getActivity(),SearchResultActivity.class);
					intent.putExtra("keywords", keywords);
					getActivity().startActivity(intent);
					return true;
				}
				return false;
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ask_for:
			Intent intent = new Intent(getActivity(),ReportHarmActivity.class);
			intent.putExtra("title", "诊断病虫草害");
			getActivity().startActivity(intent);
			break;
		case R.id.layout_intelligent:
			showNotifyDialog();
			break;
		}
	}
	
	private void showNotifyDialog() {
		final Dialog dialog = new Dialog(getActivity(), R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText("此功能稍后推出,敬请期待...");
		tv.setTextSize(18);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setText("知道了");
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		View lineView = dialog.findViewById(R.id.view_line);
		lineView.setVisibility(View.GONE);
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setVisibility(View.GONE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

}
