package com.lim.gunworld.fragment;

import java.util.ArrayList;
import java.util.List;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.lim.gunworld.R;
import com.lim.gunworld.activity.MainActivity;
import com.lim.gunworld.activity.SearchActivity;
import com.lim.gunworld.adapter.NewestFragmentListViewAdapter;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForNewestFragment;
import com.lim.gunworld.utils.BroadcastUtils;
import com.lim.gunworld.utils.CacheUtils;
import com.lim.gunworld.utils.HtmlParser;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class NewestFragment extends BaseFragment {

	private List<ItemForNewestFragment> mItemList = new ArrayList<ItemForNewestFragment>();
	private static final String INDEX_PAGE_URL = "http://pewpewpew.work/index.htm";
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	private static final String TAG = "NewestFragment";
	private StickyListHeadersListView listView;
	private NewestFragmentListViewAdapter myAdapter;
	private View rootView;
	private View emptyView;
	private boolean isRefreshing;
	private LoadDataAsyncTask mTask;
	private boolean isViewCreated = false;
	private Context mContext;
	private MenuItem refreshItem;

	public static final NewestFragment newInstance() {
		NewestFragment f = new NewestFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		// 如果rootview已经有了，就直接用，没有才初始化
		if (rootView == null) {
			initView(inflater, container);
		}
		if (savedInstanceState == null && rootView != null) {
			initData();
		} else {
		}
		isViewCreated = true;
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	protected void initData() {
		//mTask = new LoadDataAsyncTask(true);
		//mTask.execute();
	}

	public void refreshData() {

		if (isViewCreated && (!isRefreshing)) {
			mTask = new LoadDataAsyncTask(false);
			mTask.execute();
		} else if (!isViewCreated) {
			Toast.makeText(getActivity(), "界面尚未初始化，请稍候再刷新", 1).show();
		}

	}

	protected void initView(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.fragment_newest, container, false);
		listView = (StickyListHeadersListView) rootView
				.findViewById(R.id.fragment_newest_lv);
		emptyView = rootView.findViewById(R.id.ll_fragment_newest_empty);
		emptyView.setBackgroundColor(MyApplication.isDarkModel() ? Color.DKGRAY
				: Color.TRANSPARENT);
		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshData();

			}
		});
		// 拉到底时去掉黄色的edge
		listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		
		
		
		
		myAdapter = new NewestFragmentListViewAdapter(
				(MainActivity) getActivity(), mItemList, listView);
		listView.setAdapter(myAdapter);
		
		mTask = new LoadDataAsyncTask(true);
		mTask.execute();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			((MainActivity) mContext)
					.changeActionBarTitle(Constant.DEFAULT_TITLE);
			((MainActivity) mContext).showBackIcon(false);
		} else {
			if (mTask != null) {

				mTask.cancel(true);
				stopRefreshAnimation(refreshItem);
				isRefreshing = false;
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getMenuInflater()
				.inflate(R.menu.menu_fragment_main, menu);
		refreshItem = menu.findItem(R.id.fragment_main_refresh);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_main_refresh:
			if (isRefreshing == false) {

				refreshData();

			}
			break;

		default:
			break;
		}

		return true;

	}

	@Override
	public void onDestroyView() {
		if (rootView.getParent() != null) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}
		changeStateWhenDestroy();

		super.onDestroyView();
	}

	protected void changeStateWhenDestroy() {
		if (mTask != null) {
			mTask.cancel(true);
			BroadcastUtils
					.sendBroadcast(getActivity(), Constant.FINISH_REFRESH);
			isRefreshing = false;
		}
	}

	public class LoadDataAsyncTask extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;

		private boolean mIsFirstTime;

		public LoadDataAsyncTask(boolean isFirstTime) {
			mIsFirstTime = isFirstTime;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isRefreshing = true;
			if (mIsFirstTime) {
				dialog = ProgressDialog.show(getActivity(), "载入中...", "请稍候",
						false, false);
			} else {
				startRefreshAnimation(refreshItem);
				listView.setClickable(false);
			}

		}

		@Override
		protected Integer doInBackground(Void... params) {

			try {
//				if (mIsFirstTime) {
					mItemList = HtmlParser
							.getNewestFragmentDataFromWeb(INDEX_PAGE_URL);
					CacheUtils
							.putItemForNewestFragmentList(mContext, mItemList);

//				} else {
//
//
//					mItemList = CacheUtils
//							.getItemForNewestFragmentList(mContext);
//
//					if (mItemList == null || mItemList.size() == 0) {
//						mItemList = HtmlParser
//								.getNewestFragmentDataFromWeb(INDEX_PAGE_URL);
//						CacheUtils.putItemForNewestFragmentList(mContext,
//								mItemList);
//					}
//				}

				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				return FAIL;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case SUCCESS:

				emptyView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);

				myAdapter.notifyDataSetChanged(mItemList);

				if (mIsFirstTime) {
					dialog.dismiss();
				} else {
					stopRefreshAnimation(refreshItem);
					listView.setClickable(true);
				}
				isRefreshing = false;

				break;
			case FAIL:
				mItemList.clear();
				listView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				if (mIsFirstTime) {
					dialog.dismiss();
				} else {
					stopRefreshAnimation(refreshItem);
					listView.setClickable(true);
				}
				isRefreshing = false;

				break;

			default:
				if (mIsFirstTime) {
					dialog.dismiss();
				} else {
					stopRefreshAnimation(refreshItem);
					listView.setClickable(true);
				}
				isRefreshing = false;
				break;
			}
		}
	}

	@Override
	public void closeDrawer() {

	}

	@Override
	protected void restoreView() {

	}

	@Override
	protected void sendStateToMainActivityWhenVisible() {

	}

	@Override
	protected void sendStateToMainActivityWhenNotVisible() {

	}

	@Override
	public void invalidateView() {
		if (rootView != null) {
			listView.invalidate();
			emptyView
					.setBackgroundColor(MyApplication.isDarkModel() ? Color.DKGRAY
							: Color.TRANSPARENT);
		}
	}

	private void startRefreshAnimation(MenuItem item) {

		if (item != null) {

			isRefreshing = true;

			ImageView refreshActionView = (ImageView) getActivity()
					.getLayoutInflater().inflate(
							R.layout.iv_activity_main_action_view, null);

			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.rotate);
			animation.setRepeatMode(Animation.RESTART);
			animation.setRepeatCount(Animation.INFINITE);

			refreshActionView.startAnimation(animation);
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
			isRefreshing = false;

		}
	}

}
