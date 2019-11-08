package com.home.liuhao.other.liucheng.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**  
 * 概要说明 : 流程  <br>
 * 详细说明 : 流程  <br>
 * 创建时间 : 2018年7月19日 上午9:53:22 <br>
 * @author  by liuhao  
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Flow", description = "流程类")
public class Flow
{
    /**  
     * flowId:流程id  
     */
	@ApiModelProperty("流程id")
    private Integer flowId;
    
    /**  
     * FlowName:流程名称
     */
	@ApiModelProperty("流程名称")
    private String flowName;
    
    /**  
     * flowContent:流程介绍  
     */
	@ApiModelProperty("流程介绍")
    private String flowContent;
    
    /**  
     * isdel:是否删除0否1是 
     */
	@ApiModelProperty("isdel")
    private String isdel;
    
    /**  
     * isendble:是否禁用0否1是
     */
	@ApiModelProperty("isendble")
    private String isendble;
    
    /**  
     * updater:更新者id  
     */
	@ApiModelProperty("updater")
    private String updater;
    
    /**  
     * creater:创建者id
     */
	@ApiModelProperty("creater")
    private String creater;
    
    /**  
     * updateTime:更新时间  
     */
	@ApiModelProperty("updateTime")
    private String updateTime;
    
    
    /**  
     * createTime:创建时间
     */
	@ApiModelProperty("创建时间")
    private String createTime;


    
    
}
