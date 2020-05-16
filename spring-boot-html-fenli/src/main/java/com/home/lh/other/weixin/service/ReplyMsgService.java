package com.home.lh.other.weixin.service;

import java.util.List;

import com.home.lh.other.weixin.po.ReplyMsg;
import com.home.lh.util.LayuiPage;

public interface ReplyMsgService {
	

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
	Integer delete(ReplyMsg msg);
	
	
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
	List<ReplyMsg> pageFound(ReplyMsg msg,LayuiPage page);
	
	

}
