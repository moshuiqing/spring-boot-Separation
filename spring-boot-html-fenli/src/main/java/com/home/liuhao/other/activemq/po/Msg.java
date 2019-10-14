package com.home.liuhao.other.activemq.po;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="Msg",description="消息实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg {
	
	/**
	 * 消息类型
	 */
	private String type;
	
	/**
	 * 消息内容
	 */
	private Object  data;
	
	private String mycode;

}
