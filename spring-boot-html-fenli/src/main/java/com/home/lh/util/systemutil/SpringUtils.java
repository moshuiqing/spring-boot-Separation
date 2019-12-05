package com.home.lh.util.systemutil;

import javax.servlet.ServletConfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;

@Component
public class SpringUtils implements ApplicationContextAware, ServletConfigAware {

	  private static ApplicationContext applicationContext;


	// private static ServletContext context;



	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException{
		return (T) applicationContext.getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext =applicationContext;
		
	}

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		
		
	}

}
