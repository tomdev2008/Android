package com.ruyicai.util;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.widget.Toast;

public class ParseXML {

	/**
	 * 
	 * @作者：黄轲
	 * @日期：2011/3/10
	 * @参数：url
	 * @返回值：版本号
	 * @用途：解析服务器上保存软件最新的版本号
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：1.0
	 */
	public static String parseVersionXml(String url, Context context) {

		String version = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			URL u = new URL(url);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(u.openStream());
			NodeList nodeList = document.getElementsByTagName("MODEL");
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (document.getElementsByTagName("OS").item(i).getFirstChild() != null) {
					String versionVal = document.getElementsByTagName(
							"CURRENT_SOFT_VERSION").item(i).getFirstChild()
							.getNodeValue();
					version += "," + versionVal;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT);
		}
		String[] versions = version.split(",");
		return versions[1];
	}

}
