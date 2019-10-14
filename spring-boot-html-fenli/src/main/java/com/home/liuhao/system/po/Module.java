package com.home.liuhao.system.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Module", description = "权限类")
public class Module implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键id")
	private Integer mid;
	@ApiModelProperty("权限名称")
	private String mname;
	
}
