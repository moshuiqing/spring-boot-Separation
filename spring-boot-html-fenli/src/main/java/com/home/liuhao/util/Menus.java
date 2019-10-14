package com.home.liuhao.util;

import java.io.Serializable;
import java.util.List;

/**
 * @author ai996
 * 二级以下菜单
 */
public class Menus implements Serializable {
	//private static final long serialVersionUID = 1L;
	private static final long serialVersionUID=7981560250804078637l; 
	private Integer id;
	
	private String title;
	
	private String icon;
	
	private String href;
	
	private boolean spread;
	
	private List<Menus> children;
	
	private String parent;
	
	private String target;
	
	private String mpName;
	
	private Integer sort;
	
	

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	

	public String getMpName() {
		return mpName;
	}

	public void setMpName(String mpName) {
		this.mpName = mpName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean getSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}



	public List<Menus> getChildren() {
		return children;
	}

	public void setChildren(List<Menus> children) {
		this.children = children;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	
	
}
