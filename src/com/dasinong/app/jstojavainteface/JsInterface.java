package com.dasinong.app.jstojavainteface;

public class JsInterface {
	
	private WebViewClientClickListener webViewClientClickListener = null;
	
	public void setWebViewClientClickListener(WebViewClientClickListener webViewClientClickListener){
		this.webViewClientClickListener = webViewClientClickListener;
	}
	
	public void showAllImages(String ... images ){
		if(webViewClientClickListener != null){
			webViewClientClickListener.webViewHasClickEnvent(images);
		}
	}
	
	public interface WebViewClientClickListener {
		public void webViewHasClickEnvent(String ... images);
	}
	
}
