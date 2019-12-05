package com.home.lh.other.chat.po;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 * 群组 实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ChatGoup", description = "群组")
public class ChatGoup {
	
	/**
	 * 群组id
	 */
	@ApiModelProperty("主键id")
	private String id;
	
	/**
	 * 群组名
	 */
	@ApiModelProperty("群组名")
	private String groupname;
	
	/**
	 * 群头像
	 */
	@ApiModelProperty("群头像")
	private String avatar;
	
	/**
	 * 群所属人
	 */
	@ApiModelProperty("群所属人")
	private String userid;
	
	
	/**
	 * 群说明
	 */
	@ApiModelProperty("群说明")
	private String content;
	
	
	private List<ChatUser> list;
	
	private String username;
	
	


}
