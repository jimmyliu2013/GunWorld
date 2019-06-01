package com.lim.gunworld.adapter;

import java.util.List;

import com.lim.gunworld.R;
import com.lim.gunworld.adapter.GroupByCompanyFragmentListViewAdapter.ViewHolder;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.domain.ItemForClassicFragment;
import com.lim.gunworld.domain.ItemForGroupByCompanyFragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassicFragmentListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	
	private List<ItemForClassicFragment> mItemList;
	
	private Context mContext;
	
	
	public ClassicFragmentListViewAdapter(Context context, List<ItemForClassicFragment> itemList) {
		inflater = LayoutInflater.from(context);
		mContext= context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.row_fragment_group_by_company_lv,
					parent, false);
			holder.topicName = (TextView) convertView
					.findViewById(R.id.tv_fragment_group_by_company_name);
			
			convertView.setTag(holder);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
			holder = (ViewHolder) convertView.getTag();
		}

		holder.topicName.setText(mItemList.get(position).topicName);
		holder.topicName.setTextColor(Color.BLACK);
		
		if (MyApplication.isDarkModel()) {
			convertView.setBackgroundColor(Color.DKGRAY);
				holder.topicName.setTextColor(Color.WHITE);
		}
		
		
		return convertView;
	}
	
	class ViewHolder {
		TextView topicName;
	}
	
	public void notifyDataSetChanged(List<ItemForClassicFragment> list) {
		mItemList = list;
		super.notifyDataSetChanged();
	}

}
