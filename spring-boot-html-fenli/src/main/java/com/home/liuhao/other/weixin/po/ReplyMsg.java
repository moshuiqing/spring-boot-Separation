package com.home.liuhao.other.weixin.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="ReplyMsg",description="微信回复信息")
public class ReplyMsg {
	
	@ApiModelProperty("主键id")
	private Integer id;
	
	@ApiModelProperty("问题")
	private String problem;
	
	@ApiModelProperty("答案")
	private String answer;
	
	@ApiModelProperty("是否删除")
	private Integer isdel;
	
	private String sort;

}
