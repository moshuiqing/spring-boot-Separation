package com.home.lh.other.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.home.lh.other.chat.po.ChatGoup;
import com.home.lh.other.chat.po.ChatUser;
import com.home.lh.other.chat.po.GroupUser;

@Mapper
public interface ChatGroupMapper {
	 
	/**
	 * @param chatUser
	 * @return
	 * 根据用户id获取群
	 */
	List<ChatGoup> foundChatGroup(ChatUser chatUser);
	
	/**
	 * @param id
	 * @return
	 * 根据群id获取群人员
	 */
	List<ChatUser> foundGroupByid(@Param("id")String id);
	
	/**
	 * @param groupname
	 * @return
	 * 分页查询
	 */
	Integer getGroupCount(@Param("groupname")String groupname,@Param("id")String id);
	
	/**
	 * @param groupname
	 * @param start
	 * @param end
	 * @return
	 * 分页查询
	 */
	List<ChatGoup> getPageGroup(@Param("id")String id,@Param("groupname")String groupname,@Param("start")Integer start,@Param("end")Integer end);


	/**
	 * @param id
	 * @return
	 * 根据群id，获取群主id
	 */
	String getUidByGroupId(String id);
	
	/**
	 * @param groupid
	 * @param userid
	 * @return
	 * 绑定用户和群
	 */
	Integer addUserToGroup(@Param("groupid")String groupid,@Param("userid")String userid);
	
	/**
	 * @param id
	 * @return
	 * 根据id查询群信息
	 */
	ChatGoup getGroupInfoByid(String id);
	
	/**
	 * @param groupUser
	 * @return
	 * 退群
	 */
	Integer refundGroup(GroupUser groupUser);
	
	/**
	 * @param id
	 * @return
	 * 查询群信息
	 */
	ChatGoup getChatGroupInfo(String id);
	
	/**
	 * @param id
	 * @return
	 * 解散群
	 */
	Integer dissolution(String id);
	
	/**
	 * 查询前10个群
	 * @return
	 */
	List<ChatGoup> fondTopTen();
	
	/**
	 * 新增群组
	 * @return
	 */
	Integer insertGroup(ChatGoup cg);
	
	/**
	 * @param groupname
	 * @return
	 */
	Integer getByName(@Param("groupname")String groupname);
	
	
}
