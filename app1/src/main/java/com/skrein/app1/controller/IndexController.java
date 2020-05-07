package com.skrein.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 2020/5/6 13:56
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index1")
    public String index1() {
        return "index1";
    }
}
