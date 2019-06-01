package com.lim.gunworld.utils;

import com.lim.gunworld.constant.Constant;

import android.content.Context;
import android.content.Intent;

public class BroadcastUtils {

	public static void sendBroadcast(Context context, String action){
		
		Intent intent = new Intent(action);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		context.sendBroadcast(intent);
				
	}
	
	
	public static void sendBroadcastWithString(Context context, String action, String data){
		
		Intent intent = new Intent(action);
		intent.putExtra(Constant.TITLE, data);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		context.sendBroadcast(intent);
				
	}
	
	
}
