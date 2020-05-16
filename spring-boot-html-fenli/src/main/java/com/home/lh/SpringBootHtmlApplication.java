package com.home.lh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tio.websocket.starter.EnableTioWebSocketServer;

@SpringBootApplication
@ServletComponentScan // 在SpringBootApplication上使用@ServletComponentScan注解后，Servlet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册，无需其他代码
@MapperScan("**.mapper") // 扫描mybaits注解包 使用后无需再@mapper
@EnableCaching // 开启缓存
@EnableAsync // 开启异步任务
@EnableScheduling // 开启注解任务
@EnableTioWebSocketServer // Tio注解
public class SpringBootHtmlApplication {


	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(SpringBootHtmlApplication.class, args);
	}

}
