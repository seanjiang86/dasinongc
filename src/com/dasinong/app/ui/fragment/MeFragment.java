package com.dasinong.app.ui.fragment;

import com.dasinong.app.R;
import com.dasinong.app.ui.MyInfoActivity;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.SmsSubscribeActivity;
import com.dasinong.app.ui.TaskDetailsActivity;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName MeFragment
 * @author linmu
 * @Decription 我
 * @2015-6-9 下午10:29:55
 */
public class MeFragment extends Fragment implements OnClickListener {

	private View mContentView;

	private TopbarView mTopbarView;

	private RelativeLayout mMyInfoLayout;
	private RelativeLayout mScancodeLayout;
	private RelativeLayout mRecomentLayout;
	private RelativeLayout mSmsSettingLayout;
	private RelativeLayout mHelpLayout;
	private RelativeLayout mUseLayout;
	private RelativeLayout mContactUsLayout;
	private RelativeLayout mCheckUpdateLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_my, null);

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

		mMyInfoLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_my_info);
		mScancodeLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_scancode);
		mRecomentLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_recommend);
		mSmsSettingLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_sms_setting);
		mHelpLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_help_center);
		mUseLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_use);
		mContactUsLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_contact_us);
		mCheckUpdateLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_check_update);
	}

	private void setUpView() {

		mTopbarView.setCenterText("我");

		mMyInfoLayout.setOnClickListener(this);
		mScancodeLayout.setOnClickListener(this);
		mRecomentLayout.setOnClickListener(this);
		mSmsSettingLayout.setOnClickListener(this);
		mHelpLayout.setOnClickListener(this);
		mUseLayout.setOnClickListener(this);
		mContactUsLayout.setOnClickListener(this);
		mCheckUpdateLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_my_info:// 个人信息
//			Intent myInfoIntent = new Intent(getActivity(),MyInfoActivity.class);
//			getActivity().startActivity(myInfoIntent);
			break;
		case R.id.layout_scancode:// 扫一扫

			break;
		case R.id.layout_recommend:// 有奖推荐

			break;
		case R.id.layout_sms_setting:// 短信订阅设置
//			Intent smsIntent = new Intent(getActivity(),SmsSubscribeActivity.class);
//			getActivity().startActivity(smsIntent);
			break;
		case R.id.layout_help_center:// 帮助中心

			break;
		case R.id.layout_use:// 使用教程

			break;
		case R.id.layout_contact_us:// 联系我们

			break;
		case R.id.layout_check_update:// 检查更新

			break;
		}
	}

	protected void checkUpdate() {

	}

}
