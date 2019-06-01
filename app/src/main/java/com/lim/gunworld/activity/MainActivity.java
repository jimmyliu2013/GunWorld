package com.lim.gunworld.activity;

import java.io.File;
import java.io.StringBufferInputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lim.gunworld.R;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.fragment.BaseFragment;
import com.lim.gunworld.fragment.FragmentFactory;
import com.lim.gunworld.fragment.GroupByCountryFragment;
import com.lim.gunworld.fragment.NewestFragment;
import com.viewpagerindicator.TabPageIndicator;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

	private Set<Integer> mAlreadyRead;
	private MyPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private long exitTime = 0;
	private ActionBar actionBar;
	private boolean modeFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
            finish();
            return;
     }
		
		
		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();
		loadAlreadyReadCache();


		initView();


	}

	
	public void changeActionBarTitle(String title) {
		if (title != null && title != "") {
			actionBar.setTitle(title);
		}else{
			actionBar.setTitle(Constant.DEFAULT_TITLE);
		}
	}
	
	public void showBackIcon(boolean showBackIcon){
		if (showBackIcon) {
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}else{
			actionBar.setHomeButtonEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
				
	}
	

	@Override
	protected void onDestroy() {
		getSharedPreferences("settings", MODE_PRIVATE).edit().putBoolean("mode", MyApplication.isDarkModel()).commit();
		getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("size", MyApplication.getTextSize()).commit();
		
		super.onDestroy();
	}



	private void initView() {
		actionBar = getSupportActionBar();
		String[] categoryList = getResources().getStringArray(
				R.array.category_list);
		viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
		viewPager.setOffscreenPageLimit(1);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.activity_main_pageindicator);
		// 去掉黄色边
		indicator.setOverScrollMode(TabPageIndicator.OVER_SCROLL_NEVER);

		pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
				categoryList);
		viewPager.setAdapter(pagerAdapter);
		indicator.setViewPager(viewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_menu, menu);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			((BaseFragment) (pagerAdapter.getItem(viewPager
					.getCurrentItem()))).closeDrawer();
			return true;
		case R.id.actionbar_search:

			Intent intent = new Intent(MainActivity.this, SearchActivity.class);
			startActivity(intent);
			
			
			return true;

		case R.id.actionbar_settings:

			Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent1);
			
			return true;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		modeFlag = MyApplication.isDarkModel();
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (modeFlag != MyApplication.isDarkModel()) {
			invalidateFragmentView();
		}
	}

	
	private void invalidateFragmentView(){
		((BaseFragment)pagerAdapter.getItem(viewPager.getCurrentItem())).invalidateView();
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			BaseFragment bf = ((BaseFragment)pagerAdapter.getItem(i));
			if (bf != null) {
				bf.invalidateView();
			}
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (viewPager.getCurrentItem() == 1 ){
				if (((GroupByCountryFragment)(pagerAdapter.getItem(1))).fragmentState.isShowingSubView == true) {
					((BaseFragment) (pagerAdapter.getItem(viewPager
							.getCurrentItem()))).closeDrawer();
					return true;
				}else {
					exit();
					return true;
				}
				
				
				
			} else {
				exit();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	public void exit() {

		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}




	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void loadAlreadyReadCache() {
		if (mAlreadyRead == null) {
			mAlreadyRead = new HashSet<Integer>();
		}

		SharedPreferences sharedPref = getSharedPreferences(
				Constant.ALREADY_READ_ARTICLES_KEY, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		Map<String, ?> read = sharedPref.getAll();
		Long now = new Date().getTime();

		for (Map.Entry<String, ?> entry : read.entrySet()) {
			Long readAt = (Long) entry.getValue();
			Long diff = (now - readAt) / (24 * 60 * 60 * 1000);
			if (diff >= 2) {
				//editor.remove(entry.getKey());
			} else {
				mAlreadyRead.add(entry.getKey().hashCode());
			}
		}
		editor.commit();
	}

	public void markAsRead(String title) {
		Long now = new Date().getTime();
		Editor editor = getSharedPreferences(
				Constant.ALREADY_READ_ARTICLES_KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putLong(title, now);
		editor.commit();

		mAlreadyRead.add(title.hashCode());
	}

	public boolean isRead(String title) {
		if (mAlreadyRead != null) {
			//Log.d("MainActivity", "malreadyread not null");
			return mAlreadyRead.contains(title.hashCode());
		}else {

			//Log.d("MainActivity", "malreadyread null");
			return false;
		}
	}

	/**
	 * viewpager的adapter
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		private String[] mCategory;

		public MyPagerAdapter(FragmentManager fm, String[] category) {
			super(fm);
			mCategory = category;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub

			return FragmentFactory.getInstance().getFragment(arg0);
		}

		@Override
		public int getCount() {
			return mCategory.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mCategory[position];
		}

	}



}
