package com.skrein.sso.controller;

import com.skrein.sso.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 2020/5/6 13:51
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/dologin")
    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("do loin username {} password {}", username, password);

        if (username.equals("1") && password.equals("2")) {
            // success
            log.info("login success {}, {}", username, password);

            // 颁发Ticket
            String ticket = UUID.randomUUID().toString();
            redisService.setTicket(ticket);

            String cookieId = UUID.randomUUID().toString();
            request.getSession().setAttribute("CASTGC", cookieId);
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", request.getSession().getAttribute("service") + "?ticket=" + ticket);
            response.addCookie(new Cookie("CASTGC", cookieId));
            return null;
        }
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("login_message", "用户名或者密码错误!");
        return mv;
    }


}
