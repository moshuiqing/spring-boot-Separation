package com.home.lh.webbussess.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.home.lh.webbussess.po.User;

/**
 * 用户mapper
 * @author liuhao
 *
 */
public interface UserMapper {
	
	/**
	 * 新增
	 * @param user
	 * @return
	 */
	Integer insert(User user);
	
	/**
	 * 新增
	 * @param user
	 * @return
	 */
	Integer insert1(User u);
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	Integer update(User user);
	

	
	/**
	 * 删除
	 * @param user
	 */
	void delete(User user);
	
	/**
	 * 简易查询
	 * @param user
	 * @return
	 */
	List<User> simpleFound(User user);
	
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	User login(User user);
	
	/**
	 * 查询数量
	 * @param user
	 * @return
	 */
	Integer pageCount(User user);
	
	/**
	 * 分页查询
	 * @param user
	 * @return
	 */
	List<User> pageFound(@Param("u")User u,@Param("start")Integer start,@Param("end")Integer end);
	
	/**
	 * 保存最后登录时间
	 * @param u
	 * @return
	 */
	Integer saveLoginTime(User u);
	
	/**
	 * 批量删除
	 * @param uids
	 */
	void deletesWebUsers(@Param("uids")String[] uids);

}
