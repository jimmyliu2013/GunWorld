package com.lim.gunworld.adapter;

import java.util.HashMap;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lim.gunworld.R;
import com.lim.gunworld.activity.MainActivity;
import com.lim.gunworld.activity.WebViewActivity;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.domain.ItemForGroupByCompanyFragment;
import com.lim.gunworld.domain.ItemForGroupByCountryFragment;
import com.lim.gunworld.domain.ItemForNewestFragment;
import com.lim.gunworld.ui.FlowLayout;
import com.lim.gunworld.utils.DimensionUtils;


public class GroupByCompanyFragmentListViewAdapter extends BaseAdapter
		implements StickyListHeadersAdapter {

	private LayoutInflater inflater;

	private List<ItemForGroupByCompanyFragment> mItemList;

	private Context mContext;

	public GroupByCompanyFragmentListViewAdapter(Context context,
			List<ItemForGroupByCompanyFragment> itemList) {
		inflater = LayoutInflater.from(context);
		mContext = context;
		mItemList = itemList;
	}

	public void setDataSet(List<ItemForGroupByCompanyFragment> itemList) {
		mItemList = itemList;
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = inflater.inflate(
					R.layout.row_fragment_group_by_company_lv, parent, false);
			holder.companyName = (TextView) convertView
					.findViewById(R.id.tv_fragment_group_by_company_name);

			convertView.setTag(holder);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
			holder = (ViewHolder) convertView.getTag();
		}

		holder.companyName.setText(mItemList.get(position).companyName);
		holder.companyName.setTextColor(Color.BLACK);

		if (MyApplication.isDarkModel()) {
			convertView.setBackgroundColor(Color.DKGRAY);
			holder.companyName.setTextColor(Color.WHITE);
		}

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(
					R.layout.row_fragment_group_by_country_lv_header, parent,
					false);
			holder.groupByCompanyName = (TextView) convertView
					.findViewById(R.id.tv_fragment_group_by_country_header);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		String headerText = (String) mItemList.get(position).companyNameInEnglish
				.toUpperCase().subSequence(0, 1);
		holder.groupByCompanyName.setText(headerText);
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return mItemList.get(position).companyNameInEnglish.subSequence(0, 1)
				.charAt(0);
	}

	class HeaderViewHolder {
		TextView groupByCompanyName;
	}

	class ViewHolder {
		TextView companyName;
	}

	public void notifyDataSetChanged(List<ItemForGroupByCompanyFragment> list) {
		mItemList = list;
		super.notifyDataSetChanged();
	}

}