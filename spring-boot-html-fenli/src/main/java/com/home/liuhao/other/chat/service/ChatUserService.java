package com.home.liuhao.other.chat.service;

import java.util.List;

import com.home.liuhao.other.chat.po.ChatRecord;
import com.home.liuhao.other.chat.po.ChatUser;
import com.home.liuhao.system.po.Person;
import com.home.liuhao.util.LayuiPage;

/**
 * @author ai996
 * 聊天用户service
 */
public interface ChatUserService {
	
	/**
	 * @param user
	 * @return
	 * 查询聊天用户
	 */
	List<ChatUser> foundChatUser(ChatUser user);
	
	/**
	 * @param sendId
	 * @param toId
	 * @param type
	 * @return
	 * 获取与好友的聊天记录
	 */
	List<ChatUser> getRecord(String sendId,String toId,String type);
	
	/**
	 * @param id
	 * @return
	 * 获取群组的聊天记录
	 */
	List<ChatUser> getGroupRecord(String id);
	
	/**
	 * @param chatRecord
	 * @return
	 * 保存聊天记录
	 */
	Integer insertRecord(ChatRecord chatRecord);
	
	/**
	 * @param id
	 * @return
	 * 修改消息状态
	 */
	Integer updateState(String id);
	
	/**
	 * @param uid
	 * @return
	 * 获取推荐用户
	 */
	List<ChatUser> getRecom(String uid);
	
	/**
	 * @return
	 * 获取用户数量
	 */
	Integer getCount(String uid,String username);
	
	/**
	 * @param username
	 * @param page
	 * @return
	 * 分页查询
	 */
	List<ChatUser> getPage(String uid,String username,LayuiPage page);
	
	
	/**
	 * 查询人员资料
	 * @param id
	 * @return
	 */
	Person fondFrendData(String id);
	


}
