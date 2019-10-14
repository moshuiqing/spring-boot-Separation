package com.home.liuhao.other.chat.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 *  聊天用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ChatUser", description = "聊天用户信息")
public class ChatUser {
	
	@ApiModelProperty("主键id")
	private String id;
	
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String username;

	
	/**
	 * 登录状态 //在线状态 online：在线、hide：隐身  /若值为offline代表离线
	 */
	@ApiModelProperty("登录状态")
	private String status;
	
	/**
	 * 签名  
	 */
	@ApiModelProperty("签名")
	private String sign;
	
	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;
	

	/**
	 * 时间轴
	 */
	@ApiModelProperty("时间轴")
	private Long timestamp;
	
	/**
	 * 内容
	 */
	@ApiModelProperty("内容")
	private String content;
	
	
	
	/**
	 * 发送者ID
	 */
	private String sendId;
	
	/**
	 * 接受者id  或群id
	 */
	private String toId;


	
	private String type;


	
	//取数据用 存分组
	private String group;


	
	
	
}
