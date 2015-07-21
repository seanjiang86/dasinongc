package com.dasinong.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.dasinong.app.R;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

public class RecommendActivity extends BaseActivity {
	
	private ImageView qr_coder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_qrcoder);
		qr_coder = (ImageView) findViewById(R.id.qr_coder);
		
		// TODO MING 使用真实下载链接,此地不对
		
		String userId = SharedPreferencesHelper.getString(this, Field.USER_ID, "");
		
		showToast("修改真实链接"+userId);
		
		Bitmap bitmap = creatQRCoder(userId , 300);
		
		qr_coder.setImageBitmap(bitmap);
	}

	private Bitmap creatQRCoder(String url , int size) {
		try {
			return EncodingHandler.createQRCode(url,300);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		// TODO MING:容错处理如果发生异常怎么处理
		return null;
	}
}
