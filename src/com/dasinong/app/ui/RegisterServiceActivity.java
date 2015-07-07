package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterServiceActivity extends BaseActivity {

	private TextView mServiceText;
	
	private WebView mWebView;

	private TopbarView mTopbarView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register_service);
		
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		
		mServiceText = (TextView) this.findViewById(R.id.textview_text);
		
		mWebView = (WebView) this.findViewById(R.id.webview);

		
		String title = getIntent().getStringExtra("title");
		String url = getIntent().getStringExtra("url");
		
		
		mTopbarView.setCenterText(title);
		mTopbarView.setLeftView(true, true);
		
		//是否允许缩放
		mWebView.getSettings().setSupportZoom(true);
		// 设置默认缩放方式尺寸是far
		mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		// 设置出现缩放工具
		mWebView.getSettings().setBuiltInZoomControls(false);
		
		// TODO MING 打开的地址要动态传入，此处缺少 register.html
		mWebView.loadUrl("file:///android_asset/"+url);
		
	}
	
}
