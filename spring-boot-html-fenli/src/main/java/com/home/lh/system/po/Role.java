package com.home.lh.system.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Role",description="角色")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键id")
	private String id;
	@ApiModelProperty("角色名称")
	private String name;
	@ApiModelProperty("角色秒速")
	private String remark;
	@ApiModelProperty("权限id")
	private String modelid;
	@ApiModelProperty("菜单id")
	private String menuid;
	@ApiModelProperty("横菜单id")
	private String bigmenuid;
	@ApiModelProperty("是否删除0否1是")
	private String isdel;
	@ApiModelProperty("是否禁用0否1是")
	private String isdesable;

}
