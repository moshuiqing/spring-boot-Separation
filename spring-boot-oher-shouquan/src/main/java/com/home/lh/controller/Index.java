package com.home.lh.controller;

import java.io.IOException;
import java.util.UUID;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.home.lh.po.AuthorizeInfo;
import com.home.lh.util.Global;
import com.home.lh.util.XmlUtil;

@Controller
@RequestMapping("/index/")
public class Index {

	@RequestMapping("/")
	public String to() {
		/*
		 * tring text = HttpUtil.getCode("c1ebe466-1cdc-4bd3-ab69-77c3561b9dee", "code",
		 * "http%3A%2F%2Flocalhost%3A8888%2Findex%2FgetCode"); System.out.println(text);
		 */
		return "index";
	}

	@RequestMapping("getCode")
	public String getCode() {
		String requestUrl = null;
		try {

			// 配置请求参数，构建oauthd的请求。设置请求服务地址（authorizeUrl）、clientId、response_type、redirectUrl
			OAuthClientRequest accessTokenRequest = OAuthClientRequest.authorizationLocation(Global.authorizeUrl)
					.setResponseType("code").setClientId(Global.clientId).setState(UUID.randomUUID().toString())
					.setRedirectURI(Global.redirectUrl).buildQueryMessage();

			requestUrl = accessTokenRequest.getLocationUri();
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}

		System.out.println("==> 客户端重定向到服务端获取auth_code： " + requestUrl);
		return "redirect:" + requestUrl;
	}

	@RequestMapping("backCode")
	public Object backCode(String code, String state) {

		System.out.println("==> 服务端回调，获取的code：" + code);
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		try {
			XmlUtil util = new XmlUtil();
			AuthorizeInfo info = null;
			try {
				info = util.getGlobal();
			} catch (Exception e) {
				info=null;
				//e.printStackTrace();
			}
			if (info == null) {
				sehzhi(oAuthClient,code,util);
			}
			if(info!=null) {
				Long time1= info.getTime();
				Long time2 = System.currentTimeMillis();
				Long  chai =   time2-time1;
				System.out.println("剩余获取时间"+chai/1000+"秒");
				if(chai>=(info.getExpiresIn()*1000)) {					
					sehzhi(oAuthClient,code,util);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/main";

	}

	private void sehzhi(OAuthClient oAuthClient,String code,XmlUtil util) throws OAuthSystemException, OAuthProblemException, IOException {

		OAuthClientRequest accessTokenRequest = OAuthClientRequest.tokenLocation(Global.AccessTokenUrl)
				.setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(Global.clientId)
				.setClientSecret(Global.clientSecret).setCode(code).setRedirectURI(Global.redirectUrl)
				.buildQueryMessage();

		// 去服务端请求access token，并返回响应
		OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
		// 获取服务端返回过来的access token
		String accessToken = oAuthResponse.getAccessToken();
		// 查看access token是否过期
		Long expiresIn = oAuthResponse.getExpiresIn();
		System.out.println("==> 客户端根据 code值 " + code + " 到服务端获取的access_token为：" + accessToken + " 过期时间为：" + expiresIn+"秒");

		System.out.println("==> 拿到access_token然后重定向到 客户端 /oauth-client/getUserInfo服务,传过去accessToken");
		// 保存token
		AuthorizeInfo obj = new AuthorizeInfo();
		obj.setAccessToken(accessToken);
		obj.setExpiresIn(expiresIn);
		obj.setTime(System.currentTimeMillis());
		String xmlinfo = XmlUtil.textMessageToXml(obj);
		util.writeXml(xmlinfo);
	}

	// 接受服务端传回来的access token，由此token去请求服务端的资源（用户信息等）
	@RequestMapping("getUserInfo")
	@ResponseBody
	public Object accessToken() {

		XmlUtil util = new XmlUtil();
		AuthorizeInfo info = null;
		try {
			info = util.getGlobal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String accessToken = info.getAccessToken();
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		try {
			OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest(Global.UserInfoUrl)
					.setAccessToken(accessToken).buildQueryMessage();

			OAuthResourceResponse resourceResponse = oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.POST,
					OAuthResourceResponse.class);
			String body = resourceResponse.getBody();
			System.out.println("==> 客户端通过accessToken：" + accessToken + "  从服务端获取用户信息为：" + body);

			return JSONObject.parse(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
