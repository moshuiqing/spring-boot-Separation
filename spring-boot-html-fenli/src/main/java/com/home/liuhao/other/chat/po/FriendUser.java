package com.home.liuhao.other.chat.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 *  分组人员关联表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FriendUser", description = "分组人员关联表")
public class FriendUser {
	
	@ApiModelProperty("主键id")
	private String id;
	
	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	private String userid;
	
	/**
	 * 分组id
	 */
	@ApiModelProperty("分组id")
	private String Friendid;

	
}
