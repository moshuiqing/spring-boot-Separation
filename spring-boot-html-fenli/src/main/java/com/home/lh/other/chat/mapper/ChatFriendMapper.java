package com.home.lh.other.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.home.lh.other.chat.po.ChatFriend;
import com.home.lh.other.chat.po.FriendUser;

@Mapper
public interface ChatFriendMapper {
	
	/**
	 * @param userid
	 * @return
	 * 查询我的好友
	 */
	List<ChatFriend> foundChatFriend(@Param("userid")String userid);
	
	/**
	 * @param chatFriend
	 * @return
	 * 新增分组
	 */
	Integer addChatFriend(ChatFriend chatFriend);
	
	/**
	 * @param friendUser
	 * @return
	 * 绑定好友关系
	 */
	Integer BindFriend(FriendUser friendUser);
	
	/**
	 * @param chatFriend
	 * @return
	 * 删除分组根据
	 */
	Integer removeChatFriend(ChatFriend chatFriend);
	
	/**
	 * @param array
	 * @param Friendid
	 * 批量新增
	 */
	void addChatFriends(@Param("list")List<FriendUser> list);
	
	/**
	 * @param userid
	 * @return
	 * 返回id串
	 */
	List<FriendUser> getStrUserid(String userid);
	
	/**
	 * @param chatFriend
	 * @return
	 * 修改分组名
	 */
	Integer upChatFriendName(ChatFriend chatFriend);
	
	/**
	 * @param uid
	 * @param frienduid
	 * @return
	 * 根据用户id和好友id删除分组下的好友
	 */
	Integer delFriendUid(@Param("uid")String uid,@Param("frienduid")String frienduid);
	
}
