package com.home.lh.other.chat.util;

/**
 * @author ai996
 *  工具类用于接受信息
 */
public class FriendInfo {
	
	/**
	 * 对方用户ID
	 */
	private String fromuid;
	
	/**
	 * 对方设定的好友分组
	 */
	private String fromgroup;
	
	/**
	 * 我设定的好友分组
	 */
	private String group;
	
	/**
	 * 自己的id
	 */
	private String uid;

	public String getFromuid() {
		return fromuid;
	}

	public void setFromuid(String fromuid) {
		this.fromuid = fromuid;
	}

	public String getFromgroup() {
		return fromgroup;
	}

	public void setFromgroup(String fromgroup) {
		this.fromgroup = fromgroup;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
