package com.sample.anftest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PromotionWebActivity extends Activity{
	private ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		setContentView(R.layout.activity_web);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myWebView.setWebChromeClient(new MyWebViewClient());
		
		//To open URL inside webview
		myWebView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return false;
		    }
		    
		});
		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setMax(100);
		progress.setProgress(0);
		myWebView.loadUrl(url);
	}
	
	//To show progress bar 
	private class MyWebViewClient extends WebChromeClient {	
		@Override
		public void onProgressChanged(WebView view, int newProgress) {			
			PromotionWebActivity.this.setValue(newProgress);
			
			super.onProgressChanged(view, newProgress);
		}
	}
	public void setValue(int progress) {
		this.progress.setProgress(progress);		
	}
}
