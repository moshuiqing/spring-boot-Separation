package com.home.lh.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Api(value="SysController",tags="系统配置")
@Slf4j
public class SysController {
	
	
	/**
	 * @return
	 * 跳转登录页面
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ApiOperation("进入登录页面")
	public String toLogin() {
		log.info("进入登录页面");
		return "/liuhao/page/setting/login/login";
	}
	
	

}
