package com.home.lh.system.mapper;

import com.home.lh.system.po.Person;

/**
 * 人員信息
 * @author liuhao
 *
 */
public interface PersonMapper {
	
	
	/**
	 * 修改头像
	 * @param p
	 * @return
	 */
	Integer updatePerson(Person p);
	
	
	/**
	 * 新增
	 * @param p
	 * @return
	 */
	Integer insertPerson(Person p);
	
	

}
