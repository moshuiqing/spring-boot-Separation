package com.home.liuhao.other.weixin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 刘浩 微信全局变量
 */
@Component
public class WxGlobal {

	/**
	 * 域名+项目名
	 */
	public static String yuming;

	/**
	 * 与接口配置信息中Token要一致
	 */
	public static String TOKEN;

	/**
	 * 微信后台拿到的APPID
	 */
	public static String APPID;

	/**
	 * 微信后台拿到的APPSCRET
	 */
	public static String APPSECRET;

	/**
	 * 网页授权获取code
	 */
	public static String GetPageCode;

	/**
	 * 网页授权接口 获得token 和用户openid
	 */
	public static String GetPageAccessTokenUrl;

	/**
	 * 生成永久二维码要获取ticket的接口
	 */
	public static String Qrcode_ticket;

	/**
	 * 签名凭证
	 */
	public static String js_api_ticket_url;

	/**
	 * 获取accesstoken
	 */
	public static String ACCESS_TOKEN_URL;

	/**
	 * 获取临时二维码
	 */
	public static String lsewm;

	/**
	 * 获取用户信息的接口
	 */
	public static String wxuserinfo;

	/**
	 * 获取永久二维码图片
	 */
	public static String wxpicture;

	/**
	 * 发送模板消息
	 */
	public static String sendMessage;

	/**
	 * 扫码登录
	 */
	public static String saoma;
	
	/**
	 * 扫码回调
	 */
	public static String SMHD;
	
	@Value("${SMHD}")
	public void setSMHD(String sMHD) {
		SMHD = sMHD;
	}

	@Value("${saoma}")
	public void setSaoma(String saoma) {
		WxGlobal.saoma = saoma;
	}

	@Value("${APPID}")
	public void setAPPID(String aPPID) {
		APPID = aPPID;
	}

	@Value("${APPSECRET}")
	public void setAPPSECRET(String aPPSECRET) {
		APPSECRET = aPPSECRET;
	}

	@Value("${GetPageCode}")
	public void setGetPageCode(String getPageCode) {
		GetPageCode = getPageCode;
	}

	@Value("${GetPageAccessTokenUrl}")
	public void setGetPageAccessTokenUrl(String getPageAccessTokenUrl) {
		GetPageAccessTokenUrl = getPageAccessTokenUrl;
	}

	@Value("${Qrcode_ticket}")
	public void setQrcode_ticket(String qrcode_ticket) {
		Qrcode_ticket = qrcode_ticket;
	}

	@Value("${js_api_ticket_url}")
	public void setJs_api_ticket_url(String js_api_ticket_url) {
		WxGlobal.js_api_ticket_url = js_api_ticket_url;
	}

	@Value("${ACCESS_TOKEN_URL}")
	public void setACCESS_TOKEN_URL(String aCCESS_TOKEN_URL) {
		ACCESS_TOKEN_URL = aCCESS_TOKEN_URL;
	}

	@Value("${lsewm}")
	public void setLsewm(String lsewm) {
		WxGlobal.lsewm = lsewm;
	}

	@Value("${wxuserinfo}")
	public void setWxuserinfo(String wxuserinfo) {
		WxGlobal.wxuserinfo = wxuserinfo;
	}

	@Value("${wxpicture}")
	public void setWxpicture(String wxpicture) {
		WxGlobal.wxpicture = wxpicture;
	}

	@Value("${sendMessage}")
	public void setSendMessage(String sendMessage) {
		WxGlobal.sendMessage = sendMessage;
	}

	@Value("${yuming}")
	public void setYuming(String yuming) {
		WxGlobal.yuming = yuming;
	}

	@Value("${TOKEN}")
	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}

}
