package com.dasinong.app.ui;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.dasinong.app.R;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.view.TopbarView;
import com.lidroid.xutils.HttpUtils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterServiceActivity extends BaseActivity {

	private TextView mServiceText;

	private WebView mWebView;

	private TopbarView mTopbarView;

	private LinearLayout ll_error_page;
	
	private HttpClient client;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:
				loadErrorPager();
				break;
			case 2:
				String content = (String) msg.obj;
				loadPager(content);
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_service);

		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);

		mServiceText = (TextView) this.findViewById(R.id.textview_text);

		mWebView = (WebView) this.findViewById(R.id.webview);
		ll_error_page = (LinearLayout) findViewById(R.id.ll_error_page);

		String title = getIntent().getStringExtra("title");
		String url = getIntent().getStringExtra("url");

		mTopbarView.setCenterText(title);
		mTopbarView.setLeftView(true, true);

		// 是否允许缩放
		mWebView.getSettings().setSupportZoom(true);
		// 设置默认缩放方式尺寸是far
		mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		// 设置出现缩放工具
		mWebView.getSettings().setBuiltInZoomControls(false);
		// 自定义错误页面
		
		// TODO MING 打开的地址要动态传入，此处缺少 register.html
		switch (title) {
		case "为什么要测土":
			getCodeAndContent("file:///android_asset/" + url);
//			mWebView.loadUrl("file:///android_asset/" + url);
			break;
		case "采样须知":
			getCodeAndContent("file:///android_asset/" + url);
//			mWebView.loadUrl("file:///android_asset/" + url);
			break;
		case "测土报告解读":
			// TODO MING:待确定链接地址
			getCodeAndContent("http://192.168.1.136:8080/ploughHelper/html/" + url);
//			mWebView.loadUrl(NetConfig.BASE_URL + "jsp/html/" + url);
			break;
		case "哪里可以测土？":
			getCodeAndContent("http://www.baidu.com");
//			mWebView.loadUrl("file:///android_asset/" + url);
			break;
		case "服务协议条款":
			getCodeAndContent("file:///android_asset/" + url);
//			mWebView.loadUrl("file:///android_asset/" + url);
			break;
		}
	}
	
	private void getCodeAndContent(final String newUrl){
		
		if(client == null){
			client = new DefaultHttpClient();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpGet httpGet = new HttpGet(newUrl);
				try {
					HttpResponse response = client.execute(httpGet);
					int statusCode = response.getStatusLine().getStatusCode();
					System.out.println(statusCode);
					String strStatusCode = String.valueOf(statusCode);
					if(strStatusCode.startsWith("4") || strStatusCode.startsWith("5")){
						Message msg = handler.obtainMessage();
						msg.what = 1;
						handler.sendMessage(msg);
						return;
					}
					HttpEntity entity = response.getEntity();
					String content = EntityUtils.toString(entity, "utf-8");
					System.out.println(content);
					Message msg = handler.obtainMessage();
					msg.what = 2;
					msg.obj = content;
					handler.sendMessage(msg);
				} catch (Exception e) {
					loadErrorPager();
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void loadPager(String content) {
		mWebView.setVisibility(View.VISIBLE);
		ll_error_page.setVisibility(View.GONE);
		mWebView.loadData(content, "text/html",  "utf-8");
	}

	private void loadErrorPager() {
		mWebView.setVisibility(View.GONE);
		ll_error_page.setVisibility(View.VISIBLE);
	}
}
