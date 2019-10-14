package com.home.liuhao.other.ueditor.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.liuhao.other.ueditor.ActionEnter;

import springfox.documentation.annotations.ApiIgnore;





@Controller
@RequestMapping("/baidu/eduit/")
@ApiIgnore
public class EduitController {

	@RequestMapping("config")
	public void config(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("11");
		try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            String rootPath = request.getSession().getServletContext().getRealPath( "/" );
           // String rootPath = this.getClass().getResource("/").getPath();
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter w= response.getWriter();
        	w.write(exec);
        	w.flush();
        	w.close();
        	
          
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
