package com.home.lh.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import com.home.lh.po.AuthorizeInfo;
import com.thoughtworks.xstream.XStream;

public class XmlUtil {

	/**
	 * 文本消息转化为xml
	 * 
	 * @param textMessage
	 * @return
	 * @throws IOException 
	 */
	public static String textMessageToXml(Object textMessage) throws IOException {
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		String text = xstream.toXML(textMessage);
		return text; 

	}

	/**
	 * @param filename 文件名称
	 * @param info     文件内容
	 * @throws IOException
	 */
	public void writeXml(String info) throws IOException {	
		ClassPathResource cpr = new ClassPathResource("static/xml/authorize.xml");
		File file =cpr.getFile();
		FileWriter writer = new FileWriter(file);
		writer.write(info);
		writer.flush();
		writer.close();

	}

	// 读取xml文件
	public AuthorizeInfo getGlobal() throws DocumentException, ClientProtocolException, IOException {
		// 创建saxReader对象
		SAXReader reader = new SAXReader();
		ClassPathResource cpr = new ClassPathResource("static/xml/authorize.xml");
		File file =cpr.getFile();
		// 通过read方法读取一个文件 转换成Document对象
		Document document = reader.read(file);
		// 获取根节点元素对象
		Element node = document.getRootElement();
		// 获取access_token
		Element element1 = node.element("accessToken");
		// 获取ticket
		Element element2 = node.element("expiresIn");
		// 获取存的时间
		Element element3 = node.element("time");
		AuthorizeInfo global = new AuthorizeInfo(element1.getText(), Long.parseLong(element2.getText()),
				Long.parseLong(element3.getText()));

		return global;

	}

}
