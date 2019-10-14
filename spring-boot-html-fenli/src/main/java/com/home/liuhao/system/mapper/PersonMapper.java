package com.home.liuhao.system.mapper;

import com.home.liuhao.system.po.Person;

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
