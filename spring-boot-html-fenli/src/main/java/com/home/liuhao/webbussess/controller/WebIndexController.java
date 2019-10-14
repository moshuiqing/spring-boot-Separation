package com.home.liuhao.webbussess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liuhao
 *
 */
@Controller
@RequestMapping("/web")
public class WebIndexController {

	
	
	/**
	 * @return
	 */
	@RequestMapping("")
	public String toWebLogin() {

		return "redirect:/webbussess/weblogin/toWebLogin";
	}

}
