package com.home.lh.system.service;

import java.util.List;

import com.home.lh.system.po.Menu;
import com.home.lh.util.Menus;

public interface MenuService {


	
	/**
	 * 缓存菜单和权限
	 */
	public void cacheMenuModule();
	
	/**
	 * 查询全部菜单
	 * @return
	 */
	List<Menus> simpleFound(Menus m);
	
	
	/**
	 * 删除菜单
	 * @param m
	 */
	Integer deleteMenus(Menu m);
	
	/**
	  *  新增菜单
	 * @param m
	 * @return
	 */
	Integer insertMenus(Menu m,String roleId);
	
	/**
	 * 修改菜单
	 * @param m
	 * @return
	 */
	Integer updateMenus(Menu m);
	
	/**
	  *  查询数量
	 * @param m
	 * @return
	 */
	Integer countNum(Menu m);

	


}
