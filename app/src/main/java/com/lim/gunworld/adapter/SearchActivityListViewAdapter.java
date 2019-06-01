package com.lim.gunworld.adapter;

import java.util.HashMap;
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

public class SearchActivityListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	
	private List<HashMap<String, String>> mItemList;
	
	private Context mContext;
	
	
	public SearchActivityListViewAdapter(Context context, List<HashMap<String, String>> itemList) {
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

			convertView = inflater.inflate(R.layout.row_activity_search_lv,
					parent, false);
			holder.text = (TextView) convertView
					.findViewById(R.id.text);
			
			convertView.setTag(holder);
		} else {
			//convertView.setBackgroundColor(Color.TRANSPARENT);
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(mItemList.get(position).get("title"));
		//holder.topicName.setTextColor(Color.BLACK);
		
		if (MyApplication.isDarkModel()) {
			//convertView.setBackgroundColor(Color.DKGRAY);
				holder.text.setTextColor(Color.WHITE);
		}
		
		
		return convertView;
	}
	
	class ViewHolder {
		TextView text;
	}
	
	public void notifyDataSetChanged(List<HashMap<String, String>> list) {
		mItemList = list;
		super.notifyDataSetChanged();
	}

}
