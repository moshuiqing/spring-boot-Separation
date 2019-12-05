package com.home.lh.other.weixin.entity;
/**
 * 微信信息实体类
 * @author liuhao
 *
 */
public class WeixinGlobal {
	
	private String access_token;
	
	private String ticket;
	
	private long datetime;
	
	private String wxurl;

	public String getWxurl() {
		return wxurl;
	}

	public void setWxurl(String wxurl) {
		this.wxurl = wxurl;
	}

	public WeixinGlobal() {
		super();
	}
	
	
	

	public WeixinGlobal(String access_token, String ticket, long datetime,
			String wxurl) {
		super();
		this.access_token = access_token;
		this.ticket = ticket;
		this.datetime = datetime;
		this.wxurl = wxurl;
	}

	public WeixinGlobal(String access_token, String ticket, long datetime) {
		super();
		this.access_token = access_token;
		this.ticket = ticket;
		this.datetime = datetime;
	}

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long l) {
		this.datetime = l;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	
	

}
