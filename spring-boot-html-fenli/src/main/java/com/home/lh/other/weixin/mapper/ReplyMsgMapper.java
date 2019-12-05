package com.home.lh.other.weixin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.home.lh.other.weixin.po.ReplyMsg;

/**
 * 微信回复信息mapper
 * 
 * @author liuhao
 *
 */
public interface ReplyMsgMapper {

	/**
	 * 新增
	 * @param msg
	 * @return
	 */
	Integer insert(ReplyMsg msg);
	
	/**
	 * 删除
	 * @param msg
	 * @return
	 */
	void delete(ReplyMsg msg);
	
	
	/**
	 * 简易查询
	 * @param msg
	 * @return
	 */
	List<ReplyMsg> simpleFound(ReplyMsg msg);
	
	/**
	 * 修改
	 * @param msg
	 * @return
	 */
	Integer update(ReplyMsg msg);
	
	/**
	 * 查询数量
	 * @param msg
	 * @return
	 */
	Integer pageCount(ReplyMsg msg);
	
	/**
	 * 分页查询
	 * @param msg
	 * @param start
	 * @param end
	 * @return
	 */
	List<ReplyMsg> pageFound(@Param("m")ReplyMsg msg,@Param("start")Integer start,@Param("end")Integer end);

}
