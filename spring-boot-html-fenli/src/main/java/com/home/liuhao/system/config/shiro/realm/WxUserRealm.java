package com.home.liuhao.system.config.shiro.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.home.liuhao.system.config.shiro.UserToken;
import com.home.liuhao.webbussess.po.User;
import com.home.liuhao.webbussess.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信扫码后走的逻辑
 * @author liuhao
 *
 */
@Slf4j
public class WxUserRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	
		log.info("微信扫码开发验证");
		UserToken userToken = (UserToken) token;
		
		 // 1. 获取用户输入的账号
		   String openid = userToken.getUsername();
		   if(openid!=null && !openid.equals("")) {
			   User u = new User();
			   u.setOpenid(openid);
			   List<User> users= userService.simpleFound(u);
			   // 判断是否有userInfo
			   if(users.isEmpty()){
			      return null;
			   }
			   
			   u = users.get(0);
			   if(u.getIsdisable().equals("1")) {
				   throw new ExcessiveAttemptsException("您的账号已被禁用，请联系管理员！");
			   }
			   
			   SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(u, u.getPassword(), ByteSource.Util.bytes(u.getSalt()), this.getName());
			   return simpleAuthenticationInfo;

			   
		   }
		
		
		return null;
	}

}
