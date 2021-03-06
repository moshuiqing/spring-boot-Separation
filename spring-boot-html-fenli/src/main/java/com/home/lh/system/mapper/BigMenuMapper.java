package com.home.lh.system.mapper;

import java.util.List;

import com.home.lh.system.po.BigMenu;

public interface BigMenuMapper {
	
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
	Integer insertMenu(BigMenu b);
	
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
	void deleteMenu(BigMenu b);


}
