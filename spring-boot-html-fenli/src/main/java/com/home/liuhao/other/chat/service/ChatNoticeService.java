package com.home.liuhao.other.chat.service;

import java.util.List;

import com.home.liuhao.other.chat.po.ChatNotice;

public interface ChatNoticeService {
	
	
	/**
	 * @param notice
	 * @return
	 * 新增
	 */
	Integer insertNotice(ChatNotice notice);
	
	/**
	 * @param id
	 * @return
	 * 根据id查询
	 */
	ChatNotice getNoticeByid(ChatNotice notice);
	
	/**
	 * @return
	 *  根据用id查询全部未读的消息
	 */
	List<ChatNotice> getMsgBox(String uid);
	
	/**
	 * @param id
	 * @return
	 * 修改消息状态根据id
	 */
	Integer updateNoticeRead(String id);
	
	/**
	 * 修改接受者系统消息状态
	 * @param chatNotice
	 * @return
	 */
	Integer upUidRead(ChatNotice chatNotice);
	
	/**
	 * 查询发给我的未读的消息数量
	 * @param uid
	 * @return
	 */
	Integer getNoReadByUid(String uid);
	
	/**
	 * 批量新增系统消息
	 * @param list
	 */
	void insertsNotice(List<ChatNotice> list);

}
