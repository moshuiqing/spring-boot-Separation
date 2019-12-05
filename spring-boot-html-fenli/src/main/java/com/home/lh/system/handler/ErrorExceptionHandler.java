package com.home.lh.system.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.home.lh.util.systemutil.SystemUtils;

import net.sf.json.JSONObject;

@ControllerAdvice
@ResponseBody
public class ErrorExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorExceptionHandler.class);


	@ExceptionHandler({Exception.class})
	public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		log.info(e.getMessage());
		request.setAttribute("isAbnormal", "异常");
		if(SystemUtils.isAjax(request)) {
			log.info("**************");
			Map<String, Object> map = new HashMap<>();
			map.put("exception", e.getMessage());
			return JSONObject.fromObject(map);
		}else {
			ModelAndView m  = new ModelAndView();
			m.addObject("exception",e);
			System.out.println(request.getRequestURL());
			m.addObject("url",request.getRequestURL());
			m.setViewName("/liuhao/page/500");			
			return m;
		}
	
	}

	

}
