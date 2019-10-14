package com.home.liuhao.system.service;

import java.util.List;

import com.home.liuhao.system.po.BigMenu;
import com.home.liuhao.system.po.Person;
import com.home.liuhao.system.po.SysUser;
import com.home.liuhao.util.LayuiPage;

public interface SysUserService {
	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 */
	SysUser login(SysUser user);
	
	/**
	 * 根据姓名登录
	 * @param username
	 * @return
	 */
	public SysUser login(String username);
	
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	Integer update(SysUser user);
	
	/**
	 * 返回用户大菜单
	 * @return
	 */
	List<BigMenu> getUserBigMenu(SysUser u);
	
	
	/**
	 * 简易查询
	 * @return
	 */
	List<SysUser> simpleFound(SysUser user);
	
	/**
	  * 分页查询
	 * @param s
	 * @param p
	 * @return
	 */
	List<SysUser> pageFound(SysUser s,LayuiPage p);
	
	
	/**
	 * 查询数量
	 * @param s
	 * @return
	 */
	Integer pageCount(SysUser s);
	
	/**
	 * 删除
	 * @param s
	 */
	Integer deleteUser(SysUser s);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	Integer deletesUser(String[] ids);
	
	
	/**
	 * 修改人员
	 * @param p
	 * @return
	 */
	Integer updatePerson(Person p);
	
	/**
	 * 新增人员
	 * @param p
	 * @return
	 */
	Integer insertPerson(Person p);
	
	/**
	 *   新增
	 * @param s
	 * @return
	 */
	Integer insertSysUser(SysUser s);

	
	
	
	
	
	
}
