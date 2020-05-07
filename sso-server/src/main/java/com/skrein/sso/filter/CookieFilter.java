package com.skrein.sso.filter;

import com.skrein.sso.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 2020/5/7 12:17
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Slf4j
@Component
public class CookieFilter implements Filter {

    @Autowired
    private RedisService redisService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (
                servletRequest.getRequestURI().endsWith("ticketValid") ||
                        servletRequest.getRequestURI().endsWith("css") ||
                        servletRequest.getRequestURI().endsWith("js") ||
                        servletRequest.getRequestURI().endsWith("ico")

        ) {
            chain.doFilter(request, response);
            return;
        }

        if (validCookie(servletRequest)) {
            String ticket = UUID.randomUUID().toString();
            log.info("cookie contains login id, not login again,will send ticket {}", ticket);
            redisService.setTicket(ticket);
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            servletResponse.setStatus(HttpServletResponse.SC_FOUND);
            servletResponse.setHeader("Location", servletRequest.getParameter("service") + "?ticket=" + ticket);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean validCookie(HttpServletRequest servletRequest) {
        Object castgc = servletRequest.getSession().getAttribute("CASTGC");
        if (castgc == null) {
            return false;
        }

        Cookie[] cookies = servletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getValue().equals(castgc)) {
                return true;
            }
        }
        return false;
    }
}
