package com.home.lh.util;

public class LayuiPage {

	/**
	 * 每页的条数
	 */
	private Integer limit;

	/**
	 * 页数
	 */
	private Integer page;

	public Integer getStart() {

		return (page - 1) * limit;
	}

	public Integer getEnd() {
		return limit;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

}
