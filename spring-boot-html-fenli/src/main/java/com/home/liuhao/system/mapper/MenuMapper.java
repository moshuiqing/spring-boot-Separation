package com.home.liuhao.system.mapper;

import java.util.List;

import com.home.liuhao.system.po.Menu;
import com.home.liuhao.util.Menus;
public interface MenuMapper {
	
	/**
	 * 查询全部菜单
	 * @return
	 */
	List<Menus> simpleFound(Menus m);
	
	
	/**
	 * 删除菜单
	 * @param m
	 */
	void deleteMenus(Menu m);
	
	/**
	  *  新增菜单
	 * @param m
	 * @return
	 */
	Integer insertMenus(Menu m);
	
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
