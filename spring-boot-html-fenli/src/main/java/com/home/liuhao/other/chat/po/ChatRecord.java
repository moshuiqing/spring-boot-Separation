package com.home.liuhao.other.chat.po;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 * 聊天记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ChatRecord", description = "聊天记录")
public class ChatRecord {
	
	/**
	 * 记录id
	 */
	@ApiModelProperty("记录id")
	private Integer id;

	
	/**
	 * 发送者ID
	 */
	@ApiModelProperty("发送者id")
	private String sendId;
	
	/**
	 * 接受者id  或群id
	 */
	@ApiModelProperty("接受者id")
	private String toId;
	
	/**
	 * 信息
	 */
	@ApiModelProperty("信息id")
	private String  content;
	
	/**
	 * 时间轴
	 */
	@ApiModelProperty("时间轴id")
	private Date timestamp;
	
	/**
	 * 类型
	 */
	@ApiModelProperty("类型id")
	private String type;
	
	/**
	 * 聊天信息状态，0未读 1 已读
	 */
	@ApiModelProperty("聊天信息状态id")
	private String state;

	
}
