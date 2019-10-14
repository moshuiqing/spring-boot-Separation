package com.home.lh.po;

public class AuthorizeInfo {
	
	private String accessToken;
	
	private Long expiresIn;
	private Long time;
	
	
	public AuthorizeInfo() {
		super();
	}

	public AuthorizeInfo(String accessToken, Long expiresIn, Long time) {
		super();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.time = time;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	
	
	

}
