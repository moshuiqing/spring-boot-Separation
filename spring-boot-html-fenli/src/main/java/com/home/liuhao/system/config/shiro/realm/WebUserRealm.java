package com.home.liuhao.system.config.shiro.realm;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.home.liuhao.system.config.shiro.UserToken;
import com.home.liuhao.webbussess.po.User;
import com.home.liuhao.webbussess.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 前端用户realm
 * @author liuhao
 *
 */
@Slf4j
public class WebUserRealm extends AuthorizingRealm{
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EhCacheManager ehCacheManager;
	@Value("${number}")
	private Integer number;
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		log.info("web开始认证");
		User u = new User();	
		String password = null;
		UserToken userToken =(UserToken) token;
		char[] b = userToken.getPassword();
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			s.append(b[i]);
		}
		if(userToken.getUsername()!=null && !userToken.getUsername().equals("")) {
			u.setUserName(userToken.getUsername());
			u = userService.login(u);
			if(u!=null) {
				u.setLoginType(userToken.getLoginType());
				u.setStrmsg(s.toString());
			}
			
			if(u!=null && u.getIsdisable().equals("1")) {
				throw new ExcessiveAttemptsException("账号: " + u.getUserName() + "已被禁用，请联系管理员！");
			}
			
			if(u==null) {
				return null;
			}else {
				Cache<String, AtomicInteger> passwordRetryCache = ehCacheManager.getCache("webEacache");
				// retry count + 1 自增
				AtomicInteger retryCount = passwordRetryCache.get(u.getUserName() + "web");
				if (null == retryCount) {
					retryCount = new AtomicInteger(0);
					passwordRetryCache.put(u.getUserName() + "web", retryCount);
				}
				if (retryCount.incrementAndGet() > number) {
					log.warn("用户[{}]进行登录验证..失败验证超过{}次", u.getUserName(), number);
					throw new ExcessiveAttemptsException("账号: " + u.getUserName() + "尝试次数超出" + number + "次.，请一小时后再试！");
				}
				password = u.getPassword();
				
			}
			// 判断密码
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(u, password, getName());
			authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(u.getSalt()));
			return authenticationInfo;
			
			
			
		}
		
		
		return null;
	}
	
	
	 

}
