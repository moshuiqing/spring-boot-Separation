package com.home.lh;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		   System.setProperty("es.set.netty.runtime.available.processors", "false");
		return application.sources(SpringBootHtmlApplication.class);
	}

}
