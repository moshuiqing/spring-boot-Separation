package com.home.liuhao.system.service;

import java.util.List;

import com.home.liuhao.system.po.BigMenu;

public interface BigMenuService {
	
	/**
	 * 查询全部
	 * @return
	 */
	List<BigMenu> simpleFound();
	
	
	/**
	 * 查询数量
	 * @param b
	 * @return
	 */
	Integer pageCount(BigMenu b);
	
	/**
	 * 新增大菜单
	 * @param b
	 * @return
	 */
	Integer insertMenu(BigMenu b,String roleId);
	
	/**
	 * 修改菜单
	 * @param b
	 * @return
	 */
	Integer updateMenu(BigMenu b);
	
	/**
	 * 删除
	 * @param b
	 */
	Integer deleteMenu(BigMenu b);
	

}
