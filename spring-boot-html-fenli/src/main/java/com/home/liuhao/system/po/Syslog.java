package com.home.liuhao.system.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 * 系统日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Syslog",description="系统日志")
public class Syslog {
	
	/**
	 * 主键id
	 */
	@ApiModelProperty("主键id")
	private Integer logId;
	
	/**
	 * 请求地址
	 */
	@ApiModelProperty("请求地址")
	private String url;
	
	/**
	 * 操作方式
	 */
	@ApiModelProperty("请求方法")
	private String method;
	
	/**
	 * 操作ip
	 */
	@ApiModelProperty("操作id")
	private String ip;
	
	/**
	 * 耗时
	 */
	@ApiModelProperty("耗时")
	private String timeConsuming;
	
	/**
	 * 是否异常
	 */
	@ApiModelProperty("是否异常")
	private String isAbnormal;
	
	/**
	 * 操作人
	 */
	@ApiModelProperty("操作人")
	private String operator;
	
	/**
	 * 操作时间
	 */
	@ApiModelProperty("操作时间")
	private String operatingTime;


	
}
