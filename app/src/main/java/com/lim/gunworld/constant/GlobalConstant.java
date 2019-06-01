package com.lim.gunworld.constant;

public interface GlobalConstant {
 
	public int SUCCESS = 0; 
	public int FAIL = 1;
	public int END = 2; 
	public String DEFAULT_INDUSTRY = "通用";
	public String DEFAULT_CATAGORY = "高频";
	public String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.47 Safari/537.36";
	public int LoadForFristTime = 3;
	public int LoadMore = 4;
	public int LoadNewest = 5;
	public int CURRENT_PAGE = 6;
	public int NEXT_PAGE = 7;
	public String BAIDU_SERVER_UPDATE_URL = "http://paradisecity.bj.bcebos.com/update/gunworld_update.xml?responseContentDisposition=attachment";
	public int CHECKUPDATE = 8;
	public int NO_NEED_UPDATE_CLIENT = 9;
	public int UPDATE_CLIENT = 10;
	public int GET_UPDATEINFO_ERROR = 11;
	public int DOWN_ERROR = 12;
	public String ROOT_URL = "http://interviewassistant.duapp.com";//"http://192.168.1.105:8080/interviewassistant";
	public int INSERT_SDCARD = 13;
}
