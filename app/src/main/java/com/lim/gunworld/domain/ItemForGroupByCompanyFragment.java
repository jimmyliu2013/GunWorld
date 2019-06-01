package com.lim.gunworld.domain;

import java.util.HashMap;
import java.util.List;

public class ItemForGroupByCompanyFragment {

	public String companyNameInEnglish;
	public String companyName;
	public boolean subMenu;
	public String url;
	//public List<HashMap<String, String>> subMenuList;
	
	
	public ItemForGroupByCompanyFragment(String companyName,
			String companyNameInEnglish, boolean subMenu, String url) {
		super();
		this.companyName = companyName;
		this.companyNameInEnglish = companyNameInEnglish;
		this.subMenu = subMenu;
		this.url = url;
	}
	
	
	
	
}
