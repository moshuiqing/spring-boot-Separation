package com.home.liuhao.other.oauth.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.liuhao.other.oauth.mapper.ClientMapper;
import com.home.liuhao.other.oauth.po.Client;
import com.home.liuhao.other.oauth.service.ClientService;
import com.home.liuhao.system.config.shiro.UserToken;
import com.home.liuhao.webbussess.po.User;



@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientMapper clientMapper;

	@Autowired
	private EhCacheManager ehCacheManager;

	@Override
	public boolean checkClientId(String clientId) {

		Client client = new Client();
		client.setClientId(clientId);
		List<Client> clients = clientMapper.simpleFound(client);
		if (clients.isEmpty()) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public boolean login(Subject subject, HttpServletRequest request) {
		String username = request.getParameter("userName");
		String password = request.getParameter("password");

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return false;
		}
		UserToken token = new UserToken(username, password, "WebUserRealm", false);
		Cache<String, AtomicInteger> passwordRetryCache = ehCacheManager.getCache("webEacache");
		try {
			subject.login(token);
			passwordRetryCache.clear();
			return true;
		} catch (Exception e) {

			if (e instanceof UnknownAccountException) {
				request.setAttribute("msg", "用户名或密码错误！");
			}

			if (e instanceof IncorrectCredentialsException) {
				request.setAttribute("msg", "用户名或密码错误！");
			}

			if (e instanceof LockedAccountException) {
				request.setAttribute("msg", "账号已被锁定,请联系管理员！");
			}
			return false;
		}
	}

	@Override
	public Client findByClientId(String clientId) {
		Client client = new Client();
		client.setClientId(clientId);
		List<Client> clients = clientMapper.simpleFound(client);
		if (clients.size() == 1) {
			return clients.get(0);
		}
		return null;
	}

	@Override
	public boolean checkClientSecret(String Secret) {
		Client client = new Client();
		client.setClientSecret(Secret);
		List<Client> clients = clientMapper.simpleFound(client);
		if (clients.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkAuthCode(String code) {
		Cache<String, Object> authorCodeCache = ehCacheManager.getCache("authorCode");
		Object o = authorCodeCache.get(code);
		if (o != null) {
			return true;
		}
		return false;
	}

	@Override
	public void addAuthCode(String s, Object o) {
		Cache<String, Object> authorCodeCache = ehCacheManager.getCache("authorCode");
		authorCodeCache.put(s, o);
	}

	@Override
	public void addAccessToken(String token, String code) {

		Cache<String, Object> authorCodeCache = ehCacheManager.getCache("authorToken");
		authorCodeCache.put(token, getAuthCode(code));
	}

	@Override
	public Object getAuthCode(String code) {
		Cache<String, Object> authorCodeCache = ehCacheManager.getCache("authorCode");
		return authorCodeCache.get(code);
	}

	@Override
	public boolean checkAccessToken(String token) {
		Cache<String, Object> authorCodeCache = ehCacheManager.getCache("authorToken");
		if (authorCodeCache.get(token) == null) {
			return false;
		}
		return true;
	}

	@Override
	public User getUserByToken(String token) {
		Cache<String, Object> authorCodeCache = ehCacheManager.getCache("authorToken");
		return (User) authorCodeCache.get(token);
	}

}
