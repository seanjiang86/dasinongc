package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.webkit.WebView;
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
		
		mTopbarView.setCenterText("服务协议条款");
		mTopbarView.setLeftView(true, true);
		
		mServiceText = (TextView) this.findViewById(R.id.textview_text);
		
		mWebView = (WebView) this.findViewById(R.id.webview);
		mWebView.loadUrl("file:///android_asset/register.html");
		
	}
	
}
