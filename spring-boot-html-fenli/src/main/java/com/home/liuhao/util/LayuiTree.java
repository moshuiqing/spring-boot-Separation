package com.home.liuhao.util;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ai996
 * layui tree
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "LayuiTree", description = "layui菜单")
public class LayuiTree {
	
	private String id;
	
	private String title;
	
	private boolean spread;
	
	private String href;
	
	private String icon;
	
	private Integer sort;
	
	private String mpName;
	
	private List<LayuiTree> children;
	
	private String jibie;
	
	private boolean disabled;
	
	private boolean checked;


}

