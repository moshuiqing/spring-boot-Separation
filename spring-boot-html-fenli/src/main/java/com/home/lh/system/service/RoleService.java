package com.home.lh.system.service;

import java.util.List;

import com.home.lh.system.po.Role;
import com.home.lh.util.LayuiPage;

/**
 * 角色 service
 * @author liuhao
 *
 */
public interface RoleService {
	
	
	/**
	 * 简易查询
	 * @param r
	 * @return
	 */
	List<Role> simpleFoundRole(Role r);
	
	/**
	 * 新增角色
	 * @param r
	 * @return
	 */
	Integer insertRole(Role r);
	
	/**
	 * 修改角色
	 * @param r
	 * @return
	 */
	Integer updateRole(Role r);
	
	/**
	 *删除角色
	 * @param r
	 */
	Integer deleteRole(Role r);
	
	
	/**
	 * 分页查询
	 * @param r
	 * @return
	 */
	List<Role> pageFoundRole(Role r,LayuiPage p);
	
	
	/**
	 * 查询数量
	 * @return
	 */
	Integer pageCountRole(Role r);
	

}
