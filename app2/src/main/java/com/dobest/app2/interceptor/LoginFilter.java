package com.dobest.app2.interceptor;


import com.dobest.app2.utils.HttpUtils;
import com.dobest.app2.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 2020/5/7 13:48
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Slf4j
public class LoginFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        // 判断是否登陆
        Boolean isLogin = (Boolean) servletRequest.getSession().getAttribute("is_login");

        // do interceptor static resources
        if(
                ((HttpServletRequest) request).getRequestURL().toString().endsWith(".css")||
                ((HttpServletRequest) request).getRequestURL().toString().endsWith(".js")||
                ((HttpServletRequest) request).getRequestURL().toString().endsWith(".ico")
        ){
            chain.doFilter(request, response);
            return;
        }

        if (isLogin != null && isLogin) {
            log.info("already login. just go resouce");
            chain.doFilter(request, response);
            return;
        }

        String ticket = getTicket(servletRequest);
        if (StringUtils.isEmpty(ticket)) {
            log.info("ticket为空，需要跳转登SSO登陆...");
            redirect2Login(servletRequest, servletResponse);
            return;
        }
        // 需要校验ticket
        String ticketResult = HttpUtils.doGet("http://sso.skrein.com/ticketValid?ticket=" + ticket);
        if ("ok".equals(ticketResult)) {
            log.info("ticket {} check success.", ticket);
            setLoginStatus(servletRequest);
            redirect2Resource(servletRequest, servletResponse);
            return;
        } else {
            log.info("ticket {} check fail.", ticket);
            redirect2Login(servletRequest, servletResponse);
            return;
        }

    }

    private void redirect2Resource(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        servletResponse.setStatus(HttpServletResponse.SC_FOUND);
        servletResponse.setHeader("Location", UrlUtils.buildHostUrl(servletRequest));
    }

    private void setLoginStatus(HttpServletRequest servletRequest) {
        servletRequest.getSession().setAttribute("is_login", true);
    }

    private String getTicket(HttpServletRequest servletRequest) {
        return servletRequest.getParameter("ticket");
    }

    private void redirect2Login(HttpServletRequest servletRequest, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "http://sso.skrein.com/index/login?service=" + UrlUtils.buildHostUrl(servletRequest));
    }


}
