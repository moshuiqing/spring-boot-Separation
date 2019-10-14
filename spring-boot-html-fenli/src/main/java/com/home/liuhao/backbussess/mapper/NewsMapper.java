package com.home.liuhao.backbussess.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.home.liuhao.backbussess.po.News;

public interface NewsMapper {
	
	
	/**
	 * 新增
	 * @param n
	 * @return
	 */
	Integer insert(News n);
	
	/**
	 * 修改
	 * @param n
	 * @return
	 */
	Integer update(News n);
	
	/**
	 * 删除
	 * @param n
	 * @return
	 */
	Integer delete(News n);
	
	/**
	 * 分页数量
	 * @param n
	 * @return
	 */
	Integer pageCount(News n);
	
	/**
	 * 普通查询
	 * @param n
	 * @return
	 */
	List<News> simpleFound(News n);
	
	/**
	 * 分页查询
	 * @param n
	 * @param one
	 * @param two
	 * @return
	 */
	List<News> pageFound(@Param("n")News n,@Param("one")Integer one,@Param("two")Integer two);
	
	/**
	 * 查询最新
	 * @return
	 */
	List<News> foundTop();
	
	/**
	 *info: 批量删除
	 * @param ids
	 */
	void deletes(@Param("ids")String[] ids);
	
}
