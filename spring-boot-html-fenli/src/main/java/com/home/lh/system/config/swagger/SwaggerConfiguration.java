package com.home.lh.system.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	/**
	 *
	 * @return
	 * http://localhost/liuhao/swagger-ui.html
	 */
	@Bean
	public Docket accessToken() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("api")// 定义组
				.select() // 选择那些路径和api会生成document
				.apis(RequestHandlerSelectors.basePackage("com.home.liuhao")) // 拦截的包路径				
				.paths(PathSelectors.any())// 拦截的接口路径
				.build() // 创建
				.apiInfo(apiInfo()); // 配置说明
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()//
				.title("自定义项目")// 标题
				.description("学习各种技术整合")// 描述
				.termsOfServiceUrl("http://localhost/liuhao")//
				.contact(new Contact("刘浩", "http://localhost/liuhao", "javaliuhao@126.com"))// 联系
				// .license("Apache License Version 2.0")// 开源协议
				// .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")//
				// 地址
				.version("1.1")// 版本
				.build();
	}
}
