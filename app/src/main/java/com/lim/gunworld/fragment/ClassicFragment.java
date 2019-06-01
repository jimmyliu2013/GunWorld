package com.lim.gunworld.fragment;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lim.gunworld.R;
import com.lim.gunworld.activity.MainActivity;
import com.lim.gunworld.activity.WebViewActivity;
import com.lim.gunworld.adapter.ClassicFragmentListViewAdapter;
import com.lim.gunworld.adapter.GroupByCompanyFragmentListViewAdapter;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForClassicFragment;
import com.lim.gunworld.domain.ItemForGroupByCompanyFragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClassicFragment extends BaseFragment {

	private List<ItemForClassicFragment> mItemList;
	private ListView listView;
	private ClassicFragmentListViewAdapter myAdapter;

	@Override
	public void invalidateView() {
		if (listView != null) {
			listView.invalidateViews();
			listView.setBackgroundColor(MyApplication.isDarkModel() ? Color.DKGRAY
					: Color.TRANSPARENT);
		} else {
		}
	}

	@Override
	protected void restoreView() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void changeStateWhenDestroy() {

	}

	@Override
	protected void refreshData() {

	}

	private List<ItemForClassicFragment> getItemListFromStringXml() {

		List<ItemForClassicFragment> list = null;
		String jsonString = mContext.getString(R.string.topicList);
		Gson gson = new Gson();

		JsonReader reader = new JsonReader(new StringReader(jsonString));
		reader.setLenient(true);
		list = gson.fromJson(reader,
				new TypeToken<ArrayList<ItemForClassicFragment>>() {
				}.getType());
		return list;
	}

	@Override
	protected void initView(LayoutInflater inflater, ViewGroup container) {

		rootView = inflater
				.inflate(R.layout.fragment_classic, container, false);

		mItemList = getItemListFromStringXml();

		listView = (ListView) rootView.findViewById(R.id.lv_fragment_classic);
		listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

		listView.setBackgroundColor(MyApplication.isDarkModel() ? Color.DKGRAY
				: Color.TRANSPARENT);

		myAdapter = new ClassicFragmentListViewAdapter(mContext, mItemList);

		listView.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(mContext, WebViewActivity.class);
				intent.putExtra(
						Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL,
						mItemList.get(position).url);
				intent.putExtra("root", true);
				mContext.startActivity(intent);

			}
		});

	}

	@Override
	protected void sendStateToMainActivityWhenVisible() {
		((MainActivity) mContext).changeActionBarTitle(Constant.DEFAULT_TITLE);
		((MainActivity) mContext).showBackIcon(false);
	}

	@Override
	protected void sendStateToMainActivityWhenNotVisible() {

	}

}
