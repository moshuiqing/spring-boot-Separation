package com.home.lh.system.config.cors;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ai996
 * 跨域
 */
@Configuration
public class CorsConfigurat implements WebMvcConfigurer  {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		 registry.addMapping("/**")
         .allowedOrigins("*")
         .allowedMethods("*")
         .maxAge(3600)
         .allowCredentials(true);		
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
}
