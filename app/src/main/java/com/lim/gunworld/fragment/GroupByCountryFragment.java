package com.lim.gunworld.fragment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lim.gunworld.R;
import com.lim.gunworld.activity.MainActivity;
import com.lim.gunworld.activity.WebViewActivity;
import com.lim.gunworld.adapter.DrawerListAdapter;
import com.lim.gunworld.adapter.GroupByCountryFragmentListViewAdapter;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForGroupByCountryFragment;
import com.lim.gunworld.ui.FullDrawerLayout;
import com.lim.gunworld.utils.HtmlParser;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class GroupByCountryFragment extends BaseFragment {

	private static final String TAG = "GroupByCountryFragment";

	private int themeBackgroungColor;

	private StickyListHeadersListView listView;
	private List<ItemForGroupByCountryFragment> mItemList = new ArrayList<ItemForGroupByCountryFragment>();
	private GroupByCountryFragmentListViewAdapter myAdapter;
	private FullDrawerLayout drawerLayout;
	private ListView drawerListView;
	private DrawerListAdapter drawerListViewAdaper;
	private List<HashMap<String, String>> subMenuList = new ArrayList<HashMap<String, String>>();
	private View loadingView;
	private View emptyView;
	public FragmentState fragmentState;

	private MenuItem refreshItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		themeBackgroungColor = getThemeBackgroundColor();
		if (fragmentState == null) {
			fragmentState = new FragmentState();
			fragmentState.title = Constant.DEFAULT_TITLE;
			fragmentState.currentItemNumber = -1;
			fragmentState.isShowingSubView = false;
			fragmentState.isRefreshing = false;

		}

	}

	private int getThemeBackgroundColor() {
		TypedArray array = mContext.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.colorBackground, });
		int backgroundColor = array.getColor(0, 0xFF00FF);
		array.recycle();
		return backgroundColor;
	}

	@Override
	protected void restoreView() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void changeStateWhenDestroy() {
		if (fragmentState.isRefreshing == true) {
			closeDrawer();
		}
	}

	@Override
	protected void refreshData() {
		if (fragmentState.isRefreshing == false
				&& fragmentState.isShowingSubView == true
				&& fragmentState.currentItemNumber != -1) {
			new LoadDateAsyncTask().execute(fragmentState.currentItemNumber);
		}

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	protected void initView(LayoutInflater inflater, ViewGroup container) {

		rootView = inflater.inflate(R.layout.fragment_group_by_country,
				container, false);

		mItemList = getItemListFromStringXml();

		initDrawerListView();

		initContentListView(drawerLayout, drawerListView);

	}

	private void initDrawerListView() {
		drawerLayout = (FullDrawerLayout) rootView
				.findViewById(R.id.drawer_layout_fragment_group_vy_country);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		drawerLayout.setFocusableInTouchMode(true);

		View drawerView = rootView
				.findViewById(R.id.ll_fragment_group_by_country_drawer);
		drawerView.setBackgroundColor(themeBackgroungColor);

		loadingView = rootView.findViewById(R.id.ll_loading);
		emptyView = rootView.findViewById(R.id.ll_empty);

		if (MyApplication.isDarkModel()) {

			emptyView.setBackgroundColor(Color.DKGRAY);
			loadingView.setBackgroundColor(Color.DKGRAY);
		} else {

			emptyView.setBackgroundResource(R.drawable.strock_viewpager);
			loadingView.setBackgroundResource(R.drawable.strock_viewpager);
			;
		}

		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshData();
			}
		});

		drawerListView = (ListView) rootView
				.findViewById(R.id.lv_fragment_group_by_country_drawer);
		drawerListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		drawerListView.setBackgroundColor(themeBackgroungColor);
		if (MyApplication.isDarkModel()) {
			drawerListView.setBackgroundColor(Color.DKGRAY);

		} else {
			drawerListView.setBackgroundResource(R.drawable.strock_viewpager);
		}

		drawerListViewAdaper = new DrawerListAdapter(subMenuList, mContext);

		drawerListView.setAdapter(drawerListViewAdaper);

		drawerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, WebViewActivity.class);
				intent.putExtra(
						Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL,
						"http://pewpewpew.work/"
								+ mItemList
										.get(fragmentState.currentItemNumber).countryNameInEnglish
								+ "/"
								+ subMenuList.get(position).get(
										Constant.LINK_URL));
				intent.putExtra("root", true);
				mContext.startActivity(intent);
			}

		});

		drawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {

			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {

			}

			@Override
			public void onDrawerOpened(View arg0) {

			}

			@Override
			public void onDrawerClosed(View arg0) {
				fragmentState.isShowingSubView = false;
				fragmentState.currentItemNumber = -1;
				((MainActivity) mContext).changeActionBarTitle(null);
				((MainActivity) mContext).showBackIcon(false);
				getActivity().supportInvalidateOptionsMenu();
			}
		});

	}

	private void initContentListView(final FullDrawerLayout drawerLayout,
			final ListView drawerListView) {
		listView = (StickyListHeadersListView) rootView
				.findViewById(R.id.lv_fragment_group_by_country);

		listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		myAdapter = new GroupByCountryFragmentListViewAdapter(mContext,
				mItemList, listView);
		listView.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				fragmentState.title = mItemList.get(position).countryName;
				fragmentState.currentItemNumber = position;
				if (fragmentState.title.equals("中国")
						|| fragmentState.title.equals("美国")
						|| fragmentState.title.equals("俄罗斯")) {
					Intent intent = new Intent(mContext, WebViewActivity.class);
					intent.putExtra(
							Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL,
							mItemList.get(position).url);
					intent.putExtra("root", true);
					mContext.startActivity(intent);
				} else {

					getActivity().supportInvalidateOptionsMenu();

					drawerLayout
							.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
					fragmentState.isShowingSubView = true;

					((MainActivity) mContext)
							.changeActionBarTitle(fragmentState.title);
					((MainActivity) mContext).showBackIcon(true);
					if (mItemList.get(position).subMenuList == null) {
						new LoadDateAsyncTask().execute(position);

					} else {
						subMenuList = mItemList.get(position).subMenuList;
						if (drawerListViewAdaper != null) {

							drawerListViewAdaper
									.notifyDataSetChanged(subMenuList);
						}
					}
				}
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
		if (fragmentState.isShowingSubView) {
			getActivity().getMenuInflater().inflate(
					R.menu.menu_fragment_group_by_country, menu);
			refreshItem = menu.findItem(R.id.fragment_group_by_country_refresh);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_group_by_country_refresh:
			if (fragmentState.isRefreshing == false) {

				refreshData();

			}
			break;

		default:
			break;
		}

		return true;

	}

	private class LoadDateAsyncTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected void onPreExecute() {

			fragmentState.isRefreshing = true;
			drawerListView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			startRefreshAnimation(refreshItem);

		}

		@Override
		protected Integer doInBackground(Integer... params) {

			try {

				subMenuList = HtmlParser.getSubMenuListFromWeb(mItemList
						.get(params[0]).url);
				mItemList.get(params[0]).subMenuList = subMenuList;
				if (mItemList.get(params[0]).subMenuList != null
						&& mItemList.get(params[0]).subMenuList.size() > 0) {

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

				drawerListView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				loadingView.setVisibility(View.GONE);

				fragmentState.isRefreshing = false;
				stopRefreshAnimation(refreshItem);

				drawerListViewAdaper.notifyDataSetChanged(subMenuList);
				break;

			case Constant.END:

				drawerListView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				loadingView.setVisibility(View.GONE);

				fragmentState.isRefreshing = false;
				stopRefreshAnimation(refreshItem);
				drawerListViewAdaper.notifyDataSetChanged(subMenuList);
				Toast.makeText(mContext, "没有更多内容！", Toast.LENGTH_SHORT).show();
				break;

			case Constant.FAIL:

				drawerListView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);

				fragmentState.isRefreshing = false;
				stopRefreshAnimation(refreshItem);

				break;

			default:
				break;
			}

		}

	}

	private List<ItemForGroupByCountryFragment> getItemListFromStringXml() {

		List<ItemForGroupByCountryFragment> list = null;
		String jsonString = mContext.getString(R.string.jsonString);
		Gson gson = new Gson();

		JsonReader reader = new JsonReader(new StringReader(jsonString));
		reader.setLenient(true);
		list = gson.fromJson(reader,
				new TypeToken<ArrayList<ItemForGroupByCountryFragment>>() {
				}.getType());
		return list;
	}

	@Override
	public void invalidateView() {
		if (rootView != null) {

			if (MyApplication.isDarkModel()) {
				drawerListView.setBackgroundColor(Color.DKGRAY);
				emptyView.setBackgroundColor(Color.DKGRAY);
				loadingView.setBackgroundColor(Color.DKGRAY);
			} else {
				drawerListView
						.setBackgroundResource(R.drawable.strock_viewpager);
				emptyView.setBackgroundResource(R.drawable.strock_viewpager);
				loadingView.setBackgroundResource(R.drawable.strock_viewpager);
			}

			listView.invalidate();
		}

		if (drawerListView != null) {
			drawerListView.invalidateViews();
		}
	}

	public class FragmentState {

		private int currentItemNumber;
		private String title;
		public boolean isShowingSubView;
		private boolean isRefreshing;

	}

	@Override
	public void closeDrawer() {
		if (drawerLayout != null) {
			drawerLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}
	}

	@Override
	protected void sendStateToMainActivityWhenVisible() {
		if (fragmentState.isShowingSubView == true) {

			((MainActivity) mContext).changeActionBarTitle(fragmentState.title);
			((MainActivity) mContext).showBackIcon(true);

		} else {
			((MainActivity) mContext).changeActionBarTitle(null);
			((MainActivity) mContext).showBackIcon(false);
		}

	}

	@Deprecated
	public String readFully(InputStream inputStream, String encoding)
			throws IOException {
		return new String(readFully(inputStream), encoding);
	}

	private byte[] readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}

	@Deprecated
	private String getStringFromAssets() {
		InputStream is = null;
		StringBuilder sb = null;
		try {
			is = mContext.getAssets().open("nation_list_json.txt",
					Context.MODE_PRIVATE);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			sb = new StringBuilder();
			String read = br.readLine();

			while (read != null) {
				sb.append(read);
				read = br.readLine();

			}

			return sb.toString();

		} catch (IOException e) {
			Toast.makeText(mContext, "获取列表信息失败!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return sb.toString();
	}

	@Deprecated
	public static void longLog(String str) {
		if (str.length() > 3100) {
			Log.d("", str.substring(0, 3100));
			longLog(str.substring(3100));
		} else
			Log.d("", str);
	}

	@Override
	protected void sendStateToMainActivityWhenNotVisible() {

		stopRefreshAnimation(refreshItem);

	}

	private void startRefreshAnimation(MenuItem item) {

		if (item != null) {

			fragmentState.isRefreshing = true;

			final ImageView refreshActionView = (ImageView) getActivity()
					.getLayoutInflater().inflate(
							R.layout.iv_activity_main_action_view, null);

			final Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.rotate);
			animation.setRepeatMode(Animation.RESTART);
			animation.setRepeatCount(Animation.INFINITE);

			refreshActionView.setAnimation(animation);
			MenuItemCompat.setActionView(item, refreshActionView);

		} else {
		}

	}

	public void stopRefreshAnimation(MenuItem item) {
		if (item != null) {
			View v = MenuItemCompat.getActionView(item);
			if (v != null) {
				v.clearAnimation();
				MenuItemCompat.setActionView(item, null);
			}
			fragmentState.isRefreshing = false;
		}
	}

}
