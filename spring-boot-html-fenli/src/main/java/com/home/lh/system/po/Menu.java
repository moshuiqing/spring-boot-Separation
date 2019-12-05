package com.home.lh.system.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Menu", description = "系统菜单类")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键id")
	private Integer id;
	@ApiModelProperty("菜单标题")
	private String title;
	@ApiModelProperty("菜单图标")
	private String icon;
	@ApiModelProperty("菜单链接")
	private String href;
	@ApiModelProperty("父级菜单")
	private String parent;
	@ApiModelProperty("大菜单标识")
	private String mpname;
	@ApiModelProperty("菜单排序")
	private Integer sort;
	@ApiModelProperty("是否删除0否1是")
	private String isdel;
}
