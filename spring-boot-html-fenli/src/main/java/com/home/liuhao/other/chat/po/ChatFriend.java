package com.home.liuhao.other.chat.po;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 *  好友 分组
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ChatFriend", description = "好友分组")
public class ChatFriend {
	
	/**
	 * 好友分组名
	 */
	@ApiModelProperty("好友分组名")
	private String groupname;
	
	/**
	 * 分组id
	 */
	@ApiModelProperty("分组id")
	private String id;
	
	/**
	 * 分组下的好友列表
	 */
	@ApiModelProperty("分组下的好友列表")
	private List<ChatUser> list;
	
	/**
	 * 分组所属用户
	 */
	@ApiModelProperty("分组所属用户id")
	private String goupUserid;
	


}
