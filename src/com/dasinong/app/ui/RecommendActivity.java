package com.dasinong.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.GraphicUtils;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

public class RecommendActivity extends BaseActivity {
	
	private ImageView qr_coder;
	private TopbarView topBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_qrcoder);
		qr_coder = (ImageView) findViewById(R.id.qr_coder);
		topBar = (TopbarView) findViewById(R.id.topbar);
		
		initTopBar();
		
		// TODO MING 使用真实下载链接,此地不对
		
		String userId = SharedPreferencesHelper.getString(this, Field.USER_PHONE, "");
		
		Bitmap bitmap = creatQRCoder("http://a.app.qq.com/o/simple.jsp?pkgname=com.dasinong.app" , 300);
		
		qr_coder.setImageBitmap(bitmap);
	}

	private void initTopBar() {
		topBar.setCenterText("有奖推荐");
		topBar.setLeftView(true, true);
	}

	private Bitmap creatQRCoder(String url , int size) {
		try {
			return EncodingHandler.createQRCode(url,GraphicUtils.dip2px(this, 300));
		} catch (WriterException e) {
			e.printStackTrace();
		}
		// TODO MING:容错处理如果发生异常怎么处理
		return null;
	}
}
