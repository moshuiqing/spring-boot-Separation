package com.home.lh.system.config.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.home.lh.system.config.shiro.UserToken;
import com.home.lh.system.po.SysUser;

/**
 * 扩展FormAuthenticationFilter实现动态改变LoginUrl
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	/**
	 * 重写登录地址
	 */
	/* (non-Javadoc)
	 * @see org.apache.shiro.web.filter.AccessControlFilter#redirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		boolean flag = true;
		Cookie[] cookie = req.getCookies();
		if(cookie!=null) {
			for (Cookie c : cookie) {
				if (c.getName().equals("rememberMe") && c.getValue() != null && !c.getValue().equals("")) {
					flag = false;
				}
			}	
		}		
		String url = req.getRequestURI();//请求链接
		if (flag) {
			if (url.contains("backsystem")) {
				url = "/backsystem/sysuser/tologin"; //未登录进入登录页
			} else if (url.contains("webbussess")) {
				url = "/webbussess/weblogin/toWebLogin"; //客户登录页
			} else if (url.contains("appdesk")) {
				url = "/appdesk/appUser/backPrompt";  //app端返回未登陆通知
			}
			 String origin = req.getHeader("Origin");
			  if(origin == null) {
			   origin = req.getHeader("Referer");
			  }
			rep.setHeader("Access-Control-Allow-Origin", origin);
			rep.setHeader("Access-Control-Allow-Credentials", "true");//true代表允许携带cookie
			WebUtils.issueRedirect(req, rep, url); 
		}else {
			//登陆实现 将rememberMe中的用户信息取出来 不能直接赋值给user,原因我也不知道，可能序列化后产生的，因为密码存的是加密的，所以要自定义一个字段存非加密的密码
			Subject subject = SecurityUtils.getSubject();  
			if(subject.isRemembered()==true) {
				Object key = subject.getPrincipal();
				SysUser user = new SysUser();
				try {
					BeanUtils.copyProperties(user, key);
				} catch (Exception e) {
					System.out.println("转换失败！");
				}
				UserToken token = new UserToken(user.getUserName(),user.getStrmsg(), user.getLoginType(), subject.isRemembered());
				subject.login(token);
				WebUtils.issueRedirect(request, response, url.substring(url.indexOf("/",1), url.length()));
			}
		}
	}
}