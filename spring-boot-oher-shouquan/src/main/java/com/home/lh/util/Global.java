package com.home.lh.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class Global {
	
	
    /**
     *  授权id
     */
	public static String clientId;

   
    /**
     * 获取code地址
     */
    public static String authorizeUrl;

   
    /**
     * 回调地址
     */
    public static String redirectUrl;

   
    /**
     * 客户端凭证
     */
    public static String clientSecret;
    
    /**
     * 获取token的链接
     */
    public static String AccessTokenUrl;
    
    /**
     * 获取用户信息的链接
     */
    public static String UserInfoUrl;
    
  
	@Value("${AccessTokenUrl}")
    public  void setAccessTokenUrl(String accessTokenUrl) {
		AccessTokenUrl = accessTokenUrl;
	}
    @Value("${gUserInfoUrl}")
	public  void setgUserInfoUrl(String gUserInfoUrl) {
		Global.UserInfoUrl = gUserInfoUrl;
	}

	@Value("${clientId}")
	public  void setClientId(String clientId) {
		Global.clientId = clientId;
	}

    @Value("${authorizeUrl}")
	public  void setAuthorizeUrl(String authorizeUrl) {
		Global.authorizeUrl = authorizeUrl;
	}
    @Value("${redirectUrl}")
	public  void setRedirectUrl(String redirectUrl) {
		Global.redirectUrl = redirectUrl;
	}
    @Value("${clientSecret}")
	public  void setClientSecret(String clientSecret) {
		Global.clientSecret = clientSecret;
	}
   
    
    
    


}
