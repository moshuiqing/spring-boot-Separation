package com.home.liuhao.webbussess.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.liuhao.other.emalutil.EamilComment;
import com.home.liuhao.other.weixin.util.WxGlobal;
import com.home.liuhao.system.config.shiro.UserToken;
import com.home.liuhao.system.config.shiro.po.LoginType;
import com.home.liuhao.util.Global;
import com.home.liuhao.util.JsonMap;
import com.home.liuhao.util.systemutil.SimpleUtils;
import com.home.liuhao.webbussess.po.User;
import com.home.liuhao.webbussess.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/webbussess/weblogin/")
@Api(value = "WebLoginController", tags = "前台登录控制层")
@Slf4j
public class WebLoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private EhCacheManager ehCacheManager;

	@Autowired
	private EamilComment eamilComment;

	/**
	 * 跳转前端登录页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "toWebLogin", method = RequestMethod.GET)
	@ApiOperation("跳转登录页")
	public String toWebLogin() {

		return "/liuhao/page/web/login/login";
	}

	@RequestMapping(value = "webLogin", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("前台登录")
	public JsonMap login(User user, HttpSession session, HttpServletRequest request) {
		JsonMap jMap = new JsonMap();
		if (request.getAttribute("shiroLoginFailure") != null) {
			jMap.setCode(-1);
			jMap.setMsg("验证码错误");
			return jMap;
		}
		if (SimpleUtils.isEmpty(user.getUserName(), user.getPassword())) {
			jMap.setCode(-1);
			jMap.setMsg("用户名和密码不能为空");
			return jMap;
		}
		Cache<String, AtomicInteger> passwordRetryCache = ehCacheManager.getCache("webEacache");
		// 1.获取subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		UserToken token = new UserToken(user.getUserName(), user.getPassword(), LoginType.WEBBUESS,
				user.isRememberMe());

		try {
			subject.login(token);
			User user2 = (User) subject.getPrincipal();
			session.setAttribute(Global.webUser, user);
			session.setAttribute(Global.loginType, Global.web);
			jMap.setCode(1);
			jMap.setMsg("登录成功！");
			log.info("登录成功");
			passwordRetryCache.remove(user.getUserName() + "web");
			userService.saveLoginTime(user2);

		} catch (UnknownAccountException e) {
			// 登录失败:用户名不存在
			jMap.setCode(-1);
			jMap.setMsg("用户名不存在");
		} catch (IncorrectCredentialsException e) {
			// 登录失败：密码错误
			jMap.setCode(-2);
			jMap.setMsg("密码错误" + passwordRetryCache.get(user.getUserName() + "web") + "次，超过5次后锁定一小时！");
		}
		return jMap;

	}

	/**
	 * 跳转前台web页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "toWebIndex", method = RequestMethod.GET)
	@ApiOperation("跳转前台web首页")
	public String toWebIndex() {

		return "/liuhao/page/web/index/index";
	}

	/**
	 * 发送找回密码的邮件
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "retrievePwd", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("发送找回密码的邮件")
	public JsonMap retrievePwd(String email) {
		JsonMap jm = new JsonMap();

		if (email.isEmpty()) {
			jm.setCode(-2);
			jm.setMsg("邮件为空，无法发送");
		} else {

			String sjs = SimpleUtils.getSixSjs();
			String content = "<div><h1>尊敬的用户您好！</h1>欢迎使用我们的密码重置功能！点击下方重置链接进行密码重置<p></p><p><a href=\"" + WxGlobal.yuming
					+ "/webbussess/weblogin/resetPwd?userName=" + email + "&password="+sjs+"\">密码重置为:" + sjs + "</a></p></div>";
			try {
				eamilComment.sendHtmlEmail(email, "找回密码", content);
				jm.setCode(1);
				jm.setMsg("邮件发送成功！");
			} catch (MessagingException e) {
				jm.setCode(-1);
				jm.setMsg("邮件发送失败");
				e.printStackTrace();
			}
		}
		return jm;
	}

	/**
	 * 邮件中重置密码
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation("重置密码")
	@RequestMapping(value = "resetPwd", method = RequestMethod.GET)
	public String resetPwd(User user,Model m) {
		String pwd = user.getPassword();
		User u= userService.login(user);
		if (u!=null) {
			String id = u.getId();
			user = new User();
			user.setPassword(pwd);
			user.setId(id);
			Integer result= userService.update(user);
			if(result>0) {
				m.addAttribute("msg", "修改成功");
			}else {
				m.addAttribute("msg", "修改失败");
			}
		}else {
			m.addAttribute("msg", "系统错误");
		}
		return "/liuhao/page/weixin/emailResult";
	}
	
	

	/**
	 * 跳转注册页面
	 * 
	 * @return
	 */
	@RequestMapping(value="toRegister",method=RequestMethod.GET)
	@ApiOperation("跳转注册页面")
	public String toRegister() {
		return "/liuhao/page/web/register/register";
	}
	
	/**
	 * 注册
	 * @return
	 */
	@RequestMapping(value="register",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation("注册")
	public JsonMap register(User user) {
		JsonMap jm = new JsonMap();	
		User u = new User();
		u.setUserName(user.getUserName());
		User user2 =  userService.login(u);
		if(user2!=null) {
			jm.setCode(-3);
			jm.setMsg("该邮箱已被注册");
		}else {
			Integer result= userService.insert(user);
			if(result>0) {
				jm.setCode(1);
				jm.setMsg("注册成功");
			}else {
				jm.setCode(-1);
				jm.setMsg("注册失败");
			}			
		}
		return jm;
	}

}
