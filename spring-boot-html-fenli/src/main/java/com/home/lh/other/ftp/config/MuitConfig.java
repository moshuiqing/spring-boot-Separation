package com.home.lh.other.ftp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ai996
 * 自定义上传配置，过滤百度编辑器的上传
 */
@Configuration
public class MuitConfig {

	@Bean(name = "multipartResolver")
	public MyMuitPartSM multipartResolver() {
		MyMuitPartSM muitPartSM = new MyMuitPartSM();
		muitPartSM.setDefaultEncoding("UTF-8");
		// resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
		muitPartSM.setResolveLazily(true);
		muitPartSM.setMaxInMemorySize(40960);
		// 上传文件大小 50M 50*1024*1024
		muitPartSM.setMaxUploadSize(100 * 1024 * 1024);
		return muitPartSM;
	}

}
