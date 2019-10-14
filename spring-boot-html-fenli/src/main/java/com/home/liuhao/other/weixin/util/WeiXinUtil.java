package com.home.liuhao.other.weixin.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import com.home.liuhao.other.weixin.entity.AccessToken;
import com.home.liuhao.other.weixin.entity.JsapiTicket;
import com.home.liuhao.other.weixin.entity.WeiXinMoBan;
import com.home.liuhao.other.weixin.entity.WeiXinUserInfo;
import com.home.liuhao.other.weixin.entity.WeixinGlobal;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;

/**
 * @author liuhao
 * 
 *         微信工具类
 *
 */
public class WeiXinUtil {

	/**
	 * @param url get请求
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClientBuilder.create().build();// 获取defaulthttpclient请求
		HttpGet get = new HttpGet(url);// httpget将使用get方式发送请求url
		JSONObject jsonObject = null;
		HttpResponse response = client.execute(get);// 使用httpresponse
													// 接收client执行httpget的结果
		HttpEntity entity = response.getEntity();// 从response
													// 中获取结果，类型为httpentity
		if (entity != null) {
			String result = EntityUtils.toString(entity, "Utf-8");// httpentity转为字符串类型
			jsonObject = JSONObject.fromObject(result);// 字符串类型转为json类型
		}
		client.close();
		return jsonObject;
	}

	/**
	 * @param url    post请求
	 * @param outStr
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url, String outStr) throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClientBuilder.create().build();// 获取defaulthttpclient
		// 请求
		HttpPost htppPost = new HttpPost(url);// post请求
		JSONObject jsonObject = null;
		htppPost.setEntity(new StringEntity(outStr, "Utf-8"));// 调用setEntity方法
		HttpResponse response = client.execute(htppPost);// 使用httpresponse
															// 接收client执行httpget的结果
		String result = EntityUtils.toString(response.getEntity(), "Utf-8");// httpentity转为字符串类型
		jsonObject = JSONObject.fromObject(result);// 字符串类型转为json类型
		client.close();
		return jsonObject;
	}

	/**
	 * 获取普通的accesstoken 全局两个小时
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static AccessToken getAccessToken() throws ClientProtocolException, IOException {
		AccessToken token = new AccessToken();
		JSONObject jsonObject = doGetStr(WxGlobal.ACCESS_TOKEN_URL.replace("APPID", WxGlobal.APPID).replace("APPSECRET", WxGlobal.APPSECRET));// 使用doget方法接收参数
		if (jsonObject != null) {// 如果返回不为空 ，将返回结果封装进AccessToken实体类
			token.setToken(jsonObject.getString("access_token"));// 获取access_token
			token.setExpiresIn(jsonObject.getInt("expires_in"));// 获取access_token的有效期
		}
		return token;
	}

	/**
	 * 
	 * @param code 获取关注人的信息 识别得到用户id必须的一个值 得到网页授权凭证和用户id
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> oauth2GetOpenid(String code) throws IOException {
		String appid = WxGlobal.APPID;// 自己的配置appid
		String appsecret = WxGlobal.APPSECRET;// 自己的配置APPSECRET;
		String requestUrl = WxGlobal.GetPageAccessTokenUrl.replace("APPID", appid).replace("SECRET", appsecret)
				.replace("CODE", code);
		CloseableHttpClient client = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			client = HttpClientBuilder.create().build();
			HttpGet httpget = new HttpGet(requestUrl);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = client.execute(httpget, responseHandler);
			JSONObject OpenidJSONO = JSONObject.fromObject(response);// 将String类型转为json
			String Openid = String.valueOf(OpenidJSONO.get("openid"));
			String AccessToken = String.valueOf(OpenidJSONO.get("access_token"));
			String Scope = String.valueOf(OpenidJSONO.get("scope"));// 用户保存的作用域
			String refresh_token = String.valueOf(OpenidJSONO.get("refresh_token"));
			String expires_in = String.valueOf(OpenidJSONO.get("expires_in"));

			result.put("Openid", Openid);
			result.put("AccessToken", AccessToken);
			result.put("scope", Scope);
			result.put("refresh_token", refresh_token);
			result.put("expires_in", expires_in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;

	}

	/**
	 * 获得ticket 全局两个小时
	 * 
	 * @param token
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JsapiTicket getJsapiTicket(String token) throws ClientProtocolException, IOException {
		JsapiTicket ticket = new JsapiTicket();
		String requestUrl = WxGlobal.js_api_ticket_url.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = WeiXinUtil.doGetStr(requestUrl);
		if (jsonObject.getString("errcode").equals("0")) {
			ticket.setTicket(jsonObject.getString("ticket"));
			ticket.setExpiresIn(jsonObject.getString("expires_in"));
		}
		System.out.println(ticket);
		return ticket;
	}

	/**
	 * 获取Sign 调用jssdk需要的验证信息传递到前端
	 * 
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		System.out.println(string1);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ret.put("url", url);
		// 注意这里 要加上自己的appId
		ret.put("appId", WxGlobal.APPID);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	/**
	 * 签名
	 * 
	 * @param hash
	 * @return
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	
	/**
	 * 随机字符串
	 * @return
	 */
	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	
	/**
	 * 签名的时间戳
	 * @return
	 */
	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	///*****************************************************************************/////
	
	
		/**
		 * 获取二维码需要的ticket 生成永久二维码
		 * @param info
		 * @return
		 * @throws ClientProtocolException
		 * @throws IOException
		 * @throws DocumentException
		 */
		public static String qrTicket(String info) throws ClientProtocolException,
				IOException, DocumentException {
			WeixinGlobal global = useGetGlobal();
			String url = WxGlobal.Qrcode_ticket.replace("TOKEN",
					global.getAccess_token());
			String zhi = "{\"action_name\": \"QR_LIMIT_STR_SCENE\",\"action_info\": {\"scene\": {\"scene_str\": \""+info+"\"}}}";
			
			JSONObject object = doPostStr(url, zhi);
			String qrticket = (String) object.get("ticket");
			System.out.println(qrticket);
			return qrticket;

		}
		

		/**
		 * 文本消息转化为xml
		 * 
		 * @param textMessage
		 * @return
		 */
		public static String textMessageToXml(Object textMessage) {
			XStream xstream = new XStream();
			xstream.alias("xml", textMessage.getClass());
			return xstream.toXML(textMessage);

		}
		
		/**
		 * 将xml转化为Map集合
		 * 
		 * @param request
		 * @return
		 */
		public static Map<String, String> xmlToMap(Document document) {
			Map<String, String> map = new HashMap<String, String>();
			Element rootElt = document.getRootElement();
			List<Element> list = rootElt.elements();
			for (Element e : list) {
				map.put(e.getName(), e.getText());
			}

			return map;
		}

		

		/**
		 * @return
		 * @throws DocumentException
		 * @throws ClientProtocolException
		 * @throws IOException
		 * 得到微信全局信息
		 */
		public static WeixinGlobal useGetGlobal() throws DocumentException,
				ClientProtocolException, IOException {
			WeiXinUtil weiXinUtil = new WeiXinUtil();

			return weiXinUtil.getGlobal();

		}
		
		// 读取xml文件
		public WeixinGlobal getGlobal() throws DocumentException,
				ClientProtocolException, IOException {
			ClassPathResource cpr = new ClassPathResource("static/xml/weixininfo.xml");
			File file =cpr.getFile();
			// 创建saxReader对象
			SAXReader reader = new SAXReader();
			
			if (file.length() == 0) {
				setWeixinInfo();
			}
			// 通过read方法读取一个文件 转换成Document对象
			Document document = reader.read(file);
			// 获取根节点元素对象
			Element node = document.getRootElement();
			// 获取access_token
			Element element1 = node.element("access__token");
			// 获取ticket
			Element element2 = node.element("ticket");
			// 获取存的时间
			Element element3 = node.element("datetime");
			WeixinGlobal global = new WeixinGlobal(element1.getText(),
					element2.getText(), Long.parseLong(element3.getText()));

			return global;

		}
		
		/**
		 * 动态的获取ACCESS_TOKEN和ticket并生成xml文件
		 * 
		 * @throws IOException
		 * @throws ClientProtocolException
		 */
		public static void setWeixinInfo() throws ClientProtocolException,
				IOException {

			// 获取token
			AccessToken token = getAccessToken();
			JsapiTicket ticket = getJsapiTicket(token.getToken());
			WeixinGlobal global = new WeixinGlobal();
			global.setAccess_token(token.getToken());
			global.setTicket(ticket.getTicket());
			global.setDatetime(System.currentTimeMillis());
			String xmlinfo = textMessageToXml(global);
			System.out.println(xmlinfo);
			useWriteXml(xmlinfo);

		}
		
		/**
		 * 写入文件
		 * 
		 * @param filename
		 *            文件名称
		 * @param info
		 *            文件信息
		 * @throws IOException
		 */
		public static void useWriteXml(String info) throws IOException {

			WeiXinUtil xinUtil = new WeiXinUtil();
			xinUtil.writeXml(info);

		}
		
		/**
		 * @param filename
		 *            文件名称
		 * @param info
		 *            文件内容
		 * @throws IOException
		 */
		public void writeXml(String info) throws IOException {

			ClassPathResource cpr = new ClassPathResource("static/xml/weixininfo.xml");
			File file =cpr.getFile();
			FileWriter writer = new FileWriter(file);
			writer.write(info);
			writer.close();

		}

		/**
		 * 获取用户信息
		 * @param code
		 * @return
		 * @throws IOException 
		 * @throws ClientProtocolException 
		 */
		public static WeiXinUserInfo getUserInfo(String code) throws ClientProtocolException, IOException{
			Map<String, Object> map = WeiXinUtil.oauth2GetOpenid(code);
			String openid = (String) map.get("Openid");
			String AccessToken = (String) map.get("AccessToken");
			// String Expires_in = (String) map.get("");
			// 获取用户信息
			String url = WxGlobal.wxuserinfo.replace("ACCESS_TOKEN", AccessToken)
					.replace("OPENID", openid);
			JSONObject object = WeiXinUtil.doGetStr(url);
			WeiXinUserInfo rule = (WeiXinUserInfo) JSONObject.toBean(object,
					WeiXinUserInfo.class);
			// System.out.println(rule.toString());
			return rule;
		}
		
		//获取临时二维码参数用作扫码登录
		public static String getErWeiMa(String code) throws ClientProtocolException, DocumentException, IOException{
			WeixinGlobal global= WeiXinUtil.useGetGlobal();
			String token = global.getAccess_token();
			String url = WxGlobal.lsewm.replace("TOKEN", token);
			
			String shuju ="{\"expire_seconds\": 180,\"action_name\": \"QR_LIMIT_STR_SCENE\",\"action_info\": {\"scene\": {\"scene_str\":\""+code+"\"}}}";
			
			JSONObject object = doPostStr(url, shuju);
			String uri = (String) object.get("url");
			return uri;
			
		}
		
		//模板推送 自定义
		public static String tuiSong(WeiXinMoBan moBan) throws ClientProtocolException, DocumentException, IOException{
			WeixinGlobal global = WeiXinUtil.useGetGlobal();//从xml中得到token和ticket
			String token=global.getAccess_token();
			String info = "{\"touser\":\""+moBan.getTouser()+"\",\"template_id\":\""+moBan.getTemplate_id()+"\",\"url\":\""+moBan.getUrl()+"\",\"data\":"+moBan.getData()+"}";
			String url = WxGlobal.sendMessage.replace("ACCESS_TOKEN",token);
			JSONObject object = WeiXinUtil.doPostStr(url, info);
			
			return object.getString("errmsg");
			
		}



}
