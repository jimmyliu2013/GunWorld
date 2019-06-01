package com.lim.gunworld.application;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {

	private static final String TAG = "MyApplication";
	private static boolean isDarkModel;
	private static int textSize;

	public static int getTextSize() {
		return textSize;
	}

	public static void setTextSize(int textSize) {
		MyApplication.textSize = textSize;
	}

	public static boolean isDarkModel() {
		return isDarkModel;
	}

	public static void setDarkModel(boolean isDarkMode) {
		isDarkModel = isDarkMode;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isDarkModel = getSharedPreferences("settings", MODE_PRIVATE).getBoolean("mode", false);
		textSize = getSharedPreferences("settings", MODE_PRIVATE).getInt("size", 2);
		
	}
	
	
	
	

	
	
	
}
