package com.home.lh.system.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 *在线用户表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "OnlineUser", description = "在线用户")
public class OnlineUser {
	
	@ApiModelProperty("sessionId")
	private String sessionId;
	
	@ApiModelProperty("用户id")
	private String userId;
	
	@ApiModelProperty("用户ip")
	private String userIp;
	
	@ApiModelProperty("用户名")
	private String userName;
	@ApiModelProperty("时间")
	private String date;
	

	
	
}
