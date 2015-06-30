package com.dasinong.app.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dasinong.app.R;
import com.dasinong.app.utils.Logger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends BaseActivity {

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		webView = (WebView) findViewById(R.id.wv);
		// 设置web视图客户端
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
		// 设置下载监听事件
		webView.setDownloadListener(new MyWebViewDownLoadListener());

	}

	/**
	 * 点击返回按钮返回上一个页面而不是退出该webView
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 如果页面中链接，如果希望点击链接继续在当前browser中响应， 而不是新开Android的系统browser中响应该链接，必须覆盖
	 * webview的WebViewClient对象。
	 */
	class MyWebViewClient extends WebViewClient {

		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {

			view.loadUrl(url);

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}
	}

	/**
	 * 调用系统浏览器下载文件
	 * 
	 * @author Ming
	 * 
	 */

	// class MyWebViewDownLoadListener implements DownloadListener {
	//
	// @Override
	// public void onDownloadStart(String url, String userAgent, String
	// contentDisposition, String mimetype, long contentLength) {
	// Logger.d("tag", "url="+url);
	// Logger.d("tag", "userAgent="+userAgent);
	// Logger.d("tag", "contentDisposition="+contentDisposition);
	// Logger.d("tag", "mimetype="+mimetype);
	// Logger.d("tag", "contentLength="+contentLength);
	// Uri uri = Uri.parse(url);
	// Intent intent = new Intent(Intent.ACTION_VIEW,uri);
	// startActivity(intent);
	// }
	// }

	/**
	 * 在自己的应用中下载文件并打开
	 */
	class MyWebViewDownLoadListener implements DownloadListener {
		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				showToast("请检测内存卡是否存在");
				return;
			}
			DownloaderTask task = new DownloaderTask();
			task.execute(url);
		}
	}

	private class DownloaderTask extends AsyncTask<String, Integer, String> {

		private File directory;

		public DownloaderTask() {
			super();
		}

		/**
		 * 子线程执行之前
		 */
		@Override
		protected void onPreExecute() {
			startLoadingDialog();
			super.onPreExecute();
		}

		/**
		 * 子线程中执行的任务
		 */
		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			String fileName = url.substring(url.lastIndexOf("/") + 1);
			try {
				fileName = URLDecoder.decode(fileName, "UTF-8");
				directory = Environment.getExternalStorageDirectory();

				File file = new File(directory, fileName);
				if (file.exists()) {
					return fileName;
				}

				HttpClient client = new DefaultHttpClient();
				// TODO MING:设置超时 是否设置？
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();

					writeToSDCard(input, fileName);

					input.close();

					return fileName;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return null;
		}

		/**
		 * 子线程执行完毕之后
		 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismissLoadingDialog();
			if (result == null) {
				showToast("连接错误，请稍后再试！");
				return;
			}
			showToast("已保存至SD卡。");
			File file = new File(directory, result);

			Intent intent = getFileIntent(file);
			startActivity(intent);
		}

		/**
		 * 获取进度更新进度条
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			System.out.println(values[0]);
		}
	}

	public Intent getFileIntent(File file) {
		String fName = file.getName();
		Uri uri = Uri.fromFile(file);
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if ("apk".equals(end)) {
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(uri, "*/*");
		}
		return intent;
	}

	public void writeToSDCard(InputStream input, String fileName) {
		File directory = Environment.getExternalStorageDirectory();
		File file = new File(directory, fileName);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int j = 0;
			while ((j = input.read(b)) != -1) {
				fos.write(b, 0, j);
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
