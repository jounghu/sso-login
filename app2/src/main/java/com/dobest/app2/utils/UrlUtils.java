package com.dobest.app2.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 2020/5/7 9:57
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public class UrlUtils {

    public static String buildHostUrl(HttpServletRequest request) {
        // get header remote url
        String host = request.getHeader("Host");
        if (host == null || "".equals(host)) {
            return request.getRequestURL().toString();
        }

        return "http://" +
                host +
                request.getRequestURI();
    }

}
