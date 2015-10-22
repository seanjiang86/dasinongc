package com.dasinong.app.ui.fragment;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ming on 2015/10/20.
 */
public class RecommendFragment extends Fragment {
	private int fragmentPosition;
	private EditText et_input_recommend_code;
	private Button btn_sure;
	private EditText et_organization_code;
	private Button btn_sure_organization_code;
	private TextView tv_froget_code;
	private TextView tv_recommend_code;
	private EditText et_phone;
	private Button btn_send;
	private String refCode;
	private LinearLayout ll_organization;
	private int refuId;

	public static RecommendFragment newInstance(int position , int refuId) {
		RecommendFragment myFragment = new RecommendFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putInt("refuId", refuId);
		myFragment.setArguments(bundle);
		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentPosition = getArguments() != null ? getArguments().getInt("position") : 0;
		refuId = getArguments() != null ? getArguments().getInt("refuId") : -1;
		
		refCode = SharedPreferencesHelper.getString(getActivity(), Field.REFCODE, "");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		if (fragmentPosition == 1) {
			view = View.inflate(getActivity(), R.layout.fragment_recommended, null);
			et_input_recommend_code = (EditText) view.findViewById(R.id.et_input_recommend_code);
			btn_sure = (Button) view.findViewById(R.id.btn_sure);
			
			btn_sure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String invCode = et_input_recommend_code.getText().toString().trim();
					invCode = invCode.toLowerCase();
					String invRegex = "^[0-9a-z]{6}$";
					if (!TextUtils.isEmpty(invCode) && invCode.matches(invRegex)) {
						((BaseActivity) getActivity()).startLoadingDialog();
						RequestService.getInstance().setRef(getActivity(), invCode, LoginRegEntity.class, new RequestListener() {
							@Override
							public void onSuccess(int requestCode, BaseEntity resultData) {
								if (resultData.isOk()) {
									LoginRegEntity entity = (LoginRegEntity) resultData;
									((BaseActivity) getActivity()).showToast("验证成功");
									SharedPreferencesHelper.setInt(getActivity(), Field.REFUID, entity.getData().getRefuid());
									getActivity().finish();
								} else {
									((BaseActivity) getActivity()).showToast(resultData.getMessage());
								}

								((BaseActivity) getActivity()).dismissLoadingDialog();
							}

							@Override
							public void onFailed(int requestCode, Exception error, String msg) {
								((BaseActivity) getActivity()).showToast(msg);

								((BaseActivity) getActivity()).dismissLoadingDialog();
							}
						});
					} else {
						((BaseActivity) getActivity()).showToast("请核对邀请码是否确");
					}
				}
			});
			
			
		} else {
			view = View.inflate(getActivity(), R.layout.fragment_recommend, null);
			ll_organization = (LinearLayout) view.findViewById(R.id.ll_organization);
			et_organization_code = (EditText) view.findViewById(R.id.et_organization_code);
			btn_sure_organization_code = (Button) view.findViewById(R.id.btn_sure_organization_code);
			tv_froget_code = (TextView) view.findViewById(R.id.tv_froget_code);
			tv_recommend_code = (TextView) view.findViewById(R.id.tv_recommend_code);
			et_phone = (EditText) view.findViewById(R.id.et_phone);
			btn_send = (Button) view.findViewById(R.id.btn_send);
			
			if(refuId > 0){
				
				ll_organization.setVisibility(View.GONE);
			}
			
			if (!TextUtils.isEmpty(refCode)) {
				tv_recommend_code.setText(refCode);
			}
			setViewClick();
		}


		return view;
	}

	private void setViewClick() {
		btn_sure_organization_code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String orgCode = et_organization_code.getText().toString().trim();
				orgCode = orgCode.toLowerCase();
				// 老正则，可以过滤纯字母或者纯数字
				// String orgRegex = "^(?![0-9]+$)(?![a-z]+$)[0-9a-z]{4}$" ;
				String orgRegex = "^[a-z]{4}$";
				if (!TextUtils.isEmpty(orgCode) && orgCode.matches(orgRegex)) {
					((BaseActivity) getActivity()).startLoadingDialog();
					RequestService.getInstance().setRef(getActivity(), orgCode, LoginRegEntity.class, new RequestListener() {

						@Override
						public void onSuccess(int requestCode, BaseEntity resultData) {
							if (resultData.isOk()) {
								LoginRegEntity entity = (LoginRegEntity) resultData;
								((BaseActivity) getActivity()).showToast("验证成功");
								SharedPreferencesHelper.setInt(getActivity(), Field.REFUID, entity.getData().getRefuid());
								((BaseActivity) getActivity()).dismissLoadingDialog();
								ll_organization.setVisibility(View.GONE);
								getActivity().finish();
							} else {
								((BaseActivity)getActivity()).showToast(resultData.getMessage());
								((BaseActivity)getActivity()).dismissLoadingDialog();
							}
						}
						@Override
						public void onFailed(int requestCode, Exception error, String msg) {
							((BaseActivity) getActivity()).showToast(msg);
							((BaseActivity)getActivity()).dismissLoadingDialog();
						}
					});
				} else {
					((BaseActivity) getActivity()).showToast("请核对机构代码是否确");
				}
			}
		});

		tv_froget_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((BaseActivity) getActivity()).showToast("机构代码为四位数英文字母，如果遗失请联系公司项目负责人，不要随便填写其他公司的代码哦");
			}
		});

		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String regExp = "^1[3|4|5|8][0-9]\\d{8}$";
				String phone = et_phone.getText().toString().trim();
				if (!TextUtils.isEmpty(phone) && phone.matches(regExp)) {
					((BaseActivity) getActivity()).startLoadingDialog();
					RequestService.getInstance().refApp(getActivity(), phone, BaseEntity.class, new RequestListener() {

						@Override
						public void onSuccess(int requestCode, BaseEntity resultData) {
							if (resultData.isOk()) {
								((BaseActivity) getActivity()).showToast("邀请短信已发出");
								((BaseActivity) getActivity()).dismissLoadingDialog();
							} else {
								((BaseActivity) getActivity()).showToast(resultData.getMessage());
								((BaseActivity) getActivity()).dismissLoadingDialog();
							}
						}

						@Override
						public void onFailed(int requestCode, Exception error, String msg) {
							((BaseActivity) getActivity()).showToast("网络连接异常，请检测网络连接");
							((BaseActivity) getActivity()).dismissLoadingDialog();
						}
					});
				} else {
					((BaseActivity) getActivity()).showToast("请输入正确的手机号");
				}
			}
		});
	}
}
