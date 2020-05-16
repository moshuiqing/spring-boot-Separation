package com.home.lh.system.config.shiro.realm;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.home.lh.system.config.shiro.UserToken;
import com.home.lh.system.po.Module;
import com.home.lh.system.po.SysUser;
import com.home.lh.system.service.SysUserService;
import com.home.lh.util.Global;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liuhao
 * 后台realm
 *
 */
@Slf4j
public class BackUserRealm extends AuthorizingRealm {
	
	
	@Autowired
	private SysUserService userService;
	@Autowired
	private EhCacheManager ehCacheManager;
	@Value("${number}")
	private Integer number;
	
	

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.info("授权");
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		Subject subject = SecurityUtils.getSubject();
		Object key = subject.getPrincipal();
		SysUser u = new SysUser();
		try {
			BeanUtils.copyProperties(key, u);
		} catch (Exception e) {
			log.info("错误");
		}
		String[] mids = u.getRole().getModelid().split(",");
		@SuppressWarnings("unchecked")
		List<Module> modules = (List<Module>) sysCache.get(Global.MODULESCACHKEY);

		for (Module m : modules) {
			if (ArrayUtils.contains(mids, m.getMid().toString())) {
				// 添加资源授权 字符串
				info.addStringPermission(m.getMname());
			}
		}
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("认证逻辑");
		SysUser user = new SysUser();
		String password = null;
		SysUser u = null;
		// 编写shiro判断逻辑，判断用户名和密码
		// 1.判断用户名
		UserToken userToken = (UserToken) token;
		char[] b = userToken.getPassword();
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			s.append(b[i]);
		}
		if (userToken.getUsername() != null && !userToken.getUsername().equals("")) {
			user.setUserName(userToken.getUsername());
			u = userService.login(user);
			if(u!=null) {
				u.setLoginType(userToken.getLoginType());
				u.setStrmsg(s.toString());
			}
			
			if (u != null && u.getIsDisable().equals("1")) {
				throw new ExcessiveAttemptsException("账号: " + u.getUserName() + "已被禁用，请联系管理员！");
			}
			if (u != null && (u.getRid()== null || u.getRid().equals("") || u.getRole() == null || (u.getRole()!=null && u.getRole().getIsdel().equals("1")))) {
				throw new ExcessiveAttemptsException("账号: " + u.getUserName() + "未赋予角色，请联系管理员！");
			}
			if(u!=null && (u.getRole()!=null && u.getRole().getIsdesable().equals("1"))) {
				throw new ExcessiveAttemptsException("账号: " + u.getUserName() + "的角色被禁用，请联系管理员！");
			}

			if (u == null) {
				// 用户名不存在
				return null;// shiro 会自己判断
			} else {
				Cache<String, AtomicInteger> passwordRetryCache = ehCacheManager.getCache("myEacache");
				// retry count + 1 自增
				AtomicInteger retryCount = passwordRetryCache.get(u.getUserName()+"back");
				if (null == retryCount) {
					retryCount = new AtomicInteger(0);
					passwordRetryCache.put(u.getUserName()+"back", retryCount);
				}
				if (retryCount.incrementAndGet() > number) {
					log.warn("用户[{}]进行登录验证..失败验证超过{}次", u.getUserName(), number);
					throw new ExcessiveAttemptsException("账号: " + u.getUserName() + "尝试次数超出" + number + "次.，请一小时后再试！");
				}
				password = u.getPassWord();
			}

		}
		// 判断密码

		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(u, password, getName());
		authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(u.getSalt()));
		return authenticationInfo;
	}

}
