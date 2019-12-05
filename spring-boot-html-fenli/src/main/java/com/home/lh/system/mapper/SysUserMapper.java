package com.home.lh.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.home.lh.system.po.SysUser;


public interface SysUserMapper {

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 */
	SysUser login(SysUser user);
	
	/**
	 * 登录查询
	 * @param username
	 * @return
	 */
	SysUser login(String username);
	/**
	 * 修改
	 * 
	 * @param user
	 * @return
	 */
	Integer update(SysUser user);

	@Select("select * from sysuser where id=#{id}")
	SysUser findById(String id);
	
	/**
	 * 简易查询
	 * @return
	 */
	List<SysUser> simpleFound(SysUser user);
	
	/**
	  * 分页查询
	 * @param s
	 * @param start
	 * @param end
	 * @return
	 */
	List<SysUser> pageFound(@Param("s")SysUser s,@Param("start")Integer start,@Param("end")Integer end);
	
	
	/**
	 * 查询数量
	 * @param s
	 * @return
	 */
	Integer pageCount(@Param("s")SysUser s);
	
	/**
	 * 删除
	 * @param s
	 */
	void deleteUser(SysUser s);
	
	/**
	 * 批量删除
	 * @param s
	 */
	void deletesUsers(@Param("uids")String[] uids);
	
	/**
	 *   新增
	 * @param s
	 * @return
	 */
	Integer insertSysUser(SysUser s);

}
