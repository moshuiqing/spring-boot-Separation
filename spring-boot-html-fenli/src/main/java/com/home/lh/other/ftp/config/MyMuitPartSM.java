package com.home.lh.other.ftp.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class MyMuitPartSM extends CommonsMultipartResolver {

	@Override
	public boolean isMultipart(HttpServletRequest request) {

		String url = request.getRequestURI();
		if (url != null && url.contains("/config")) {
				return false;
		}

		return super.isMultipart(request);
	}

}