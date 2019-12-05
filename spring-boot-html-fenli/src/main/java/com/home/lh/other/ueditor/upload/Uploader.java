package com.home.lh.other.ueditor.upload;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baidu.ueditor.define.State;



public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			System.out.println(this.conf.get("useFtp"));
			if((boolean) (this.conf.get("useFtp"))){//判断是否使用ftp上传
				state = UploaderFtp.save(this.request, this.conf);
			}else{
				state = BinaryUploader.save(this.request, this.conf);
			}
			
		}

		return state;
	}
}
