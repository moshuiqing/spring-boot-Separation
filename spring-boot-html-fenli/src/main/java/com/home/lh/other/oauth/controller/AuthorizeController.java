package com.home.lh.other.oauth.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.lh.other.oauth.po.Status;
import com.home.lh.other.oauth.po.ThirdUser;
import com.home.lh.other.oauth.service.ClientService;
import com.home.lh.webbussess.po.User;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 *
 * 0、代码的作用: 1、首先通过如
 * http://localhost/oauthserver/authorize?response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A9080%2Foauth-client%2FcallbackCode&client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee
 * 2、该控制器首先检查clientId是否正确；如果错误将返回相应的错误信息 3、然后判断用户是否登录了，如果没有登录首先到登录页面登录
 */

@Controller
@RequestMapping("/oauthserver/")
@Api(value = "AuthorizeController", tags = "授权控制器")
@Slf4j
public class AuthorizeController {

	@Autowired
	private ClientService clientService;

	/**
	 * @param m
	 * @param request
	 * @return info返回授权码code
	 * @throws OAuthSystemException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/authorize")
	public Object authorize(Model m, HttpServletRequest request) throws OAuthSystemException, URISyntaxException {

		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			// 根据传入的clientId 判断 客户端是否存在
			if (!clientService.checkClientId(oauthRequest.getClientId())) {
				// 生成错误信息,告知客户端不存在
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("客户端验证失败，如错误的client_id/client_secret").buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}
			// 判断用户是否登录
			Subject subject = SecurityUtils.getSubject();
			// 如果用户没有登录,跳转到登录页面
			if (!subject.isAuthenticated()) {
				if (!clientService.login(subject, request)) {
					// 登录失败时跳转到登陆页面
					m.addAttribute("client", clientService.findByClientId(oauthRequest.getClientId()));
					return "/liuhao/page/other/oauth/login";
				}
			}
			User webUser = new User();
			Object obj =  subject.getPrincipal();
			if(obj!=null) {
				
				try {
					BeanUtils.copyProperties(webUser, obj);
				} catch (Exception e) {
					log.info("错误！");
				}
				
			}
			// 生成授权码
			String authorizationCode = null;
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
				authorizationCode = oAuthIssuer.authorizationCode();				
				// 把授权码放到缓存中
				clientService.addAuthCode(authorizationCode, webUser);
			}
			// 进行OAuth响应构建
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
					HttpServletResponse.SC_FOUND);
			// 设置授权码
			builder.setCode(authorizationCode);
			// 根据客户端重定向地址
			String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
			// 构建响应
			final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

			// 根据OAuthResponse 返回 ResponseEntity响应
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
		} catch (OAuthProblemException e) {
			// 出错处理
			String redirectUri = e.getRedirectUri();
			if (OAuthUtils.isEmpty(redirectUri)) {
				// 告诉客户端没有传入redirectUri直接报错
				return new ResponseEntity("告诉客户端没有传入redirectUri直接报错！", HttpStatus.NOT_FOUND);
			}
			// 返回错误消息
			final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
		}

	}

	@RequestMapping("accessToken")
	public Object token(HttpServletRequest request) throws OAuthSystemException {
		try {
			// 构建Oauth请求
			OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);

			// 检查提交的客户端id是否正确
			if (!clientService.checkClientId(oAuthTokenRequest.getClientId())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT).setErrorDescription("客户端验证失败，client_id错误！")
						.buildJSONMessage();
				return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			// 检查客户端安全Key是否正确
			if (!clientService.checkClientSecret(oAuthTokenRequest.getClientSecret())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
						.setErrorDescription("客户端验证失败，client_secret错误！").buildJSONMessage();
				return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			String authCode = oAuthTokenRequest.getParam(OAuth.OAUTH_CODE);

			// 检查验证类型，此处只检查AUTHORIZATION类型，其他的还有PASSWORD或者REFRESH_TOKEN
			if (oAuthTokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				if (!clientService.checkAuthCode(authCode)) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT).setErrorDescription("code错误！")
							.buildJSONMessage();
					return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
				}
			}

			// 生成Access Token
			OAuthIssuer issuer = new OAuthIssuerImpl(new MD5Generator());
			final String accessToken = issuer.accessToken();
			clientService.addAccessToken(accessToken,authCode);

			// 生成OAuth响应
			OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken).setExpiresIn(String.valueOf(7200)).buildJSONMessage();

			return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
		} catch (OAuthProblemException e) {
			OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildBodyMessage();
			return new ResponseEntity<String>(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
		}
	}
	
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="getUserInfo",produces="application/json;charset=UTF-8")
	    public HttpEntity userInfo(HttpServletRequest request,HttpServletResponse res) throws OAuthSystemException, IOException {
	        try {

	            //构建OAuth资源请求
	            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
	            //获取Access Token
	            String accessToken = oauthRequest.getAccessToken();
	            		
	            //验证Access Token
	            if (!clientService.checkAccessToken(accessToken)) {
	                // 如果不存在/过期了，返回未验证错误，需重新验证
	                OAuthResponse oauthResponse = OAuthRSResponse
	                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
	                        .setRealm("oauthserver")
	                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
	                        .buildHeaderMessage();

	                HttpHeaders headers = new HttpHeaders();
	                res.setCharacterEncoding("Utf-8");
	                headers.add("Content-Type", "application/json; charset=utf-8");
	                res.addHeader(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
	               
	                PrintWriter writer = res.getWriter();
	                writer.write(JSONObject.fromObject(getStatus(HttpStatus.UNAUTHORIZED.value(),"accessToken无效或已过期。")).toString());
	                writer.flush();
	                writer.close();
	                return null;
	            }
	            //返回用户名
	            User appUser = clientService.getUserByToken(accessToken);
	            ThirdUser thirdUser = new ThirdUser();
	            thirdUser.setUserName(appUser.getUserName());
	            thirdUser.setHeadImg(appUser.getHeadImg());
	            thirdUser.setRemake(appUser.getRemark());
	            
	            return new ResponseEntity(thirdUser, HttpStatus.OK);
	        } catch (OAuthProblemException e) {
	            //检查是否设置了错误码
	            String errorCode = e.getError();
	            if (OAuthUtils.isEmpty(errorCode)) {
	                OAuthResponse oauthResponse = OAuthRSResponse
	                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
	                        .setRealm("fxb")
	                        .buildHeaderMessage();

	                HttpHeaders headers = new HttpHeaders();
	                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
	                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
	            }

	            OAuthResponse oauthResponse = OAuthRSResponse
	                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
	                    .setRealm("oauthserver")
	                    .setError(e.getError())
	                    .setErrorDescription(e.getDescription())
	                    .setErrorUri(e.getUri())
	                    .buildHeaderMessage();

	            HttpHeaders headers = new HttpHeaders();
	            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }
	    }

	  private Status getStatus(int code,String msg){
	        Status status = new Status();
	        status.setCode(code);
	        status.setMsg(msg);
	        return status;
	    }
	

}
