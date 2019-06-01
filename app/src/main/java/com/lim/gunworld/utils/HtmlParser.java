package com.lim.gunworld.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;

import com.lim.gunworld.constant.Constant;
import com.lim.gunworld.domain.ItemForNewestFragment;


public class HtmlParser {

	public static List<ItemForNewestFragment> getNewestFragmentDataFromWeb(
			String url) throws IOException {
		int itemListSize = 0;
		Connection conn = Jsoup
				.connect(url)
				.timeout(50000)
				.header("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");
		Response response = conn.execute();

		Document doc = response.parse();

		Elements trList = doc.select("table[id=table2]").select("tr");

		List<ItemForNewestFragment> itemList = new ArrayList<ItemForNewestFragment>();

		// 从第二条起，第一条是“最近更新”四个字，舍去不要
		for (int i = 1; i < trList.size(); i++) {

			// 从tr中得到第一个td，是这整个tr的更新时间
			String updateTime = trList.get(i).select("td").get(0).text();
			// 从tr中得到第二个td，包含很多p标签，是消息主体
			Elements pList = trList.get(i).select("td").get(1).select("p");

			for (int j = 0; j < pList.size(); j++) {

				// 每条item的状态
				String state = pList.get(j).select("b").text();
				// System.out.println(state);

				// 每条item的内容
				pList.get(j).select("b").remove();
				String content = pList.get(j).text();
				// System.out.println(content);

				Elements aList = pList.get(j).select("a");

				// 每条item的包含详细链接的list
				List<HashMap<String, String>> detailList = new ArrayList<HashMap<String, String>>();
				for (int k = 0; k < aList.size(); k++) {

					HashMap<String, String> map = new HashMap<String, String>();
					String name = aList.get(k).text();
					String linkUrl = Constant.SERVER_ROOT_URL
							+ aList.get(k).attr("href");
					map.put(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_NAME,
							name);
					map.put(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL,
							linkUrl);

					detailList.add(map);
				}

				// 不规范的舍去不要了
				if (detailList.size() != 0
						&& (state.contains("NEW") || state.contains("UPDATE"))) {

					ItemForNewestFragment itemForNewestFragment = new ItemForNewestFragment(
							updateTime, content, state, detailList);
					itemList.add(itemForNewestFragment);
					itemListSize += 1;
					// 有偏差，先添加后判断，可能超出一点
					if (itemListSize >= Constant.MAX_SIZE_OF_ITEMLIST) {
						break;
					}
				}

			}

		}
		return itemList;

	}

	public static List<ItemForNewestFragment> getClassicFragmentDataFromWeb(
			String url) throws IOException {
		int itemListSize = 0;
		Connection conn = Jsoup.connect(url)
				.timeout(50000)
				// 设置连接超时
				.header("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");
		Response response = conn.execute();

		Document doc = response.parse();

		// 得到table2为id的标签，包含很多个tr，每个tr是同一日期更新的
		Elements trList = doc.select("table[id=table2]").select("tr");
		// System.out.println(tabList.get(1).html());

		// List<String> dateList = new ArrayList<String>();
		List<ItemForNewestFragment> itemList = new ArrayList<ItemForNewestFragment>();

		// 从第二条起，第一条是“最近更新”四个字，舍去不要
		for (int i = 1; i < trList.size(); i++) {

			// 从tr中得到第一个td，是这整个tr的更新时间
			String updateTime = trList.get(i).select("td").get(0).text();
			// System.out.println(updateTime);
			// dateList.add(updateTime);
			// 从tr中得到第二个td，包含很多p标签，是消息主体
			Elements pList = trList.get(i).select("td").get(1).select("p");

			for (int j = 0; j < pList.size(); j++) {

				// 每条item的状态
				String state = pList.get(j).select("b").text();
				// System.out.println(state);

				// 每条item的内容
				pList.get(j).select("b").remove();
				String content = pList.get(j).text();
				// System.out.println(content);

				Elements aList = pList.get(j).select("a");

				// 每条item的包含详细链接的list
				List<HashMap<String, String>> detailList = new ArrayList<HashMap<String, String>>();
				for (int k = 0; k < aList.size(); k++) {

					HashMap<String, String> map = new HashMap<String, String>();
					String name = aList.get(k).text();
					String linkUrl = Constant.SERVER_ROOT_URL
							+ aList.get(k).attr("href");
					map.put(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_NAME,
							name);
					map.put(Constant.ITEM_FOR_NEWEST_FRAGMENT_DETAIL_ITEM_LINKURL,
							linkUrl);

					detailList.add(map);
				}

				// 不规范的舍去不要了
				if (detailList.size() != 0
						&& (state.contains("NEW") || state.contains("UPDATE"))) {

					ItemForNewestFragment itemForNewestFragment = new ItemForNewestFragment(
							updateTime, content, state, detailList);
					itemList.add(itemForNewestFragment);
					itemListSize += 1;
					// 有偏差，先添加后判断，可能超出一点
					if (itemListSize >= Constant.MAX_SIZE_OF_ITEMLIST) {
						break;
					}
				}

			}

		}
		return itemList;

	}

	public static List<HashMap<String, String>> getSubMenuListFromWeb(String url)
			throws IOException {
		// TODO Auto-generated method stub
		Connection conn = Jsoup.connect(url)
				.timeout(50000)
				// 设置连接超时
				.header("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");
		Response response = conn.execute();

		Document doc = response.parse();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// 得到table2为id的标签，包含很多个tr，每个tr是同一日期更新的
		Elements pList = doc.select("p");// doc.select("table[id=table1]").select("p");

		for (int i = 2; i < pList.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String name = pList.get(i).text();
			String linkUrl = pList.get(i).select("a").attr("href");
			map.put(Constant.Name, name);
			map.put(Constant.LINK_URL, linkUrl);
			list.add(map);

		}

		return list;
	}

}
