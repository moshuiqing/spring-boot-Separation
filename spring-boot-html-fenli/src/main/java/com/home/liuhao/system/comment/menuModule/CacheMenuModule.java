package com.home.liuhao.system.comment.menuModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.home.liuhao.system.service.MenuService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ai996
 *  自定义初始化加载  （在所有配置类加载完成之后加载）
 */
@Component
@Order(2)
@Slf4j
public class CacheMenuModule implements CommandLineRunner {
	
	
	@Autowired
	private MenuService menusService;

	@Override
	public void run(String... args) throws Exception {
		
		try {
			menusService.cacheMenuModule();
			log.info("菜单缓存");
		} catch (Exception e) {
			log.info("缓存失败");
			e.printStackTrace();
		}
		
	}

}
