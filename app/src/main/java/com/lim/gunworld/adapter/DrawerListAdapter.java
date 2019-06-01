package com.lim.gunworld.adapter;

import java.util.HashMap;
import java.util.List;

import com.lim.gunworld.R;
import com.lim.gunworld.R.id;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForNewestFragment;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {

	private List<HashMap<String, String>> mItemList;
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	
	
	public DrawerListAdapter(List<HashMap<String, String>> itemList,
			Context context) {
		super();
		this.mItemList = itemList;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.row_fragment_group_by_country_drawer_list, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.tv_fragment_group_by_country_drawer_list);
			convertView.setTag(holder);
		}else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(mItemList.get(position).get(Constant.Name));
		holder.name.setTextColor(Color.BLACK);
		
		if (MyApplication.isDarkModel()) {
			convertView.setBackgroundColor(Color.DKGRAY);
				holder.name.setTextColor(Color.WHITE);
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView name;
		
		
	}
	
	
	
	public void notifyDataSetChanged(List<HashMap<String, String>> list) {
		// TODO Auto-generated method stub
		mItemList = list;
		super.notifyDataSetChanged();
	}

}
