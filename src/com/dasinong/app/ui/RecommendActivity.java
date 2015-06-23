package com.dasinong.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.dasinong.app.R;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

public class RecommendActivity extends BaseActivity {
	
	private ImageView qr_coder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_qrcoder);
		qr_coder = (ImageView) findViewById(R.id.qr_coder);
		
		
		// TODO MING:生成二维码,修改为用户id
		Bitmap bitmap = creatQRCoder("你好世界" , 300);
		
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
