package com.home.lh.other.oauth.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 * 客户端信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="Client",description="授权客户端实体")
public class Client {
	
	@ApiModelProperty("主键id")
	private String id;
	
	@ApiModelProperty("客户端名称")
	private String clientName;
	
	@ApiModelProperty("认证Id")
	private String clientId;
	
	@ApiModelProperty("赋予的key")
	private String clientSecret;

	
	

}
