package com.home.lh.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.home.lh.system.po.Role;

/**
 * 
 * @author ai996 角色dao
 */
public interface RoleMapper {
	
	
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
	void deleteRole(Role r);
	
	
	/**
	 * 分页查询
	 * @param r
	 * @return
	 */
	List<Role> pageFoundRole(@Param("r")Role r,@Param("start")Integer start,@Param("end")Integer end);
	
	
	/**
	 * 查询数量
	 * @return
	 */
	Integer pageCountRole(@Param("r")Role r);
	
	
	
	
	
	
	
	
	
	

}
