package com.dobest.app2.controller;

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
}
