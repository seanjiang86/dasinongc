package com.dasinong.app.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.Logger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebBrowserActivity extends BaseActivity {
	protected static final String TAG = WebBrowserActivity.class.getSimpleName();

	public static final String URL = "url";
	public static final String TITLE = "title";

	private String mUrlStr;
	private String mTitleStr = "";
	private WebView mWebView;

	private Map<String, String> mAdditionalHttpHeaders;

	private TopbarView mTopbarView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_browser);

		mUrlStr = this.getIntent().getStringExtra(URL);
		mTitleStr = this.getIntent().getStringExtra(TITLE);

		mWebView = (WebView) this.findViewById(R.id.web_browser);

		setTopbar();
		initHttpHeaders();
		openUrl(mUrlStr);
	}

	private void initHttpHeaders() {
		mAdditionalHttpHeaders = new HashMap<String, String>();
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void openUrl(String url) {
		WebSettings settings = mWebView.getSettings();
		// mWebView.addJavascriptInterface(jsInterface, "JSInterface");
		// settings.setJavaScriptCanOpenWindowsAutomatically(true);
		// settings.setBlockNetworkImage(true);
		// settings.setBlockNetworkLoads(true);
		settings.setDomStorageEnabled(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setJavaScriptEnabled(true); // js
		// settings.setPluginsEnabled(true); // 支持插件
		// settings.setUserAgent(0); // 0为手机默认, 1为PC台机，2为IPHONE
		settings.setDefaultTextEncodingName("utf-8");
		settings.setSupportZoom(true);// 缩放

//		settings.setUseWideViewPort(true);
//		settings.setLoadWithOverviewMode(true);
		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		settings.setBuiltInZoomControls(true);// 支持手势缩放
		settings.setDisplayZoomControls(false);// 支持手势缩放
		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Logger.d1("mWebView", "url:" + url);

				if (url != null) {
					if (url.length() > 3) {
						if (url.substring(0, 4).equals("tel:")) {
							return false;
						}
					}
				}

				view.loadUrl(url, mAdditionalHttpHeaders);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				try {
					startLoadingDialog();
				} catch (Exception e) {
					Logger.e1(url, e.getMessage());
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dismissLoadingDialog();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);

			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				super.onReceivedSslError(view, handler, error);
				
				// TODO 
				dismissLoadingDialog();
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
				// TODO

				super.onReceivedHttpAuthRequest(view, handler, host, realm);

			}

		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				result.confirm();
				return super.onJsAlert(view, url, message, result);
			}
		});

		mWebView.loadUrl(url, mAdditionalHttpHeaders);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mWebView.loadUrl(mUrlStr, mAdditionalHttpHeaders);
			// openUrl(mUrlStr);
			// mWebView.reload();
		}
	}

	private void setTopbar() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mTopbarView.setCenterText(mTitleStr);
		mTopbarView.setLeftView(true, true);
	}

	private void callHiddenWebViewMethod(String name) {
		if (mWebView != null) {
			try {
				Method method = WebView.class.getMethod(name);
				method.invoke(mWebView);
			} catch (NoSuchMethodException e) {
				Logger.e1("No such method: " + name, e);
			} catch (IllegalAccessException e) {
				Logger.e1("Illegal Access: " + name, e);
			} catch (InvocationTargetException e) {
				Logger.e1("Invocation Target Exception: " + name, e);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		this.callHiddenWebViewMethod("onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		this.callHiddenWebViewMethod("onResume");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}

	@Override
	public void onBackPressed() {
		boolean isBack = false;
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			isBack = true;
		}
		if (!isBack) {
			super.onBackPressed();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.stopLoading();
			mWebView.destroy();
		}
	}

}
