package com.lim.gunworld.adapter;

import java.util.HashMap;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lim.gunworld.R;
import com.lim.gunworld.activity.MainActivity;
import com.lim.gunworld.activity.WebViewActivity;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForNewestFragment;
import com.lim.gunworld.ui.FlowLayout;
import com.lim.gunworld.utils.DimensionUtils;


public class NewestFragmentListViewAdapter extends BaseAdapter implements
		StickyListHeadersAdapter {

	private static final String TAG = "NewestFragmentListViewAdapter";

	private LayoutInflater inflater;

	private int topAndBottomPadding;

	private int leftAndRightPadding;

	private int expandedPosition = -1;

	private List<ItemForNewestFragment> mItemList;
	private StickyListHeadersListView mListView;

	private MainActivity mContext;

	private ViewGroup currentExpandedLayout;

	public NewestFragmentListViewAdapter(MainActivity context,
			List<ItemForNewestFragment> itemList,
			StickyListHeadersListView listView) {
		inflater = LayoutInflater.from(context);
		mContext = context;
		
		mItemList = itemList;
		mListView = listView;
		leftAndRightPadding = DimensionUtils.dpToPx(context, 8);
		topAndBottomPadding = DimensionUtils.dpToPx(context, 5);
	}

	public void setDataSet(List<ItemForNewestFragment> itemList) {
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.row_fragment_newest_lv,
					parent, false);
			holder.state = (TextView) convertView
					.findViewById(R.id.fragment_newest_tv_state);
			holder.content = (TextView) convertView
					.findViewById(R.id.fragment_newest_tv_content);
			holder.button = (ImageButton) convertView
					.findViewById(R.id.fragment_newest_btn_expand);
			holder.expandLayout = (FlowLayout) convertView
					.findViewById(R.id.fragment_newest_ll_expandable);

			convertView.setTag(holder);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);

			holder = (ViewHolder) convertView.getTag();
			holder.expandLayout.setBackgroundColor(Color.WHITE);
			if (position != expandedPosition) {
				holder.expandLayout.setVisibility(View.GONE);
				holder.button
						.setImageResource(R.drawable.ic_expand_more_black_24dp);
			} else {
				holder.expandLayout.setVisibility(View.VISIBLE);
				holder.button
						.setImageResource(R.drawable.ic_expand_less_black_24dp);
			}

		}

		
		holder.expandLayout.setTag("l" + position);
		holder.button.setTag("i" + position);
		
		holder.state.setText(mItemList.get(position).state);
		final String content = mItemList.get(position).content;
		holder.content.setText(content);
		
		
//		if (mContext == null) {
//			Log.d(TAG, "mcontent null");
//		}else {
//			Log.d(TAG, "mcontent not null");
//		}
//		if (content == null) {
//			Log.d(TAG, "content null");
//		}else {
//			Log.d(TAG, "content not null");
//		}
		
		if (mContext.isRead(content)) {
	//		System.out.println("set text color");
			holder.content.setTextColor(Color.GRAY);
		} else {
			holder.content.setTextColor(Color.BLACK);
		}
		final List<HashMap<String, String>> list = mItemList.get(position).detailList;

		holder.expandLayout.removeAllViews();
		//if (holder.expandLayout.getChildCount() == 0) {
			for (int i = 0; i < list.size(); i++) {
				final HashMap<String, String> map = list.get(i);
				String name = map
						.get(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_NAME);
				String linkUrl = map
						.get(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL);
				TextView tv = createLinkedTextView(name, linkUrl, content,
						leftAndRightPadding, topAndBottomPadding);

				holder.expandLayout.addView(tv);
			}
	//	}
		
		
		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*holder.expandLayout.removeAllViews();
				for (int i = 0; i < list.size(); i++) {
					final HashMap<String, String> map = list.get(i);
					String name = map
							.get(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_NAME);
					String linkUrl = map
							.get(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL);
					TextView tv = createLinkedTextView(name, linkUrl, content,
							leftAndRightPadding, topAndBottomPadding);

					holder.expandLayout.addView(tv);
				}*/

				toggle(position, holder.expandLayout, holder.button, mListView);
			}

		});

		if (MyApplication.isDarkModel()) {
			holder.expandLayout.setBackgroundColor(Color.LTGRAY);
			convertView.setBackgroundColor(Color.DKGRAY);
			if (mContext.isRead(content)) {
				holder.content.setTextColor(Color.GRAY);
			} else {
				holder.content.setTextColor(Color.WHITE);
			}
		}

		return convertView;
	}

	private TextView createLinkedTextView(String name, final String linkUrl,
			final String content, int leftAndRightPadding,
			int topAndBottomPadding) {
		TextView tv = new TextView(mContext);
		tv.setPadding(leftAndRightPadding, topAndBottomPadding,
				leftAndRightPadding, topAndBottomPadding);
		tv.setText(name);
		tv.setTextAppearance(mContext,
				android.R.style.TextAppearance_Widget_Button);
		tv.setTextColor(Color.BLUE);
		tv.setBackgroundResource(R.drawable.tv_normal_strock);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WebViewActivity.class);
				intent.putExtra(
						Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL,
						linkUrl);
				intent.putExtra("root", true);
				mContext.markAsRead(content);
				mContext.startActivity(intent);
			}
		});
		return tv;
	}

	private void toggle(final int position, ViewGroup expandLayout,
			ImageButton button, StickyListHeadersListView listView) {
		if (expandLayout.getVisibility() == View.VISIBLE) {
			expandLayout.setVisibility(View.GONE);
			((ImageButton) button)
					.setImageResource(R.drawable.ic_expand_more_black_24dp);
			expandedPosition = -1;
			//currentExpandedLayout = expandLayout;
		} else {
			if (expandedPosition != -1) {
				if ((listView.findViewWithTag("l" + expandedPosition) != null)
						&& (listView.findViewWithTag("i" + expandedPosition) != null)) {
					listView.findViewWithTag("l" + expandedPosition)
							.setVisibility(View.GONE);
					((ImageButton) listView.findViewWithTag("i"
							+ expandedPosition))
							.setImageResource(R.drawable.ic_expand_more_black_24dp);

				}else {
					
					//Toast.makeText(mContext, "find with tag return null", 1).show();
				}
//				if (listView.getChildAt(expandedPosition) != null) {
//					
//					((LinearLayout)listView.getChildAt(expandedPosition)).getChildAt(1).setVisibility(View.GONE);
//				}
				
				//currentExpandedLayout.setVisibility(View.GONE);
				
				//getViewByPosition(expandedPosition, listView).findViewById(R.id.fragment_newest_ll_expandable).setVisibility(View.GONE);
				

			} 

			expandLayout.setVisibility(View.VISIBLE);
			button.setImageResource(R.drawable.ic_expand_less_black_24dp);
			expandedPosition = position;
			//expandLayout.setTag("l" + position);
			//button.setTag("i" + position);
		}

	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(
					R.layout.row_fragment_newest_lv_header, parent, false);
			holder.groupByUpdateTime = (TextView) convertView
					.findViewById(R.id.fragment_newest_tv_group_by_update_time);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		String headerText = mItemList.get(position).updateTime;
		holder.groupByUpdateTime.setText(headerText);
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return Integer.parseInt(mItemList.get(position).updateTime.replaceAll(
				"-", "0"));
	}

	class HeaderViewHolder {
		TextView groupByUpdateTime;
	}

	class ViewHolder {
		TextView state;
		TextView content;
		ImageButton button;
		FlowLayout expandLayout;
	}

	public void notifyDataSetChanged(List<ItemForNewestFragment> list) {
		mItemList = list;
		super.notifyDataSetChanged();
	}
	
	
	/*public View getViewByPosition(int pos, StickyListHeadersListView listView) {
	    final int firstListItemPosition = listView.getFirstVisiblePosition();
	    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

	    if (pos < firstListItemPosition || pos > lastListItemPosition ) {
	        return listView.getAdapter().getView(pos, null, listView);
	    } else {
	        final int childIndex = pos - firstListItemPosition;
	        return listView.getChildAt(childIndex);
	    }
	}*/

}