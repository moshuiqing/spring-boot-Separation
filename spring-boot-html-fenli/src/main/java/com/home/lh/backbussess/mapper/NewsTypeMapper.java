package com.home.lh.backbussess.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.home.lh.backbussess.po.NewsType;

/**
 * @author ai996
 * 新闻类型
 *
 */
public interface NewsTypeMapper {

	/**
	 * @param newsType
	 * @return
	 * 普通查询
	 */
	List<NewsType> simpleFound(NewsType newsType);

	/**
	 * 修改
	 * @param newsType
	 * @return
	 */
	Integer update(NewsType newsType);

	/**
	 * 删除
	 * @param newsType
	 * @return
	 */
	Integer delete(NewsType newsType);

	/**
	 * 查询数量
	 * @param newsType
	 * @return
	 */
	Integer pageCount(NewsType newsType);

	/**
	 * 新增
	 * @param newsType
	 * @return
	 */
	Integer insert(NewsType newsType);

	/**
	 * 分页查询
	 * @param n
	 * @param one
	 * @param two
	 * @return
	 */
	List<NewsType> pageFound(@Param("n") NewsType n, @Param("one") Integer one,@Param("two") Integer two);

}
