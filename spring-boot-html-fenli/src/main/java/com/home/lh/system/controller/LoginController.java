package com.home.lh.system.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.lh.other.chat.po.ChatGoup;
import com.home.lh.other.chat.po.ChatUser;
import com.home.lh.other.chat.service.ChatGroupService;
import com.home.lh.system.config.shiro.UserToken;
import com.home.lh.system.config.shiro.po.LoginType;
import com.home.lh.system.po.SysUser;
import com.home.lh.system.service.MenuService;
import com.home.lh.system.service.SysUserService;
import com.home.lh.util.Global;
import com.home.lh.util.JsonMap;
import com.home.lh.util.systemutil.SimpleUtils;
import com.home.lh.util.systemutil.SystemUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/backsystem/sysuser/")
@Api(value="LoginController",tags="系统后台登录控制")
@Slf4j
public class LoginController {		
	
	
	@Autowired
	private EhCacheManager ehCacheManager;

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private SysUserService userService;
	
	@Autowired
	private ChatGroupService chatGroupService;

	/**
	 * 开发登陆时加载菜单
	 */
	@Value("${lg}")
	private String lg;
	
	@RequestMapping(value="sysLogin",method=RequestMethod.POST)
	@ApiOperation("通过用户名密码登录")
	@ResponseBody
	public JsonMap sysLogin(SysUser u, HttpServletRequest request) {
		log.debug("登录");
		///// 开发时使用，正式时不调用
		if (lg.equals("1")) {
			menuService.cacheMenuModule();
		}
		/////
		JsonMap jMap = new JsonMap();
		if (request.getAttribute("shiroLoginFailure") != null) {
			jMap.setCode(-1);
			jMap.setMsg("验证码错误");
			return jMap;
		}
		
		if(SimpleUtils.isEmpty(u.getUserName(),u.getPassWord())) {
			jMap.setCode(-1);
			jMap.setMsg("用户名和密码不能为空");
			return jMap;
		}

		Cache<String, AtomicInteger> passwordRetryCache = ehCacheManager.getCache("myEacache");
		// 1.获取subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		UserToken token = new UserToken(u.getUserName(), u.getPassWord(),LoginType.BACKSYSTEM, u.isRememberMe());
		// 3.执行登录方法
		try {
			subject.login(token);
			
			SysUser user = (SysUser) subject.getPrincipal();
			Session session = subject.getSession();
			session.setAttribute(Global.sysUser, user);         //设置用户信息
			session.setAttribute(Global.loginType, Global.back);//设置登录类型
			jMap.setCode(1);
			jMap.setMsg("登录成功！");
			log.info("登录成功");
			passwordRetryCache.remove(u.getUserName() + "back"); 
			ChatUser chatUser = new ChatUser();
			chatUser.setId(user.getId());
			List<ChatGoup> chatGoups = chatGroupService.foundChatGroup(chatUser);
			String groups = "";
			for (ChatGoup c : chatGoups) {
				groups += c.getId() + ",";
			}
			session.setAttribute("groups", groups);// 存入用户所在的群
			// 记录登陆时间
			SysUser sysUser = new SysUser();
			sysUser.setId(user.getId());
			sysUser.setUserEndTime("1");// 表示不为空插入时间
			userService.update(sysUser);
			String ip= SystemUtils.getIpAddress(request); 
			subject.getSession().setAttribute("host",ip );
		} catch (UnknownAccountException e) {
			// 登录失败:用户名不存在
			jMap.setCode(-1);
			jMap.setMsg("用户名不存在");
		} catch (IncorrectCredentialsException e) {
			// 登录失败：密码错误
			jMap.setCode(-2);
			jMap.setMsg("密码错误" + passwordRetryCache.get(u.getUserName() + "back") + "次，超过5次后锁定一小时！");
		}
		return jMap;
	}
	
	
	@RequestMapping(value="noLogin",method=RequestMethod.GET)
	@ApiOperation("未登录返回提示信息")
	public JsonMap noLogin() {
		
		JsonMap jm = new JsonMap();
		jm.setCode(-500);
		jm.setMsg("系统未登录！");
		return jm;
	}
	

	/**
	 * @param bookUser
	 * @param session
	 * @return 1 普通登录
	 */
	@RequestMapping(value = "tologin", method = RequestMethod.GET)
	@ApiOperation(value = "跳转回登录页", notes = "跳转回登录页")
	public String toLogin(String tologin, Model m) {
		log.info("跳转回登录页");
		if (tologin != null && !tologin.equals("")) {
			m.addAttribute("warn", tologin);
		}

		return "/liuhao/page/setting/login/login";
	}
	
	@ResponseBody
	@RequestMapping(value="toother",method=RequestMethod.GET)
	public JsonMap toother() {
		JsonMap jm = new JsonMap();
		jm.setCode(1);
		jm.setMsg("没有权限");
		
		return jm;		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
