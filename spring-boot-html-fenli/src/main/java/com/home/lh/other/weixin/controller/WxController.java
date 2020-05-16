package com.home.lh.other.weixin.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.lh.other.activemq.comment.ApolloClient;
import com.home.lh.other.activemq.po.Msg;
import com.home.lh.other.weixin.entity.WeiXinUserInfo;
import com.home.lh.other.weixin.util.WeiXinUtil;
import com.home.lh.other.weixin.util.WxGlobal;
import com.home.lh.system.config.shiro.UserToken;
import com.home.lh.system.config.shiro.po.LoginType;
import com.home.lh.util.Global;
import com.home.lh.util.JsonMap;
import com.home.lh.util.systemutil.SimpleUtils;
import com.home.lh.util.systemutil.SystemUtils;
import com.home.lh.webbussess.po.User;
import com.home.lh.webbussess.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/wx/")
@Api(value = "WxController", tags = "微信控制层")
@Slf4j
public class WxController {

	@Autowired
	private UserService userService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 获取随机码和临时二维码url
	 */
	@RequestMapping(value = "getSjm", method = RequestMethod.POST)
	@ApiOperation("获取随机码和临时二维码url")
	@ResponseBody
	public JsonMap getSjm() {
		String code = "wx" + UUID.randomUUID().toString();
		String url = null;
		try {
			url = WeiXinUtil.getErWeiMa(code);
		} catch (Exception e) {

			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("url", url);
		log.info("获取随机码");
		Global.context.setAttribute(code, "可用");
		return SimpleUtils.addOruPdate(1, map, null);
	}

	/**
	 * 3分钟后清除code
	 */
	@ApiOperation("3分钟后清除code")
	@RequestMapping("clearCode")
	@ResponseBody
	public String clearCode(String code) {
		Global.context.removeAttribute(code);
		return null;
	}

	/**
	 * 微信登录扫码逻辑
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "wxsmLogin", method = RequestMethod.GET)
	@ApiOperation("微信登录扫码逻辑")
	public String wxsmLogin(String mycode, HttpServletRequest request) {

		String recode = (String) Global.context.getAttribute(mycode);
		String page = null;
		if (recode == null) {
			return "/liuhao/page/weixin/shixiao";
		} else {
			// 得到code
			String code = request.getParameter("code");

			try {
				// ͨ获取微信用户信息
				WeiXinUserInfo userInfo = WeiXinUtil.getUserInfo(code);
				// 得到openid
				String openid = userInfo.getOpenid();
				User user = new User();
				user.setOpenid(openid);
				List<User> users = userService.simpleFound(user);
				if (users.size() == 1) {
					// 登录成功！
					Msg m = new Msg();
					m.setData(openid);
					m.setMycode(mycode);
					m.setType("wxsm");
					ApolloClient.PushMsg(JSONObject.fromObject(m).toString());
					page = "/liuhao/page/weixin/wxsucess";

				} else {
					request.setAttribute("userInfo", userInfo);
					request.setAttribute("mycode", mycode);
					page = "/liuhao/page/weixin/wxbd";
				}
				Global.context.removeAttribute(mycode);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return page;

	}

	/**
	 * 绑定用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "banding", method = RequestMethod.POST)
	@ApiOperation("绑定用户")
	@ResponseBody
	public JsonMap banding(User user, String mycode) {
		JsonMap jm = new JsonMap();
		Msg m = new Msg();
		User u = new User();
		u.setUserName(user.getUserName());
		List<User> users = userService.simpleFound(u);
		if (users.isEmpty()) {
			// 新增
			Integer num = userService.insert1(user);
			if (num > 0) {
				jm.setCode(num);
				jm.setMsg("绑定成功！");
				m.setData(user.getOpenid());
				m.setMycode(mycode);
				m.setType("wxsm");

			} else {
				jm.setCode(num);
				jm.setMsg("绑定失败！");
			}

		} else {
			// 判断是否是这个用户
			u = users.get(0);
			String pwd = SystemUtils.MD5(Global.type, user.getPassword(), u.getSalt(), Global.iterations);
			if (pwd.equals(u.getPassword())) {
				// 是这个用户直接绑定
				String id = u.getId();
				String openid = user.getOpenid();
				u = new User();
				u.setId(id);
				u.setOpenid(openid);
				Integer num = userService.wxUpdate(u);
				if (num > 0) {
					jm.setCode(num);
					jm.setMsg("绑定成功！");
					m.setData(u.getOpenid());
					m.setMycode(mycode);
					m.setType("wxsm");

				} else {
					jm.setCode(num);
					jm.setMsg("绑定失败！");
				}
			} else {
				jm.setCode(-1);
				jm.setMsg("用户名和密码不匹配，请修改邮箱或者填写正确的密码！");

			}
		}

		try {
			ApolloClient.PushMsg(JSONObject.fromObject(m).toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jm;
	}

	/**
	 * 扫码登录成功后把用户信息设置进shiro
	 */
	@ResponseBody
	@RequestMapping(value = "setWebSmInfo", method = RequestMethod.POST)
	@ApiOperation("扫码登录成功后把用户信息设置进shiro")
	public JsonMap setWebSmInfo(User user) {

		JsonMap jm = new JsonMap();
		if (user.getOpenid() != null && !user.getOpenid().equals("")) {

			UserToken token = new UserToken(user.getOpenid(), user.getPassword(), LoginType.NOPASSWD,
					user.isRememberMe());
			Subject subject = SecurityUtils.getSubject();
			try {
				subject.login(token);
				User u = (User) subject.getPrincipal();
				Session session = subject.getSession();
				session.setAttribute(Global.webUser, u);
				session.setAttribute(Global.loginType, Global.web);
				jm.setCode(1);
				jm.setMsg("登录成功！");
				log.info("登录成功");
				userService.saveLoginTime(u);
			} catch (Exception e) {
				jm.setCode(-1);
				jm.setMsg("系统错误");
				e.printStackTrace();
			}
		}
		return jm;

	}

	/**
	 * 生成永久二维码
	 * 
	 * @return
	 */
	@RequestMapping("getPermanent")
	@ResponseBody
	@ApiOperation("生成永久二维码")
	public JsonMap getPermanent() {
		JsonMap jm = new JsonMap();
		String str = stringRedisTemplate.opsForValue().get("gzewm");
		jm.setCode(1);
		jm.setObject(str);
		if (str == null || str.equals("")) {

			try {
				str = WeiXinUtil.qrTicket("autoChat");
				String url = WxGlobal.wxpicture.replace("TICKET", str);
				stringRedisTemplate.opsForValue().set("gzewm", url);
				jm.setObject(str);
			} catch (Exception e) {
				jm.setCode(-1);
				e.printStackTrace();
			}
		}
		return jm;
	}

	

}
