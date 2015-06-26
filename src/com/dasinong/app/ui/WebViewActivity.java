package com.dasinong.app.ui;

import com.dasinong.app.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity {

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		webView = (WebView) findViewById(R.id.wv);
		webView.setWebViewClient(new MyWebViewClient());
		// 得到WebSettings对象，设置支持JavaScript的参数
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		webView.getSettings().setSupportZoom(true);
		// 设置默认缩放方式尺寸是far
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		// 设置出现缩放工具
		webView.getSettings().setBuiltInZoomControls(true);

		String url = getIntent().getStringExtra("url");

		webView.loadUrl("http://" + url);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MyWebViewClient extends WebViewClient {

		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {

			view.loadUrl(url);

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}
	}
}
