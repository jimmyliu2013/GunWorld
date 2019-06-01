package com.lim.gunworld.fragment;

import java.util.HashMap;
import com.lim.gunworld.utils.CacheMap;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.util.Log;

public class FragmentFactory {

	private static final String TAG = "FragmentFactory";

	private HashMap<Integer, BaseFragment> fragmentMap;

	private static FragmentFactory factory = new FragmentFactory();

	@SuppressLint("UseSparseArrays")
	private FragmentFactory() {

		long maxMemory = Runtime.getRuntime().maxMemory();
		long enoughMemory = 1024 * 1024 * 32;

		fragmentMap = new HashMap<Integer, BaseFragment>();
	}

	public static FragmentFactory getInstance() {

		return factory;

	}

	public void clear() {
		fragmentMap.clear();

	}

	public Fragment getFragment(int position) {

		Fragment fragment = null;

		switch (position) {
		case 0:
			if (fragmentMap.get(0) != null) {
				fragment = fragmentMap.get(0);
			} else {
				NewestFragment newestFragment = new NewestFragment();
				fragmentMap.put(0, newestFragment);
				fragment = newestFragment;
			}
			break;

		case 1:
			if (fragmentMap.get(1) != null) {
				fragment = fragmentMap.get(1);
			} else {
				GroupByCountryFragment groupByCountryFragment = new GroupByCountryFragment();
				fragmentMap.put(1, groupByCountryFragment);
				fragment = groupByCountryFragment;
			}
			break;

		case 2:
			if (fragmentMap.get(2) != null) {
				fragment = fragmentMap.get(2);
			} else {
				GroupByCompanyFragment groupByCompanyFragment = new GroupByCompanyFragment();
				fragmentMap.put(2, groupByCompanyFragment);
				fragment = groupByCompanyFragment;
			}
			break;

		case 3:
			if (fragmentMap.get(3) != null) {
				fragment = fragmentMap.get(3);
			} else {
				ClassicFragment classicFragment = new ClassicFragment();
				fragmentMap.put(3, classicFragment);
				fragment = classicFragment;
			}
			break;

		default:
			break;
		}

		return fragment;

	}

}
