package com.home.lh.system.config.shiro.po;

public class LoginType {
	
	
	/**
	 * 后台登录类型
	 */
	public final static String BACKSYSTEM="BackUserRealm";
	
	
	/**
	 * 前台登录类型
	 */
	public final static String WEBBUESS="WebUserRealm";
	
	
	/**
	 * 微信扫码免密码登录
	 */
	public final static  String NOPASSWD = "WxUserRealm";
	
	/**
	 * app扫码类型
	 */
	public final static String APPSM="AppSmRealm";
	
	/**
	 * app登录类型
	 */
	public final static String APP="AppUserRealm";

}
