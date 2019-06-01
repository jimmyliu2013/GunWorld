package com.lim.gunworld.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	private static final String TAG = "BaseFragment";
	protected Context mContext;
	public View rootView;
	protected boolean isViewCreated;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			sendStateToMainActivityWhenVisible();
		} else {
			sendStateToMainActivityWhenNotVisible();
		}
	}

	protected abstract void sendStateToMainActivityWhenNotVisible();

	protected abstract void sendStateToMainActivityWhenVisible();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		// 如果rootview已经有了，就直接用，没有才初始化
		if (rootView == null) {
			initView(inflater, container);
		}
		if (savedInstanceState == null || rootView != null) {
			initData();
		} else {
			restoreView();
		}
		isViewCreated = true;

		return rootView;
	}

	public void closeDrawer() {

	}

	protected abstract void restoreView();

	@Override
	public void onDestroyView() {
		if (rootView != null) {
			if (rootView.getParent() != null) {
				((ViewGroup) rootView.getParent()).removeView(rootView);
			}
		}
		changeStateWhenDestroy();

		super.onDestroyView();
	}

	public void doSthAfterRefreshIconReady() {

	}

	protected abstract void initData();

	protected abstract void changeStateWhenDestroy();

	protected abstract void refreshData();

	protected abstract void initView(LayoutInflater inflater,
			ViewGroup container);

	public void invalidateView() {
		if (rootView != null) {
			rootView.invalidate();
		} else {
		}
	}

}
