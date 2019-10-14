package com.home.liuhao.other.ftp.po;

import java.io.Serializable;

/**
 * @author 刘浩 配置路径
 */
public class FilePath implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 存web端路径
	 */
	private String webFilePath[];

	/**
	 * 存手机端路劲
	 */
	private String mobileFilePath[];

	public String[] getWebFilePath() {
		return webFilePath;
	}

	public void setWebFilePath(String[] webFilePath) {
		this.webFilePath = webFilePath;
	}

	public String[] getMobileFilePath() {
		return mobileFilePath;
	}

	public void setMobileFilePath(String[] mobileFilePath) {
		this.mobileFilePath = mobileFilePath;
	}

}
