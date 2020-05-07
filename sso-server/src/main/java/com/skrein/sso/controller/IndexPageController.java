package com.skrein.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 2020/5/6 13:47
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Controller
@RequestMapping("/index")
@Slf4j
public class IndexPageController {

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        log.info("enter login");
        request.getSession().setAttribute("service",request.getParameter("service"));
        return "login";
    }

}
