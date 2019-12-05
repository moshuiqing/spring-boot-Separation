package com.home.lh.other.weixin.entity;

public class TextMessage {
	
	

	/**
	 * 类型
	 */
	private String MsgType;
	
	/**
	 * 	开发者微信号
	 */
	private String FromUserName;
	
	
	/**
	 * 接收方帐号（收到的OpenID）
	 */
	private String ToUserName;
	
	
	/**
	 * 创建时间
	 */
	private String CreateTime;
	
	
	/**
	 * 内容
	 */
	private String Content;


	public String getMsgType() {
		return MsgType;
	}


	public void setMsgType(String msgType) {
		MsgType = msgType;
	}


	public String getFromUserName() {
		return FromUserName;
	}


	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}


	public String getToUserName() {
		return ToUserName;
	}


	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}


	public String getCreateTime() {
		return CreateTime;
	}


	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}


	public String getContent() {
		return Content;
	}


	public void setContent(String content) {
		Content = content;
	}
	
	
	
	
}
