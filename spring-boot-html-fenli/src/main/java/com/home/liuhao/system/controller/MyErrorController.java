package com.home.liuhao.system.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

/**
 * 重写错误页面
 * @author liuhao
 *
 */
@Controller
@Api(value="MyErrorController",tags="重写错误controller")
public class MyErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "/liuhao/page/404";
    }


    @Override
    public String getErrorPath() {
        return PATH;
    }
}
