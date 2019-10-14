package com.home.liuhao.other.weixin.entity;

public class WeiXinMoBan {

	/**
	 * 用户openid
	 */
	private String touser;

	/**
	 * 模板消息id
	 */
	private String template_id;

	/**
	 * 模板跳转链接
	 */
	private String url;

	/**
	 * 消息数据
	 */
	private String data;

	public WeiXinMoBan() {
		super();
	}

	public WeiXinMoBan(String touser, String template_id, String url,
			String data) {
		super();
		this.touser = touser;
		this.template_id = template_id;
		this.url = url;
		this.data = data;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
