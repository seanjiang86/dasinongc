package com.dasinong.app.ui;

import java.util.logging.LogRecord;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.GraphicUtils;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

public class RecommendActivity extends BaseActivity implements OnClickListener {

	private TopbarView topBar;
	private TextView tv_my_invitation_code;
	private EditText et_organization_code;
	private Button btn_sure_organization_code;
	private TextView tv_froget_code;
	private EditText et_other_invitation_code;
	private Button btn_sure_invitation_code;
	private View ll_fill_code;
	private View ll_not_fill_code;
	private TextView tv_my_code;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_qrcoder);
		topBar = (TopbarView) findViewById(R.id.topbar);

		initTopBar();

		tv_my_invitation_code = (TextView) findViewById(R.id.tv_my_invitation_code);
		et_organization_code = (EditText) findViewById(R.id.et_organization_code);
		btn_sure_organization_code = (Button) findViewById(R.id.btn_sure_organization_code);
		tv_froget_code = (TextView) findViewById(R.id.tv_froget_code);
		et_other_invitation_code = (EditText) findViewById(R.id.et_other_invitation_code);
		btn_sure_invitation_code = (Button) findViewById(R.id.btn_sure_invitation_code);
		ll_not_fill_code = findViewById(R.id.ll_not_fill_code);
		ll_fill_code = findViewById(R.id.ll_fill_code);
		tv_my_code = (TextView) findViewById(R.id.tv_my_code);

		String refCode = SharedPreferencesHelper.getString(this, Field.REFCODE, "");
		int refuId = SharedPreferencesHelper.getInt(this, Field.REFUID, -1);
		
		if (refuId > 0) {
			ll_not_fill_code.setVisibility(View.GONE);
			ll_fill_code.setVisibility(View.VISIBLE);
			tv_my_code.setText(refCode);
			return;
		} else {
			ll_not_fill_code.setVisibility(View.VISIBLE);
			ll_fill_code.setVisibility(View.GONE);
		}
		
		if (!TextUtils.isEmpty(refCode)) {
			tv_my_invitation_code.setText(String.format("您的专属邀请码：%s", refCode));
		}
		btn_sure_organization_code.setOnClickListener(this);
		tv_froget_code.setOnClickListener(this);
		btn_sure_invitation_code.setOnClickListener(this);
	}

	private void initTopBar() {
		topBar.setCenterText("有奖推荐");
		topBar.setLeftView(true, true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_sure_organization_code:

			String orgCode = et_organization_code.getText().toString().trim();
			orgCode = orgCode.toLowerCase();
			// 老正则，可以过滤纯字母或者纯数字
			// String orgRegex = "^(?![0-9]+$)(?![a-z]+$)[0-9a-z]{4}$" ;
			String orgRegex = "^[a-z]{4}$";
			if (!TextUtils.isEmpty(orgCode) && orgCode.matches(orgRegex)) {
				startLoadingDialog();
				RequestService.getInstance().setRef(this, orgCode, LoginRegEntity.class, new RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							LoginRegEntity entity = (LoginRegEntity) resultData;
							showToast("验证成功");
							SharedPreferencesHelper.setInt(RecommendActivity.this, Field.REFUID, entity.getData().getRefuid());
							dismissLoadingDialog();

							finish();
						}
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						showToast(msg);
					}
				});
			} else {
				showToast("请核对您的机构代码是否确");
			}
			break;
		case R.id.tv_froget_code:
			showToast("忘了如何找回？");
			break;
		case R.id.btn_sure_invitation_code:

			String invCode = et_other_invitation_code.getText().toString().trim();
			invCode = invCode.toLowerCase();
			String invRegex = "^[0-9a-z]{6}$";
			if (!TextUtils.isEmpty(invCode) && invCode.matches(invRegex)) {
				startLoadingDialog();
				RequestService.getInstance().setRef(this, invCode, LoginRegEntity.class, new RequestListener() {
					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							LoginRegEntity entity = (LoginRegEntity) resultData;
							showToast("验证成功");
							SharedPreferencesHelper.setInt(RecommendActivity.this, Field.REFUID, entity.getData().getRefuid());
							finish();
						} else {
							showToast(resultData.getMessage());
						}

						dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						showToast("错误信息   ： " + msg);

						dismissLoadingDialog();
					}
				});
			} else {
				showToast("请核对邀请码是否确");
			}

			break;
		}
	}

}
