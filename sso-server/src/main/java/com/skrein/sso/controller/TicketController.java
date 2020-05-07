package com.skrein.sso.controller;

import com.skrein.sso.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2020/5/7 10:51
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@RestController
@Slf4j
public class TicketController {

    @Autowired
    RedisService redisService;

    @GetMapping("/ticketValid")
    public Object validTicket(String ticket) {
        log.info("get ticket from client {}", ticket);
        if (redisService.checkTicket(ticket)) {
            return "ok";
        } else {
            return "false";
        }
    }

}
