package com.lim.gunworld.activity;

import im.delight.android.webview.AdvancedWebView;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lim.gunworld.R;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.renderscript.Element;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageButton;
import android.widget.Toast;

public class WebViewActivity extends ActionBarActivity {

	private static final String TAG = "WebViewActivity";
	private ProgressDialog dialog;
	private WebView webView;
	private boolean isRoot;

	private ArrayList<String> searchHistory;
	private View loadingView;
	private View emptyView;
	private String url;
	private LoadHtmlTask mTask;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		url = extras.getString("linkUrl");
		isRoot = extras.getBoolean("root");

		loadingView = findViewById(R.id.ll_loading);
		emptyView = findViewById(R.id.ll_empty);

		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTask = new LoadHtmlTask();
				mTask.execute();
			}
		});

		webView = (WebView) findViewById(R.id.webview);

		if (MyApplication.isDarkModel()) {
			loadingView.setBackgroundColor(Color.DKGRAY);
			emptyView.setBackgroundColor(Color.DKGRAY);
			webView.setBackgroundColor(Color.DKGRAY);
		}

		final WebSettings settings = webView.getSettings();

		switch (MyApplication.getTextSize()) {
		case 0:

			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			settings.setTextSize(TextSize.NORMAL);
			break;
		}

		if (Build.VERSION.SDK_INT >= 19) {
			settings.setLoadsImagesAutomatically(true);
		} else {
			settings.setLoadsImagesAutomatically(false);
		}

		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setAppCacheEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setJavaScriptEnabled(false);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				if (!settings.getLoadsImagesAutomatically()) {
					settings.setLoadsImagesAutomatically(true);
				}

			/*	webView.loadUrl("javascript:(function() { "
						+ "document.getElementsByTagName('p')[0].style.display='none';"
						+ "document.getElementsByTagName('table')[document.getElementsByTagName('table').length-1].style.display='none';"
						+ "document.body.background= 'black';" + "})()");*/

				webView.setVisibility(View.VISIBLE);
				if (emptyView.getVisibility() == View.VISIBLE) {
					emptyView.setVisibility(View.GONE);
				}
				if (loadingView.getVisibility() == View.VISIBLE) {
					loadingView.setVisibility(View.GONE);
				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				Intent intent = new Intent(WebViewActivity.this,
						WebViewActivity.class);
				intent.putExtra("linkUrl", url);
				intent.putExtra("root", false);
				WebViewActivity.this.startActivity(intent);
				if (!isRoot && url.contains("htm")) {
					WebViewActivity.this.finish();

				}
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				webView.setVisibility(View.GONE);
				if (emptyView.getVisibility() == View.GONE) {
					emptyView.setVisibility(View.VISIBLE);
				}
				if (loadingView.getVisibility() == View.VISIBLE) {
					loadingView.setVisibility(View.GONE);
				}

			}

		});
		mTask = new LoadHtmlTask();
		mTask.execute();
	}

	private String handleHtml(String urlString) throws IOException {

		String baseUrl = urlString.substring(0, urlString.lastIndexOf("/"))
				+ "/";
		String html = null;

		Document doc = Jsoup
				.connect(urlString)
				.ignoreContentType(true)
				.header("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
				.get();

		if (urlString.contains("pewpewpew.work")
				&& urlString.contains("htm")) {

			if (MyApplication.isDarkModel()) {
				doc.select("body").attr("text", "#ffffff");
				doc.select("body").attr("bgcolor", "#404040");
			}
			doc.select("head").html("<style>img{max-width: 100%; width:auto; height: auto;}</style>");
			doc.select("p").get(0).remove();
			if (doc.select("table").size() > 0
					&& doc.select("table").get(doc.select("table").size() - 1)
							.select("img").size() > 0
					&& doc.select("table").get(doc.select("table").size() - 1)
							.select("img").get(0).attr("alt").equals("首页/更新列表")) {

				doc.select("table").get(doc.select("table").size() - 1)
						.remove();
			}

			doc.append("<hr size=\"30\" color=\"#8080C0\"/>");
			doc.append("<h2><font color=\"#8080C0\">相关链接</font></h2>");
			Elements aTagList = doc.select("a");
			for (int i = 0; i < aTagList.size(); i++) {
				String rawUrl = aTagList.get(i).attr("href");
				if (!rawUrl.contains("http:") && rawUrl.length() > 3) {
					if (!rawUrl.contains("jpg")) {

						doc.append("<li style=\"padding-left:20px;\"><p align=\"left\" style=\"line-height: 150%\">"
								+ aTagList.get(i).outerHtml() + "<p></li>");
						aTagList.get(i).remove();
						aTagList.get(i).attr("target", "_blank");// baseUrl +
					} // rawUrl);
				} else {
				}
			}
			Elements tableTagList = doc.select("td");

			for (int i = 0; i < tableTagList.size(); i++) {
				if (tableTagList.get(i).text() == null
						|| tableTagList.get(i).text().equals("")
						|| tableTagList.get(i).text().equals(" ")
						|| tableTagList.get(i).text().length() < 2
						|| tableTagList.get(i).text().contains(">>")) {
					tableTagList.get(i).remove();
				}
			}
		} else {

		}
		html = doc.html();
		return html;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			this.finish();
			return true;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		if (mTask != null) {
			mTask.cancel(true);
		}

		super.onDestroy();
	}

	private class LoadHtmlTask extends AsyncTask<Void, Void, Integer> {

		private String html;

		@Override
		protected void onPreExecute() {

			loadingView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			webView.setVisibility(View.GONE);

		}

		@Override
		protected Integer doInBackground(Void... params) {

			try {

				if (url.contains("pewpewpew.work")
						&& url.contains("htm")) {

					html = handleHtml(url);
				} else {

					return Constant.LOAD_DIRECTLY;
				}
				if (html != null) {
					return Constant.SUCCESS;
				} else {
					return Constant.END;
				}

			} catch (IOException e) {
				e.printStackTrace();
				return Constant.FAIL;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
			case Constant.SUCCESS:

				if (webView != null) {

					webView.loadDataWithBaseURL(url, html, "text/html",
							"utf-8", null);
				}

				break;

			case Constant.LOAD_DIRECTLY:

				if (webView != null) {

					webView.loadUrl(url);
				}

				break;
			case Constant.END:
				if (webView != null) {
					webView.loadDataWithBaseURL(url, html, "text/html",
							"utf-8", null);
				}
				break;
			case Constant.FAIL:

				if (webView != null) {
					emptyView.setVisibility(View.VISIBLE);
					loadingView.setVisibility(View.GONE);
					webView.setVisibility(View.GONE);
				}

				break;
			default:
				break;
			}

		}

	}

	public void logLargeString(String str) {

		if (str.length() > 3000) {
			Log.i(TAG, str.substring(0, 3000));
			logLargeString(str.substring(3000));
		} else
			Log.i(TAG, str);
	}
}
