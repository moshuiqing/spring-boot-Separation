package com.home.liuhao.other.weixin.po;

import org.springframework.data.elasticsearch.annotations.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="MsgStrInfo",description="微信回复信息")
@Document(indexName = "msgstrinfo")    //必须小写
public class MsgStrInfo {
	
	
	
	@ApiModelProperty("排序")
	private Integer id;

	@ApiModelProperty("问题")
	private String problem;
	
	@ApiModelProperty("答案")
	private String answer;

}
