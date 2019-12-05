package com.home.lh.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.system.mapper.MenuMapper;
import com.home.lh.system.mapper.PersonMapper;
import com.home.lh.system.mapper.SysUserMapper;
import com.home.lh.system.po.BigMenu;
import com.home.lh.system.po.Person;
import com.home.lh.system.po.SysUser;
import com.home.lh.system.service.SysUserService;
import com.home.lh.util.Global;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.Menus;
import com.home.lh.util.systemutil.SimpleUtils;

@Service
public class SysUserServiceImpl implements SysUserService {
	
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private EhCacheManager ehCacheManager;

	@Autowired
	private MenuMapper menusMapper;
	
	@Autowired
	private PersonMapper personMapper;

	@Override
	public SysUser login(SysUser user) {
		SysUser u = userMapper.login(user);
		if (u == null) {
			return null;
		}
		// 存入缓存
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");

		@SuppressWarnings("unchecked")
		List<BigMenu> bigMenus = (List<BigMenu>) sysCache.get(Global.BIGMENU);

		String[] menuids = null;
		if (u.getRole()!= null && u.getRole().getMenuid()!=null) {
			menuids = u.getRole().getMenuid().split(",");
		}
		String[] bigmenuids = null;
		if (u.getRole()!= null  && u.getRole().getBigmenuid()!=null) {
			bigmenuids = u.getRole().getBigmenuid().split(",");
		}

		Map<String, Object> menumap = new HashMap<>();
		List<BigMenu> bms = new ArrayList<>();
		// 减去大菜单
		for (BigMenu bigMenu : bigMenus) {
			if (ArrayUtils.contains(bigmenuids, bigMenu.getId().toString())) {
				bms.add(bigMenu);
				Menus ms = new Menus();
				ms.setMpName(bigMenu.getMpname());
				List<Menus> menuss = menusMapper.simpleFound(ms);
				Iterator<Menus> iterator = menuss.iterator();
				while (iterator.hasNext()) {
					Menus menus = iterator.next();
					if (ArrayUtils.contains(menuids, menus.getId().toString())) {
						Menus men = new Menus();
						men.setParent(menus.getId() + "");
						List<Menus> list = menusMapper.simpleFound(men);
						Iterator<Menus> iterator2 = list.iterator();
						while (iterator2.hasNext()) {
							Menus menus2 = iterator2.next();
							if (!ArrayUtils.contains(menuids, menus2.getId().toString())) {
								iterator2.remove();
							}
						}
						menus.setChildren(list);

					} else {
						iterator.remove();
					}

				}
				menumap.put(bigMenu.getMpname(), menuss);
			}
		}
		sysCache.put(u.getUserName(), menumap);
		sysCache.put(Global.USERBIGMENU+u.getUserName(), bms);
		return u;
	}
	
	@Override
	public SysUser login(String username) {

		return userMapper.login(username);
	}
	
	
	/* 
	 *  更新数据同时更新缓存
	 *  	@CacheEvict 删除
	 */
	//@CachePut(value="sysUser",key="#user.id")  
	@Override

	public Integer update(SysUser user) {
		
		return userMapper.update(user);
	}

	/**
	 * cacheNames/value :指定缓存组件的名字
	 * key:缓存数据使用的key;可以指定也可以默认;编写spel  #id；参数id的值 
	 * CacheManager:指定缓存管理器；或者cacheResolver 指定获取解析器‘
	 * condition:指定符合条件的情况下才缓存；condition="#id>0"
	 * unless:否定缓存；当unless指定的条件为true,方法的返回值就不会被缓存
	 * sync:是否使用异步模式
	 * 
	 * @return
	 */
	//@Cacheable(cacheNames="bigMenu",key="#u.id")
	@Override
	public List<BigMenu> getUserBigMenu(SysUser u) {
		Cache<String, Object> sysCache = ehCacheManager.getCache("menuEacache");
		@SuppressWarnings("unchecked")
		List<BigMenu> bigMenus = (List<BigMenu>) sysCache.get(Global.USERBIGMENU+u.getUserName());
		return bigMenus;
	}

	@Override
	public List<SysUser> simpleFound(SysUser user) {
		
		return userMapper.simpleFound(user);
	}

	@Override
	public List<SysUser> pageFound(SysUser s, LayuiPage p) {
	
		return userMapper.pageFound(s, p.getStart(), p.getEnd());
	}

	@Override
	public Integer pageCount(SysUser s) {
		
		return userMapper.pageCount(s);
	}

	@Override
	public Integer deleteUser(SysUser s) {
		Integer result=-1;
		try {
			userMapper.deleteUser(s);
			result = 1;
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return result;
		
	}

	@Override
	public Integer deletesUser(String[] ids) {
		Integer result=-1;
		try {
			userMapper.deletesUsers(ids);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Integer insertPerson(Person p) {
		if(SimpleUtils.isEmpty(p.getId())) {
			p.setId(UUID.randomUUID().toString());
		}
		
	
		return personMapper.insertPerson(p);
	}

	@Override
	public Integer updatePerson(Person p) {
		
		return personMapper.updatePerson(p);
	}

	@Override
	public Integer insertSysUser(SysUser s) {
		s.setId(UUID.randomUUID().toString());
		
		return  userMapper.insertSysUser(s);
	}


}
