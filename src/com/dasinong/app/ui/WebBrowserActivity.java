package com.dasinong.app.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.LayoutFocusTraversalPolicy;

import com.dasinong.app.R;
import com.dasinong.app.jstojavainteface.JsInterface;
import com.dasinong.app.jstojavainteface.JsInterface.WebViewClientClickListener;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.Logger;
import com.dasinong.app.utils.Logger.LogTag;
import com.lidroid.xutils.BitmapUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WebBrowserActivity extends BaseActivity {
	protected static final String TAG = WebBrowserActivity.class.getSimpleName();

	public static final String URL = "url";
	public static final String TITLE = "title";

	private String mUrlStr;
	private String mTitleStr = "";
	private WebView mWebView;

	private Map<String, String> mAdditionalHttpHeaders;

	private TopbarView mTopbarView;

	private LinearLayout ll_error_page;

	private RelativeLayout rl_web_view;

	private JsInterface jsInterface = new JsInterface();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_browser);

		mUrlStr = this.getIntent().getStringExtra(URL);

		System.out.println(mUrlStr);

		mTitleStr = this.getIntent().getStringExtra(TITLE);

		mWebView = (WebView) this.findViewById(R.id.web_browser);
		ll_error_page = (LinearLayout) findViewById(R.id.ll_error_page);
		rl_web_view = (RelativeLayout) findViewById(R.id.rl_web_view);

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

		// settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		settings.setBuiltInZoomControls(true);// 支持手势缩放
		settings.setDisplayZoomControls(false);// 支持手势缩放
		//指定浏览器不是用缓存
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		mWebView.addJavascriptInterface(jsInterface, "androidObj");
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

				jsInterface.setWebViewClientClickListener(new WebViewClientClickListener() {

					@Override
					public void webViewHasClickEnvent(final String... images) {
						runOnUiThread(new Runnable() {
							public void run() {
								showPopupWindow(images);
							}
						});
					}
				});
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				ll_error_page.setVisibility(View.VISIBLE);
				rl_web_view.setVisibility(View.GONE);
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
				dismissLoadingDialog();
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
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

	// 弹出窗口显示 图片
	private void showPopupWindow(final String... images) {
		View contentView = View.inflate(this, R.layout.popupwindow_show_pic, null);
		ViewPager vp = (ViewPager) contentView.findViewById(R.id.vp_pic);
		final TextView tv_count = (TextView) contentView.findViewById(R.id.tv_count);
		final PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		vp.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				return images.length;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = View.inflate(WebBrowserActivity.this, R.layout.pop_pic_item, null);
				ImageView iv = (ImageView) view.findViewById(R.id.iv);
				LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
				BitmapUtils bitmapUtils = new BitmapUtils(WebBrowserActivity.this);
				bitmapUtils.display(iv, "http://120.26.208.198:8080/" + images[position].replace("../", ""));

				ll.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
					}
				});

				container.addView(view);
				return view;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}
		});

		tv_count.setText("1" + "/" + images.length);

		vp.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				tv_count.setText((arg0 + 1) + "/" + images.length);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pic_bg));

		popupWindow.showAsDropDown(View.inflate(this, R.layout.activity_web_browser, null));
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
