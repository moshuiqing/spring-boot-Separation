package com.home.lh.other.chat.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 *  通知消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ChatNotice", description = "通知消息")
public class ChatNotice {
	
	/**
	 * 主键id
	 */
	@ApiModelProperty("主键id")
	private Integer id;
	
	/**
	 * 申请名称
	 */
	@ApiModelProperty("申请名称")
	private String content;
	
	/**
	 * 接收用户id
	 */
	@ApiModelProperty("接收用户")
	private String uid;
	
	/**
	 * 发送用户id
	 */
	@ApiModelProperty("发送用户")
	private String from;
	
	/**
	 * 通知类型 1 加好友  ，2 加群 3:系统消息
	 */
	@ApiModelProperty("通知类型")
	private String type;
	
	
	/**
	 * 通知消息
	 */
	@ApiModelProperty("通知消息")
	private String remark;
	
	/**
	 * 分组id(或群id)
	 */
	@ApiModelProperty("分组id")
	private String from_group;
	
	/**
	 * 
	 */
	private String href;
	
	/**
	 *  0未读  1 已读 
	 */
	private String read;
	
	/**
	 * 时间
	 */
	private String time;
	
	private ChatUser user;


}
