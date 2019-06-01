package com.lim.gunworld.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForNewestFragment;


public class CacheUtils {

	// private SharedPreferences mSharedPreferences;

	public static void putItemForNewestFragmentList(Context context,
			List<ItemForNewestFragment> list) {


		Gson gson = new Gson();
		String jString = gson.toJson(list);
		
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constant.CACHE_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit()
				.putString(Constant.ITEM_FOR_NEWEST_FRAGMENT_STRING, jString)
				.commit();

	}

	public static List<ItemForNewestFragment> getItemForNewestFragmentList(Context context)
			 {

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constant.CACHE_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		String jString = sharedPreferences.getString(
				Constant.ITEM_FOR_NEWEST_FRAGMENT_STRING, null);

		if (jString != null) {
			Gson gson = new Gson();
			List<ItemForNewestFragment> list = gson.fromJson(jString,
					new TypeToken<List<ItemForNewestFragment>>() {
					}.getType());
			return list;

		} else {
			return null;
		}

	}

}
