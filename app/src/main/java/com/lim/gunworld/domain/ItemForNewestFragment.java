package com.lim.gunworld.domain;

import java.util.HashMap;
import java.util.List;
/**
 * 首页的list上显示的条目
 * @author Administrator
 *
 */
public class ItemForNewestFragment {
	
	
	
public String updateTime;
public String content;
public String state;
public List<HashMap<String, String>> detailList;





public ItemForNewestFragment(String updateTime, String content, String state,List<HashMap<String, String>> detailList) {
	this.updateTime = updateTime;
	this.content = content;
	this.state = state;
	this.detailList = detailList;
}




}
