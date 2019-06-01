package com.lim.gunworld.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lim.gunworld.R;
import com.lim.gunworld.adapter.SearchActivityListViewAdapter;
import com.lim.gunworld.application.MyApplication;




import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends ActionBarActivity {

	public static final String TAG = "SearchActivity";
	private List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
	private final int SUCCESS = 0;
	private final int FAIL = 1;
	private final int END = 2;
	private LinearLayout loadingView;
	private View emptyView;
	private String mKeyword;
	private SearchView searchView;
	private int pageNumber = 1;
	private SearchActivityListViewAdapter adapter;
	private PullToRefreshListView listView;
	private boolean isRefreshing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
         getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		Intent intent = getIntent();
		intent.getExtras();
		
		loadingView = (LinearLayout)findViewById(R.id.ll_loading);
		loadingView.setFocusable(true);
		emptyView = findViewById(R.id.ll_empty);
		emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mKeyword != null) {
					new SearchTask().execute();
					
				}
			}
		});
		
		
		
		listView = (PullToRefreshListView) findViewById(R.id.search_listview);
		listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		listView.setMode(Mode.PULL_FROM_END);
		//listView.setFocusable(false);
		//listView.getRefreshableView().setFocusable(false);
		//listView.setVisibility(View.GONE);
		
		if (MyApplication.isDarkModel()) {
			listView.getRefreshableView().setBackgroundColor(Color.DKGRAY);
			loadingView.setBackgroundColor(Color.DKGRAY);
			emptyView.setBackgroundColor(Color.DKGRAY);
		}else {
			loadingView.setBackgroundResource(R.drawable.strock_viewpager);
			emptyView.setBackgroundResource(R.drawable.strock_viewpager);
		}
		
		
		adapter = new SearchActivityListViewAdapter(this, dataList);
		
		listView.setAdapter(adapter);
		
		
		listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				// tabListItem.clear();
//				if (isRefreshing == false) {
//					new LoadDataAsyncTask(Constant.LoadNewest, currentPageNumber)
//					.execute();
//				}else {
//					Log.d(TAG, "is refreshing! try later");
//					listView.onRefreshComplete();
//				}
				

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (isRefreshing == false) {
					new LoadMoreTask().execute(pageNumber + 1);
				}else {
					//Log.d(TAG, "is refreshing! try later");
					//listView.onRefreshComplete();
				}
				
				// currentIndex += 1;
			}
		});
		
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SearchActivity.this,
						WebViewActivity.class);
				intent.putExtra("linkUrl", dataList.get((arg2-1>=0)?arg2-1:0).get("linkUrl"));
				intent.putExtra("root", false);
				startActivity(intent);

			}
		});
		
		
		

	}

	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_activity_search, menu);
		
		final MenuItem searchItem = menu.findItem(R.id.actionbar_search);
//		MenuItem findItem = menu.findItem(android.R.id.home);
//		if (findItem != null) {
//			Log.d(TAG, "not null");
//			findItem.setVisible(false);
//		}else {
//			Log.d(TAG, "null");
//		}
		searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		searchView.setIconified(false);
		//searchView.setSubmitButtonEnabled(true);
       // searchView.set
		
		searchView.setQueryHint("输入关键字");
		
		
        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		
		
		MenuItemCompat.expandActionView(searchItem);
		
MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem arg0) {
				
				//Log.d("searchview", "expand");
				if (mKeyword != null) {
					searchView.post(new Runnable() {

					    @Override
					    public void run() {
					    	searchView.setQuery(mKeyword, false);
					    }
					});
				}
				
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem arg0) {
				//SearchActivity.this.finish();
				//searchView.requestFocus();
				return true;
			}
		});
		
		OnQueryTextListener onQueryTextListener = new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				mKeyword = arg0;
				new SearchTask().execute();
				searchView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						searchView.clearFocus();
						
					}
				});
				MenuItemCompat.collapseActionView(searchItem);
				return true;

			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				return true;
			};

		};

		searchView.setOnQueryTextListener(onQueryTextListener);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			this.finish();
			return true;

		default:
			//return true;
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	

	private class SearchTask extends AsyncTask<Void, Void, Integer> {
		

		@Override
		protected void onPreExecute() {
			//Log.d(TAG, "start");
			isRefreshing = true;
			loadingView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
			
			
		}

		@Override
		protected Integer doInBackground(Void... params) {

			String encoded = null;
			try {
				encoded = URLEncoder.encode(mKeyword,
						"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//System.out.println(encoded);
			String url = "http://www.sogou.com/sogou?query="
					+ encoded
					+ "+site%3Apewpewpew.work";
			try {
				dataList = getResultFromWeb(url);

				if (dataList.size() == 0) {
					return END;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return FAIL;
			}
			return SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
			case SUCCESS:
//Log.d(TAG, "success");
				/*SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this,
						dataList, R.layout.row_activity_search_lv,
						new String[] { "title" }, new int[] { R.id.text })
				{

			        public View getView(int position, View convertView, ViewGroup parent) {
			            View view = super.getView(position, convertView, parent);
			             TextView text1 = (TextView) view.findViewById(R.id.text);
			             if (MyApplication.isDarkModel()) {
							
			            	 text1.setTextColor(Color.WHITE);
						}

			              return view;

			        };
				};
				listView.setAdapter(adapter);*/
				//listView.setAdapter(adapter);
				adapter.notifyDataSetChanged(dataList);
				listView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				emptyView.setVisibility(View.GONE);
				
				isRefreshing = false;

				break;
			case END:
				Toast.makeText(SearchActivity.this, "没有找到相关内容！",
						Toast.LENGTH_SHORT).show();

				loadingView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				isRefreshing = false;
				break;
			case FAIL:

				loadingView.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				isRefreshing =false;
				break;

			default:
				break;
			}

		}

	}
	
	
	
	
	
	private class LoadMoreTask extends AsyncTask<Integer, Void, Integer> {
		

		@Override
		protected void onPreExecute() {
			isRefreshing =true;
			listView.setRefreshing();
			//loadingView.setVisibility(View.VISIBLE);
			//listView.setVisibility(View.GONE);
			//emptyView.setVisibility(View.GONE);
			
			
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			String encoded = null;
			try {
				encoded = URLEncoder.encode(mKeyword,
						"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//System.out.println(encoded);
			String url = "http://www.sogou.com/sogou?query="
					+ encoded
					+ "+site%3Apewpewpew.work" + "&page=" + params[0];
			try {
				List<HashMap<String, String>> tempList = getResultFromWeb(url);
				//dataList = getResultFromWeb(url);

				if (tempList != null && tempList.size() == 0) {
					return END;
				}else if(tempList != null && tempList.size() > 0){
					dataList.addAll(tempList);
					return SUCCESS;
				}else {
					return FAIL;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return FAIL;
			}
			//return SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
			case SUCCESS:

				/*SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this,
						dataList, R.layout.row_activity_search_lv,
						new String[] { "title" }, new int[] { R.id.text })
				{

			        public View getView(int position, View convertView, ViewGroup parent) {
			            View view = super.getView(position, convertView, parent);
			             TextView text1 = (TextView) view.findViewById(R.id.text);
			             if (MyApplication.isDarkModel()) {
							
			            	 text1.setTextColor(Color.WHITE);
						}

			              return view;

			        };
				};*/
			//	listView.setAdapter(adapter);
				if (adapter != null) {
					
					adapter.notifyDataSetChanged();
				}
				//loadingView.setVisibility(View.GONE);
				//listView.setVisibility(View.VISIBLE);
				//emptyView.setVisibility(View.GONE);
				
				pageNumber += 1;

				break;
			case END:
				Toast.makeText(SearchActivity.this, "没有找到相关内容！",
						Toast.LENGTH_SHORT).show();

				//loadingView.setVisibility(View.GONE);
				//listView.setVisibility(View.VISIBLE);
				//emptyView.setVisibility(View.GONE);
				
				break;
			case FAIL:

				//loadingView.setVisibility(View.GONE);
				//listView.setVisibility(View.GONE);
				//emptyView.setVisibility(View.VISIBLE);
				Toast.makeText(SearchActivity.this, "网络不好，请重试！",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			listView.onRefreshComplete();
           isRefreshing =false;
		}

	}
	
	
	
	
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}


	private List<HashMap<String, String>> getResultFromWeb(String url)
			throws IOException {

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		Document doc = Jsoup
				.connect(url)
				.header("User-Agent",
						"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
				.get();

		Elements elements = doc.select("h3");
		for (int i = 1; i < elements.size(); i++) {
			String title = elements.get(i).text();
			String linkUrl = elements.get(i).select("a").attr("href");
		//	Log.d(TAG, title);
			if (linkUrl.contains("pewpewpew.work")) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", title);
				map.put("linkUrl", linkUrl);
				list.add(map);
			}
			
			
		}

		return list;
	}

}

