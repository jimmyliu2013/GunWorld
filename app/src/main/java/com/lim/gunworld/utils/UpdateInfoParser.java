package com.lim.gunworld.utils;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lim.gunworld.domain.UpdateInfo;

public class UpdateInfoParser {
	public static UpdateInfo getUpdateInfo(InputStream inStream)
			throws Exception {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		UpdateInfo updateInfo = new UpdateInfo();

		// ???????????????????
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// ???????????????????????????????
		DocumentBuilder builder = factory.newDocumentBuilder();
		// ???????????????????????????????
		Document document = builder.parse(inStream);
		// ???XML???????
		Element root = document.getDocumentElement();
		// ???????????
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			// ????????
			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				// ?��??
				if ("version".equals(childElement.getNodeName())) {
					// hashMap.put("version",childElement.getFirstChild().getNodeValue());
					updateInfo.setVersion(childElement.getFirstChild()
							.getNodeValue());
				}
				// ??????
				else if (("description".equals(childElement.getNodeName()))) {
					// hashMap.put("name",childElement.getFirstChild().getNodeValue());
					updateInfo.setDescription(childElement.getFirstChild()
							.getNodeValue());
				}
				// ??????
				else if (("url".equals(childElement.getNodeName()))) {
					// hashMap.put("url",childElement.getFirstChild().getNodeValue());
					updateInfo.setUrl(childElement.getFirstChild()
							.getNodeValue());
				}
			}
		}
		return updateInfo;
	}
}