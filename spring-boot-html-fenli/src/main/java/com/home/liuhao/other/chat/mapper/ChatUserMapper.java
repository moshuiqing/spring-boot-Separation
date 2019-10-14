package com.home.liuhao.other.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.home.liuhao.other.chat.po.ChatRecord;
import com.home.liuhao.other.chat.po.ChatUser;
import com.home.liuhao.system.po.Person;

/**
 * @author ai996
 * 聊天消息接口
 */
@Mapper
public interface ChatUserMapper {
	
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
	List<ChatUser> getRecord(@Param("sendId")String sendId,@Param("toId")String toId,@Param("type")String type);
	
	/**
	 * @param id
	 * @return
	 * 获取群组的聊天记录
	 */
	List<ChatUser> getGroupRecord(@Param("id")String id);
	
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
	Integer updateState(@Param("id")String id);
	
	/**
	 * @param uid
	 * @return
	 * 获取推荐用户
	 */
	List<ChatUser> getRecom(@Param("uid")String uid);
	
	/**
	 * @return
	 * 获取用户数量
	 */
	Integer getCount(@Param("uid")String uid,@Param("username")String username);
	
	/**
	 * @param username
	 * @param start
	 * @param end
	 * @return
	 * 分页查询
	 */
	List<ChatUser> getPage(@Param("id")String uid,@Param("username")String username,@Param("start")Integer start,@Param("end")Integer end);
	
	
	/**
	 * 查询人员资料
	 * @param id
	 * @return
	 */
	Person fondFrendData(@Param("id")String id);

}
