package com.home.lh.other.chat.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 * 用户与群关联
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "GroupUser", description = "用户与群关联")
public class GroupUser {

	@ApiModelProperty("主键id")
	private Integer id;
	
	@ApiModelProperty("群组id")
	private String groupid;
	
	@ApiModelProperty("用户id")
	private String userid;

	
	
}
