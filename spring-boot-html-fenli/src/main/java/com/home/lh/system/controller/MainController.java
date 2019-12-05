package com.home.lh.system.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.lh.other.chat.po.ChatFriend;
import com.home.lh.other.chat.service.ChatFriendService;
import com.home.lh.system.po.BigMenu;
import com.home.lh.system.po.Menu;
import com.home.lh.system.po.Module;
import com.home.lh.system.po.OnlineUser;
import com.home.lh.system.po.Person;
import com.home.lh.system.po.Role;
import com.home.lh.system.po.SysUser;
import com.home.lh.system.po.Syslog;
import com.home.lh.system.service.BigMenuService;
import com.home.lh.system.service.MenuService;
import com.home.lh.system.service.RoleService;
import com.home.lh.system.service.SysLogService;
import com.home.lh.system.service.SysUserService;
import com.home.lh.util.Global;
import com.home.lh.util.JsonMap;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.LayuiTree;
import com.home.lh.util.Menus;
import com.home.lh.util.systemutil.DateUtils;
import com.home.lh.util.systemutil.ShiroUtils;
import com.home.lh.util.systemutil.SimpleUtils;
import com.home.lh.util.systemutil.SystemUtils;
import com.home.lh.webbussess.po.User;
import com.home.lh.webbussess.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/backsystem/main/")
@Api(value = "MainController", tags = "主页控制器")
@Slf4j
public class MainController {

	/**
	 * 开发登陆时加载菜单
	 */
	@Value("${lg}")
	private String lg;

	@Autowired
	private EhCacheManager ehCacheManager;

	@Autowired
	private MenuService menuService;

	@Autowired
	private BigMenuService bigMenuService;

	@Autowired
	private SysUserService userService;

	@Autowired
	private SysLogService sysLogService;

	// @Autowired
	// private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ChatFriendService chatFriendService;

	@Autowired
	private SessionDAO sessionDAO;

	@RequestMapping(value = "toMain", method = RequestMethod.GET)
	@ApiOperation("进入首页")
	public String toMain(Model m) {

		// 1.获取subject
		Subject subject = SecurityUtils.getSubject();
		Object key = subject.getPrincipal();
		SysUser user = new SysUser();
		try {
			BeanUtils.copyProperties(user, key);
		} catch (Exception e) {
			log.info("错误");
		}
		List<BigMenu> bigMenus = userService.getUserBigMenu(user);
		String[] bigIds = user.getRole().getBigmenuid().split(",");
		Iterator<BigMenu> iterator = (Iterator<BigMenu>) bigMenus.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			BigMenu bigMenu = new BigMenu();
			try {
				BeanUtils.copyProperties(bigMenu, o);
			} catch (Exception e) {
				log.info("错误");
			}

			if (!ArrayUtils.contains(bigIds, bigMenu.getId() + "")) {
				iterator.remove();
			}
		}
		Person p = user.getPerson();
		String headimg = null;
		if (p != null) {
			headimg = p.getHeadimg();
		}
		m.addAttribute("bigMenus", bigMenus);
		m.addAttribute("user", user);
		if (headimg != null && headimg.equals("")) {
			headimg = null;
		}
		m.addAttribute("headimg", headimg);
		return "/liuhao/index";
	}

	/**
	 * ***************************************************************************************************************************************************
	 * 菜单开始
	 * ***************************************************************************************************************************************************
	 */

	@RequestMapping(value = "foundLeftMenu", method = RequestMethod.GET)
	@ApiOperation("查询左部菜单")
	@ResponseBody
	public JSONObject foundLeftMenu() {
		Subject subject = SecurityUtils.getSubject();
		Object key = subject.getPrincipal();
		SysUser u = new SysUser();
		try {
			BeanUtils.copyProperties(u, key);
		} catch (Exception e) {
			log.info("错误");
		}
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) sysCache.get(u.getUserName());
		JSONObject array = JSONObject.fromObject(map);
		return array;
	}

	/**
	 * 跳转菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "toMenus", method = RequestMethod.GET)
	@ApiOperation(value = "跳转菜单页面", notes = "进入菜单管理页面")
	public String toMenus() {
		return "/liuhao/page/setting/menu/menu";
	}

	/**
	 * 获取全部菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "getTrees", method = RequestMethod.POST)
	@ApiOperation(value = "获取菜单", notes = "获取全部菜单")
	@ResponseBody
	public JSONArray getTrees() {
		///// 开发时使用，正式时不调用
		if (lg.equals("1")) {
			menuService.cacheMenuModule();
		}
		/////
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		List<LayuiTree> xtrees = (List<LayuiTree>) sysCache.get(Global.ALLMENUS);
		if (xtrees == null) {

			List<LayuiTree> LayuiTrees = new ArrayList<>();
			@SuppressWarnings("unchecked")
			List<BigMenu> bigMenus = (List<BigMenu>) sysCache.get(Global.BIGMENU);
			for (BigMenu bmu : bigMenus) {
				List<LayuiTree> data = new ArrayList<>();
				LayuiTree layuiTree = new LayuiTree();
				layuiTree.setTitle(bmu.getTitle());
				layuiTree.setMpName(bmu.getMpname());
				layuiTree.setIcon(bmu.getIcon());
				layuiTree.setSort(bmu.getSort());
				layuiTree.setJibie("1");
				layuiTree.setSpread(true);
				layuiTree.setId(bmu.getId());
				Menus ms = new Menus();
				ms.setMpName(bmu.getMpname());
				List<Menus> menuss = menuService.simpleFound(ms);
				for (Menus m : menuss) {
					List<LayuiTree> list1 = new ArrayList<>();
					LayuiTree xtree = new LayuiTree();
					xtree.setTitle(m.getTitle());
					xtree.setId(m.getId() + "");
					xtree.setIcon(m.getIcon());
					xtree.setSort(m.getSort());
					xtree.setMpName(m.getMpName());
					xtree.setSpread(true);
					xtree.setHref(m.getHref());
					xtree.setJibie("2");
					Menus men = new Menus();
					men.setParent(m.getId() + "");
					List<Menus> list = menuService.simpleFound(men);
					for (Menus mn : list) {
						LayuiTree le = new LayuiTree();
						le.setTitle(mn.getTitle());
						le.setId(mn.getId() + "");
						le.setIcon(mn.getIcon());
						le.setChildren(null);
						le.setHref(mn.getHref());
						le.setJibie("3");
						list1.add(le);
					}
					xtree.setChildren(list1);
					data.add(xtree);
				}

				layuiTree.setChildren(data);
				LayuiTrees.add(layuiTree);

			}
			xtrees = LayuiTrees;
			sysCache.put(Global.ALLMENUS, LayuiTrees);

		}
		System.out.println(JSONArray.fromObject(xtrees));
		return JSONArray.fromObject(xtrees);

	}

	/**
	 * 新增一级菜单
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping(value = "addBigMenu", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增大菜单一级菜单", notes = "新增一级菜单")
	public JsonMap addBigMenu(BigMenu m) {
		Integer result = null;
		// 判断标题不能重复 和 名称不能重复
		BigMenu b = new BigMenu();
		b.setMpname(m.getMpname());
		Integer count = bigMenuService.pageCount(b);
		if (count > 0) {
			return SimpleUtils.addOruPdate(-3, null, "名称重复");
		}
		b.setMpname(null);
		b.setTitle(m.getTitle());
		count = bigMenuService.pageCount(b);
		if (count > 0) {
			return SimpleUtils.addOruPdate(-3, null, "标题重复");
		}

		// 1.获取subject
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		result = bigMenuService.insertMenu(m, user.getRid());
		if (result > 0) {
			Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
			sysCache.put(Global.ALLMENUS, null);
		}
		return SimpleUtils.addOruPdate(result, null, null);
	}

	/**
	 * 修改一级菜单
	 * 
	 * @param bigMenu
	 * @return
	 */
	@RequestMapping(value = "updateBigMenu", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改一级菜单", notes = "修改一级菜单")
	public JsonMap updateBigMenu(BigMenu bigMenu) {

		Integer result = bigMenuService.updateMenu(bigMenu);
		if (result > 0) {
			Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
			sysCache.put(Global.ALLMENUS, null);
		}

		return SimpleUtils.addOruPdate(result, bigMenu, null);

	}

	/**
	 * 删除
	 * 
	 * @param jibie
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteMenu", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除菜单", notes = "删除菜单，根据级别的不同删除")
	public JsonMap deleteMenu(String jibie, String id) {
		Integer result = null;
		if (jibie.equals("1")) {
			// 删除大菜单
			BigMenu bigMenu = new BigMenu();
			bigMenu.setId(id + "");
			result = bigMenuService.deleteMenu(bigMenu);

		} else {
			// 删除二级三级菜单
			Menu menu = new Menu();
			menu.setId(Integer.parseInt(id));
			menu.setParent(id + "");
			result = menuService.deleteMenus(menu);

		}
		if (result > 0) {
			Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
			sysCache.put(Global.ALLMENUS, null);
		}

		return SimpleUtils.addOruPdate(result, null, null);

	}

	/**
	 * 新增二级菜单
	 * 
	 * @param menus
	 * @return
	 */
	@RequestMapping(value = "addMenu", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增二级菜单", notes = "新增二级菜单")
	public JsonMap addMenu(Menu menu) {
		Integer result = null;
		Menu m = new Menu();
		m.setTitle(menu.getTitle());
		result = menuService.countNum(m);
		if (result > 0) {
			return SimpleUtils.addOruPdate(-3, null, "标题重复");
		}

		// 1.获取subject
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		result = menuService.insertMenus(menu, user.getRid());
		if (result > 0) {
			Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
			sysCache.put(Global.ALLMENUS, null);
		}

		return SimpleUtils.addOruPdate(result, null, null);
	}

	/**
	 * 修改二级菜单
	 * 
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "updateMenu", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改二级菜单", notes = "修改二级菜单")
	public JsonMap updateMenu(Menu menu) {

		Integer result = menuService.updateMenus(menu);
		if (result > 0) {
			Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
			sysCache.put(Global.ALLMENUS, null);
		}
		return SimpleUtils.addOruPdate(result, menu, null);
	}

	/**
	 * ***************************************************************************************************************************************************
	 * 菜单结束
	 * ***************************************************************************************************************************************************
	 */

	/**
	 * 退出系统
	 * 
	 * @return
	 */
	@RequestMapping(value = "loginOut", method = RequestMethod.GET)
	@ApiOperation(value = "退出系统", notes = "退出系统清空缓存")
	public String loginOut() {
		Subject subject = SecurityUtils.getSubject();
		Object key = subject.getPrincipal();
		SysUser user = new SysUser();
		try {
			BeanUtils.copyProperties(user, key);
		} catch (Exception e) {
			log.info("错误");
		}
		// redisCommnet.del(user.getUsername());

		SysUser sysUser = new SysUser();
		sysUser.setId(user.getId());
		sysUser.setStatus("offline");// 表示离线状态
		userService.update(sysUser);
		subject.logout();
		return "redirect:/";
	}

	/**
	 * 跳转系统日志
	 * 
	 * @return
	 */
	@RequestMapping(value = "toLogs", method = RequestMethod.GET)
	@ApiOperation(value = "跳转系统日志", notes = "进入查看系统日志")
	public String toLogs() {
		return "/liuhao/page/setting/log/logs";
	}

	/**
	 * 分页查询系统日志
	 * 
	 * @param page   分页参数
	 * @param syslog 实体
	 * @return
	 */
	@RequestMapping(value = "getLogs", method = RequestMethod.GET)
	@ApiOperation(value = "分页查询系统日志", notes = "分页查询系统日志")
	@ResponseBody
	public String foundLogs(LayuiPage page, Syslog syslog) {
		JSONObject obj = new JSONObject();
		List<Syslog> syslogs = sysLogService.pageFound(syslog, page);
		Integer count = sysLogService.pageCount(syslog);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", count);
		obj.put("data", syslogs);
		return obj.toString();
	}

	/**
	 * 跳转个人资料
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "toSysUser", method = RequestMethod.GET)
	@ApiOperation(value = "跳转个人资料", notes = "进入系统个人资料页面")
	public String toSysUser() {

		return "/liuhao/page/setting/user/userInfo";

	}

	/**
	 * 自己修改系统用户信息
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "insertSysUser", method = RequestMethod.POST)
	@ApiOperation(value = "新增自己的用户信息", notes = "新增自己的用户信息")
	@ResponseBody
	public JsonMap insertSysUser(Person p, HttpSession session) {
		SysUser user = new SysUser();
		SysUser su = (SysUser) session.getAttribute(Global.sysUser);
		Integer result = -1;
		if (SimpleUtils.isEmpty(p.getId())) {
			// 新增
			String pid = UUID.randomUUID().toString();
			p.setId(pid);
			result = userService.insertPerson(p);
			if (result > 0) {
				SysUser u = new SysUser();
				u.setId(su.getId());
				u.setPid(pid);
				u.setHeadImg(p.getHeadimg());
				result = userService.update(u);
			}
		} else {
			// 修改
			result = userService.updatePerson(p);
			if (result > 0 && p.getHeadimg() != null && !p.getHeadimg().isEmpty()) {
				SysUser u = new SysUser();
				u.setId(su.getId());
				u.setHeadImg(p.getHeadimg());
				result = userService.update(u);
			}
		}

		if (result > 0) {
			su.setId(su.getId());
			List<SysUser> sysUsers = userService.simpleFound(su);
			if (!sysUsers.isEmpty()) {
				user = sysUsers.get(0);
				session.setAttribute(Global.sysUser, user);
				// session.setAttribute("headimg", user.getPerson().getHeadimg());
				// SysUser subUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
				ShiroUtils.setUser(user);
			}
		}
		return SimpleUtils.addOruPdate(result, user, null);

	}

	/**
	 * 是否单点登录
	 * 
	 * @param isSingle
	 * @return
	 */
	@RequestMapping(value = "isSingle", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "是否单点登录", notes = "是否单点登录")
	public JsonMap isSingle(@ApiParam("是否单点登录参数") String isSingle, @ApiParam("用户id") String id, HttpSession session) {
		boolean flag = SimpleUtils.isEmpty(isSingle, id);
		if (flag) {
			return SimpleUtils.addOruPdate(-2, null, null);
		}
		SysUser user = new SysUser();
		user.setIsSingle(isSingle);
		user.setId(id);
		Integer code = userService.update(user);
		if (code > 0) {
			user = (SysUser) session.getAttribute(Global.sysUser);
			user.setIsSingle(isSingle);
			session.setAttribute(Global.sysUser, user);
			user = (SysUser) SecurityUtils.getSubject().getPrincipal();

		}
		return SimpleUtils.addOruPdate(code, null, null);

	}

	/**************************************************************************************************************
	 * 
	 * ******************************************用户管理***********************************
	 */
	/**
	 * 跳转用户管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "toUserList", method = RequestMethod.GET)
	@ApiOperation("进入用户管理页面")
	public String toUserList(Model m) {
		return "/liuhao/page/setting/user/userList";
	}

	/**
	 * 获取全部角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllRole", method = RequestMethod.POST)
	@ApiOperation("获取全部角色")
	@ResponseBody
	public JsonMap getAllRole() {
		JsonMap jm = new JsonMap();
		List<Role> roles = roleService.simpleFoundRole(new Role());
		jm.setCode(1);
		jm.setObject(roles);
		return jm;
	}

	/**
	 * 分页查询系统用户
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "pageFoundSysUsers", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "分页查询系统用户", notes = "分页查询系统用户")
	public String getSysUsers(SysUser user, LayuiPage page) {

		JSONObject obj = new JSONObject();
		List<SysUser> sysUsers = userService.pageFound(user, page);
		Integer count = userService.pageCount(user);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", count);
		obj.put("data", sysUsers);
		return obj.toString();
	}

	/**
	 * @param bookUser
	 * @return 新增用户
	 */
	@RequestMapping(value = "addSystemUser", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "后台管理新增系统用户", notes = "md5+盐+迭代 生成密码")
	@Transactional(propagation = Propagation.REQUIRED)
	public JsonMap addSystemUser(SysUser user) {
		// 判断用户名是否重名
		if (!SimpleUtils.isEmpty(user.getUserName())) {
			SysUser s = new SysUser();
			s.setUserName(user.getUserName());
			Integer count = userService.simpleFound(s).size();
			if (count > 0) {
				return SimpleUtils.addOruPdate(-3, null, "用户名已存在！");
			}
		}
		Integer code = -1;
		String salt = DateUtils.backNum("");
		if (user.getPassWord() != null) {
			String pwd = SystemUtils.MD5(Global.type, user.getPassWord(), salt, Global.iterations);
			user.setPassWord(pwd);
		}
		user.setSalt(salt);
		code = userService.insertSysUser(user);
		ChatFriend chatFriend = new ChatFriend();
		chatFriend.setGroupname("我的好友");
		chatFriend.setGoupUserid(user.getId());
		chatFriendService.addChatFriend(chatFriend);// 新增用户时默认给一个我的好友的分组

		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public JsonMap updateUser(SysUser user) {
		// 判断用户名是否重名
		if (!SimpleUtils.isEmpty(user.getUserName())) {
			SysUser s = new SysUser();
			s.setUserName(user.getUserName());
			Integer count = userService.simpleFound(s).size();
			if (count > 0) {
				return SimpleUtils.addOruPdate(-3, null, "用户名已存在！");
			}
		}
		Integer code = -1;
		String salt = DateUtils.backNum("");
		if (user.getPassWord() != null) {
			String pwd = SystemUtils.MD5(Global.type, user.getPassWord(), salt, Global.iterations);
			user.setPassWord(pwd);
		}
		user.setSalt(salt);
		// 修改
		code = userService.update(user);

		return SimpleUtils.addOruPdate(code, null, null);

	}

	/**
	 * 启用 禁用
	 * 
	 * @param sysUser
	 * @return
	 */
	@RequestMapping(value = "change", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "启用或者禁用系统用户账号", notes = "启用或者禁用系统用户账号")
	public JsonMap change(SysUser sysUser) {
		Integer code = userService.update(sysUser);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 删除用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除系统用户", notes = "删除系统用户")
	public JsonMap delete(SysUser user) {
		Integer code = userService.deleteUser(user);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * 批量删除
	 * 
	 * @param uids
	 * @return
	 */
	@RequestMapping(value = "deletes", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "批量删除系统用户", notes = "批量删除系统用户")
	public JsonMap deletes(String uids) {
		Integer code = -2;
		if (uids != null && !uids.equals("")) {
			String[] ids = uids.split(",");
			code = userService.deletesUser(ids);
		}
		return SimpleUtils.addOruPdate(code, null, null);

	}

	/**
	 * ********************************************************************用户管理结束*************************************************************
	 */

	/**
	 * ***************************************************************角色管理**************************************************************8
	 */

	/**
	 * 跳转系统角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "toRole", method = RequestMethod.GET)
	@ApiOperation(value = "跳转系统角色", notes = "跳转系统角色")
	public String toRole() {
		return "/liuhao/page/setting/role/role";
	}

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "pageFoundRole", method = RequestMethod.GET)
	@ApiOperation(value = "跳转系统角色", notes = "跳转系统角色")
	public String pageFoundRole(Role role, LayuiPage page) {
		JSONObject obj = new JSONObject();
		List<Role> roles = roleService.pageFoundRole(role, page);
		Integer count = roleService.pageCountRole(role);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", count);
		obj.put("data", roles);
		return obj.toString();
	}

	/**
	 * @param info
	 * @return 查询权限
	 */
	@ResponseBody
	@RequestMapping(value = "getModel", method = RequestMethod.POST)
	@ApiOperation(value = "查询权限", notes = "查询权限")
	public JSONArray getModel(String info) {
		if (info == null) {
			return null;
		}
		String[] ids = info.split(",");
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		List<Module> modules = (List<Module>) sysCache.get(Global.MODULESCACHKEY);
		List<LayuiTree> layuiTrees = new ArrayList<>();
		for (Module m : modules) {
			if (ArrayUtils.contains(ids, m.getMid() + "")) {
				LayuiTree lt = new LayuiTree();
				lt.setId(m.getMid() + "");
				lt.setTitle(m.getMname());
				layuiTrees.add(lt);
			}
		}
		return JSONArray.fromObject(layuiTrees);
	}

	/**
	 * @param info
	 * @return 获取菜单
	 */
	@RequestMapping(value = "getMenus", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取竖菜单", notes = "获取竖菜单")
	public JSONArray getMenus(String info, String binfo) {
		if (binfo == null) {
			return null;
		}
		if (info == null) {
			return null;
		}
		String[] ids = info.split(",");
		String[] bids = binfo.split(",");
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		List<LayuiTree> xtrees = (List<LayuiTree>) sysCache.get(Global.ALLMENUS);
		List<LayuiTree> trees = new ArrayList<>();

		for (LayuiTree lt : xtrees) {
			LayuiTree tree = new LayuiTree();
			tree.setId(lt.getId());
			tree.setTitle(lt.getTitle());
			tree.setJibie(lt.getJibie());
			tree.setSpread(true);
			if (ArrayUtils.contains(bids, tree.getId() + "")) {
				trees.add(tree);
			}
			if (lt.getChildren() != null) {
				List<LayuiTree> trees1 = lt.getChildren();
				List<LayuiTree> tre1 = new ArrayList<>();
				for (LayuiTree lt1 : trees1) {
					LayuiTree tree1 = new LayuiTree();
					tree1.setId(lt1.getId());
					tree1.setTitle(lt1.getTitle());
					tree1.setJibie(lt1.getJibie());
					tree1.setSpread(true);
					if (ArrayUtils.contains(ids, tree1.getId() + "")) {
						tre1.add(tree1);
					}
					if (lt1.getChildren() != null) {
						List<LayuiTree> trees2 = lt1.getChildren();
						List<LayuiTree> tre2 = new ArrayList<>();
						for (LayuiTree lt2 : trees2) {
							LayuiTree tree2 = new LayuiTree();
							tree2.setId(lt2.getId());
							tree2.setTitle(lt2.getTitle());
							tree2.setJibie(lt2.getJibie());
							if (ArrayUtils.contains(ids, tree2.getId() + "")) {
								tre2.add(tree2);
							}

						}
						tree1.setChildren(tre2);
					}

				}
				tree.setChildren(tre1);
			}

		}
		return JSONArray.fromObject(trees);
	}

	/**
	 * 启用或禁用
	 * 
	 * @param r
	 * @return
	 */
	@RequestMapping(value = "updateDisable", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "启用或禁用", notes = "启用或禁用")
	public JsonMap updateDisable(Role r) {
		if (r.getId() == null) {
			return SimpleUtils.addOruPdate(-2, null, null);
		}
		Integer code = roleService.updateRole(r);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param r
	 * @return 修改角色信息
	 */
	@RequestMapping(value = "updateRoleById", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改角色信息", notes = "修改角色信息")
	public JsonMap updateRoleById(Role r) {
		Integer code = 0;

		if ((r.getName() == null || r.getName().equals("")) && (r.getRemark() == null || r.getRemark().equals(""))) {
			return SimpleUtils.addOruPdate(-2, null, "");
		}
		if (r.getName() != null && !r.getName().equals("")) {
			Role role = new Role();
			role.setName(r.getName());
			List<Role> roles = roleService.simpleFoundRole(role);
			if (!roles.isEmpty()) {
				return SimpleUtils.addOruPdate(-3, null, "用户名已存在");
			}
		}

		code = roleService.updateRole(r);

		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param r
	 * @return
	 */
	@RequestMapping(value = "addRole", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改角色信息", notes = "修改角色信息")
	public JsonMap addRole(Role r) {

		if ((r.getName() == null || r.getName().equals("")) && (r.getRemark() == null || r.getRemark().equals(""))) {
			return SimpleUtils.addOruPdate(-2, null, "");
		}
		if (r.getName() != null && !r.getName().equals("")) {
			Role role = new Role();
			role.setName(r.getName());
			List<Role> roles = roleService.simpleFoundRole(role);
			if (!roles.isEmpty()) {
				return SimpleUtils.addOruPdate(-3, null, "用户名已存在");
			}
		}
		r.setModelid("4");
		Integer code = roleService.insertRole(r);

		return SimpleUtils.addOruPdate(code, null, null);

	}

	/**
	 * @param r
	 * @return 删除
	 */
	@ResponseBody
	@RequestMapping(value = "deleteRole", method = RequestMethod.POST)
	@ApiOperation(value = "删除角色", notes = "删除角色")
	public JsonMap deleteRole(Role r) {
		if (r.getId() == null) {
			return SimpleUtils.addOruPdate(-2, null, null);
		}
		Integer code = roleService.deleteRole(r);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param r
	 * @return 查询编辑
	 */
	@ResponseBody
	@RequestMapping(value = "updateFoundRole", method = RequestMethod.POST)
	@ApiOperation(value = "查询编辑", notes = " 查询编辑")
	public JSONArray updateFoundRole(Role r) {
		if (r.getModelid() == null) {
			return null;
		}
		String[] ids = r.getModelid().split(",");
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		List<Module> modules = (List<Module>) sysCache.get(Global.MODULESCACHKEY);
		List<LayuiTree> layuiXtrees = new ArrayList<>();
		for (Module md : modules) {
			LayuiTree lx = new LayuiTree();
			if (md.getMname().equals("查找")) {
				// lx.setDisabled(true);
				lx.setChecked(true);
			} else {
				lx.setDisabled(false);
			}
			lx.setTitle(md.getMname());
			lx.setId(md.getMid() + "");
			if (ArrayUtils.contains(ids, md.getMid() + "")) {
				lx.setChecked(true);
			}
			layuiXtrees.add(lx);
		}
		return JSONArray.fromObject(layuiXtrees);
	}

	/**
	 * @param r
	 * @return 修改角色
	 */
	@ResponseBody
	@RequestMapping(value = "updateRole", method = RequestMethod.POST)
	@ApiOperation(value = "修改角色", notes = "修改角色")
	public JsonMap updateRole(Role r) {
		Integer code = roleService.updateRole(r);
		return SimpleUtils.addOruPdate(code, null, null);
	}

	/**
	 * @param r
	 * @return 查询菜单编辑
	 */
	@RequestMapping(value = "updateFoundMenu", method = RequestMethod.POST)
	@ApiOperation(value = "查询菜单编辑", notes = "查询菜单编辑")
	@ResponseBody
	public JSONArray updateFoundMenu(Role r) {
		String[] ids = null;
		if (r.getMenuid() != null && !r.getMenuid().equals("")) {
			ids = r.getMenuid().split(",");
		}
		String[] bids = null;
		if (r.getBigmenuid() != null && !r.getBigmenuid().equals("")) {
			bids = r.getBigmenuid().split(",");
		}
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		List<LayuiTree> xtrees = (List<LayuiTree>) sysCache.get(Global.ALLMENUS);
		List<LayuiTree> trees = new ArrayList<>();

		for (LayuiTree lt : xtrees) {
			LayuiTree tree = new LayuiTree();
			tree.setId(lt.getId());
			tree.setTitle(lt.getTitle());
			tree.setJibie(lt.getJibie());
			tree.setSpread(true);
			if (ArrayUtils.contains(bids, tree.getId() + "")) {
				tree.setChecked(true);
			}
			if (lt.getChildren() != null) {

				List<LayuiTree> trees1 = lt.getChildren();
				List<LayuiTree> tre1 = new ArrayList<>();
				for (LayuiTree lt1 : trees1) {
					LayuiTree tree1 = new LayuiTree();
					tree1.setId(lt1.getId());
					tree1.setTitle(lt1.getTitle());
					tree1.setJibie(lt1.getJibie());
					tree1.setSpread(true);
					if (ArrayUtils.contains(ids, tree1.getId() + "")) {
						tree1.setChecked(true);
						tree.setChecked(false);
					}
					if (lt1.getChildren() != null) {

						List<LayuiTree> trees2 = lt1.getChildren();
						List<LayuiTree> tre2 = new ArrayList<>();
						for (LayuiTree lt2 : trees2) {
							LayuiTree tree2 = new LayuiTree();
							tree2.setId(lt2.getId());
							tree2.setTitle(lt2.getTitle());
							tree2.setJibie(lt2.getJibie());
							if (ArrayUtils.contains(ids, tree2.getId() + "")) {
								tree2.setChecked(true);
								tree1.setChecked(false);
							}
							tre2.add(tree2);
						}
						tree1.setChildren(tre2);
					}
					tre1.add(tree1);
				}
				tree.setChildren(tre1);
			}
			trees.add(tree);
		}

		System.out.println(JSONArray.fromObject(trees));
		return JSONArray.fromObject(trees);
	}

	/**
	 * *************************************************************角色结束*******************************************
	 */

	/**
	 * 修改秒
	 * 
	 * @return
	 */
	@RequestMapping(value = "toUpPwdPage", method = RequestMethod.GET)
	@ApiOperation(value = "跳转密码修改", notes = "跳转密码修改")
	public String toUpPwdPage() {

		return "/liuhao/page/setting/pwd/changePwd";
	}

	/**
	 * @return
	 */
	@ApiOperation(value = "修改自己的登录密码", notes = "修改自己的登录密码")
	@RequestMapping(value = "upMyPwd", method = RequestMethod.POST)
	@ResponseBody
	public JsonMap upMyPwd(SysUser user, @ApiParam("旧的密码") String oldpwd) {
		Integer code = null;
		String msg = null;
		SysUser sysUser = userService.login(user.getUserName());
		if (sysUser != null) {
			String pwd = SystemUtils.MD5(Global.type, oldpwd, sysUser.getSalt(), Global.iterations);
			if (pwd.equals(sysUser.getPassWord())) {
				String salt = DateUtils.backNum("");
				String newpwd = SystemUtils.MD5(Global.type, user.getPassWord(), salt, Global.iterations);
				SysUser su = new SysUser();
				su.setId(sysUser.getId());
				su.setPassWord(newpwd);
				su.setSalt(salt);
				code = userService.update(su);
			} else {
				code = -3;
				msg = "原密码错误";
			}

		} else {
			code = -3;
			msg = "没有该用户";
		}

		return SimpleUtils.addOruPdate(code, null, msg);
	}

	/////////////////////////////////// ******************************在线用户管理**************************************

	/**
	 * @return 跳转在线用户管理
	 */
	@RequestMapping(value = "toOnlinUser", method = RequestMethod.GET)
	@ApiOperation(value = "跳转在线用户管理", notes = "跳转在线用户管理")
	public String toOnlinUser() {

		return "/liuhao/page/setting/onlineUser/onlineUser";
	}

	/**
	 * @return 获取在线后台用户
	 */
	@ResponseBody
	@RequestMapping(value = "getOnlineUser", method = RequestMethod.GET)
	@ApiOperation(value = "获取在线后台用户", notes = "获取在线后台用户")
	public String getOnlineUser() {
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		List<OnlineUser> onlineUsers = new ArrayList<OnlineUser>();
		String[] ips = new String[sessions.size()];
		String[] names = new String[sessions.size()];
		int i = 0;
		for (Iterator<Session> iterator = sessions.iterator(); iterator.hasNext();) {
			try {
				Session session = (Session) iterator.next();
				String type= (String) session.getAttribute(Global.loginType);
				if(type.equals(Global.back)) {
					OnlineUser onlineUser = new OnlineUser();
					Object key = session.getAttribute(Global.sysUser);
					SysUser user = new SysUser();
					try {
						BeanUtils.copyProperties(user, key);
					} catch (Exception e) {
						//e.printStackTrace();
						log.info("错误");
					}
					if (user.getUserName() != null) {

						if (session.getAttribute(Global.BLOOENKEY) == null) {
							onlineUser.setSessionId(session.getId().toString());
							onlineUser.setUserIp(session.getAttribute("host").toString());
							onlineUser.setUserName(user.getUserName());
							Date date = session.getLastAccessTime();
							onlineUser.setDate(DateUtils.getStrDateTime(date));
							onlineUser.setUserId(user.getId() + "");
							onlineUsers.add(onlineUser);
							ips[i] = session.getHost();
							names[i] = user.getUserName();
							i++;
						}
					}
					
				}

			} catch (Exception e) {
				// e.printStackTrace();
			}

		}

		JSONObject obj = new JSONObject();
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", 0);
		obj.put("data", onlineUsers);

		return obj.toString();
	}

	/**
	 * @param sessionId
	 * @return 踢掉用户
	 */
	@RequestMapping(value = "deleteoutUser", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = " 踢掉用户", notes = " 踢掉用户")
	public JsonMap deleteoutUser(String sessionId) {
		try {
			Session session = sessionDAO.readSession(sessionId);
			if (session != null) {
				session.setAttribute(Global.BLOOENKEY, Boolean.TRUE);
			}
		} catch (Exception e) {
			return SimpleUtils.addOruPdate(-1, null, null);
		}

		return SimpleUtils.addOruPdate(1, null, null);
	}

	///////////////////////////////////////////////////

	///////////////////////////////////////////////////// 前台用户管理/////////////////////////////

	@Autowired
	private UserService webUserService;

	/**
	 * 进入前台用户管理页
	 * 
	 * @return
	 */
	@RequestMapping(value = "towebUser", method = RequestMethod.GET)
	@ApiOperation("跳转用户管理页")
	public String towebUser() {
		return "/liuhao/page/setting/webuser/webuser";
	}

	@RequestMapping("foundWebUsers")
	@ResponseBody
	@ApiOperation("分页查询前台用户")
	public String foundWebUsers(User u, LayuiPage p) {

		List<User> users = webUserService.pageFound(u, p);
		Integer count = webUserService.pageCount(u);
		JSONObject obj = new JSONObject();
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", count);
		obj.put("data", users);
		return obj.toString();

	}

	@ResponseBody
	@RequestMapping(value = "addWebUser", method = RequestMethod.POST)
	@ApiOperation("新增用户")
	public JsonMap addWebUser(User u) {
		Integer result = webUserService.insert1(u);
		return SimpleUtils.addOruPdate(result, null, null);
	}

	@ResponseBody
	@RequestMapping(value = "updatewebUser", method = RequestMethod.POST)
	@ApiOperation("修改用户")
	public JsonMap updateUser(User u) {
		Integer result = webUserService.update(u);
		return SimpleUtils.addOruPdate(result, null, null);
	}

	@ResponseBody
	@RequestMapping(value = "deletewebUser", method = RequestMethod.POST)
	@ApiOperation("删除用户")
	public JsonMap deleteUser(User u) {
		Integer result = webUserService.delete(u);
		return SimpleUtils.addOruPdate(result, null, null);
	}

	/**
	 * 更改用户状态
	 * 
	 * @param u
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateChange", method = RequestMethod.POST)
	@ApiOperation("更改用户状态")
	public JsonMap updateChange(User u) {
		Integer result = webUserService.updateChange(u);
		return SimpleUtils.addOruPdate(result, null, null);

	}
	
	@ResponseBody
	@RequestMapping(value="deleteWebUsers",method=RequestMethod.POST)
	@ApiOperation("批量删除")
	public JsonMap deleteUsers(String strid) {
		
		Integer result = -1;
		if(strid!=null && !strid.isEmpty()) {
			String[] ids = strid.split(",");
			result= webUserService.deletesWebUsers(ids);
		}else {
			result = -2;
		}
		return SimpleUtils.addOruPdate(result, null, null);
		
	}

}
