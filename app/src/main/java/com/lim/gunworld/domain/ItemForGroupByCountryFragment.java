package com.lim.gunworld.domain;

import java.util.HashMap;
import java.util.List;

public class ItemForGroupByCountryFragment {

	public String countryNameInEnglish;
	public String countryName;
	public boolean subMenu;
	public String url;
	public List<HashMap<String, String>> subMenuList;
	
	
	public ItemForGroupByCountryFragment(String countryName,
			String countryNameInEnglish, boolean subMenu, String url) {
		super();
		this.countryName = countryName;
		this.countryNameInEnglish = countryNameInEnglish;
		this.subMenu = subMenu;
		this.url = url;
	}
	
	
	
	
}
