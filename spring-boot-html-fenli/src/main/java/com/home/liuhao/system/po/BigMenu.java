package com.home.liuhao.system.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author liuhao  菜单
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BigMenu", description = "横向系统菜单类")
public class BigMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键id")
	private String id;
	@ApiModelProperty("标题")
	private String title;
	@ApiModelProperty("菜单标识")
	private String mpname;
	@ApiModelProperty("是否删除0否1是")
	private String isdel;
	@ApiModelProperty("菜单图标")
	private String icon;
	@ApiModelProperty("菜单排序")
	private Integer sort;

}
