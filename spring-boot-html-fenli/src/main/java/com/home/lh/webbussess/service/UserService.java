package com.home.lh.webbussess.service;

import java.util.List;

import com.home.lh.util.JsonMap;
import com.home.lh.util.LayuiPage;
import com.home.lh.webbussess.po.User;

/**
 * 用户service
 * @author liuhao
 *
 */
public interface UserService {
	
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
	Integer insert1(User user);
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	Integer update(User user);
	
	Integer wxUpdate(User user);
	
	/**
	 * 删除
	 * @param user
	 */
	Integer delete(User user);
	
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
	List<User> pageFound(User u,LayuiPage page);
	
	/**
	 * 保存最后登录时间
	 * @param u
	 * @return
	 */
	Integer saveLoginTime(User u);
	
	
	/**
	 * 启用禁用
	 * @param u
	 * @return
	 */
	Integer updateChange(User u);
	
	/**
	 * 批量删除
	 * @param uids
	 */
	Integer deletesWebUsers(String[] uids);
	
	/**
	 * 退出登录
	 * @return
	 */
	JsonMap loginOut();

}
