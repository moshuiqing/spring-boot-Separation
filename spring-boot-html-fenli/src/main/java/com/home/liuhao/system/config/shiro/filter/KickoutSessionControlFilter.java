package com.home.liuhao.system.config.shiro.filter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.home.liuhao.system.po.SysUser;
import com.home.liuhao.util.Global;
import com.home.liuhao.util.JsonMap;
import com.home.liuhao.util.systemutil.CloseImpl;
import com.home.liuhao.util.systemutil.SimpleUtils;
import com.home.liuhao.util.systemutil.SystemUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * 后台并发登录人数控制
 * 
 * @author wgc
 */
@Slf4j
public class KickoutSessionControlFilter extends AccessControlFilter {





	/**
	 * 踢出后到的地址
	 */
	private String kickoutUrl = "/backsystem/sysuser/tologin";

	/**
	 * 踢出之前登录的之后登录的用户 默认踢出之后 的用户
	 */
	private boolean kickoutAfter = false;
	/**
	 * 同一个帐号最大会话数 默认1
	 */
	private int maxSession = 1;

	private SessionManager sessionManager;
	private Cache<String, Deque<Serializable>> cache;

	public void setKickoutUrl(String kickoutUrl) {
		this.kickoutUrl = kickoutUrl;
	}

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * 设置Cache的key的前缀
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = getSubject(request, response);
		HttpServletRequest req = (HttpServletRequest) request;
		Object obj = req.getSession().getAttribute(Global.loginType);
		if (obj != null) {
			if (!obj.toString().equals(Global.back)) {
				subject.logout();
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unused")
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			// 如果没有登录，直接进行之后的流程
			return true;
		}
		/////////////////////////////////
		Session session1 = getSubject(request, response).getSession(false);
		if (session1 == null) {
			return true;
		}
		HttpServletRequest req = (HttpServletRequest) request;

		Session session = subject.getSession();

		Object key = subject.getPrincipal();
		SysUser user = new SysUser();
		try {
			BeanUtils.copyProperties(user, key);
		} catch (Exception e) {
			log.info("错误");
		}

		// SysUser user = (SysUser) subject.getPrincipal();
		String username = user.getUserName();
		Serializable sessionId = session.getId();
		//////////////////////////////////////////
		Object key1 = session1.getAttribute(Global.BLOOENKEY);

		if (key1 != null) {
			try {
				String url = req.getRequestURI().toString();
				Deque<Serializable> dq = cache.get(username);
				if (dq != null) {
					dq.remove(session.getId().toString());
				}

				log.info("退出系统移除缓存中的sessionId");

				getSubject(request, response).logout();// 强制退出

			} catch (Exception e) {

			}
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			if (SystemUtils.isAjax(httpRequest)) {
				log.info("ajax访问");
				// throw new Exception("您的账号已在别处登录！如非本人操作！请尽快修改密码！刷新页面后可重新登录！");
				JsonMap jm = SimpleUtils.addOruPdate(-4, null, "您已被管理员踢下线，请联系管理员！");
				out(response, jm);
			} else {
				saveRequest(request);
				// 重定向
				log.info("用户登录人数超过限制, 重定向到{}", kickoutUrl);
				String reason = URLEncoder.encode("您已被管理员踢下线，请联系管理员！", "UTF-8");
				// String redirectUrl = kickoutUrl + (kickoutUrl.contains("?") ? "&" : "?") +
				// "shiroLoginFailure=" + reason;
				/*
				 * Map<String, String> map = new HashMap<>(); map.put("tologin", "1");
				 */
				WebUtils.issueRedirect(request, response, kickoutUrl + "?tologin=" + reason);
			}
			return false;
		}
		//////////////////////////////////////

		Global.setClose(new CloseImpl() {
			
			@Override
			public void removeCache() {
				log.info("关闭浏览器退出系统");
				Deque<Serializable> dq = cache.get(username);
				if (dq != null) {
					dq.remove(session.getId().toString());
				}

				log.info("退出系统移除缓存中的sessionId");
			}
		});
		
		/////////////

		String url = req.getRequestURI().toString();
		if (url.indexOf("/backsystem/main/loginOut") != -1) {
			Deque<Serializable> dq = cache.get(username);
			if (dq != null) {
				dq.remove(session.getId().toString());
			}

			log.info("退出系统移除缓存中的sessionId");
			return true;
		}
		//////

		if (user!=null &&user.getIsSingle()!=null&& user.getIsSingle().equals("1")) {

			// 读取缓存 没有就存入
			Deque<Serializable> deque = cache.get(username);
			if (deque == null) {
				// 初始化队列
				deque = new ArrayDeque<Serializable>();
			}

			// 如果队列里没有此sessionId，且用户没有被踢出；放入队列
			if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
				// 将sessionId存入队列
				deque.push(sessionId);
				// 将用户的sessionId队列缓存
				cache.put(username, deque);
			}

			// 如果队列里的sessionId数超出最大会话数，开始踢人
			while (deque.size() > maxSession) {
				Serializable kickoutSessionId = null;
				if (kickoutAfter) { // 如果踢出后者
					kickoutSessionId = deque.removeFirst();
				} else { // 否则踢出前者
					kickoutSessionId = deque.removeLast();
				}
				// 踢出后再更新下缓存队列
				cache.put(username, deque);

				try {
					// 获取被踢出的sessionId的session对象
					Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
					System.out.println(kickoutSession.getTimeout());
					if (kickoutSession != null) {
						// 设置会话的kickout属性表示踢出了
						kickoutSession.setAttribute("kickout", true);
					}
				} catch (Exception e) {
					// e.printStackTrace();
					// ignore exception
					log.info("111");
				}
			}
			// 如果被踢出了，直接退出，重定向到踢出后的地址
			// String sid=comment.get(username);
			// System.out.println(session.getId());
			if ((Boolean) session.getAttribute("kickout") != null
					&& (Boolean) session.getAttribute("kickout") == true) {
				// 会话被踢出了

				// 会话被踢出了
				try {

					// 退出登录
					subject.logout();

				} catch (Exception e) {
					log.warn(e.getMessage());
					e.printStackTrace();
				}
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				if (SystemUtils.isAjax(httpRequest)) {
					log.info("ajax访问");
					// throw new Exception("您的账号已在别处登录！如非本人操作！请尽快修改密码！刷新页面后可重新登录！");
					JsonMap jm = SimpleUtils.addOruPdate(-4, null, "您的账号已在别处登录！如非本人操作！请尽快修改密码！");
					out(response, jm);
				} else {
					saveRequest(request);
					// 重定向
					log.info("用户登录人数超过限制, 重定向到{}", kickoutUrl);
					String reason = URLEncoder.encode("您的账号已在别处登录！如非本人操作！请尽快修改密码", "UTF-8");
					// String redirectUrl = kickoutUrl + (kickoutUrl.contains("?") ? "&" : "?") +
					// "shiroLoginFailure=" + reason;
					/*
					 * Map<String, String> map = new HashMap<>(); map.put("tologin", "1");
					 */
					WebUtils.issueRedirect(request, response,kickoutUrl + "?tologin=" + reason);
				}

				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @描述：response输出json
	 * @创建人：wyait
	 * @创建时间：2018年4月24日 下午5:14:22
	 * @param response
	 * @param result
	 */
	public static void out(ServletResponse response, JsonMap result) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");// 设置编码
			response.setContentType("application/json");// 设置返回类型
			out = response.getWriter();
			out.println(JSONObject.fromObject(result));// 输出
			log.error("用户在线数量限制【wyait-manager-->KickoutSessionFilter.out】响应json信息成功");
		} catch (Exception e) {
			log.error("用户在线数量限制【wyait-manager-->KickoutSessionFilter.out】响应json信息出错", e);
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
	}
}
